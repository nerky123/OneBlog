package com.zyd.blog.business.service.impl;

import cn.hutool.core.date.DateTime;
import com.zyd.blog.business.entity.User;
import com.zyd.blog.business.entity.UserPwd;
import com.zyd.blog.business.enums.ConfigKeyEnum;
import com.zyd.blog.business.enums.UserTypeEnum;
import com.zyd.blog.business.service.AuthService;
import com.zyd.blog.business.service.MailService;
import com.zyd.blog.business.service.SysConfigService;
import com.zyd.blog.business.service.SysUserService;
import com.zyd.blog.plugin.oauth.RequestFactory;
import com.zyd.blog.util.BeanConvertUtil;
import com.zyd.blog.util.PasswordUtil;
import com.zyd.blog.util.RequestUtil;
import com.zyd.blog.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2019/5/25 14:34
 * @since 1.8
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SysUserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private SysConfigService configService;

    @Override
    public boolean login(String source, AuthCallback callback) {
        AuthRequest authRequest = RequestFactory.getInstance(source).getRequest();
        AuthResponse response = authRequest.login(callback);
        if (response.ok()) {
            AuthUser authUser = (AuthUser) response.getData();
            User newUser = BeanConvertUtil.doConvert(authUser, User.class);
            newUser.setSource(authUser.getSource().toString());
            if (null != authUser.getGender()) {
                newUser.setGender(authUser.getGender().getCode());
            }
            User user = userService.getByUuidAndSource(authUser.getUuid(), authUser.getSource().toString());
            newUser.setUserType(UserTypeEnum.USER);
            if (null != user) {
                newUser.setId(user.getId());
                userService.updateSelective(newUser);
            } else {
                userService.insert(newUser);
            }
            SessionUtil.setUser(newUser);
            return true;
        }
        log.warn("[{}] {}", source, response.getMsg());
        return false;
    }

    @Override
    public boolean revoke(String source, Long userId) {
        return false;
    }

    @Override
    public void logout() {
        SessionUtil.removeUser();
    }

    @Override
    public String sendMail(String mail,String code,String action) {
        User user = userService.getByMail(mail);
        if ("register".equals(action)){
            if (user!=null)return "该邮件已被注册，如忘记密码可通过邮件找回！";
            return mailService.sendToRegister(mail,code);
        }else if("forgetPass".equals(action)){
            if (user==null)return "该邮件还未被注册，请确认！";
            if (!UserTypeEnum.USER.name().equals(user.getUserType())) return "该账号不要尝试修改密码呦~";
            return mailService.sendToUserForgetPass(mail,code);
        }else{
            return "小网站，请手下留情！";
        }

    }


    @Override
    public String register(String mail, String pass, String name, String code) {
        User user1 = userService.getByMail(mail);
        if (user1!=null)return "该邮件已被注册，如忘记密码可通过邮件找回！";

        if (!code.equals(SessionUtil.getKaptcha())) return "验证码错误！";
        SessionUtil.removeKaptcha();
        SessionUtil.removeToken("registerCount");
        SessionUtil.removeToken("registerTime");

        try {
            User user = new User();
            user.setPassword(PasswordUtil.encrypt(pass,name));
            user.setUsername(name);
            user.setNickname(name);
            user.setEmail(mail);
            user.setUserType(UserTypeEnum.USER);
            //user.setAvatar(configService.getByKey(ConfigKeyEnum.STATIC_WEB_SITE.getKey()).getConfigValue()+"/img/touxiang.jpg");
            user.setPrivacy(0);
            user.setNotification(1);
            user.setRegIp(RequestUtil.getIp());
            user.setStatus(1);
            if (userService.insert(user)!=null){
                return "注册成功，请登录！";
            }
        } catch (Exception e) {
            log.error("注册失败，当前用户："+mail+",具体原因："+e.getMessage());
        }
        return "注册失败，请稍后重试或者联系站长！";
    }


    @Override
    public boolean login(String mail, String pass,String remoteAddr) {
        User user = userService.getByMail(mail);
        SessionUtil.removeUser();
        if (user!=null){
            try {
                String encrypt = PasswordUtil.encrypt(pass, user.getUsername());
                if (encrypt.equals(user.getPassword())){
                    SessionUtil.setUser(user);
                    try{
                        User user1 = new User();
                        user1.setLastLoginTime(DateTime.now());
                        user1.setLastLoginIp(remoteAddr);
                        userService.updateSelective(user1);
                    }catch (Exception e){
                        log.error("登录失败");
                    }
                    return true;
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public String forget(String pass, String code,String mail) {

        User user = userService.getByMail(mail);
        if (user==null) return "该账户不存在，请确认邮件是否正确！";
        if (!code.equals(SessionUtil.getKaptcha())) return "验证码错误！";
        SessionUtil.removeKaptcha();
        try {
            user.setPassword(pass);
            if (userService.updateSelective(user)) return "密码修改成功，请重新登录！";

        } catch (Exception e) {
            log.error("密码修改失败，用户："+mail+"，具体原因："+e.getMessage());
        }
        return "密码修改失败！";
    }
}

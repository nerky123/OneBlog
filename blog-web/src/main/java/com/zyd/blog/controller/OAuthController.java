package com.zyd.blog.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.zyd.blog.business.enums.ConfigKeyEnum;
import com.zyd.blog.business.enums.ResponseStatus;
import com.zyd.blog.business.service.AuthService;
import com.zyd.blog.business.service.SysConfigService;
import com.zyd.blog.persistence.beans.SysConfig;
import com.zyd.blog.plugin.kaptcha.GifCaptcha;
import com.zyd.blog.plugin.oauth.RequestFactory;
import com.zyd.blog.util.RequestUtil;
import com.zyd.blog.util.ResultUtil;
import com.zyd.blog.util.SessionUtil;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2019/2/19 9:28
 * @since 1.8
 */
@Controller
@RequestMapping("/oauth")
public class OAuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private SysConfigService configService;

    @RequestMapping("/render/{source}")
    public void renderAuth(@PathVariable("source") String source, HttpServletResponse response, HttpSession session) throws IOException {
        AuthRequest authRequest = RequestFactory.getInstance(source).getRequest();
        session.setAttribute("historyUrl", RequestUtil.getReferer());
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    /**
     * 授权回调地址
     *
     * @param source   授权回调来源
     * @param callback 回调参数包装类
     * @return
     */
    @RequestMapping("/callback/{source}")
    public ModelAndView login(@PathVariable("source") String source, AuthCallback callback, HttpSession session) {
        authService.login(source, callback);
        String historyUrl = (String) session.getAttribute("historyUrl");
        session.removeAttribute("historyUrl");
        if (StringUtils.isEmpty(historyUrl)) {
            return ResultUtil.redirect("/");
        }
        return ResultUtil.redirect(historyUrl);
    }

    /**
     * 收回授权
     *
     * @param source
     * @param token
     * @return
     * @throws IOException
     */
    @RequestMapping("/revoke/{source}/{token}")
    public ModelAndView revokeAuth(@PathVariable("source") String source, @PathVariable("token") String token) throws IOException {
        AuthRequest authRequest = RequestFactory.getInstance(source).getRequest();
        AuthResponse response = authRequest.revoke(AuthToken.builder().accessToken(token).build());
        if (response.getCode() == 2000) {
            return ResultUtil.redirect("/");
        }
        return ResultUtil.redirect("/login");
    }

    /**
     * 退出登录
     *
     * @throws IOException
     */
    @RequestMapping("/logout")
    public ModelAndView logout() {
        authService.logout();
        return ResultUtil.redirect("/");
    }


    /**
     * 用户注册/忘记密码发送邮件
     */
    @PostMapping("/sendMail")
    @ResponseBody
    public String sendMail(@RequestParam("mail") String mail,HttpSession session,@RequestParam("action")String action){
        Integer registerCount = (Integer)session.getAttribute("registerCount");
        DateTime registerTime = (DateTime) session.getAttribute("registerTime");
        if (registerTime !=null && DateTime.now().isIn(registerTime, DateUtil.offsetMinute(registerTime,2))){
            return "两分钟内只能发一次邮件，请两分钟后重试！";
        }
        if (registerCount!=null && registerCount>5){
            return "系统检测到您恶意访问，请半小时后重试！";
        }
        session.setAttribute("registerCount",registerCount==null?1:++registerCount);
        session.setAttribute("registerTime", DateTime.now());
        session.setAttribute("mailAddress",mail);
        //生成随机字符串
        GifCaptcha gifCaptcha = new GifCaptcha();
        String randomString = gifCaptcha.getRandomString();
        return authService.sendMail(mail,randomString,action);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @ResponseBody
    public String register(@RequestParam("mail") String mail, @RequestParam("pass") String pass, @RequestParam("confirmPass") String confirmPass,@RequestParam("name") String name,
                           @RequestParam("code")  String code,HttpSession session){
        if (!pass.equals(confirmPass)){
            return "两次密码不一致呀，确认下！";
        }
        if (!mail.equals((String) session.getAttribute("mailAddress"))){
            return "两次邮件地址不一致，请勿修改发送邮件地址！";
        }
        return authService.register(mail,pass,name,code);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestParam("mail") String mail,@RequestParam("code") String code, @RequestParam("pass") String pass,HttpServletRequest request){
        if(SessionUtil.getUser()!=null){
            return "alreadyLogin";
        }
        if (!code.equals(SessionUtil.getKaptcha())){
            return "codeError";
        }
        boolean flag = authService.login(mail,pass,request.getRemoteAddr());
        if (!flag){
            return "fail";
        }
        SysConfig siteUrl = configService.getByKey(ConfigKeyEnum.SITE_URL.getKey());
        String redirectUrl = SessionUtil.getToken("redirectUrl");
        SessionUtil.removeToken("redirectUrl");
        return redirectUrl==null?siteUrl.getConfigValue()+"/":redirectUrl;
    }

    /**
     * 忘记密码
     */
    @PostMapping("/forget")
    @ResponseBody
    public String forget(@RequestParam("mail") String mail, @RequestParam("pass") String pass, @RequestParam("confirmPass") String confirmPass,@RequestParam("code")  String code,HttpSession session){
        if (!pass.equals(confirmPass)){
            return "两次密码不一致呀，确认下！";
        }
        if (!mail.equals((String) session.getAttribute("mailAddress"))){
            return "两次邮件地址不一致，请勿修改发送邮件地址！";
        }
        return authService.forget(pass,code,mail);
    }

}

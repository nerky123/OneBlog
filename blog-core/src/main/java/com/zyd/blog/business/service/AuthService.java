package com.zyd.blog.business.service;

import me.zhyd.oauth.model.AuthCallback;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2019/5/25 14:32
 * @since 1.8
 */
public interface AuthService {

    boolean login(String source, AuthCallback callback);

    boolean revoke(String source, Long userId);

    void logout();

    String sendMail(String mail,String code,String action);

    String register(String mail, String pass, String name, String code);

    boolean login(String mail,String pass,String remoteAddr);

    String forget(String pass, String code,String mail);
}

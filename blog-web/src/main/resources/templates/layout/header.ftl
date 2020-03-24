<nav id="topmenu" class="navbar navbar-default navbar-fixed-top" xmlns="http://www.w3.org/1999/html">
    <div class="menu-box">
        <div class="pull-left current-time">
            <ul class="list-unstyled list-inline">
                <li><span id="currentTime"></span></li>
            </ul>
            <div class="clear"></div>
        </div>
        <div class="menu-topmenu-container pull-right">
            <ul class="list-unstyled list-inline pull-left">
                <li><a href="${config.siteUrl}/about" class="menu_a" title="关于博客" data-toggle="tooltip" data-placement="bottom">关于本站</a></li>
                <li><a href="${config.siteUrl}/links" class="menu_a" title="友情链接" data-toggle="tooltip" data-placement="bottom">友情链接</a></li>
            </ul>
            <#if user??>
                <ul class="list-unstyled list-inline pull-left">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle menu_a" data-toggle="dropdown" aria-expanded="false"><i class="fa fa-user fa-fw"></i>${user.username!} <span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="/oauth/logout"><i class="fa fa-sign-out"></i>退出</a></li>
                        </ul>
                    </li>
                </ul>
            <#else>
                <ul class="list-unstyled list-inline pull-left">
                    <li><a href="javascript:;;" data-toggle="modal" data-target="#login" rel="nofollow" title="登录">登录</a></li>
                    <li><a href="javascript:;;" data-toggle="modal" data-target="#register" rel="nofollow" title="注册">注册</a></li>
                </ul>
            </#if>
        </div>
    </div>
</nav>

<!--登录页面-->
<div class="modal fade" id="login" tabindex="-1" role="dialog" aria-labelledby="oauthTitle">
    <div class="modal-dialog" role="document">
        <div class="modal-content" style="background-image: url('${config.siteUrl}/img/true.png')">
            <div class="modal-body">
                <div class="login_wrapper">
                    <div class="animate form login_form" style="position: relative;">
                        <section class="login_content">
                            <form action="/passport/signin" method="POST" id="login-form">
                                <h1>登录</h1>
                                <div style="position: relative">
                                    <input type="email" class="form-control input-medium"  placeholder="请输入邮箱地址" name="mail" maxlength="100" required="required">
                                </div>
                                <div style="position: relative">
                                    <input type="password" class="form-control input-medium" placeholder="请输入密码" name="pass" minlength="6" maxlength="20" required="required">
                                </div>
                                <div style="position: relative;width: 50%;">
                                    <input type="text" class="form-control input-medium" maxlength="10" placeholder="请输入验证码" name="code" required="required" style="position:absolute">
                                    <img src="/getKaptcha" onclick="$(this).attr('src','/getKaptcha?'+Math.random())" style="margin-bottom: 20px;margin-left: 100%;">
                                </div>
                                <div>
                                    <button type="button" class="btn btn-success btn-login" style="width: 100%;">登录</button>
                                </div>
                                <div class="login-loading hide">
                                    <i class="fa fa-spinner fa-pulse"></i>正在登录中...
                                </div>

                                <div class="clearfix"></div>

                                <div class="separator">
                                    <div class="clearfix"></div>
                                    <div style="margin-top: 10px">
                                        <a href="javascript:;;" onclick="$('#login').modal('hide')" data-toggle="modal" data-target="#forget" rel="nofollow" title="找回密码" style="    font-size: medium;color: aliceblue;">找回密码</a>
                                        <button type="button" class="btn btn-info" style="background-color: azure;border-color: azure;margin-left: 6%;" onclick="$('#login').modal('hide')" data-toggle="modal" data-target="#register"><a  style="font-size: small;" href="javascript:;;"  rel="nofollow" title="注册">注册</a></button>
                                    </div>
                                </div>
                            </form>
                        </section>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<#--忘记密码页面-->
<div class="modal fade" id="forget" tabindex="-1" role="dialog" aria-labelledby="oauthTitle">
    <div class="modal-dialog" role="document">
        <div class="modal-content" style="background-image: url('${config.siteUrl}/img/true.png')">
            <div class="modal-body">
                <div class="login_wrapper">
                    <div class="animate form login_form" style="position: relative;">
                        <section class="login_content">
                            <form action="/passport/signin" method="POST" id="forget-form">
                                <h1>找回密码</h1>
                                <div style="position: relative">
                                    <input type="email" class="form-control input-medium" placeholder="请输入邮箱地址" name="mail" maxlength="100" id="forget-email" required="required">
                                    <input type="text" name="action" value="forgetPass" hidden>
                                </div>
                                <#--<div style="position: relative">
                                    <input type="text" class="form-control input-medium" placeholder="请输入昵称" name="name" required="required">
                                </div>-->
                                <div style="position: relative">
                                    <input type="password" class="form-control input-medium" placeholder="请输入新密码" minlength="6" maxlength="20"  name="pass" required="required">
                                </div>
                                <div style="position: relative">
                                    <input type="password" class="form-control input-medium" placeholder="请确认新密码" minlength="6" maxlength="20"  name="confirmPass" required="required">
                                </div>
                                <div style="position: relative;width: 50%;">
                                    <input type="text" class="form-control input-medium" maxlength="10" placeholder="验证码" name="code" required="required" style="position:absolute">
                                    <button type="button" class="btn btn-group-vertical btn-forget-pass-sendmail" style="margin-bottom: 20px;margin-left: 100%;" >获取验证码</button>
                                </div>
                                <div>
                                    <button type="button" class="btn btn-success btn-forget-pass" style="width: 100%;">找回密码</button>
                                </div>
                                <div class="forget-loading hide">
                                    <i class="fa fa-spinner fa-pulse"></i>正在修改中...
                                </div>

                                <div class="clearfix"></div>

                                <div class="separator">
                                    <div class="clearfix"></div>
                                    <div style="margin-top: 10px">
                                        <button type="button" class="btn btn-info" style="background-color: azure;border-color: azure;width: 100%;" onclick="$('#forget').modal('hide')"  data-toggle="modal" data-target="#login"><a style="font-size: small;"  href="javascript:;;" rel="nofollow" title="登录">返回登录</a></button>
                                    </div>
                                </div>
                            </form>
                        </section>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<#--注册页面-->
<div class="modal fade" id="register" tabindex="-1" role="dialog" aria-labelledby="oauthTitle">
    <div class="modal-dialog" role="document">
        <div class="modal-content" style="background-image: url('${config.siteUrl}/img/true.png')">
            <div class="modal-body">
                <div class="login_wrapper">
                    <div class="animate form login_form" style="position: relative;">
                        <section class="login_content">
                            <form action="/passport/signin" method="POST" id="register-form">
                                <h1>注册</h1>
                                <div style="position: relative">
                                    <input type="email" class="form-control input-medium" placeholder="请输入邮箱地址" name="mail" maxlength="100" id="register-email" required="required">
                                    <input type="text" name="action" value="register" hidden>
                                </div>
                                <div style="position: relative">
                                    <input type="text" class="form-control input-medium" placeholder="请输入昵称" name="name" required="required">
                                </div>
                                <div style="position: relative">
                                    <input type="password" class="form-control input-medium" placeholder="请输入密码" minlength="6" maxlength="20"  name="pass" required="required">
                                </div>
                                <div style="position: relative">
                                    <input type="password" class="form-control input-medium" placeholder="请确认密码" minlength="6" maxlength="20"  name="confirmPass" required="required">
                                </div>
                                <div style="position: relative;width: 50%;">
                                    <input type="text" class="form-control input-medium" maxlength="10" placeholder="验证码" name="code" required="required" style="position:absolute">
                                    <button type="button" class="btn btn-group-vertical btn-register-sendmail" style="margin-bottom: 20px;margin-left: 100%;" >获取验证码</button>
                                </div>
                                <div>
                                    <button type="button" class="btn btn-success btn-register" style="width: 100%;">注册</button>
                                </div>
                                <div class="register-loading hide">
                                    <i class="fa fa-spinner fa-pulse"></i>正在注册中...
                                </div>

                                <div class="clearfix"></div>

                                <div class="separator">
                                    <div class="clearfix"></div>
                                    <div style="margin-top: 10px">
                                        <button type="button" class="btn btn-info" style="background-color: azure;border-color: azure;width: 100%;" onclick="$('#register').modal('hide')" data-toggle="modal" data-target="#login"><a style="font-size: small;"  href="javascript:;;"  rel="nofollow" title="登录">返回登录</a></button>
                                    </div>
                                </div>
                            </form>
                        </section>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>





<nav id="mainmenu" class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="menu-box">
        <div class="navbar-header">
            <span class="pull-right nav-search toggle-search" data-toggle="modal" data-target=".nav-search-box"><i class="fa fa-search"></i></span>
            <button class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand logo" href="#"></a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <div class="pull-left site-desc">
                <h1 class="auto-shake"><a href="${config.siteUrl}" data-original-title="${config.siteDesc}" data-toggle="tooltip" data-placement="bottom">${config.siteName}</a></h1>
                <p class="site-description">${config.siteDesc}</p>
            </div>
            <ul class="nav navbar-nav ">
                <li>
                    <a href="/" class="menu_a"><i class="fa fa-home"></i>首页</a>
                </li>
                <@zhydTag method="types">
                    <#if types?? && types?size gt 0>
                        <#list types as item>
                            <#if item.nodes?? && item.nodes?size gt 0>
                                <li class="dropdown">
                                    <a href="#" class="dropdown-toggle menu_a" data-toggle="dropdown" aria-expanded="false">
                                        <i class="${item.icon!}"></i>${item.name!} <span class="caret"></span>
                                    </a>
                                    <ul class="dropdown-menu" role="menu">
                                        <#list item.nodes as node>
                                        <li><a href="/type/${node.id?c}" title="点击查看《${node.name!}》的文章">${node.name!}</a></li>
                                        </#list>
                                    </ul>
                                </li>
                            <#else>
                                <li><a href="/type/${item.id?c}" class="menu_a"><i class="${item.icon!}"></i>${item.name!}</a></li>
                            </#if>
                        </#list>
                    </#if>
                </@zhydTag>
                <li><a href="/guestbook" class="menu_a"><i class="fa fa-comments-o"></i>留言板</a></li>
                <li><span class="pull-right nav-search main-search" data-toggle="modal" data-target=".nav-search-box"><i class="fa fa-search"></i></span></li>
            </ul>
        </div>
    </div>
</nav>
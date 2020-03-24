<#include "include/macros.ftl">
<@compress single_line=false>
    <@header title="登录认证"></@header>

    <div class="container custome-container">
        <nav class="breadcrumb">
            <a class="crumbs" title="返回首页" href="${config.siteUrl}" data-toggle="tooltip" data-placement="bottom"><i class="fa fa-home"></i>首页</a>
            <i class="fa fa-angle-right"></i>登录认证
        </nav>
        <div class="row about-body">
            <div class="col-sm-12 blog-main" style="text-align: center">
                <div class="blog-body expansion">
                    <#if time??>
                        <div>
                            <h5 >
                                注：新注册用户
                                <strong style="color: green;">3天</strong>
                                后才能阅读资源受限文章
                            </h5>
                        </div>
                        <div>
                            到期时间：<strong style="color: green;">${time}</strong>
                        </div>
                        <#else >
                            <h5 class="custom-title"><i class="fa fa-user-secret fa-fw"></i>
                                当前文章内容资源受限，请点击
                                <strong>
                                    <a href="javascript:;;" data-toggle="modal" data-target="#login" rel="nofollow" title="登录">登录</a>
                                </strong>
                            </h5>
                    </#if>

                </div>
            </div>
        </div>
    </div>

    <@footer>
        <script src="https://v1.hitokoto.cn/?encode=js&c=d&select=%23hitokoto" defer></script>
    </@footer>
</@compress>
package me.wechat.server;

import com.google.common.base.Strings;
import io.vertx.core.Vertx;
import io.vertx.core.http.*;
import io.vertx.core.net.SocketAddress;
import me.wechat.sever.entity.AccessToken;
import me.wechat.sever.entity.UserInfo;
import me.wechat.util.WechatJsonHelper;
import me.wechat.util.WechatMsgXml;
import me.wechat.util.WechatXml;
import me.wechat.util.WechatXmlHelper;

public class WeChatPServer {

    private static final String APP_ID = "wxaaab93db33d15a55";

    private static final String APP_SECRET = "f41f33ad25b3d75552ac9d209ca6e56a";

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();
        HttpClient accessClient = vertx.createHttpClient(new HttpClientOptions()
                .setSsl(true)
                .setDefaultHost("api.weixin.qq.com")
                .setDefaultPort(443));
        server.requestHandler(req -> {
            SocketAddress remoteAddress = req.remoteAddress();
            System.out.println("remote address " + remoteAddress.toString());
            //区块链大数据分析页面
            if (req.path().startsWith("/index")) {
                String code = req.getParam("code");
                String accessTokenApiPath = "/sns/oauth2/access_token?appid=" + APP_ID + "&secret=" + APP_SECRET + "&code=" + code + "&grant_type=authorization_code";
                accessClient.getNow(accessTokenApiPath, accessTokenResp ->
                        accessTokenResp.bodyHandler(accessTokenContent -> {
                        AccessToken access = WechatJsonHelper.parseText(accessTokenContent.toString(), AccessToken.class);
                        System.out.println("accessToken: " + access);
                        String accessToken = access.getAccessToken();
                        String scrope = access.getScope();
                        String openid = access.getOpenid();
                        if (scrope.equals("snsapi_userinfo") && !Strings.isNullOrEmpty(accessToken)) {
                            String userInfoApiPath = "/sns/userinfo?access_token=" + accessToken + "&openid=" + openid + "&lang=zh_CN";
                            accessClient.getNow(userInfoApiPath, userInfoResp ->
                                    userInfoResp.bodyHandler(userInfoContent -> {
                                        UserInfo userInfo = WechatJsonHelper.parseText(userInfoContent.toString(), UserInfo.class);
                                        System.out.println("userInfo: " + userInfoApiPath);
                                    })
                            );
                        }
                        req.response().putHeader("Content-type", "text/html;charset=utf-8").end("<html><head></head><body>hello world</body></html>", "utf-8");
                    }));
            } else {
                req.bodyHandler(body -> {
                    if (body.length() > 0) {
                        String reqXmlStr = body.toString();
                        System.out.println("req: " + reqXmlStr);
                        try {
                            WechatXml reqXml = WechatXmlHelper.getWechatXml(reqXmlStr);
                            if (reqXml.isMsgXml()) {
                                WechatMsgXml respXml = WechatXmlHelper.reqToRespMsg(reqXml);
                                //全部编码改成utf-8
                                respXml.setMsgContent(new String("生活就像海洋，只有意志坚强的人才能到达彼岸".getBytes(), "utf-8"));
                                System.out.println("resp: " + respXml.toString());
                                req.response().putHeader("Content-type", "text/xml;charset=utf-8").end(respXml.toString(), "utf-8");
                            } else {
                                System.out.println("resp: success");
                                req.response().putHeader("Content-type", "text/plain;charset=utf-8").end("success", "utf-8");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("resp: no body");
                        req.response().putHeader("Content-type", "text/plain;charset=utf-8").end("success", "utf-8");
                    }
                });
            }
        });
        server.listen(443, res -> {
            if (res.succeeded()) {
                System.out.println("Server listening");
            } else {
                System.out.println(res.cause().getMessage());
            }
        });
    }
}

package me.wechat.server;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import io.vertx.core.net.SocketAddress;
import me.wechat.util.WechatMsgXml;
import me.wechat.util.WechatXml;
import me.wechat.util.WechatXmlHelper;

public class WeChatPServer {

    private static final String APP_ID = "wxaaab93db33d15a55";

    private static final String APP_SECRET = "f41f33ad25b3d75552ac9d209ca6e56a";

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();
        HttpClient accessTokenClient = vertx.createHttpClient(new HttpClientOptions()
                .setSsl(true)
                .setDefaultHost("api.weixin.qq.com")
                .setDefaultPort(443));
        server.requestHandler(req -> {
            SocketAddress remoteAddress = req.remoteAddress();
            System.out.println("remote address " + remoteAddress.toString());
            //区块链大数据分析页面
            if (req.path().startsWith("/index")) {
                String code = req.getParam("code");
                String accessTokenApiHost = "api.weixin.qq.com";
                String accessTokenApiPath = "/sns/oauth2/access_token?appid=" + APP_ID + "&secret=" + APP_SECRET + "&code=" + code + "&grant_type=authorization_code";
                accessTokenClient.getNow(accessTokenApiPath, resp -> {
                    resp.bodyHandler(accessTokenContent -> {
                        System.out.println("accessTokenContent: " + accessTokenContent);
                        req.response().putHeader("Content-type", "text/html;charset=utf-8").end("<html><head></head><body>hello world</body></html>", "utf-8");
                    });
                });
            }
            req.bodyHandler((Buffer body) -> {
                if (body.length() > 0) {
                    String reqXmlStr = body.toString();
                    System.out.println("req: " + reqXmlStr);
                    try {
                        WechatXml reqXml = WechatXmlHelper.getWechatXml(reqXmlStr);
                        if (reqXml.isMsgXml()) {
                            System.out.println("resp: " + reqXml.convertToWechatMsgXml().getMsgContent());
                            WechatMsgXml respXml = WechatXmlHelper.reqToRespMsg(reqXml);
                            //全部编码改成utf-8
                            respXml.setMsgContent(new String("生活就像海洋，只有意志坚强的人才能到达彼岸".getBytes(), "utf-8"));
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

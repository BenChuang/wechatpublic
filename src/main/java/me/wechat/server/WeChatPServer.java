package me.wechat.server;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.net.SocketAddress;
import me.wechat.util.WechatMsgXml;
import me.wechat.util.WechatXml;
import me.wechat.util.WechatXmlHelper;
import org.jdom2.input.SAXBuilder;

public class WeChatPServer {

    private static SAXBuilder builder = new SAXBuilder();

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();
        server.requestHandler(req -> {
            SocketAddress remoteAddress = req.connection().remoteAddress();
            System.out.println("remote address " + remoteAddress.toString());
            req.bodyHandler((Buffer body) -> {
                String path = req.path();
                //区块链大数据分析页面
                if (path.equals("/index")) {
                    req.response().putHeader("Content-type", "text/html;charset=utf-8").end("<html><head></head><body>hello world</body></html>", "utf-8");
                } else {
                    if (body.length() > 0) {
                        String reqXmlStr = body.toString();
                        System.out.print("req body content: " + reqXmlStr);
                        try {
                            WechatXml reqXml = WechatXmlHelper.getWechatXml(reqXmlStr);
                            if (reqXml.isMsgXml()) {
                                System.out.println(req.connection().remoteAddress() + ": " + reqXml.convertToWechatMsgXml().getMsgContent());
                                WechatMsgXml respXml = WechatXmlHelper.reqToRespMsg(reqXml);
                                //全部编码改成utf-8
                                respXml.setMsgContent(new String("生活就像海洋，只有意志坚强的人才能到达彼岸".getBytes(), "utf-8"));
                                req.response().putHeader("Content-type", "text/xml;charset=utf-8").end(respXml.toString(), "utf-8");
                            } else {
                                req.response().putHeader("Content-type", "text/plain;charset=utf-8").end("success", "utf-8");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
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

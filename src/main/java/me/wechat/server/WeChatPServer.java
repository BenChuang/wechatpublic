package me.wechat.server;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.net.SocketAddress;
import me.wechat.util.WechatMsgType;
import me.wechat.util.WechatMsgXml;
import me.wechat.util.WechatXmlHelper;
import org.dom4j.DocumentException;
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
                    req.response().setChunked(true).write("<html><head></head><body>hello world</body></html>").end();
                } else {
                    if (body.length() > 0) {
                        String reqXmlStr = body.toString();
                        try {
                            WechatMsgXml reqXml = WechatXmlHelper.getWechatMsgXml(reqXmlStr);
                            if (reqXml.getMsgType() == WechatMsgType.TEXT) {
                                System.out.println(req.connection().remoteAddress() + ": " + reqXml.getStringContent());
                                WechatMsgXml respXml = WechatXmlHelper.reqToRespMsg(reqXml);
                                respXml.setStringContent(new String("生活就像海洋，只有意志坚强的人才能到达彼岸".getBytes(), "ISO-8859-1"));
                                req.response().putHeader("Content-type", "text/xml;charset=utf-8").end(respXml.toString(), "utf-8");
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

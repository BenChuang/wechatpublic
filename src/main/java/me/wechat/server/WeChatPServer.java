package me.wechat.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.net.SocketAddress;
import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.Optional;


public class WeChatPServer {

    private static SAXBuilder builder = new SAXBuilder();

    public static void main(String[] args) {Element respXmlRoot = new Element("xml");
        Document respXml = new Document(respXmlRoot);
        CDATA reToUserName = new CDATA("ToUserName");
        reToUserName.setText("userOpid");
        CDATA reFromUserData = new CDATA("FromUserData");
        reFromUserData.setText("devName");
        Element reCreateTime = new Element("CreateTime");
        reCreateTime.setText("createTime");
        CDATA reMsgType = new CDATA("MsgType");
        reMsgType.setText("text");
        CDATA reContent = new CDATA("Content");
        reContent.setText("你好");
        respXmlRoot
                .addContent(reToUserName)
                .addContent(reFromUserData)
                .addContent(reCreateTime)
                .addContent(reMsgType)
                .addContent(reContent);
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();
        server.requestHandler(res -> {
            SocketAddress remoteAddress = res.connection().remoteAddress();
            System.out.println("remote address " + remoteAddress.toString());
            res.bodyHandler((Buffer body) -> {
                String path = res.path();
                //区块链大数据分析页面
                if (path.equals("/index")) {
                    res.response().setChunked(true).write("<html><head></head><body>hello world</body></html>").end();
                } else {
                    if (body.length() > 0) {
                        String rspXml = body.toString();
                        Element root = parseToXmlRootElement(rspXml);
                        Optional.ofNullable(root).ifPresent(r -> {
                            String devName = r.getChildText("ToUserName");
                            String userOpid = r.getChildText("FromUserName");
                            String createTime = r.getChildText("CreateTime");
                            String msgType = r.getChildText("MsgType");
                            String content = r.getChildText("Content");
                            String msgId = r.getChildText("MsgId");
//                            Element respXmlRoot = new Element("xml");
//                            Document respXml = new Document(respXmlRoot);
//                            CDATA reToUserName = new CDATA("ToUserName");
//                            reToUserName.setText(userOpid);
//                            CDATA reFromUserData = new CDATA("FromUserData");
//                            reFromUserData.setText(devName);
//                            Element reCreateTime = new Element("CreateTime");
//                            reCreateTime.setText(createTime);
//                            CDATA reMsgType = new CDATA("MsgType");
//                            reMsgType.setText("text");
//                            CDATA reContent = new CDATA("Content");
//                            reContent.setText("你好");
//                            respXmlRoot
//                                    .addContent(reToUserName)
//                                    .addContent(reFromUserData)
//                                    .addContent(reCreateTime)
//                                    .addContent(reMsgType)
//                                    .addContent(reContent);
//                            res.response().end(respXml.toString());

                        });
                        res.response().end("<xml> <ToUserName>< ![CDATA[toUser] ]></ToUserName> <FromUserName>< ![CDATA[gh_c5a9bb72c452] ]></FromUserName> <CreateTime>12345678</CreateTime> <MsgType>< ![CDATA[text] ]></MsgType> <Content>< ![CDATA[你好] ]></Content> </xml>");
                        System.out.println("Request body: " + body.toString());
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

    private static Element parseToXmlRootElement(String xmlStr) {
        try {
            xmlStr = xmlStr.replaceAll("<\\s!\\[", "<![");
            xmlStr = xmlStr.replaceAll("\\]\\s\\]>", "]]>");
            Document document = builder.build(new StringReader(xmlStr));
            return document.getRootElement();
        } catch (JDOMException | IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}

package me.wechat.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;

import java.io.StringReader;
import java.util.Date;
import java.util.List;

public class WechatXmlHelper {

    private static SAXReader reader = new SAXReader();

    private static <T extends WechatXml> T parseText(String strXml, Class<T> clazz) throws DocumentException {
        strXml = formatXml(strXml);
        Document document = reader.read(new StringReader(strXml));
        Element rootElement = document.getRootElement();
        List elements = rootElement.content();
        T wechatXml = getWechatXmlInstance(clazz);
        if (wechatXml != null) {
            for (Object ele : elements) {
                if (ele instanceof DefaultElement) {
                    DefaultElement element = ((DefaultElement) ele);
                    if (element.content().size() > 0) {
                        Node content = (Node) element.content().get(0);
                        String name = element.getName();
                        wechatXml.putNode(name, content);
                    }
                }
            }
        }
        return wechatXml;
    }

    public static WechatXml getWechatXml(String strXml) throws DocumentException {
        return parseText(strXml, WechatXml.class);
    }


    public static WechatMsgXml getWechatMsgXml(String strXml) throws DocumentException {
        WechatMsgXml msgXml = parseText(strXml, WechatMsgXml.class);
        String msgTypeStr = msgXml.getNodeString("msgType");
        switch (msgTypeStr) {
            case "text":
                msgXml.setNodeValue("msgType", WechatMsgType.TEXT);
        }
        return msgXml;
    }

    private static <T extends WechatXml> T getWechatXmlInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    public static WechatMsgXml reqToRespMsg(WechatXml reqXml) {
        WechatMsgXml respXml = new WechatMsgXml(WechatMsgType.TEXT);
        respXml.setNode("toUserName", reqXml.getNode("fromUserName"));
        respXml.setNode("fromUserName", reqXml.getNode("toUserName"));
        respXml.setNode("msgId", reqXml.getNode("msgId"));
        int currenTime = (int) (new Date().getTime() / 1000);
        respXml.setNodeValue("createtime", currenTime);
        return respXml;
    }

    private static String formatXml(String strXml) {
        strXml = strXml.replaceAll("<\\s?!\\s?\\[\\s?CDATA\\s?\\[", "<![CDATA[");
        strXml = strXml.replaceAll("\\]\\s?\\]\\s?>", "]]>");
        strXml = strXml.replaceAll("\\&", "&amp;");
        strXml = strXml.replaceAll("\n", "");
        return strXml;
    }

    public static void main(String[] args) {
        String reqStr = "<xml><ToUserName><![CDATA[gh_c5a9bb72c452]]></ToUserName>\n" +
                "<FromUserName><![CDATA[opkTR0qyq0_USM9ANM9ePExe5oPU]]></FromUserName>\n" +
                "<CreateTime>1532688724</CreateTime>\n" +
                "<MsgType><![CDATA[text]]></MsgType>\n" +
                "<Content><![CDATA[jj]]></Content>\n" +
                "<MsgId>6582847944962708364</MsgId>\n" +
                "</xml>\n";
        try {
            WechatMsgXml reqXml = WechatXmlHelper.getWechatMsgXml(reqStr);
            WechatMsgXml respXml = WechatXmlHelper.reqToRespMsg(reqXml);
            respXml.setMsgContent("hello wechat friend");
            System.out.println("req: " + reqXml.toString());
            System.out.println("resp: " + respXml.toString());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

}

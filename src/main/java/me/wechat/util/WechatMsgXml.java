package me.wechat.util;

public class WechatMsgXml extends WechatXml {

    public WechatMsgXml(){
        super();
    }

    public WechatMsgXml(WechatMsgType msgType) {
        super();
        setNodeValue("msgType", msgType);
    }

    public WechatMsgType getMsgType() {
        Object msgType = getNodeValue("msgType");
        if (msgType instanceof WechatMsgType) {
            return ((WechatMsgType) msgType);
        }
        return null;
    }

    public String getStringContent() {
        return getNodeString("content");
    }

    public void setStringContent(String content) {
        setNodeValue("content", content);
    }
}

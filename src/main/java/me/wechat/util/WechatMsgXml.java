package me.wechat.util;

public class WechatMsgXml extends WechatXml {

    public WechatMsgXml(){
        super();
    }

    public WechatMsgXml(String msgType) {
        super();
        setNodeValue("msgType", msgType);
    }

    public WechatMsgXml(String errerMsg, Throwable t) {
        super();
        isBadXml = true;
        this.errerMsg = errerMsg;
        this.errerThrowable = t;
    }

    public String getMsgType() {
        return getNodeValue("msgType").toString();
    }

    public String getMsgContent() {
        return getNodeString("content");
    }

    public void setMsgContent(String content) {
        setNodeValue("content", content);
    }
}

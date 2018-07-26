package me.wechat.util;

public class WechatXmlNode {

    private String nodeKey;
    private Object nodeValue;

    public WechatXmlNode(String nodeKey, Object nodeValue) {
        this.nodeKey = nodeKey;
        this.nodeValue = nodeValue;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    public Object getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(Object nodeValue) {
        this.nodeValue = nodeValue;
    }

    @Override
    public String toString() {
        return "<" + getNodeKey() + ">" +
                getNodeValue() +
                "</" + getNodeKey() + ">";
    }
}

package me.wechat.util;

public class WechatXmlCDATA extends WechatXmlNode {

    public WechatXmlCDATA(String nodeKey, Object nodeValue) {
        super(nodeKey, nodeValue);
    }

    @Override
    public String toString() {
        return "<" + getNodeKey() + ">" +
                "<![CDATA[" + getNodeValue() + "]]>" +
                "</" + getNodeKey() + ">";
    }
}

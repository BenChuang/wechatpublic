package me.wechat.util;

import java.util.*;

public class WechatXml {

    private WechatXmlNode rootNode = new WechatXmlNode("xml", "");
    private Map<String, WechatXmlNode> dataNodes = new LinkedHashMap<>(10);
    private boolean isBadXml;
    private String errerMsg = "";
    private Throwable errerThrowable;

    public WechatXml() {
        dataNodes.put("tousername", new WechatXmlCDATA("ToUserName", ""));
        dataNodes.put("fromusername", new WechatXmlCDATA("FromUserName", ""));
        dataNodes.put("createtime", new WechatXmlNode("CreateTime", ""));
        dataNodes.put("msgtype", new WechatXmlCDATA("MsgType", ""));
        dataNodes.put("content", new WechatXmlCDATA("Content", ""));
        dataNodes.put("msgid", new WechatXmlNode("MsgId", ""));
    }

    public WechatXml(String errerMsg, Throwable t) {
        this();
        isBadXml = true;
        this.errerMsg = errerMsg;
        this.errerThrowable = t;
    }

    public void setNodeValue(String key, Object value) {
        key = key.toLowerCase();
        WechatXmlNode node = dataNodes.get(key);
        if (node != null) {
            node.setNodeValue(value);
        }
    }

    public Object getNodeValue(String key) {
        key = key.toLowerCase();
        WechatXmlNode node = dataNodes.get(key);
        if (node != null) {
            return node.getNodeValue();
        }
        return null;
    }

    public String getNodeString(String key) {
        key = key.toLowerCase();
        WechatXmlNode node = dataNodes.get(key);
        if (node != null) {
            Object o = node.getNodeValue();
            return o.toString();
        }
        return null;
    }

    public Object getNodeInteger(String key) {
        key = key.toLowerCase();
        WechatXmlNode node = dataNodes.get(key);
        if (node != null) {
            Object o = node.getNodeValue();
            return Integer.parseInt(o.toString());
        }
        return null;
    }

    public Map<String, Object> getValueMap() {
        Map<String, Object> valueMap = new HashMap<>(dataNodes.size());
        dataNodes.forEach((k, v) -> valueMap.put(k, v.getNodeValue()));
        return valueMap;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<xml>");
        dataNodes.forEach((name, node) -> stringBuilder.append(node.toString()).append("\n"));
        stringBuilder.append("</xml>");
        return stringBuilder.toString();
    }



}

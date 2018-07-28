package me.wechat.util;

import org.dom4j.Node;
import org.dom4j.tree.DefaultCDATA;
import org.dom4j.tree.DefaultText;

import java.util.*;

public class WechatXml {

    WechatXmlNode rootNode = new WechatXmlNode("xml", "");
    Map<String, WechatXmlNode> dataNodes = new HashMap<>(10);
    boolean isBadXml;
    String errerMsg = "";
    Throwable errerThrowable;

    public WechatXml() {
    }

    public WechatXml(String errerMsg, Throwable t) {
        this();
        isBadXml = true;
        this.errerMsg = errerMsg;
        this.errerThrowable = t;
    }

    void putDomNode(String key, Node content) {
        if (content instanceof DefaultCDATA) {
            WechatXmlCDATA cdata = new WechatXmlCDATA(key, content.getStringValue());
            dataNodes.put(key.toLowerCase(), cdata);
        } else if (content instanceof DefaultText) {
            WechatXmlNode cdata = new WechatXmlNode(key, content.getStringValue());
            dataNodes.put(key.toLowerCase(), cdata);
        }
    }

    public void setNode(String key, WechatXmlNode node) {
        try {
            WechatXmlNode newNode = node.clone();
            newNode.setNodeKey(key);
            dataNodes.put(key.toLowerCase(), newNode);
        } catch (CloneNotSupportedException e) {
            //throw exception
        }
    }

    public WechatXmlNode getNode(String key) {
        return dataNodes.get(key.toLowerCase());
    }

    public void setNodeValue(String key, Object value) {
        WechatXmlNode node = dataNodes.get(key.toLowerCase());
        if (node != null) {
            node.setNodeValue(value);
        } else {
            if (value instanceof String) {
                setNode(key, new WechatXmlCDATA(key, value));
            } else {
                setNode(key, new WechatXmlNode(key, value));
            }
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

    public boolean isMsgXml() {
        WechatXmlNode msgTypeNode = dataNodes.get("msgtype");
        if (msgTypeNode != null) {
            return !"event".equals(msgTypeNode.getNodeValue().toString());
        }
        return false;
    }


    public boolean isEventXml() {
        WechatXmlNode msgTypeNode = dataNodes.get("msgtype");
        if (msgTypeNode != null) {
            return "event".equals(msgTypeNode.getNodeValue().toString());
        }
        return false;
    }

    public boolean isBadXml() {
        return isBadXml;
    }

    public WechatMsgXml convertToWechatMsgXml() {
        if (isMsgXml()) {
            if (isBadXml) {
                return new WechatMsgXml(this.errerMsg, this.errerThrowable);
            } else {
                WechatMsgXml wechatMsgXml = new WechatMsgXml(dataNodes.get("msgtype").toString());
                wechatMsgXml.dataNodes = dataNodes;
                return wechatMsgXml;
            }
        }
        return null;
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

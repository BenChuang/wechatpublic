package me.wechat.util;

public enum WechatMsgType {
    TEXT("text");

    private final String typeStr;

    WechatMsgType(String typeStr) {
        this.typeStr = typeStr;
    }

    public String getTypeStr() {
        return typeStr;
    }

    @Override
    public String toString() {
        return typeStr;
    }
}

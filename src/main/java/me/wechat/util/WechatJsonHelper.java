package me.wechat.util;

import io.vertx.core.json.JsonObject;
import me.wechat.sever.entity.UserInfo;

public class WechatJsonHelper {

    private static String convertULineToUCase(String jsonStr) {
        String[] strings = jsonStr.split("_");
        int length = strings.length;
        StringBuilder retSb = new StringBuilder();
        if (length > 0) {
            retSb.append(strings[0]);
            for (int i = 1; i < length; i++) {
                String s = strings[i];
                int l = s.length();
                String pre = "";
                String other = "";
                if (l > 0) {
                    pre = s.substring(0, 1).toUpperCase();
                }
                if (l > 1) {
                    other = s.substring(1);
                }
                retSb.append(pre).append(other);
            }
        }
        return retSb.toString();
    }

    public static <T> T parseText(String strXml, Class<T> clazz)  {
        JsonObject jsonObject = new JsonObject(strXml);
        JsonObject newJsonObject = new JsonObject();
        jsonObject.forEach(entry -> {
            String key = entry.getKey();
            key = convertULineToUCase(key);
            newJsonObject.put(key, entry.getValue());
        });
        return newJsonObject.mapTo(clazz);
    }

    public static void main(String[] args) {
        UserInfo userInfo = WechatJsonHelper.parseText("{    \"openid\":\" OPENID\",\n" +
                "\"nickname\": \"NICKNAME\",\n" +
                "\"sex\":\"1\",\n" +
                "\"province\":\"PROVINCE\",\n" +
                "\"city\":\"CITY\",\n" +
                "\"country\":\"COUNTRY\",\n" +
                "\"headimgurl\":    \"http://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46\",\n" +
                "\"privilege\":[ \"PRIVILEGE1\",\"PRIVILEGE2\"     ],\n" +
                "\"unionid\": \"o6_bmasdasdsad6_2sgVt7hMZOPfL\"\n" +
                "}", UserInfo.class);
        System.out.println(userInfo);
    }
}

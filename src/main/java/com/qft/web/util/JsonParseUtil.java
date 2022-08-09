package com.qft.web.util;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ognl.Ognl;
import ognl.OgnlContext;

import java.util.Map;

public class JsonParseUtil {
    /**
     * 将指定JSON转为Map对象，Key固定为String，JSONkey
     * Value分情况：
     * 1. Value是字符串，自动转为字符串,例如:{"a","b"}
     * 2. Value是其他JSON对象，自动转为Map，例如：{"a":{"b":"2"}}}
     * 3. Value是数组，自动转为List<Map>，例如：{"a":[{"b":"2"},"c":"3"]}
     * @param json 输入的JSON对象
     * @return 动态的Map集合
     */
    public static Map<String, Object> transferToMap(String json) {
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(json,
                new TypeToken<Map<String, Object>>() {
                }.getType());
        return map;
    }


    /**
     *简化方法
     * @param json 原始的JSON数据
     * @param path OGNL 规则表达式
     * @param clazz Value 对应的目标类
     * @return clazz 对应数据
     */
    public static <T> T getValue(String json, String path, Class<T> clazz) {
        try {
            Map map = transferToMap(json);
            OgnlContext context = new OgnlContext();
            context.setRoot(map);
            T value = (T) Ognl.getValue(path, context, context.getRoot());
            return value;
        } catch ( Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getValueFromMap(Map map, String path, Class<T> clazz) {
        try {
            OgnlContext context = new OgnlContext();
            context.setRoot(map);
            T value = (T) Ognl.getValue(path, context, context.getRoot());
            return value;
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }
}

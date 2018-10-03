package com.example.demo.utils.api;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author valord577
 * @date 18-8-22 下午6:47
 */
public class ToMaps {

    public static Map<String, String> convertToMap(Object object) {
        return convertToMap(object, null);
    }

    /**
     * Java Entity 转 map
     *
     * @param object Java实体类
     * @param capacity map的初始值大小
     * @return hashMap
     */
    public static Map<String, String> convertToMap(Object object, Integer capacity) {

        if (null == object) {
            return null;
        }

        if (null == capacity) {
            capacity = 16;
        }

        Map<String, String> map = new HashMap<>(capacity);
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String o = field.get(object).toString();
                map.put(field.getName(), o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;

    }


}

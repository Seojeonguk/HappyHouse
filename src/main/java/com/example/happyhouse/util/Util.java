package com.example.happyhouse.util;

import java.util.Collection;
import java.util.Map;

public class Util {
    /**
     * Checks if an object is empty or null. Supports String, Collection, Map, and Array.
     *
     * @param obj the object to check
     * @return true if the object is empty or null
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof String) {
            return ((String) obj).isEmpty();
        } else if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        } else if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        } else if (obj.getClass().isArray()) {
            return ((Object[]) obj).length == 0;
        }
        return false;
    }

    public static String removeSuffix(String str, String suffix) {
        if (isEmpty(str) || isEmpty(suffix) || !str.endsWith(suffix)) {
            return str;
        }
        return str.substring(0, str.length() - suffix.length()).trim();
    }
}

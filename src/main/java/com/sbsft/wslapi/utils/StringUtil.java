package com.sbsft.wslapi.utils;

import org.springframework.util.StringUtils;

public class StringUtil {
    public StringUtil() {
    }

    public static boolean isEmpty(String str) {
        return isEmpty(str, true);
    }

    public static boolean isEmpty(Object obj) {
        String str = "";
        if (obj == null) {
            str = "";
        } else {
            str = obj.toString();
        }

        return isEmpty(str, true);
    }

    public static boolean isEmpty(String str, boolean checkBlank) {
        if (isNull(str)) {
            return true;
        } else {
            if (checkBlank) {
                str = str.trim();
            }

            return StringUtils.isEmpty(str);
        }
    }




    public static boolean isNull(String str) {
        return str == null;
    }




}
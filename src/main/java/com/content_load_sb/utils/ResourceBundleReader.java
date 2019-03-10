package com.content_load_sb.utils;

import com.content_load_sb.helper.Helper;

import java.util.Map;

public class ResourceBundleReader {
    public static String getExchangeProperties(String key) {
        String propertyFileName = "Content_Load.properties";
        Map<String, Object> hashProp = Helper.getInstance().readProperties(propertyFileName);
        return (String) hashProp.get(key);
    }
}

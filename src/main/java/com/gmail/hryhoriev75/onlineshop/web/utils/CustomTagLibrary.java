package com.gmail.hryhoriev75.onlineshop.web.utils;

public class CustomTagLibrary {

    public static boolean contains(String[] list, String s) {
        if (list != null && s != null) {
            for (String el: list) {
                if (s.equals(el)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean contains(java.util.List<String> list, String s) {
        return list != null && s != null && list.contains(s);
    }

}

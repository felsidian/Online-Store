package com.gmail.hryhoriev75.onlineshop.web.utils;

/**
 * Custom JSP Tag with function
 */
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

}

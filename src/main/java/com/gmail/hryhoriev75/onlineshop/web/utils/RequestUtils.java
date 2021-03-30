package com.gmail.hryhoriev75.onlineshop.web.utils;

import com.gmail.hryhoriev75.onlineshop.model.OrderDAO;
import com.gmail.hryhoriev75.onlineshop.model.entity.Order;
import com.gmail.hryhoriev75.onlineshop.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestUtils {

    // returns parameter value or 0 if there is no such parameter or it's not a long number
    public static long getLongParameter(HttpServletRequest request, String parameterName) {
        try {
            return Long.parseLong(request.getParameter(parameterName));
        } catch(NumberFormatException nfe) {
            return 0;
        }
    }

    // returns parameter value or null if there is no such parameter or it's not a long number
    public static BigDecimal getBigDecimalParameter(HttpServletRequest request, String parameterName) {
        try {
            return new BigDecimal(request.getParameter(parameterName));
        } catch(NumberFormatException nfe) {
            return  null;
        }
    }

    // returns parameter value or 0 if there is no such parameter or it's not a long number
    public static int getIntParameter(HttpServletRequest request, String parameterName) {
        try {
            return Integer.parseInt(request.getParameter(parameterName));
        } catch(NumberFormatException nfe) {
            return 0;
        }
    }

    // returns parameter value or null if there is no such parameter
    public static String getStringParameter(HttpServletRequest request, String parameterName) {
        return request.getParameter(parameterName);
    }

    public static <T> T getSessionAttribute(HttpServletRequest request, String attributeName, Class<T> clazz) {
      HttpSession session = request.getSession(false);
      if (session != null && attributeName != null) {
          Object attribute = session.getAttribute(attributeName);
          if (clazz.isInstance(attribute)) {
              return clazz.cast(attribute);
          }
      }
      return null;
    }

    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String> resultMap = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> resultMap.put(key, value[0]));
        return resultMap;
    }

    public static String parameterMapToQuery(Map<String, String> map) {
        return map.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).reduce((p1, p2) -> p1 + "&" + p2).orElse("");
    }

}

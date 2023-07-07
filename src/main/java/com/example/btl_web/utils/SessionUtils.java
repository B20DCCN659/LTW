package com.example.btl_web.utils;

import javax.servlet.http.HttpServletRequest;

public class SessionUtils {
    private static SessionUtils sesstionUtils = null;
    public static SessionUtils getInstance()
    {
        if(sesstionUtils == null)
            sesstionUtils = new SessionUtils();
        return sesstionUtils;
    }

    public void putValue(HttpServletRequest request, String key, Object value)
    {
        request.getSession().setAttribute(key, value);
    }

    public Object getValue(HttpServletRequest request, String key)
    {
        return request.getSession().getAttribute(key);
    }

    public void removeValue(HttpServletRequest request, String key)
    {
        request.getSession().removeAttribute(key);
    }
}

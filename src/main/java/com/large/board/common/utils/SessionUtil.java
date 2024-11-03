package com.large.board.common.utils;

import jakarta.servlet.http.HttpSession;

public class SessionUtil {
    private SessionUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final String LOGIN_MEMBER_ID = "LOGIN_MEMBER_ID";
    private static final String LOGIN_ADMIN_ID = "LOGIN_ADMIN_ID";

    public static void setLoginId(HttpSession session, String key, String id) {
        session.setAttribute(key, id);
    }

    public static String getLoginId(HttpSession session, String key) {
        return (String) session.getAttribute(key);
    }

    public static void setLoginMemberId(HttpSession session, String id) {
        setLoginId(session, LOGIN_MEMBER_ID, id);
    }

    public static String getLoginMemberId(HttpSession session) {
        return getLoginId(session, LOGIN_MEMBER_ID);
    }

    public static void setLoginAdminId(HttpSession session, String id) {
        setLoginId(session, LOGIN_ADMIN_ID, id);
    }

    public static String getLoginAdminId(HttpSession session) {
        return getLoginId(session, LOGIN_ADMIN_ID);
    }

    public static void clear(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }
}

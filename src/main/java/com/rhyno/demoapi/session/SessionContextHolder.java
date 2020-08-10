package com.rhyno.demoapi.session;

public class SessionContextHolder {
    private static final String BASE_TENANT = "TNT_BASE";

    private static ThreadLocal<UserSession> session = new ThreadLocal<>();

    public static void setSession(UserSession session) {
        SessionContextHolder.session.set(session);
    }

    public static UserSession getSession() {
        // TODO. set temporary session
        SessionContextHolder.setSession(UserSession.builder()
                .tenantId(BASE_TENANT)
                .build());

        return SessionContextHolder.session.get();
    }

    public static void clear() {
        SessionContextHolder.session.remove();
    }
}

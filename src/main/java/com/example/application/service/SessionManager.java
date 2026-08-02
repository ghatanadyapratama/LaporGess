package com.example.application.service;

import com.vaadin.flow.server.VaadinSession;

/**
 * SessionManager - Simpan dan ambil data user yang sedang login
 * menggunakan Vaadin VaadinSession (per-user session).
 */
public class SessionManager {

    private static final String KEY_USERNAME = "currentUsername";
    private static final String KEY_PERAN = "currentPeran";
    private static final String KEY_NAMA = "currentNama";
    private static final String KEY_POIN = "currentPoin";

    public static void login(String username, String peran, String namaLengkap, int poin) {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(KEY_USERNAME, username);
            session.setAttribute(KEY_PERAN, peran);
            session.setAttribute(KEY_NAMA, namaLengkap);
            session.setAttribute(KEY_POIN, poin);
        }
    }

    public static void logout() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(KEY_USERNAME, null);
            session.setAttribute(KEY_PERAN, null);
            session.setAttribute(KEY_NAMA, null);
            session.setAttribute(KEY_POIN, null);
        }
    }

    public static String getUsername() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session == null) return null;
        Object val = session.getAttribute(KEY_USERNAME);
        return val != null ? val.toString() : null;
    }

    public static String getPeran() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session == null) return null;
        Object val = session.getAttribute(KEY_PERAN);
        return val != null ? val.toString() : null;
    }

    public static String getNama() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session == null) return null;
        Object val = session.getAttribute(KEY_NAMA);
        return val != null ? val.toString() : null;
    }

    public static int getPoin() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session == null) return 0;
        Object val = session.getAttribute(KEY_POIN);
        return val != null ? (int) val : 0;
    }

    public static boolean isLoggedIn() {
        return getUsername() != null;
    }

    public static boolean isAdmin() {
        return "ADMIN".equals(getPeran());
    }

    public static boolean isPetugas() {
        return "PETUGAS_LAPANGAN".equals(getPeran());
    }

    public static boolean isWarga() {
        return "WARGA".equals(getPeran());
    }
}

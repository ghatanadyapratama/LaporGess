package com.example.application.views.admin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;

public class AdminLayout {

    public static Div buildSidebar(String activeRoute) {
        Div sidebar = new Div();
        sidebar.addClassName("ad-sidebar");

        // Logo Header
        Div logo = new Div();
        logo.addClassName("ad-logo");
        Image logoImg = new Image("icons/logoLaporGess.png", "Admin Logo");
        logoImg.addClassName("ad-logo-img");
        Span logoTxt = new Span("Admin Lapor");
        logoTxt.addClassName("ad-logo-txt");
        logo.add(logoImg, logoTxt);
        sidebar.add(logo);

        // Navigation Menu
        Div nav = new Div();
        nav.addClassName("ad-nav");

        // 1. Dasbor
        Div dasborItem = createNavItem("icons/home.png", "Dasbor", "admin/dashboard".equals(activeRoute), null, null);
        dasborItem.addClickListener(e -> UI.getCurrent().navigate("admin/dashboard"));
        nav.add(dasborItem);

        // 2. Jadwal Petugas
        Div jadwalItem = createNavItem("icons/iconWaktu.png", "Jadwal Petugas", "admin/jadwal-petugas".equals(activeRoute), null, null);
        jadwalItem.addClickListener(e -> UI.getCurrent().navigate("admin/jadwal-petugas"));
        nav.add(jadwalItem);

        // 3. Laporan (badge: 5)
        Div laporanItem = createNavItem("icons/laporan.png", "Laporan", "admin/laporan".equals(activeRoute), "5", "ad-nav-badge-orange");
        laporanItem.addClickListener(e -> UI.getCurrent().navigate("admin/laporan"));
        nav.add(laporanItem);

        // 4. Pengguna (badge: 2)
        Div penggunaItem = createNavItem("icons/profile.png", "Pengguna", "admin/pengguna".equals(activeRoute), "2", "ad-nav-badge-orange");
        penggunaItem.addClickListener(e -> UI.getCurrent().navigate("admin/pengguna"));
        nav.add(penggunaItem);

        // 5. Verifikasi (badge: 3)
        Div verifikasiItem = createNavItem("icons/ceklist.png", "Verifikasi", "admin/verifikasi".equals(activeRoute), "3", "ad-nav-badge-red");
        verifikasiItem.addClickListener(e -> UI.getCurrent().navigate("admin/verifikasi"));
        nav.add(verifikasiItem);

        sidebar.add(nav);

        // Footer Admin Profile
        Div footer = new Div();
        footer.addClassName("ad-sidebar-footer");
        Div avatar = new Div(new Span("A"));
        avatar.addClassName("ad-avatar");

        Div userInfo = new Div();
        userInfo.addClassName("ad-user-info");
        Span userName = new Span("Admin Utama");
        userName.addClassName("ad-user-name");
        Span userSub = new Span("Kantor Pusat");
        userSub.addClassName("ad-user-sub");
        userInfo.add(userName, userSub);

        footer.add(avatar, userInfo);
        sidebar.add(footer);

        return sidebar;
    }

    private static Div createNavItem(String iconPath, String label, boolean isActive, String badgeText, String badgeClass) {
        Div item = new Div();
        item.addClassName("ad-nav-item");
        if (isActive) {
            item.addClassName("ad-nav-active");
        }

        Image icon = new Image(iconPath, label);
        icon.addClassName("ad-nav-icon");

        Span txt = new Span(label);
        txt.addClassName("ad-nav-label");

        item.add(icon, txt);

        if (badgeText != null) {
            Span badge = new Span(badgeText);
            badge.addClassName("ad-nav-badge");
            if (badgeClass != null) {
                badge.addClassName(badgeClass);
            }
            item.add(badge);
        }

        return item;
    }

    public static Div buildTopbar(String titleText) {
        Div topbar = new Div();
        topbar.addClassName("ad-topbar");

        Span title = new Span(titleText);
        title.addClassName("ad-topbar-title");

        // Notification Bell Icon with Popup
        Div notifBtn = new Div();
        notifBtn.addClassName("ad-notif-btn");
        Image bellIcon = new Image("icons/bell.png", "Notifikasi");
        bellIcon.getStyle().set("width", "20px").set("height", "20px");
        Div notifDot = new Div();
        notifDot.addClassName("ad-notif-dot");
        notifBtn.add(bellIcon, notifDot);

        // Popup message card
        Div notifPopup = new Div();
        notifPopup.addClassName("ad-notif-popup");
        notifPopup.setVisible(true); // Default visible like in screenshot or toggle on click

        Div iconBox = new Div(new Span("🔔"));
        iconBox.addClassName("ad-notif-icon-box");

        Div content = new Div();
        content.addClassName("ad-notif-content");

        Div titleRow = new Div();
        titleRow.addClassName("ad-notif-title-row");
        Span notifTitle = new Span("Laporan Baru Masuk!");
        notifTitle.addClassName("ad-notif-title");
        Span notifTime = new Span("Baru saja");
        notifTime.addClassName("ad-notif-time");
        titleRow.add(notifTitle, notifTime);

        Span notifBody = new Span("Pohon tumbang menutup jalan utama di area RT 02 / RW 01.");
        notifBody.addClassName("ad-notif-body");

        content.add(titleRow, notifBody);
        notifPopup.add(iconBox, content);

        notifBtn.addClickListener(e -> UI.getCurrent().navigate("admin/notifikasi"));
        notifPopup.addClickListener(e -> UI.getCurrent().navigate("admin/notifikasi"));

        topbar.add(title, notifBtn, notifPopup);

        return topbar;
    }
}

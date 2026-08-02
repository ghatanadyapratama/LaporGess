package com.example.application.views.petugas;

import com.example.application.service.SessionManager;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;

public class PetugasLayout {

    public static Div buildSidebar(String activeRoute) {
        Div sidebar = new Div();
        sidebar.addClassName("pt-sidebar");

        // 1. Logo Row
        Div logoRow = new Div();
        logoRow.addClassName("pt-logo-row");
        Div logoBox = new Div();
        logoBox.addClassName("pt-logo-box");
        Image logoImg = new Image("icons/logoLaporGess.png", "logo");
        logoImg.addClassName("pt-logo-img");
        logoBox.add(logoImg);
        logoRow.add(logoBox);
        sidebar.add(logoRow);

        // 2. Profile Info Box
        Div profileBox = new Div();
        profileBox.addClassName("pt-profile-box");
        String nama = SessionManager.getNama();
        Span name = new Span(nama != null && !nama.isEmpty() ? nama : "Petugas Lapangan");
        name.addClassName("pt-profile-name");
        Div locRow = new Div();
        locRow.addClassName("pt-profile-loc-row");
        Span locIcon = new Span("📍");
        // Show username as subtitle since area/kecamatan may not be in session
        String usernameLabel = SessionManager.getUsername();
        Span locText = new Span(usernameLabel != null ? "@" + usernameLabel : "Petugas Lapangan");
        locRow.add(locIcon, locText);
        profileBox.add(name, locRow);
        sidebar.add(profileBox);


        // 3. Navigation using image icons
        Div nav = new Div();
        nav.addClassName("pt-nav");

        // Pekerjaan Tersedia (active for dashboard and detail-pekerjaan)
        boolean isPekerjaanActive = "petugas/dashboard".equals(activeRoute) || "petugas/detail-pekerjaan".equals(activeRoute) || "petugas".equals(activeRoute);
        Div menu1 = createNavItem("icons/pekerjaan.png", "Pekerjaan Tersedia", isPekerjaanActive);
        menu1.addClickListener(e -> UI.getCurrent().navigate("petugas/dashboard"));

        Div menu2 = createNavItem("icons/jam.png", "Jadwal Shift", "petugas/jadwal-shift".equals(activeRoute));
        menu2.addClickListener(e -> UI.getCurrent().navigate("petugas/jadwal-shift"));

        Div menu3 = createNavItem("icons/tugas.png", "Tugas Saya", "petugas/tugas-saya".equals(activeRoute));
        menu3.addClickListener(e -> UI.getCurrent().navigate("petugas/tugas-saya"));

        Div menu4 = createNavItem("icons/riwayat.png", "Riwayat Selesai", "petugas/riwayat-selesai".equals(activeRoute));
        menu4.addClickListener(e -> UI.getCurrent().navigate("petugas/riwayat-selesai"));

        Div menu5 = createNavItem("icons/profile.png", "Profil", "petugas/profil".equals(activeRoute));
        menu5.addClickListener(e -> UI.getCurrent().navigate("petugas/profil"));

        nav.add(menu1, menu2, menu3, menu4, menu5);
        sidebar.add(nav);

        return sidebar;
    }

    private static Div createNavItem(String iconPath, String label, boolean isActive) {
        Div item = new Div();
        item.addClassName("pt-nav-item");
        if (isActive) {
            item.addClassName("pt-nav-active");
        }
        Image icon = new Image(iconPath, label);
        icon.getStyle().set("width", "18px").set("height", "18px").set("object-fit", "contain");
        // Add brightness filter to match sidebar gray / white hover states
        if (isActive) {
            icon.getStyle().set("filter", "brightness(0) invert(1)"); // white icon for active
        } else {
            icon.getStyle().set("filter", "opacity(0.6) grayscale(1)"); // gray icon for inactive
        }
        Span labelSpan = new Span(label);
        item.add(icon, labelSpan);
        return item;
    }

    public static Div buildTopbar(String titleText) {
        Div topbar = new Div();
        topbar.addClassName("pt-topbar");

        Span title = new Span(titleText);
        title.addClassName("pt-topbar-title");

        Div notifBtn = new Div();
        notifBtn.addClassName("pt-notif-btn");
        Image bellIcon = new Image("icons/bell.png", "Notifikasi");
        bellIcon.getStyle().set("width", "20px").set("height", "20px");
        notifBtn.add(bellIcon);
        notifBtn.addClickListener(e -> UI.getCurrent().navigate("petugas/notifikasi"));

        topbar.add(title, notifBtn);
        return topbar;
    }
}

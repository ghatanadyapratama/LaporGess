package com.example.application.views.admin;

import com.example.application.model.Notifikasi;
import com.example.application.model.Pengguna;
import com.example.application.repository.NotifikasiRepository;
import com.example.application.service.SessionManager;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class AdminLayout {

    public static Div buildSidebar(String activeRoute) {
        return buildSidebar(activeRoute, -1, -1, -1);
    }

    public static Div buildSidebar(String activeRoute, long laporanPending, long penggunaBaru, long verifikasiPending) {
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

        // 3. Laporan (badge: laporan PENDING count)
        String laporanBadge = laporanPending > 0 ? String.valueOf(laporanPending) : null;
        Div laporanItem = createNavItem("icons/laporan.png", "Laporan", "admin/laporan".equals(activeRoute), laporanBadge, "ad-nav-badge-orange");
        laporanItem.addClickListener(e -> UI.getCurrent().navigate("admin/laporan"));
        nav.add(laporanItem);

        // 4. Pengguna (badge: pengguna aktif petugas count)
        String penggunaBadge = penggunaBaru > 0 ? String.valueOf(penggunaBaru) : null;
        Div penggunaItem = createNavItem("icons/profile.png", "Pengguna", "admin/pengguna".equals(activeRoute), penggunaBadge, "ad-nav-badge-orange");
        penggunaItem.addClickListener(e -> UI.getCurrent().navigate("admin/pengguna"));
        nav.add(penggunaItem);

        // 5. Verifikasi (badge: pengguna PENDING count)
        String verifBadge = verifikasiPending > 0 ? String.valueOf(verifikasiPending) : null;
        Div verifikasiItem = createNavItem("icons/ceklist.png", "Verifikasi", "admin/verifikasi".equals(activeRoute), verifBadge, "ad-nav-badge-red");
        verifikasiItem.addClickListener(e -> UI.getCurrent().navigate("admin/verifikasi"));
        nav.add(verifikasiItem);

        sidebar.add(nav);

        // Footer Admin Profile
        Div footer = new Div();
        footer.addClassName("ad-sidebar-footer");
        String adminName = SessionManager.getNama();
        if (adminName == null || adminName.isEmpty()) {
            adminName = "Admin Utama";
        }
        Div avatar = new Div(new Span(adminName.substring(0, 1).toUpperCase()));
        avatar.addClassName("ad-avatar");

        Div userInfo = new Div();
        userInfo.addClassName("ad-user-info");
        Span userName = new Span(adminName);
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
        return buildTopbar(titleText, null);
    }

    public static Div buildTopbar(String titleText, NotifikasiRepository notifikasiRepository) {
        DateTimeFormatter FMT_TIME = DateTimeFormatter.ofPattern("dd MMM, HH:mm");

        // Ambil notifikasi dari DB jika repo tersedia
        List<Notifikasi> notifList = Collections.emptyList();
        if (notifikasiRepository != null) {
            notifList = notifikasiRepository.findByPengguna_PeranOrderByDibuatPadaDesc(Pengguna.Peran.ADMIN);
        }
        long unreadCount = notifList.stream().filter(n -> !n.isDibaca()).count();

        Div topbar = new Div();
        topbar.addClassName("ad-topbar");

        Span title = new Span(titleText);
        title.addClassName("ad-topbar-title");

        // Notification Bell — popup hanya muncul saat diklik
        Div notifWrapper = new Div();
        notifWrapper.getStyle().set("position", "relative");

        Div notifBtn = new Div();
        notifBtn.addClassName("ad-notif-btn");
        Image bellIcon = new Image("icons/bell.png", "Notifikasi");
        bellIcon.getStyle().set("width", "20px").set("height", "20px");
        Div notifDot = new Div();
        notifDot.addClassName("ad-notif-dot");
        if (unreadCount == 0) notifDot.setVisible(false);
        notifBtn.add(bellIcon, notifDot);

        // Popup — tersembunyi secara default
        Div notifPopup = new Div();
        notifPopup.addClassName("ad-notif-popup");
        notifPopup.setVisible(false);

        if (notifList.isEmpty()) {
            // Kosong
            Div emptyRow = new Div();
            emptyRow.getStyle()
                .set("padding", "20px 16px")
                .set("text-align", "center")
                .set("color", "#94A3B8")
                .set("font-size", "0.88rem");
            emptyRow.add(new Span("Tidak ada notifikasi baru."));
            notifPopup.add(emptyRow);
        } else {
            // Tampilkan max 3 notifikasi terbaru
            int max = Math.min(3, notifList.size());
            for (int i = 0; i < max; i++) {
                Notifikasi notif = notifList.get(i);

                String iconSymbol = switch (notif.getTipe()) {
                    case "WARNING" -> "⚠️";
                    case "SUCCESS" -> "✅";
                    default -> "🔔";
                };

                Div iconBox = new Div(new Span(iconSymbol));
                iconBox.addClassName("ad-notif-icon-box");

                Div content = new Div();
                content.addClassName("ad-notif-content");

                Div titleRow = new Div();
                titleRow.addClassName("ad-notif-title-row");
                Span notifTitle = new Span(notif.getTipe());
                notifTitle.addClassName("ad-notif-title");
                String timeStr = notif.getDibuatPada() != null ? notif.getDibuatPada().format(FMT_TIME) : "Baru saja";
                Span notifTime = new Span(timeStr);
                notifTime.addClassName("ad-notif-time");
                titleRow.add(notifTitle, notifTime);

                Span notifBody = new Span(notif.getPesan());
                notifBody.addClassName("ad-notif-body");

                content.add(titleRow, notifBody);

                Div row = new Div();
                row.addClassName("ad-notif-row");
                if (!notif.isDibaca()) row.addClassName("ad-notif-row-unread");
                row.add(iconBox, content);
                notifPopup.add(row);
            }

            if (notifList.size() > 3) {
                Span more = new Span("Lihat semua notifikasi →");
                more.addClassName("ad-notif-more");
                more.getStyle().set("cursor", "pointer");
                more.addClickListener(e -> UI.getCurrent().navigate("admin/notifikasi"));
                notifPopup.add(more);
            }
        }

        // Toggle popup saat bell diklik
        notifBtn.addClickListener(e -> notifPopup.setVisible(!notifPopup.isVisible()));
        // Klik popup area kosong → navigasi ke halaman notifikasi
        notifPopup.addClickListener(e -> UI.getCurrent().navigate("admin/notifikasi"));

        notifWrapper.add(notifBtn, notifPopup);
        topbar.add(title, notifWrapper);

        return topbar;
    }
}

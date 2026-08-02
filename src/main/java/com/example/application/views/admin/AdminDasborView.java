package com.example.application.views.admin;

import com.example.application.views.BlankLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "admin", layout = BlankLayout.class)
@PageTitle("Dasbor - Admin Lapor")
public class AdminDasborView extends Div {

    public AdminDasborView() {
        addClassName("a-root");
        add(buildSidebar("dasbor"), buildMain());
        add(buildNotificationToast());
    }

    // ══════════════════════════════════════════
    //  SIDEBAR (shared helper)
    // ══════════════════════════════════════════
    static Div buildSidebar(String activePage) {
        Div sidebar = new Div();
        sidebar.addClassName("a-sidebar");

        // Logo
        Div logo = new Div();
        logo.addClassName("a-logo");
        Image logoImg = new Image("icons/logoLaporGess.png", "logo");
        logoImg.addClassName("a-logo-img");
        Span logoTxt = new Span("Admin Lapor");
        logoTxt.addClassName("a-logo-txt");
        logo.add(logoImg, logoTxt);
        sidebar.add(logo);

        // Nav
        Div nav = new Div();
        nav.addClassName("a-nav");

        Div dasborNav = navItem("📊", "Dasbor", "dasbor".equals(activePage), null);
        dasborNav.addClickListener(e -> dasborNav.getUI().ifPresent(ui -> ui.navigate("admin")));
        nav.add(dasborNav);

        Div jadwalNav = navItem("📋", "Jadwal Petugas", "jadwal".equals(activePage), null);
        jadwalNav.addClickListener(e -> jadwalNav.getUI().ifPresent(ui -> ui.navigate("admin/jadwal")));
        nav.add(jadwalNav);

        Div laporanNav = navItem("📄", "Laporan", "laporan".equals(activePage), "5");
        laporanNav.addClickListener(e -> laporanNav.getUI().ifPresent(ui -> ui.navigate("admin/laporan")));
        nav.add(laporanNav);

        Div penggunaNav = navItem("👥", "Pengguna", "pengguna".equals(activePage), "2");
        penggunaNav.addClickListener(e -> penggunaNav.getUI().ifPresent(ui -> ui.navigate("admin/pengguna")));
        nav.add(penggunaNav);

        Div verifNav = navItem("✅", "Verifikasi", "verifikasi".equals(activePage), "3");
        verifNav.addClickListener(e -> verifNav.getUI().ifPresent(ui -> ui.navigate("admin/verifikasi")));
        nav.add(verifNav);

        sidebar.add(nav);

        // Admin profile at bottom
        Div profile = new Div();
        profile.addClassName("a-admin-profile");
        Div avatar = new Div();
        avatar.addClassName("a-admin-avatar");
        avatar.add(new Span("A"));
        Div info = new Div();
        info.addClassName("a-admin-info");
        Span name = new Span("Admin Utama");
        name.addClassName("a-admin-name");
        Span role = new Span("Kantor Pusat");
        role.addClassName("a-admin-role");
        info.add(name, role);
        profile.add(avatar, info);
        sidebar.add(profile);

        return sidebar;
    }

    static Div navItem(String icon, String label, boolean active, String badgeCount) {
        Div item = new Div();
        item.addClassName("a-nav-item");
        if (active) item.addClassName("a-nav-active");

        Span iconSpan = new Span(icon);
        iconSpan.addClassName("a-nav-icon");
        Span labelSpan = new Span(label);
        labelSpan.addClassName("a-nav-label");
        item.add(iconSpan, labelSpan);

        if (badgeCount != null) {
            Span badge = new Span(badgeCount);
            badge.addClassName("a-nav-badge");
            item.add(badge);
        }

        return item;
    }

    // ══════════════════════════════════════════
    //  MAIN CONTENT
    // ══════════════════════════════════════════
    private Div buildMain() {
        Div main = new Div();
        main.addClassName("a-main");

        // Topbar
        Div topbar = new Div();
        topbar.addClassName("a-topbar");
        Span title = new Span("Dasbor");
        title.addClassName("a-topbar-title");
        Div bell = new Div();
        bell.addClassName("a-bell");
        bell.add(new Span("🔔"));
        Div bellDot = new Div();
        bellDot.addClassName("a-bell-dot");
        bell.add(bellDot);
        topbar.add(title, bell);
        main.add(topbar);

        // Body
        Div body = new Div();
        body.addClassName("a-body");
        body.add(buildStatsRow());
        body.add(buildContentRow());
        body.add(buildChartSection());
        main.add(body);

        return main;
    }

    // ── Stats row ────────────────────────────
    private Div buildStatsRow() {
        Div row = new Div();
        row.addClassName("a-stats-row");
        row.add(statCard("📋", "a-stat-icon-orange", "Total Laporan", "1.248"));
        row.add(statCard("✅", "a-stat-icon-teal", "Selesai", "984"));
        row.add(statCard("⏳", "a-stat-icon-amber", "Menunggu\nVerifikasi", "12"));
        row.add(statCard("❌", "a-stat-icon-red", "Ditolak", "486"));
        return row;
    }

    private Div statCard(String icon, String iconClass, String label, String value) {
        Div card = new Div();
        card.addClassName("a-stat-card");

        Div iconBox = new Div();
        iconBox.addClassName("a-stat-icon");
        iconBox.addClassName(iconClass);
        iconBox.add(new Span(icon));

        Div info = new Div();
        info.addClassName("a-stat-info");
        Span lbl = new Span(label);
        lbl.addClassName("a-stat-label");
        Span val = new Span(value);
        val.addClassName("a-stat-value");
        info.add(lbl, val);

        card.add(iconBox, info);
        return card;
    }

    // ── Content row: Map + Pending ───────────
    private Div buildContentRow() {
        Div row = new Div();
        row.addClassName("a-content-row");
        row.add(buildMapSection());
        row.add(buildPendingPanel());
        return row;
    }

    private Div buildMapSection() {
        Div section = new Div();
        section.addClassName("a-map-section");

        // Header with legend
        Div header = new Div();
        header.addClassName("a-map-header");
        Span title = new Span("Peta Sebaran Laporan");
        title.addClassName("a-section-title");

        Div legend = new Div();
        legend.addClassName("a-map-legend");
        legend.add(legendItem("Darurat", "a-legend-red"));
        legend.add(legendItem("Menengah", "a-legend-orange"));
        legend.add(legendItem("Ringan", "a-legend-green"));
        header.add(title, legend);
        section.add(header);

        // Map container with dots
        Div mapContainer = new Div();
        mapContainer.addClassName("a-map-container");
        mapContainer.add(mapDot("a-map-dot-green", "30%", "75%"));
        mapContainer.add(mapDot("a-map-dot-red", "45%", "55%"));
        mapContainer.add(mapDot("a-map-dot-green", "65%", "25%"));
        mapContainer.add(mapDot("a-map-dot-orange", "75%", "65%"));
        mapContainer.add(mapDot("a-map-dot-red", "55%", "40%"));
        section.add(mapContainer);

        return section;
    }

    private Div legendItem(String text, String dotClass) {
        Div item = new Div();
        item.addClassName("a-legend-item");
        Div dot = new Div();
        dot.addClassName("a-legend-dot");
        dot.addClassName(dotClass);
        item.add(dot, new Span(text));
        return item;
    }

    private Div mapDot(String colorClass, String left, String top) {
        Div dot = new Div();
        dot.addClassName("a-map-dot");
        dot.addClassName(colorClass);
        dot.getStyle().set("left", left);
        dot.getStyle().set("top", top);
        Div inner = new Div();
        inner.addClassName("a-map-dot-inner");
        dot.add(inner);
        return dot;
    }

    // ── Pending registrations ────────────────
    private Div buildPendingPanel() {
        Div panel = new Div();
        panel.addClassName("a-pending-panel");

        Span title = new Span("Pendaftaran Tertunda");
        title.addClassName("a-pending-title");
        panel.add(title);

        Div list = new Div();
        list.addClassName("a-pending-list");
        list.add(pendingItem("P1", "Pengguna Baru 1", "RT 01/02"));
        list.add(pendingItem("P2", "Pengguna Baru 2", "RT 01/02"));
        list.add(pendingItem("P3", "Pengguna Baru 3", "RT 01/02"));
        panel.add(list);

        Span lihat = new Span("Lihat Semua");
        lihat.addClassName("a-lihat-semua");
        panel.add(lihat);

        return panel;
    }

    private Div pendingItem(String code, String name, String loc) {
        Div item = new Div();
        item.addClassName("a-pending-item");

        Div codeBox = new Div();
        codeBox.addClassName("a-pending-code");
        codeBox.add(new Span(code));

        Div info = new Div();
        info.addClassName("a-pending-info");
        Span nameSpan = new Span(name);
        nameSpan.addClassName("a-pending-name");
        Span locSpan = new Span(loc);
        locSpan.addClassName("a-pending-loc");
        info.add(nameSpan, locSpan);

        Div actions = new Div();
        actions.addClassName("a-pending-actions");
        Div approve = new Div();
        approve.addClassName("a-action-approve");
        approve.add(new Span("✓"));
        Div reject = new Div();
        reject.addClassName("a-action-reject");
        reject.add(new Span("🗑"));
        actions.add(approve, reject);

        item.add(codeBox, info, actions);
        return item;
    }

    // ── Weekly chart ─────────────────────────
    private Div buildChartSection() {
        Div section = new Div();
        section.addClassName("a-chart-section");

        Span title = new Span("Volume Laporan Mingguan");
        title.addClassName("a-chart-title");
        section.add(title);

        Div wrapper = new Div();
        wrapper.addClassName("a-chart-wrapper");

        // Y-axis labels
        Div yAxis = new Div();
        yAxis.addClassName("a-chart-y-axis");
        yAxis.add(new Span("32"));
        yAxis.add(new Span("24"));
        yAxis.add(new Span("16"));
        yAxis.add(new Span("8"));
        yAxis.add(new Span("0"));
        wrapper.add(yAxis);

        // Bar groups
        int[][] data = {
            {28, 20}, {22, 18}, {30, 24}, {18, 14}, {32, 26}, {24, 16}, {20, 12}
        };
        String[] labels = {"Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min"};

        for (int i = 0; i < data.length; i++) {
            Div group = new Div();
            group.addClassName("a-chart-bar-group");

            Div barContainer = new Div();
            barContainer.addClassName("a-chart-bar-container");

            Div bar1 = new Div();
            bar1.addClassName("a-chart-bar");
            bar1.addClassName("a-chart-bar-orange");
            bar1.getStyle().set("height", (data[i][0] * 100 / 32) + "%");

            Div bar2 = new Div();
            bar2.addClassName("a-chart-bar");
            bar2.addClassName("a-chart-bar-teal");
            bar2.getStyle().set("height", (data[i][1] * 100 / 32) + "%");

            barContainer.add(bar1, bar2);

            Span label = new Span(labels[i]);
            label.addClassName("a-chart-label");

            group.add(barContainer, label);
            wrapper.add(group);
        }

        section.add(wrapper);
        return section;
    }

    // ── Notification Toast ───────────────────
    private Div buildNotificationToast() {
        Div toast = new Div();
        toast.addClassName("a-toast");

        Div icon = new Div();
        icon.addClassName("a-toast-icon");
        icon.add(new Span("⚠"));

        Div content = new Div();
        content.addClassName("a-toast-content");
        Span title = new Span("Laporan Baru Masuk!");
        title.addClassName("a-toast-title");
        Span msg = new Span("Pohon tumbang menutup jalan utama di area RT 02 / RW 01.");
        msg.addClassName("a-toast-msg");
        content.add(title, msg);

        Span time = new Span("Baru saja");
        time.addClassName("a-toast-time");

        toast.add(icon, content, time);
        return toast;
    }
}

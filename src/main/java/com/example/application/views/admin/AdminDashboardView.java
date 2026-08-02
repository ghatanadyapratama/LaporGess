package com.example.application.views.admin;

import com.example.application.views.BlankLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "admin/dashboard", layout = BlankLayout.class)
@RouteAlias(value = "admin", layout = BlankLayout.class)
@PageTitle("Dasbor Admin - Lapor Gess")
public class AdminDashboardView extends Div {

    public AdminDashboardView() {
        addClassName("ad-root");

        // Build Sidebar
        Div sidebar = AdminLayout.buildSidebar("admin/dashboard");

        // Main content area
        Div main = new Div();
        main.addClassName("ad-main");

        // Topbar
        Div topbar = AdminLayout.buildTopbar("Dasbor");

        // Scrollable body
        Div body = new Div();
        body.addClassName("ad-body");

        // 1. Stats Row (4 Cards)
        body.add(buildStatCards());

        // 2. Middle Row (Map & Pending Registrations)
        body.add(buildMiddleRow());

        // 3. Weekly Volume Chart Row
        body.add(buildWeeklyChartCard());

        main.add(topbar, body);
        add(sidebar, main);
    }

    private Div buildStatCards() {
        Div grid = new Div();
        grid.addClassName("ad-stats-grid");

        // Card 1: Total Laporan
        grid.add(createStatCard("Total Laporan", "1.248", "📄", "ad-stat-bg-orange"));

        // Card 2: Selesai
        grid.add(createStatCard("Selesai", "984", "✔", "ad-stat-bg-teal"));

        // Card 3: Menunggu Verifikasi
        grid.add(createStatCard("Menunggu Verifikasi", "12", "⚠️", "ad-stat-bg-yellow"));

        // Card 4: Diproses
        grid.add(createStatCard("Diproses", "45", "⏱", "ad-stat-bg-blue"));

        return grid;
    }

    private Div createStatCard(String label, String value, String iconSymbol, String bgClass) {
        Div card = new Div();
        card.addClassName("ad-stat-card");

        Div iconBox = new Div(new Span(iconSymbol));
        iconBox.addClassName("ad-stat-icon-wrapper");
        iconBox.addClassName(bgClass);

        Div info = new Div();
        info.addClassName("ad-stat-info");

        Span lbl = new Span(label);
        lbl.addClassName("ad-stat-label");

        Span val = new Span(value);
        val.addClassName("ad-stat-value");

        info.add(lbl, val);
        card.add(iconBox, info);

        return card;
    }

    private Div buildMiddleRow() {
        Div row = new Div();
        row.addClassName("ad-row-2col");

        // Left Card: Peta Sebaran Laporan
        Div mapCard = new Div();
        mapCard.addClassName("ad-card");

        Div header = new Div();
        header.addClassName("ad-card-header");
        Span title = new Span("Peta Sebaran Laporan");
        title.addClassName("ad-card-title");

        Div legend = new Div();
        legend.addClassName("ad-map-legend");

        Div itemRed = new Div(new Div(), new Span("Darurat"));
        itemRed.addClassName("ad-legend-item");
        itemRed.getChildren().findFirst().ifPresent(c -> c.getElement().getClassList().add("ad-dot-red"));

        Div itemOrange = new Div(new Div(), new Span("Menengah"));
        itemOrange.addClassName("ad-legend-item");
        itemOrange.getChildren().findFirst().ifPresent(c -> c.getElement().getClassList().add("ad-dot-orange"));

        Div itemTeal = new Div(new Div(), new Span("Ringan"));
        itemTeal.addClassName("ad-legend-item");
        itemTeal.getChildren().findFirst().ifPresent(c -> c.getElement().getClassList().add("ad-dot-teal"));

        legend.add(itemRed, itemOrange, itemTeal);
        header.add(title, legend);

        // Map box with pins
        Div mapBox = new Div();
        mapBox.addClassName("ad-map-box");

        Div overlay = new Div();
        overlay.addClassName("ad-map-overlay");

        Div pinRed = new Div(new Span("📍"));
        pinRed.addClassName("ad-map-pin");
        pinRed.addClassName("ad-pin-red");

        Div pinOrange1 = new Div(new Span("📍"));
        pinOrange1.addClassName("ad-map-pin");
        pinOrange1.addClassName("ad-pin-orange1");

        Div pinOrange2 = new Div(new Span("📍"));
        pinOrange2.addClassName("ad-map-pin");
        pinOrange2.addClassName("ad-pin-orange2");

        Div pinTeal = new Div(new Span("📍"));
        pinTeal.addClassName("ad-map-pin");
        pinTeal.addClassName("ad-pin-teal");

        mapBox.add(overlay, pinRed, pinOrange1, pinOrange2, pinTeal);
        mapCard.add(header, mapBox);

        // Right Card: Pendaftaran Tertunda
        Div pendingCard = new Div();
        pendingCard.addClassName("ad-card");

        Div pendingHeader = new Div();
        pendingHeader.addClassName("ad-card-header");
        Span pendingTitle = new Span("Pendaftaran Tertunda");
        pendingTitle.addClassName("ad-card-title");
        pendingHeader.add(pendingTitle);

        Div pendingList = new Div();
        pendingList.addClassName("ad-pending-list");

        pendingList.add(createPendingItem("P1", "Pengguna Baru 1", "RT 01/02"));
        pendingList.add(createPendingItem("P2", "Pengguna Baru 2", "RT 01/02"));
        pendingList.add(createPendingItem("P3", "Pengguna Baru 3", "RT 01/02"));

        Anchor linkAll = new Anchor("#", "Lihat Semua");
        linkAll.addClassName("ad-btn-link-orange");
        linkAll.getElement().addEventListener("click", e -> UI.getCurrent().navigate("admin/verifikasi"));

        pendingCard.add(pendingHeader, pendingList, linkAll);

        row.add(mapCard, pendingCard);
        return row;
    }

    private Div createPendingItem(String initial, String name, String rt) {
        Div item = new Div();
        item.addClassName("ad-pending-item");

        Div left = new Div();
        left.addClassName("ad-pending-left");

        Div avatar = new Div(new Span(initial));
        avatar.addClassName("ad-avatar-initial");

        Div info = new Div();
        Span nameTxt = new Span(name);
        nameTxt.addClassName("ad-pending-name");
        Span rtTxt = new Span(rt);
        rtTxt.addClassName("ad-pending-rt");
        info.add(nameTxt, new Br(), rtTxt);

        left.add(avatar, info);

        Div actions = new Div();
        actions.addClassName("ad-pending-actions");

        Div btnApprove = new Div(new Span("✔"));
        btnApprove.addClassName("ad-icon-btn-green");

        Div btnReject = new Div(new Span("🗑"));
        btnReject.addClassName("ad-icon-btn-red");

        actions.add(btnApprove, btnReject);
        item.add(left, actions);

        return item;
    }

    private Div buildWeeklyChartCard() {
        Div card = new Div();
        card.addClassName("ad-card");

        Div header = new Div();
        header.addClassName("ad-card-header");
        Span title = new Span("Volume Laporan Mingguan");
        title.addClassName("ad-card-title");
        header.add(title);

        Div chartBox = new Div();
        chartBox.addClassName("ad-chart-box");

        // Days bar representation
        String[] days = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};
        int[] heights = {40, 65, 30, 85, 50, 75, 45};

        for (int i = 0; i < days.length; i++) {
            Div barWrap = new Div();
            barWrap.addClassName("ad-chart-bar-wrap");

            Div bar = new Div();
            bar.addClassName("ad-chart-bar");
            bar.getStyle().set("height", heights[i] + "%");

            Span dayLbl = new Span(days[i]);
            dayLbl.addClassName("ad-chart-day");

            barWrap.add(bar, dayLbl);
            chartBox.add(barWrap);
        }

        card.add(header, chartBox);
        return card;
    }
}

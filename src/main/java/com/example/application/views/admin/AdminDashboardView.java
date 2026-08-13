package com.example.application.views.admin;

import com.example.application.model.Laporan;
import com.example.application.model.Pengguna;
import com.example.application.repository.NotifikasiRepository;
import com.example.application.repository.PenggunaRepository;
import com.example.application.service.LaporanService;
import com.example.application.views.warga.BlankLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Route(value = "admin/dashboard", layout = BlankLayout.class)
@RouteAlias(value = "admin", layout = BlankLayout.class)
@PageTitle("Dasbor Admin - Lapor Gess")
public class AdminDashboardView extends Div {

    private final LaporanService laporanService;
    private final PenggunaRepository penggunaRepository;
    private final NotifikasiRepository notifikasiRepository;

    public AdminDashboardView(LaporanService laporanService, PenggunaRepository penggunaRepository, NotifikasiRepository notifikasiRepository) {
        this.laporanService = laporanService;
        this.penggunaRepository = penggunaRepository;
        this.notifikasiRepository = notifikasiRepository;
        addClassName("ad-root");

        long laporanPending = laporanService.countByStatus(Laporan.Status.PENDING);
        long petugasAktif = penggunaRepository.countByStatusAndPeran(Pengguna.Status.AKTIF, Pengguna.Peran.PETUGAS_LAPANGAN);
        long verifikasiPending = penggunaRepository.countByStatus(Pengguna.Status.PENDING);
        Div sidebar = AdminLayout.buildSidebar("admin/dashboard", laporanPending, petugasAktif, verifikasiPending);

        Div main = new Div();
        main.addClassName("ad-main");

        Div topbar = AdminLayout.buildTopbar("Dasbor", notifikasiRepository);

        Div body = new Div();
        body.addClassName("ad-body");

        body.add(buildStatCards());
        body.add(buildMiddleRow());
        body.add(buildWeeklyChartCard());

        main.add(topbar, body);
        add(sidebar, main);
    }

    private Div buildStatCards() {
        Div grid = new Div();
        grid.addClassName("ad-stats-grid");

        long totalLaporan = laporanService.countByStatus(Laporan.Status.PENDING)
                + laporanService.countByStatus(Laporan.Status.DIPROSES)
                + laporanService.countByStatus(Laporan.Status.SELESAI)
                + laporanService.countByStatus(Laporan.Status.DITOLAK);
        long totalPengguna = penggunaRepository.findByStatusAndPeran(Pengguna.Status.AKTIF, Pengguna.Peran.WARGA).size();
        long diproses = laporanService.countByStatus(Laporan.Status.DIPROSES);
        long selesai = laporanService.countByStatus(Laporan.Status.SELESAI);

        grid.add(createStatCard("Total Laporan", String.valueOf(totalLaporan), "📄", "ad-stat-bg-orange"));
        grid.add(createStatCard("Pengguna Aktif", String.valueOf(totalPengguna), "👥", "ad-stat-bg-blue"));
        grid.add(createStatCard("Sedang Diproses", String.valueOf(diproses), "⏱", "ad-stat-bg-yellow"));
        grid.add(createStatCard("Laporan Selesai", String.valueOf(selesai), "✔", "ad-stat-bg-teal"));

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

        List<Pengguna> pendingUsers = penggunaRepository.findByStatus(Pengguna.Status.PENDING);
        if (pendingUsers.isEmpty()) {
            Div empty = new Div(new Span("Tidak ada pendaftaran tertunda."));
            empty.getStyle().set("padding", "20px").set("color", "#94A3B8").set("text-align", "center");
            pendingList.add(empty);
        } else {
            pendingUsers.stream().limit(3).forEach(p -> {
                String roleTag = p.getPeran() == Pengguna.Peran.PETUGAS_LAPANGAN ? " (Petugas)" : " (Warga)";
                String rt = p.getRtRw() != null ? "RT " + p.getRtRw() : "-";
                String initial = p.getNamaLengkap() != null && !p.getNamaLengkap().isEmpty() ? p.getNamaLengkap().substring(0, 1).toUpperCase() : "U";
                pendingList.add(createPendingItem(initial, p.getNamaLengkap() + roleTag, rt));
            });
        }

        Span linkAll = new Span("Lihat Semua");
        linkAll.addClassName("ad-btn-link-orange");
        linkAll.getStyle().set("cursor", "pointer").set("display", "block").set("text-align", "center").set("margin-top", "16px").set("font-weight", "600");
        linkAll.addClickListener(e -> UI.getCurrent().navigate("admin/verifikasi"));

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
        rtTxt.getStyle().set("display", "block");
        info.add(nameTxt, rtTxt);

        left.add(avatar, info);

        Div actions = new Div();
        actions.addClassName("ad-pending-actions");

        Div btnDetail = new Div(new Span("➔"));
        btnDetail.addClassName("ad-icon-btn-green");
        btnDetail.getStyle().set("background-color", "#FFF0E0").set("color", "#FF7A00").set("cursor", "pointer");
        btnDetail.addClickListener(e -> UI.getCurrent().navigate("admin/verifikasi"));

        actions.add(btnDetail);
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
        int[] heights = new int[7]; 
        
        List<Laporan> allLaporan = laporanService.getAllLaporan();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        
        for (Laporan l : allLaporan) {
            LocalDate dt = l.getDibuatPada().toLocalDate();
            if (!dt.isBefore(startOfWeek) && !dt.isAfter(startOfWeek.plusDays(6))) {
                int dayIndex = dt.getDayOfWeek().getValue() - 1;
                heights[dayIndex]++;
            }
        }
        
        int max = 0;
        for (int h : heights) if (h > max) max = h;
        if (max > 0) {
            for (int i = 0; i < heights.length; i++) {
                heights[i] = (int) Math.round((double) heights[i] / max * 100);
            }
        } else {
            // fallback if no data
            for (int i = 0; i < heights.length; i++) heights[i] = 5;
        }

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

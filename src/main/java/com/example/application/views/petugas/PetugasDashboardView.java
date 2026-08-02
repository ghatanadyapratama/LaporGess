package com.example.application.views.petugas;

import com.example.application.model.JadwalShift;
import com.example.application.model.Laporan;
import com.example.application.model.Pengguna;
import com.example.application.repository.JadwalShiftRepository;
import com.example.application.repository.PenggunaRepository;
import com.example.application.service.LaporanService;
import com.example.application.service.SessionManager;
import com.example.application.views.warga.BlankLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "petugas/dashboard", layout = BlankLayout.class)
@RouteAlias(value = "petugas", layout = BlankLayout.class)
@PageTitle("Dashboard Petugas - LaporGess")
public class PetugasDashboardView extends Div implements BeforeEnterObserver {

    private final LaporanService laporanService;
    private final PenggunaRepository penggunaRepository;
    private final JadwalShiftRepository jadwalShiftRepository;

    public PetugasDashboardView(LaporanService laporanService,
                                 PenggunaRepository penggunaRepository,
                                 JadwalShiftRepository jadwalShiftRepository) {
        this.laporanService = laporanService;
        this.penggunaRepository = penggunaRepository;
        this.jadwalShiftRepository = jadwalShiftRepository;
        addClassName("pt-root");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!SessionManager.isLoggedIn() || !SessionManager.isPetugas()) {
            event.rerouteTo("login");
            return;
        }
        buildUI();
    }

    private void buildUI() {
        removeAll();
        String username = SessionManager.getUsername();
        Pengguna petugas = penggunaRepository.findByUsername(username).orElse(null);

        Div sidebar = PetugasLayout.buildSidebar("petugas/dashboard");
        Div main = new Div();
        main.addClassName("pt-main");
        Div topbar = PetugasLayout.buildTopbar("Dashboard Petugas");
        Div body = new Div();
        body.addClassName("pt-body");

        // Welcome header
        Div welcomeCard = new Div();
        welcomeCard.addClassName("pt-dashboard-welcome");
        String nama = petugas != null ? petugas.getNamaLengkap() : "Petugas";
        H2 welcomeText = new H2("Selamat Datang, " + nama + "! 👋");
        welcomeText.getStyle().set("margin", "0 0 4px 0").set("font-size", "1.35rem").set("font-weight", "800").set("color", "#1E293B");
        Span subText = new Span("Berikut ringkasan aktivitas Anda hari ini.");
        subText.getStyle().set("color", "#CBD5E1").set("font-size", "0.92rem");
        welcomeCard.add(welcomeText, subText);
        body.add(welcomeCard);

        // Stat Cards
        List<Laporan> aktifList = laporanService.getLaporanDiprosesByPetugas(username);
        List<Laporan> selesaiList = laporanService.getLaporanSelesaiByPetugas(username);
        List<JadwalShift> todayShifts = jadwalShiftRepository.findByPetugasAndTanggal(petugas, LocalDate.now());

        Div statsGrid = new Div();
        statsGrid.addClassName("pt-stats-row");
        statsGrid.getStyle().set("display", "grid").set("grid-template-columns", "1fr 1fr 1fr").set("gap", "16px").set("margin-bottom", "24px");

        statsGrid.add(buildStatCard("Tugas Aktif", String.valueOf(aktifList.size()), "🔧", "#FF7A00", "#FFF0E0"));
        statsGrid.add(buildStatCard("Tugas Selesai", String.valueOf(selesaiList.size()), "✅", "#0D9488", "#E6F7F5"));
        statsGrid.add(buildStatCard("Shift Hari Ini", String.valueOf(todayShifts.size()), "📅", "#3B82F6", "#EFF6FF"));
        body.add(statsGrid);

        // Today's shift info
        if (!todayShifts.isEmpty()) {
            Div shiftCard = new Div();
            shiftCard.addClassName("pt-detail-left-card");
            shiftCard.getStyle().set("margin-bottom", "24px").set("padding", "24px");
            H3 shiftTitle = new H3("Jadwal Shift Hari Ini");
            shiftTitle.getStyle().set("margin", "0 0 16px 0").set("font-size", "1.05rem").set("font-weight", "800").set("color", "#1E293B");
            shiftCard.add(shiftTitle);

            DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
            for (JadwalShift shift : todayShifts) {
                Div shiftRow = new Div();
                shiftRow.getStyle().set("display", "flex").set("align-items", "center").set("gap", "12px").set("padding", "10px 0").set("border-bottom", "1px solid #F1F5F9");
                String shiftColor = shift.getJenisShift() == JadwalShift.JenisShift.PAGI ? "#F59E0B" :
                                    shift.getJenisShift() == JadwalShift.JenisShift.SIANG ? "#3B82F6" : "#8B5CF6";
                Span badge = new Span(shift.getJenisShift().name());
                badge.getStyle().set("background", shiftColor).set("color", "white").set("font-size", "0.75rem")
                    .set("font-weight", "700").set("padding", "3px 10px").set("border-radius", "20px");
                Span info = new Span(shift.getZona() + " • " + shift.getJamMulai().format(timeFmt) + " - " + shift.getJamSelesai().format(timeFmt));
                info.getStyle().set("font-size", "0.9rem").set("color", "#475569").set("font-weight", "500");
                shiftRow.add(badge, info);
                shiftCard.add(shiftRow);
            }
            body.add(shiftCard);
        }

        // Active tasks preview
        Div taskCard = new Div();
        taskCard.addClassName("pt-detail-left-card");
        taskCard.getStyle().set("padding", "24px");
        H3 taskTitle = new H3("Tugas Aktif");
        taskTitle.getStyle().set("margin", "0 0 16px 0").set("font-size", "1.05rem").set("font-weight", "800").set("color", "#1E293B");
        taskCard.add(taskTitle);

        if (aktifList.isEmpty()) {
            Div empty = new Div();
            empty.getStyle().set("text-align", "center").set("padding", "24px").set("color", "#94A3B8");
            empty.add(new Span("Tidak ada tugas aktif saat ini."));
            taskCard.add(empty);
        } else {
            aktifList.stream().limit(3).forEach(l -> {
                Div row = new Div();
                row.getStyle().set("display", "flex").set("justify-content", "space-between").set("align-items", "center")
                    .set("padding", "12px 0").set("border-bottom", "1px solid #F1F5F9").set("cursor", "pointer");
                Div left = new Div();
                Span titleSpan = new Span(l.getJudul());
                titleSpan.getStyle().set("font-weight", "600").set("color", "#1E293B").set("font-size", "0.92rem").set("display", "block");
                Span locSpan = new Span("📍 " + (l.getLokasi() != null ? l.getLokasi() : "-"));
                locSpan.getStyle().set("font-size", "0.8rem").set("color", "#64748B");
                left.add(titleSpan, locSpan);
                Span arrow = new Span("→");
                arrow.getStyle().set("color", "#FF7A00").set("font-weight", "700");
                row.add(left, arrow);
                row.addClickListener(e -> UI.getCurrent().navigate("petugas/tugas-saya"));
                taskCard.add(row);
            });
        }

        Div viewAllBtn = new Div();
        viewAllBtn.getStyle().set("text-align", "center").set("margin-top", "16px");
        Span viewAll = new Span("Lihat Semua Tugas →");
        viewAll.getStyle().set("color", "#FF7A00").set("font-weight", "700").set("cursor", "pointer").set("font-size", "0.9rem");
        viewAll.addClickListener(e -> UI.getCurrent().navigate("petugas/tugas-saya"));
        viewAllBtn.add(viewAll);
        taskCard.add(viewAllBtn);
        body.add(taskCard);

        main.add(topbar, body);
        add(sidebar, main);
    }

    private Div buildStatCard(String label, String value, String icon, String color, String bgColor) {
        Div card = new Div();
        card.getStyle().set("background", bgColor).set("border-radius", "16px").set("padding", "20px")
            .set("display", "flex").set("align-items", "center").set("gap", "16px");
        Div iconBox = new Div(new Span(icon));
        iconBox.getStyle().set("font-size", "1.5rem").set("width", "48px").set("height", "48px")
            .set("display", "flex").set("align-items", "center").set("justify-content", "center")
            .set("background", "white").set("border-radius", "12px");
        Div info = new Div();
        info.getStyle().set("display", "flex").set("flex-direction", "column");
        Span lbl = new Span(label);
        lbl.getStyle().set("font-size", "0.8rem").set("font-weight", "600").set("color", "#64748B");
        Span val = new Span(value);
        val.getStyle().set("font-size", "1.8rem").set("font-weight", "800").set("color", color);
        info.add(lbl, val);
        card.add(iconBox, info);
        return card;
    }
}

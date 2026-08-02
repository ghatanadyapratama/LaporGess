package com.example.application.views.petugas;

import com.example.application.model.JadwalShift;
import com.example.application.model.Pengguna;
import com.example.application.repository.JadwalShiftRepository;
import com.example.application.repository.PenggunaRepository;
import com.example.application.service.SessionManager;
import com.example.application.views.warga.BlankLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Route(value = "petugas/jadwal-shift", layout = BlankLayout.class)
@PageTitle("Jadwal Shift - Petugas LaporGess")
public class PetugasJadwalShiftView extends Div implements BeforeEnterObserver {

    private final JadwalShiftRepository jadwalShiftRepository;
    private final PenggunaRepository penggunaRepository;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMM");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public PetugasJadwalShiftView(JadwalShiftRepository jadwalShiftRepository,
                                   PenggunaRepository penggunaRepository) {
        this.jadwalShiftRepository = jadwalShiftRepository;
        this.penggunaRepository = penggunaRepository;
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
        Div sidebar = PetugasLayout.buildSidebar("petugas/jadwal-shift");
        Div main = new Div();
        main.addClassName("pt-main");
        Div topbar = PetugasLayout.buildTopbar("Jadwal Shift Saya");
        Div body = new Div();
        body.addClassName("pt-body");

        String username = SessionManager.getUsername();
        Pengguna petugas = penggunaRepository.findByUsername(username).orElse(null);

        // Week range header
        LocalDate today = LocalDate.now();
        LocalDate monday = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate sunday = monday.plusDays(6);

        Div weekHeader = new Div();
        weekHeader.getStyle().set("display", "flex").set("justify-content", "space-between")
            .set("align-items", "center").set("margin-bottom", "20px");

        H2 weekTitle = new H2("Minggu Ini");
        weekTitle.getStyle().set("margin", "0").set("font-size", "1.2rem").set("font-weight", "800").set("color", "#1E293B");

        Span weekRange = new Span(monday.format(DATE_FMT) + " – " + sunday.format(DATE_FMT) + " 2026");
        weekRange.getStyle().set("font-size", "0.9rem").set("color", "#64748B").set("font-weight", "600")
            .set("background", "#F1F5F9").set("padding", "6px 14px").set("border-radius", "8px");

        weekHeader.add(weekTitle, weekRange);
        body.add(weekHeader);

        // Load shifts for this week
        List<JadwalShift> shifts = petugas != null
                ? jadwalShiftRepository.findByPetugasAndTanggalBetweenOrderByTanggalAscJamMulaiAsc(petugas, monday, sunday)
                : List.of();

        // Summary Stats
        long pagiCount = shifts.stream().filter(s -> s.getJenisShift() == JadwalShift.JenisShift.PAGI).count();
        long siangCount = shifts.stream().filter(s -> s.getJenisShift() == JadwalShift.JenisShift.SIANG).count();
        long malamCount = shifts.stream().filter(s -> s.getJenisShift() == JadwalShift.JenisShift.MALAM).count();

        Div statsRow = new Div();
        statsRow.getStyle().set("display", "flex").set("gap", "14px").set("margin-bottom", "24px");
        statsRow.add(buildShiftStat("Total Shift", String.valueOf(shifts.size()), "#1E293B", "#F8FAFC"));
        statsRow.add(buildShiftStat("Shift Pagi ☀️", String.valueOf(pagiCount), "#B45309", "#FEF3C7"));
        statsRow.add(buildShiftStat("Shift Siang 🌤", String.valueOf(siangCount), "#1D4ED8", "#EFF6FF"));
        statsRow.add(buildShiftStat("Shift Malam 🌙", String.valueOf(malamCount), "#6D28D9", "#EDE9FE"));
        body.add(statsRow);

        // Weekly calendar grid
        Div calendarCard = new Div();
        calendarCard.addClassName("pt-detail-left-card");
        calendarCard.getStyle().set("padding", "0").set("overflow", "hidden");

        // Day columns header
        Div calHeader = new Div();
        calHeader.getStyle().set("display", "grid").set("grid-template-columns", "repeat(7, 1fr)")
            .set("background", "#F8FAFC").set("border-bottom", "1px solid #E2E8F0");

        String[] dayNames = {"Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min"};
        for (int i = 0; i < 7; i++) {
            LocalDate day = monday.plusDays(i);
            Div dayHeader = new Div();
            dayHeader.getStyle().set("padding", "14px 8px").set("text-align", "center").set("border-right", "1px solid #E2E8F0");
            if (i == 6) dayHeader.getStyle().set("border-right", "none");

            boolean isToday = day.equals(today);
            Span dayName = new Span(dayNames[i]);
            dayName.getStyle().set("font-size", "0.78rem").set("font-weight", "700")
                .set("color", isToday ? "#FF7A00" : "#64748B").set("display", "block").set("margin-bottom", "4px");
            Span dayNum = new Span(String.valueOf(day.getDayOfMonth()));
            dayNum.getStyle()
                .set("font-size", "1.1rem").set("font-weight", "800")
                .set("color", isToday ? "#FFFFFF" : "#1E293B")
                .set("display", "inline-block").set("width", "32px").set("height", "32px")
                .set("line-height", "32px").set("text-align", "center").set("border-radius", "50%")
                .set("background", isToday ? "#FF7A00" : "transparent");

            dayHeader.add(dayName, dayNum);
            calHeader.add(dayHeader);
        }
        calendarCard.add(calHeader);

        // Day columns content
        Div calBody = new Div();
        calBody.getStyle().set("display", "grid").set("grid-template-columns", "repeat(7, 1fr)")
            .set("min-height", "200px");

        for (int i = 0; i < 7; i++) {
            LocalDate day = monday.plusDays(i);
            Div dayCol = new Div();
            dayCol.getStyle().set("padding", "10px 6px").set("border-right", "1px solid #E2E8F0")
                .set("display", "flex").set("flex-direction", "column").set("gap", "6px").set("min-height", "180px");
            if (i == 6) dayCol.getStyle().set("border-right", "none");

            final int idx = i;
            shifts.stream()
                .filter(s -> s.getTanggal().equals(monday.plusDays(idx)))
                .forEach(s -> {
                    Div shiftPill = new Div();
                    String bg = switch (s.getJenisShift()) {
                        case PAGI -> "#FEF3C7";
                        case SIANG -> "#EFF6FF";
                        case MALAM -> "#EDE9FE";
                    };
                    String color = switch (s.getJenisShift()) {
                        case PAGI -> "#B45309";
                        case SIANG -> "#1D4ED8";
                        case MALAM -> "#6D28D9";
                    };
                    shiftPill.getStyle()
                        .set("background", bg).set("border-radius", "8px").set("padding", "6px 8px")
                        .set("font-size", "0.72rem").set("font-weight", "700").set("color", color)
                        .set("line-height", "1.4");
                    shiftPill.getElement().setProperty("innerHTML",
                        "<div>" + s.getJenisShift().name() + "</div>" +
                        "<div style='font-weight:500;color:#475569;font-size:0.68rem;margin-top:2px;'>" +
                        s.getJamMulai().format(TIME_FMT) + "-" + s.getJamSelesai().format(TIME_FMT) + "</div>" +
                        "<div style='font-weight:500;color:#64748B;font-size:0.66rem;margin-top:1px;'>" + s.getZona() + "</div>"
                    );
                    dayCol.add(shiftPill);
                });

            calBody.add(dayCol);
        }
        calendarCard.add(calBody);
        body.add(calendarCard);

        // Legend
        Div legend = new Div();
        legend.getStyle().set("display", "flex").set("gap", "16px").set("margin-top", "16px").set("flex-wrap", "wrap");
        legend.add(buildLegendItem("Shift Pagi (06:00-12:00)", "#FEF3C7", "#B45309"));
        legend.add(buildLegendItem("Shift Siang (12:00-18:00)", "#EFF6FF", "#1D4ED8"));
        legend.add(buildLegendItem("Shift Malam (18:00-00:00)", "#EDE9FE", "#6D28D9"));
        body.add(legend);

        main.add(topbar, body);
        add(sidebar, main);
    }

    private Div buildShiftStat(String label, String value, String color, String bg) {
        Div box = new Div();
        box.getStyle().set("flex", "1").set("background", bg).set("border-radius", "16px")
            .set("padding", "18px 20px").set("display", "flex").set("flex-direction", "column").set("gap", "4px");
        Span val = new Span(value);
        val.getStyle().set("font-size", "1.8rem").set("font-weight", "800").set("color", color);
        Span lbl = new Span(label);
        lbl.getStyle().set("font-size", "0.8rem").set("font-weight", "600").set("color", "#64748B");
        box.add(val, lbl);
        return box;
    }

    private Div buildLegendItem(String label, String bg, String color) {
        Div item = new Div();
        item.getStyle().set("display", "flex").set("align-items", "center").set("gap", "8px");
        Div dot = new Div();
        dot.getStyle().set("width", "14px").set("height", "14px").set("border-radius", "4px").set("background", bg)
            .set("border", "2px solid " + color);
        Span text = new Span(label);
        text.getStyle().set("font-size", "0.82rem").set("color", "#475569").set("font-weight", "500");
        item.add(dot, text);
        return item;
    }
}

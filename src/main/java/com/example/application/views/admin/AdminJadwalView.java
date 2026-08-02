package com.example.application.views.admin;

import com.example.application.views.BlankLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "admin/jadwal", layout = BlankLayout.class)
@PageTitle("Jadwal Petugas - Admin Lapor")
public class AdminJadwalView extends Div {

    // Officer code → color mapping
    private static final String[][] OFFICERS = {
        {"AR", "a-ob-red",    "Arif Rahman"},
        {"DN", "a-ob-green",  "Dinda Nur"},
        {"IM", "a-ob-blue",   "Imam Maulana"},
        {"KS", "a-ob-yellow", "Kevin Saputra"},
        {"RH", "a-ob-purple", "Rina Hidayati"},
        {"FT", "a-ob-pink",   "Fitri Aulia"},
        {"BG", "a-ob-teal",   "Bagas Nugraha"}
    };

    // Schedule data: [timeSlot][zone A-E] = officer index in OFFICERS
    private static final int[][] SCHEDULE_AM = {
        {0, 1, 2, 3, 4},  // 06.00 – 08.00
        {5, 6, 0, 1, 2},  // 08.00 – 10.00
        {3, 4, 5, 6, 0}   // 10.00 – 12.00
    };

    private static final int[][] SCHEDULE_PM = {
        {1, 2, 3, 4, 5},  // 13.00 – 15.00
        {6, 0, 1, 2, 3}   // 15.00 – 17.00
    };

    private static final String[] TIME_AM = {
        "06.00 – 08.00", "08.00 – 10.00", "10.00 – 12.00"
    };

    private static final String[] TIME_PM = {
        "13.00 – 15.00", "15.00 – 17.00"
    };

    public AdminJadwalView() {
        addClassName("a-root");
        add(AdminDasborView.buildSidebar("jadwal"), buildMain());
    }

    private Div buildMain() {
        Div main = new Div();
        main.addClassName("a-main");

        // Topbar
        Div topbar = new Div();
        topbar.addClassName("a-topbar");
        Span title = new Span("Jadwal Petugas Lapangan");
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
        body.add(buildFilterRow());
        body.add(buildScheduleContent());
        main.add(body);

        return main;
    }

    private Div buildStatsRow() {
        Div row = new Div();
        row.addClassName("a-stats-row");
        row.add(statCard("👥", "a-stat-icon-blue", "Petugas Aktif", "42"));
        row.add(statCard("📋", "a-stat-icon-orange", "Laporan Hari Ini", "18"));
        row.add(statCard("⏳", "a-stat-icon-amber", "Laporan Diproses", "5"));
        row.add(statCard("✅", "a-stat-icon-teal", "Laporan Selesai", "12"));
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

    private Div buildFilterRow() {
        Div row = new Div();
        row.addClassName("a-filter-row");

        // Search
        NativeLabel search = new NativeLabel();
        search.getElement().setProperty("innerHTML",
            "<input class='a-search-input' placeholder='Cari kode/nama petugas...' />");
        row.add(search);

        // Month select
        NativeLabel monthLabel = new NativeLabel();
        monthLabel.getElement().setProperty("innerHTML",
            "<select class='a-select'><option>Juli 2026</option></select>");
        row.add(monthLabel);

        // Week select
        NativeLabel weekLabel = new NativeLabel();
        weekLabel.getElement().setProperty("innerHTML",
            "<select class='a-select'><option>Minggu 1</option></select>");
        row.add(weekLabel);

        // Spacer
        Div spacer = new Div();
        spacer.addClassName("a-filter-spacer");
        row.add(spacer);

        // Generate button
        NativeLabel genBtn = new NativeLabel();
        genBtn.getElement().setProperty("innerHTML",
            "<button class='a-btn-outline'>⚙ Generate Otomatis</button>");
        row.add(genBtn);

        // Edit button
        NativeLabel editBtn = new NativeLabel();
        editBtn.getElement().setProperty("innerHTML",
            "<button class='a-btn-outline'>✏ Edit Jadwal</button>");
        row.add(editBtn);

        // Simpan button
        NativeLabel simpanBtn = new NativeLabel();
        simpanBtn.getElement().setProperty("innerHTML",
            "<button class='a-btn-teal'>💾 Simpan</button>");
        row.add(simpanBtn);

        return row;
    }

    private Div buildScheduleContent() {
        Div content = new Div();
        content.addClassName("a-schedule-content");
        content.add(buildScheduleTable());
        content.add(buildOfficerLegend());
        return content;
    }

    private Div buildScheduleTable() {
        Div wrapper = new Div();
        wrapper.addClassName("a-schedule-table-wrapper");

        StringBuilder html = new StringBuilder();
        html.append("<table class='a-schedule-table'>");
        html.append("<thead><tr>");
        html.append("<th>Jam</th>");
        html.append("<th>Zona A</th><th>Zona B</th><th>Zona C</th><th>Zona D</th><th>Zona E</th>");
        html.append("</tr></thead>");
        html.append("<tbody>");

        // AM slots
        for (int i = 0; i < TIME_AM.length; i++) {
            html.append("<tr>");
            html.append("<td>").append(TIME_AM[i]).append("</td>");
            for (int j = 0; j < 5; j++) {
                int officerIdx = SCHEDULE_AM[i][j];
                html.append("<td><span class='a-officer-badge ").append(OFFICERS[officerIdx][1])
                    .append("'>").append(OFFICERS[officerIdx][0]).append("</span></td>");
            }
            html.append("</tr>");
        }

        // Break row
        html.append("<tr class='a-break-row'><td colspan='6'>I S T I R A H A T</td></tr>");

        // PM slots
        for (int i = 0; i < TIME_PM.length; i++) {
            html.append("<tr>");
            html.append("<td>").append(TIME_PM[i]).append("</td>");
            for (int j = 0; j < 5; j++) {
                int officerIdx = SCHEDULE_PM[i][j];
                html.append("<td><span class='a-officer-badge ").append(OFFICERS[officerIdx][1])
                    .append("'>").append(OFFICERS[officerIdx][0]).append("</span></td>");
            }
            html.append("</tr>");
        }

        html.append("</tbody></table>");
        wrapper.getElement().setProperty("innerHTML", html.toString());

        return wrapper;
    }

    private Div buildOfficerLegend() {
        Div legend = new Div();
        legend.addClassName("a-officer-legend");

        Span title = new Span("Daftar Kode Petugas");
        title.addClassName("a-legend-title");
        legend.add(title);

        Div list = new Div();
        list.addClassName("a-legend-list");

        for (String[] officer : OFFICERS) {
            Div entry = new Div();
            entry.addClassName("a-legend-entry");

            Div code = new Div();
            code.addClassName("a-legend-code");
            code.addClassName(officer[1]);
            code.add(new Span(officer[0]));

            Span name = new Span(officer[2]);
            name.addClassName("a-legend-name");

            entry.add(code, name);
            list.add(entry);
        }

        legend.add(list);
        return legend;
    }
}

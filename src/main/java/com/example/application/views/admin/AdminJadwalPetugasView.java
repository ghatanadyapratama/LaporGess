package com.example.application.views.admin;

import com.example.application.views.BlankLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "admin/jadwal-petugas", layout = BlankLayout.class)
@PageTitle("Jadwal Petugas - Lapor Gess")
public class AdminJadwalPetugasView extends Div {

    public AdminJadwalPetugasView() {
        addClassName("ad-root");

        Div sidebar = AdminLayout.buildSidebar("admin/jadwal-petugas");

        Div main = new Div();
        main.addClassName("ad-main");

        Div topbar = AdminLayout.buildTopbar("Jadwal Petugas Lapangan");

        Div body = new Div();
        body.addClassName("ad-body");

        // 1. Stat Cards
        body.add(buildStatCards());

        // 2. Controls Bar
        body.add(buildControlsBar());

        // 3. Grid & Officer Legend
        body.add(buildGridAndLegend());

        main.add(topbar, body);
        add(sidebar, main);
    }

    private Div buildStatCards() {
        Div grid = new Div();
        grid.addClassName("ad-stats-grid");

        grid.add(createStatCard("Petugas Aktif", "42", "👥", "ad-stat-bg-blue"));
        grid.add(createStatCard("Laporan Hari Ini", "18", "📄", "ad-stat-bg-orange"));
        grid.add(createStatCard("Laporan Diproses", "5", "⏱", "ad-stat-bg-yellow"));
        grid.add(createStatCard("Laporan Selesai", "12", "✔", "ad-stat-bg-teal"));

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

    private Div buildControlsBar() {
        Div bar = new Div();
        bar.addClassName("ad-controls-bar");

        Div left = new Div();
        left.addClassName("ad-controls-left");

        TextField search = new TextField();
        search.setPlaceholder("Cari kode/nama petugas...");
        search.getStyle().set("width", "240px");

        ComboBox<String> monthSelect = new ComboBox<>();
        monthSelect.setItems("Juli 2026", "Agustus 2026", "September 2026");
        monthSelect.setValue("Juli 2026");
        monthSelect.getStyle().set("width", "140px");

        ComboBox<String> weekSelect = new ComboBox<>();
        weekSelect.setItems("Minggu 1", "Minggu 2", "Minggu 3", "Minggu 4");
        weekSelect.setValue("Minggu 1");
        weekSelect.getStyle().set("width", "130px");

        left.add(search, monthSelect, weekSelect);

        Div right = new Div();
        right.addClassName("ad-controls-right");

        Button btnGen = new Button("✨ Generate Otomatis");
        btnGen.addClassName("ad-btn-secondary");

        Button btnEdit = new Button("⚙ Edit Jadwal");
        btnEdit.addClassName("ad-btn-secondary");

        Button btnSave = new Button("📝 Simpan");
        btnSave.addClassName("ad-btn-primary-green");

        right.add(btnGen, btnEdit, btnSave);

        bar.add(left, right);
        return bar;
    }

    private Div buildGridAndLegend() {
        Div wrap = new Div();
        wrap.addClassName("ad-schedule-grid-wrap");

        // Schedule Table Card
        Div cardTable = new Div();
        cardTable.addClassName("ad-card");
        cardTable.getStyle().set("padding", "0").set("overflow", "hidden");

        Table table = new Table();
        table.addClassName("ad-table");

        // Header Row
        Thead thead = new Thead();
        Tr headerRow = new Tr();
        headerRow.add(new Th("Jam"), new Th("Zona A"), new Th("Zona B"), new Th("Zona C"), new Th("Zona D"), new Th("Zona E"));
        thead.add(headerRow);
        table.add(thead);

        Tbody tbody = new Tbody();

        // Row 1: 06.00 - 08.00
        tbody.add(createScheduleRow("06.00 - 08.00", "AR", "ad-pill-blue", "DN", "ad-pill-pink", "IM", "ad-pill-teal", "KS", "ad-pill-orange", "RH", "ad-pill-purple"));

        // Row 2: 08.00 - 10.00
        tbody.add(createScheduleRow("08.00 - 10.00", "FT", "ad-pill-green", "BG", "ad-pill-yellow", "AR", "ad-pill-blue", "DN", "ad-pill-pink", "IM", "ad-pill-teal"));

        // Row 3: 10.00 - 12.00
        tbody.add(createScheduleRow("10.00 - 12.00", "KS", "ad-pill-orange", "RH", "ad-pill-purple", "FT", "ad-pill-green", "BG", "ad-pill-yellow", "AR", "ad-pill-blue"));

        // Break Row
        Tr breakRow = new Tr();
        breakRow.addClassName("ad-row-break");
        Td breakTd = new Td("I S T I R A H A T");
        breakTd.setColspan(6);
        breakRow.add(breakTd);
        tbody.add(breakRow);

        // Row 4: 13.00 - 15.00
        tbody.add(createScheduleRow("13.00 - 15.00", "DN", "ad-pill-pink", "IM", "ad-pill-teal", "KS", "ad-pill-orange", "RH", "ad-pill-purple", "FT", "ad-pill-green"));

        // Row 5: 15.00 - 17.00
        tbody.add(createScheduleRow("15.00 - 17.00", "BG", "ad-pill-yellow", "AR", "ad-pill-blue", "DN", "ad-pill-pink", "IM", "ad-pill-teal", "KS", "ad-pill-orange"));

        table.add(tbody);
        cardTable.add(table);

        // Right Officer List Card
        Div cardOfficers = new Div();
        cardOfficers.addClassName("ad-card");

        Span title = new Span("Daftar Kode Petugas");
        title.addClassName("ad-card-title");

        Div list = new Div();
        list.addClassName("ad-officer-list");

        list.add(createOfficerItem("AR", "ad-pill-blue", "Arif Rahman"));
        list.add(createOfficerItem("DN", "ad-pill-pink", "Dinda Nur"));
        list.add(createOfficerItem("IM", "ad-pill-teal", "Imam Maulana"));
        list.add(createOfficerItem("KS", "ad-pill-orange", "Kevin Saputra"));
        list.add(createOfficerItem("RH", "ad-pill-purple", "Rina Hidayati"));
        list.add(createOfficerItem("FT", "ad-pill-green", "Fitri Aulia"));
        list.add(createOfficerItem("BG", "ad-pill-yellow", "Bagas Nugraha"));

        cardOfficers.add(title, list);

        wrap.add(cardTable, cardOfficers);
        return wrap;
    }

    private Tr createScheduleRow(String time, String c1, String p1, String c2, String p2, String c3, String p3, String c4, String p4, String c5, String p5) {
        Tr row = new Tr();
        row.add(new Td(time));
        row.add(createPillTd(c1, p1));
        row.add(createPillTd(c2, p2));
        row.add(createPillTd(c3, p3));
        row.add(createPillTd(c4, p4));
        row.add(createPillTd(c5, p5));
        return row;
    }

    private Td createPillTd(String code, String pillClass) {
        Td td = new Td();
        Span pill = new Span(code);
        pill.addClassName("ad-pill");
        pill.addClassName(pillClass);
        td.add(pill);
        return td;
    }

    private Div createOfficerItem(String code, String pillClass, String name) {
        Div item = new Div();
        item.addClassName("ad-officer-item");

        Span pill = new Span(code);
        pill.addClassName("ad-pill");
        pill.addClassName(pillClass);

        Span nameTxt = new Span(name);
        nameTxt.addClassName("ad-officer-name");

        item.add(pill, nameTxt);
        return item;
    }
}

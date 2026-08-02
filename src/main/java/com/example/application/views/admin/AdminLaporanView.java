package com.example.application.views.admin;

import com.example.application.views.BlankLayout;
<<<<<<< HEAD
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.textfield.TextField;
=======
import com.vaadin.flow.component.html.*;
>>>>>>> 95c1a299f9ff90e419379ec411258642255f57ec
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "admin/laporan", layout = BlankLayout.class)
<<<<<<< HEAD
@PageTitle("Kelola Laporan - Lapor Gess")
public class AdminLaporanView extends Div {

    public AdminLaporanView() {
        addClassName("ad-root");

        Div sidebar = AdminLayout.buildSidebar("admin/laporan");

        Div main = new Div();
        main.addClassName("ad-main");

        Div topbar = AdminLayout.buildTopbar("Laporan");

        Div body = new Div();
        body.addClassName("ad-body");

        // Main Card
        Div card = new Div();
        card.addClassName("ad-card");
        card.getStyle().set("padding", "20px 24px");

        // Controls bar
        Div controls = new Div();
        controls.getStyle().set("display", "flex").set("justify-content", "space-between").set("margin-bottom", "20px");

        TextField search = new TextField();
        search.setPlaceholder("Cari laporan...");
        search.getStyle().set("width", "280px");

        ComboBox<String> filterStatus = new ComboBox<>();
        filterStatus.setItems("Semua Status", "Diproses", "Selesai", "Menunggu");
        filterStatus.setValue("Semua Status");
        filterStatus.getStyle().set("width", "160px");

        controls.add(search, filterStatus);
        card.add(controls);

        // Table
        Table table = new Table();
        table.addClassName("ad-table");

        Thead thead = new Thead();
        Tr headerRow = new Tr();
        headerRow.add(new Th("ID"), new Th("Judul"), new Th("Lokasi"), new Th("Pelapor"), new Th("Tanggal"), new Th("Status"), new Th("Aksi"));
        thead.add(headerRow);
        table.add(thead);

        Tbody tbody = new Tbody();

        // Row 1
        tbody.add(createRow("#LAP-001", "Pohon Tumbang di Jl. Utama", "Jl. Sudirman, RT 01/02", "Budi S.", "2026-07-16", "Diproses", "ad-status-diproses"));

        // Row 2
        tbody.add(createRow("#LAP-002", "Lampu Jalan Mati", "Jl. Merdeka, RT 03/02", "Siti M.", "2026-07-15", "Selesai", "ad-status-selesai"));

        // Row 3
        tbody.add(createRow("#LAP-003", "Pembuangan Sampah Liar", "Area Taman, RT 05/01", "Agus P.", "2026-07-17", "Menunggu", "ad-status-menunggu"));

        table.add(tbody);
        card.add(table);

        body.add(card);
        main.add(topbar, body);
        add(sidebar, main);
    }

    private Tr createRow(String id, String judul, String lokasi, String pelapor, String tanggal, String statusTxt, String statusClass) {
        Tr row = new Tr();

        Td tdId = new Td(id);
        tdId.getStyle().set("font-weight", "700");

        Td tdJudul = new Td(judul);
        tdJudul.getStyle().set("font-weight", "600");

        Td tdLokasi = new Td(lokasi);
        tdLokasi.getStyle().set("color", "#64748B");

        Td tdPelapor = new Td(pelapor);
        Td tdTanggal = new Td(tanggal);

        Td tdStatus = new Td();
        Span badge = new Span(statusTxt);
        badge.addClassName(statusClass);
        tdStatus.add(badge);

        Td tdAksi = new Td();
        Span action = new Span("Tinjau");
        action.addClassName("ad-action-link");
        action.addClickListener(e -> showDetailDialog(id, judul, lokasi, pelapor, tanggal, statusTxt));
        tdAksi.add(action);

        row.add(tdId, tdJudul, tdLokasi, tdPelapor, tdTanggal, tdStatus, tdAksi);
        return row;
    }

    private void showDetailDialog(String id, String judul, String lokasi, String pelapor, String tanggal, String status) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Detail Laporan " + id);

        Div content = new Div();
        content.getStyle().set("display", "flex").set("flex-direction", "column").set("gap", "12px").set("padding", "10px 0");

        content.add(new Paragraph("Judul: " + judul));
        content.add(new Paragraph("Lokasi: " + lokasi));
        content.add(new Paragraph("Pelapor: " + pelapor));
        content.add(new Paragraph("Tanggal: " + tanggal));
        content.add(new Paragraph("Status saat ini: " + status));

        dialog.add(content);
        dialog.open();
=======
@PageTitle("Laporan - Admin Lapor")
public class AdminLaporanView extends Div {

    public AdminLaporanView() {
        addClassName("a-root");
        add(AdminDasborView.buildSidebar("laporan"), buildMain());
    }

    private Div buildMain() {
        Div main = new Div();
        main.addClassName("a-main");

        // Topbar
        Div topbar = new Div();
        topbar.addClassName("a-topbar");
        Span title = new Span("Laporan");
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
        body.add(buildTableCard());
        main.add(body);

        return main;
    }

    private Div buildTableCard() {
        Div card = new Div();
        card.addClassName("a-table-card");

        // Filters row
        Div filters = new Div();
        filters.addClassName("a-table-filters");

        NativeLabel search = new NativeLabel();
        search.getElement().setProperty("innerHTML",
            "<input class='a-search-input' placeholder='Cari laporan...' />");
        filters.add(search);

        NativeLabel statusSelect = new NativeLabel();
        statusSelect.getElement().setProperty("innerHTML",
            "<select class='a-select'>" +
            "<option>Semua Status</option>" +
            "<option>Diproses</option>" +
            "<option>Selesai</option>" +
            "<option>Menunggu</option>" +
            "<option>Ditolak</option>" +
            "</select>");
        filters.add(statusSelect);
        card.add(filters);

        // Table
        StringBuilder html = new StringBuilder();
        html.append("<table class='a-data-table'>");
        html.append("<thead><tr>");
        html.append("<th>ID</th>");
        html.append("<th>Judul</th>");
        html.append("<th>Lokasi</th>");
        html.append("<th>Pelapor</th>");
        html.append("<th>Tanggal</th>");
        html.append("<th>Status</th>");
        html.append("<th>Aksi</th>");
        html.append("</tr></thead>");
        html.append("<tbody>");

        // Row 1
        html.append("<tr>");
        html.append("<td>#LAP-0 001</td>");
        html.append("<td>Pohon Tumbang di Jl. Utama</td>");
        html.append("<td>Jl. Sudirman, RT 01/02</td>");
        html.append("<td>Budi S.</td>");
        html.append("<td>2026-07-16</td>");
        html.append("<td><span class='a-status-badge a-status-diproses'>Diproses</span></td>");
        html.append("<td><span class='a-tinjau-link'>Tinjau</span></td>");
        html.append("</tr>");

        // Row 2
        html.append("<tr>");
        html.append("<td>#LAP-0 002</td>");
        html.append("<td>Lampu Jalan Mati</td>");
        html.append("<td>Jl. Merdeka, RT 03/02</td>");
        html.append("<td>Siti M.</td>");
        html.append("<td>2026-07-15</td>");
        html.append("<td><span class='a-status-badge a-status-selesai'>Selesai</span></td>");
        html.append("<td><span class='a-tinjau-link'>Tinjau</span></td>");
        html.append("</tr>");

        // Row 3
        html.append("<tr>");
        html.append("<td>#LAP-0 003</td>");
        html.append("<td>Pembuangan Sampah Liar</td>");
        html.append("<td>Area Taman, RT 05/01</td>");
        html.append("<td>Agus P.</td>");
        html.append("<td>2026-07-17</td>");
        html.append("<td><span class='a-status-badge a-status-menunggu'>Menunggu</span></td>");
        html.append("<td><span class='a-tinjau-link'>Tinjau</span></td>");
        html.append("</tr>");

        html.append("</tbody></table>");

        Div tableWrapper = new Div();
        tableWrapper.getElement().setProperty("innerHTML", html.toString());
        card.add(tableWrapper);

        return card;
>>>>>>> 95c1a299f9ff90e419379ec411258642255f57ec
    }
}

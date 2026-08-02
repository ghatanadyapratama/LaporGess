package com.example.application.views.admin;

import com.example.application.views.BlankLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "admin/laporan", layout = BlankLayout.class)
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
    }
}

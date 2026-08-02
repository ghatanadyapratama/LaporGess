package com.example.application.views.admin;

import com.example.application.views.BlankLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "admin/laporan", layout = BlankLayout.class)
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
    }
}

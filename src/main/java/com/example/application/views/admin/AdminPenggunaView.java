package com.example.application.views.admin;

import com.example.application.views.BlankLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "admin/pengguna", layout = BlankLayout.class)
@PageTitle("Pengguna - Admin Lapor")
public class AdminPenggunaView extends Div {

    public AdminPenggunaView() {
        addClassName("a-root");
        add(AdminDasborView.buildSidebar("pengguna"), buildMain());
    }

    private Div buildMain() {
        Div main = new Div();
        main.addClassName("a-main");

        // Topbar
        Div topbar = new Div();
        topbar.addClassName("a-topbar");
        Span title = new Span("Pengguna");
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
            "<input class='a-search-input' placeholder='Cari pengguna...' />");
        filters.add(search);

        NativeLabel roleSelect = new NativeLabel();
        roleSelect.getElement().setProperty("innerHTML",
            "<select class='a-select'>" +
            "<option>Semua Peran</option>" +
            "<option>Warga</option>" +
            "<option>Petugas</option>" +
            "<option>Admin</option>" +
            "</select>");
        filters.add(roleSelect);

        NativeLabel statusSelect = new NativeLabel();
        statusSelect.getElement().setProperty("innerHTML",
            "<select class='a-select'>" +
            "<option>Semua Status</option>" +
            "<option>Aktif</option>" +
            "<option>Nonaktif</option>" +
            "</select>");
        filters.add(statusSelect);
        card.add(filters);

        // Table
        StringBuilder html = new StringBuilder();
        html.append("<table class='a-data-table'>");
        html.append("<thead><tr>");
        html.append("<th>Data Pengguna</th>");
        html.append("<th>ID Pengguna</th>");
        html.append("<th>Peran</th>");
        html.append("<th>Area / Lokasi</th>");
        html.append("<th>Tanggal Lahir</th>");
        html.append("<th>Jenis Kelamin</th>");
        html.append("</tr></thead>");
        html.append("<tbody>");

        // Row 1
        html.append("<tr>");
        html.append("<td><div class='a-user-cell'>");
        html.append("<div class='a-user-avatar'></div>");
        html.append("<div class='a-user-info'>");
        html.append("<span class='a-user-name-text'>Budi Santoso</span>");
        html.append("<span class='a-user-username'>@budis</span>");
        html.append("</div></div></td>");
        html.append("<td>USR-001</td>");
        html.append("<td><span class='a-role-badge a-role-warga'>Warga</span></td>");
        html.append("<td><div class='a-location-cell'><span class='a-loc-icon'>📍</span> RT 01/RW 02</div></td>");
        html.append("<td>15 Mei 1985</td>");
        html.append("<td>Laki-laki</td>");
        html.append("</tr>");

        // Row 2
        html.append("<tr>");
        html.append("<td><div class='a-user-cell'>");
        html.append("<div class='a-user-avatar'></div>");
        html.append("<div class='a-user-info'>");
        html.append("<span class='a-user-name-text'>Siti Aminah</span>");
        html.append("<span class='a-user-username'>@sitia</span>");
        html.append("</div></div></td>");
        html.append("<td>USR-002</td>");
        html.append("<td><span class='a-role-badge a-role-warga'>Warga</span></td>");
        html.append("<td><div class='a-location-cell'><span class='a-loc-icon'>📍</span> RT 03/RW 02</div></td>");
        html.append("<td>22 Ags 1990</td>");
        html.append("<td>Perempuan</td>");
        html.append("</tr>");

        // Row 3
        html.append("<tr>");
        html.append("<td><div class='a-user-cell'>");
        html.append("<div class='a-user-avatar'></div>");
        html.append("<div class='a-user-info'>");
        html.append("<span class='a-user-name-text'>Agus Pratama</span>");
        html.append("<span class='a-user-username'>@agusp</span>");
        html.append("</div></div></td>");
        html.append("<td>USR-003</td>");
        html.append("<td><span class='a-role-badge a-role-petugas'>Petugas</span></td>");
        html.append("<td><div class='a-location-cell'><span class='a-loc-icon'>📍</span> Distrik Pusat</div></td>");
        html.append("<td>10 Nov 1988</td>");
        html.append("<td>Laki-laki</td>");
        html.append("</tr>");

        html.append("</tbody></table>");

        Div tableWrapper = new Div();
        tableWrapper.getElement().setProperty("innerHTML", html.toString());
        card.add(tableWrapper);

        // Pagination
        card.add(buildPagination());

        return card;
    }

    private Div buildPagination() {
        Div pagination = new Div();
        pagination.addClassName("a-pagination");

        Span info = new Span("Menampilkan 1 hingga 5 dari 450 pengguna");
        info.addClassName("a-pagination-info");

        Div controls = new Div();
        controls.addClassName("a-pagination-controls");

        // Previous button
        NativeLabel prevBtn = new NativeLabel();
        prevBtn.getElement().setProperty("innerHTML",
            "<button class='a-page-btn a-page-btn-nav a-page-btn-disabled'>Sebelumnya</button>");
        controls.add(prevBtn);

        // Page numbers
        NativeLabel page1 = new NativeLabel();
        page1.getElement().setProperty("innerHTML",
            "<button class='a-page-btn a-page-btn-active'>1</button>");
        controls.add(page1);

        NativeLabel page2 = new NativeLabel();
        page2.getElement().setProperty("innerHTML",
            "<button class='a-page-btn'>2</button>");
        controls.add(page2);

        NativeLabel page3 = new NativeLabel();
        page3.getElement().setProperty("innerHTML",
            "<button class='a-page-btn'>3</button>");
        controls.add(page3);

        Span ellipsis = new Span("...");
        ellipsis.addClassName("a-page-ellipsis");
        controls.add(ellipsis);

        // Next button
        NativeLabel nextBtn = new NativeLabel();
        nextBtn.getElement().setProperty("innerHTML",
            "<button class='a-page-btn a-page-btn-nav'>Selanjutnya</button>");
        controls.add(nextBtn);

        pagination.add(info, controls);
        return pagination;
    }
}

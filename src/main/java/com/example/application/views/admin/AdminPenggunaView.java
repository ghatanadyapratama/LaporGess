package com.example.application.views.admin;

import com.example.application.views.BlankLayout;
<<<<<<< HEAD
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.textfield.TextField;
=======
import com.vaadin.flow.component.html.*;
>>>>>>> 95c1a299f9ff90e419379ec411258642255f57ec
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "admin/pengguna", layout = BlankLayout.class)
<<<<<<< HEAD
@PageTitle("Kelola Pengguna - Lapor Gess")
public class AdminPenggunaView extends Div {

    public AdminPenggunaView() {
        addClassName("ad-root");

        Div sidebar = AdminLayout.buildSidebar("admin/pengguna");

        Div main = new Div();
        main.addClassName("ad-main");

        Div topbar = AdminLayout.buildTopbar("Pengguna");

        Div body = new Div();
        body.addClassName("ad-body");

        // Main Card Container
        Div card = new Div();
        card.addClassName("ad-card");

        // Controls
        Div controls = new Div();
        controls.getStyle().set("display", "flex").set("align-items", "center").set("gap", "16px").set("margin-bottom", "20px");

        TextField search = new TextField();
        search.setPlaceholder("Cari pengguna...");
        search.getStyle().set("width", "260px");

        ComboBox<String> filterPeran = new ComboBox<>();
        filterPeran.setItems("Semua Peran", "Warga", "Petugas", "Admin");
        filterPeran.setValue("Semua Peran");
        filterPeran.getStyle().set("width", "150px");

        ComboBox<String> filterStatus = new ComboBox<>();
        filterStatus.setItems("Semua Status", "Aktif", "Pending", "Non-aktif");
        filterStatus.setValue("Semua Status");
        filterStatus.getStyle().set("width", "150px");

        controls.add(search, filterPeran, filterStatus);
        card.add(controls);

        // Table
        Table table = new Table();
        table.addClassName("ad-table");

        Thead thead = new Thead();
        Tr headerRow = new Tr();
        headerRow.add(
            new Th("Data Pengguna"),
            new Th("ID Pengguna"),
            new Th("Peran"),
            new Th("Area / Lokasi"),
            new Th("Tanggal Lahir"),
            new Th("Jenis Kelamin")
        );
        thead.add(headerRow);
        table.add(thead);

        Tbody tbody = new Tbody();

        // Row 1
        tbody.add(createPenggunaRow("Budi Santoso", "@budis", "USR-001", "Warga", "ad-role-warga", "RT 01/RW 02", "15 Mei 1985", "Laki-laki"));

        // Row 2
        tbody.add(createPenggunaRow("Siti Aminah", "@sitia", "USR-002", "Warga", "ad-role-warga", "RT 03/RW 02", "22 Ags 1990", "Perempuan"));

        // Row 3
        tbody.add(createPenggunaRow("Agus Pratama", "@agusp", "USR-003", "Petugas", "ad-role-petugas", "Distrik Pusat", "10 Nov 1988", "Laki-laki"));

        table.add(tbody);
        card.add(table);

        // Pagination Footer
        Div pagination = new Div();
        pagination.addClassName("ad-pagination");

        Span info = new Span("Menampilkan 1 hingga 5 dari 450 pengguna");
        info.addClassName("ad-page-info");

        Div pageNav = new Div();
        pageNav.addClassName("ad-page-nav");

        Button btnPrev = new Button("Sebelumnya");
        btnPrev.addClassName("ad-page-btn");

        Button btn1 = new Button("1");
        btn1.addClassName("ad-page-btn");
        btn1.addClassName("ad-page-btn-active");

        Button btn2 = new Button("2");
        btn2.addClassName("ad-page-btn");

        Button btn3 = new Button("3");
        btn3.addClassName("ad-page-btn");

        Span dots = new Span("...");
        dots.getStyle().set("padding", "0 6px").set("color", "#94A3B8");

        Button btnNext = new Button("Selanjutnya");
        btnNext.addClassName("ad-page-btn");

        pageNav.add(btnPrev, btn1, btn2, btn3, dots, btnNext);
        pagination.add(info, pageNav);

        card.add(pagination);
        body.add(card);

        main.add(topbar, body);
        add(sidebar, main);
    }

    private Tr createPenggunaRow(String name, String username, String id, String role, String roleClass, String area, String birth, String gender) {
        Tr row = new Tr();

        // Data Pengguna
        Td tdUser = new Td();
        Div flexUser = new Div();
        flexUser.getStyle().set("display", "flex").set("align-items", "center").set("gap", "12px");

        Div avatar = new Div();
        avatar.getStyle().set("width", "36px").set("height", "36px").set("border-radius", "50%")
            .set("background-color", "#F1F5F9").set("border", "1px solid #E2E8F0");

        Div names = new Div();
        names.getStyle().set("display", "flex").set("flex-direction", "column");
        Span nameTxt = new Span(name);
        nameTxt.getStyle().set("font-weight", "700").set("color", "#1E293B");
        Span unameTxt = new Span(username);
        unameTxt.getStyle().set("font-size", "0.78rem").set("color", "#94A3B8");
        names.add(nameTxt, unameTxt);

        flexUser.add(avatar, names);
        tdUser.add(flexUser);

        // ID Pengguna
        Td tdId = new Td(id);
        tdId.getStyle().set("font-weight", "600").set("color", "#64748B");

        // Peran
        Td tdRole = new Td();
        Span badge = new Span(role);
        if ("Petugas".equalsIgnoreCase(role)) {
            badge.getStyle().set("background-color", "#DBEAFE").set("color", "#1D4ED8")
                .set("padding", "4px 12px").set("border-radius", "20px").set("font-weight", "700").set("font-size", "0.8rem");
        } else {
            badge.getStyle().set("background-color", "#F1F5F9").set("color", "#475569")
                .set("padding", "4px 12px").set("border-radius", "20px").set("font-weight", "700").set("font-size", "0.8rem");
        }
        tdRole.add(badge);

        // Area / Lokasi
        Td tdArea = new Td(area);

        // Tanggal Lahir
        Td tdBirth = new Td(birth);

        // Jenis Kelamin
        Td tdGender = new Td(gender);

        row.add(tdUser, tdId, tdRole, tdArea, tdBirth, tdGender);
        return row;
=======
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
>>>>>>> 95c1a299f9ff90e419379ec411258642255f57ec
    }
}

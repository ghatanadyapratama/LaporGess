package com.example.application.views.admin;

import com.example.application.views.BlankLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "admin/verifikasi", layout = BlankLayout.class)
@PageTitle("Verifikasi Pengguna - Lapor Gess")
public class AdminVerifikasiView extends Div {

    private Div verifList;

    public AdminVerifikasiView() {
        addClassName("ad-root");

        Div sidebar = AdminLayout.buildSidebar("admin/verifikasi");

        Div main = new Div();
        main.addClassName("ad-main");

        Div topbar = AdminLayout.buildTopbar("Verifikasi Pengguna");

        Div body = new Div();
        body.addClassName("ad-body");

        // Main Card Container
        Div card = new Div();
        card.addClassName("ad-card");

        Div header = new Div();
        header.addClassName("ad-card-header");
        Span title = new Span("Pendaftaran Menunggu Verifikasi");
        title.addClassName("ad-card-title");

        TextField search = new TextField();
        search.setPlaceholder("Cari nama atau NIK...");
        search.getStyle().set("width", "260px");

        header.add(title, search);
        card.add(header);

        verifList = new Div();
        verifList.addClassName("ad-verif-list");

        // Card 1 (from screenshot)
        verifList.add(createVerifCard(
            "Rina Wijaya",
            "@rinaw",
            "Warga",
            "3271012345678901",
            "05 Mar 1995 • Perempuan",
            "RT 02/RW 01 (Perumahan Indah, Jl. Melati No 5)"
        ));

        // Additional Card 2 for rich demonstration
        verifList.add(createVerifCard(
            "Budi Gunawan",
            "@budig",
            "Warga",
            "3271019876543210",
            "12 Jan 1990 • Laki-laki",
            "RT 01/RW 02 (Jl. Mawar No 12)"
        ));

        card.add(verifList);
        body.add(card);

        main.add(topbar, body);
        add(sidebar, main);
    }

    private Div createVerifCard(String name, String username, String role, String nik, String ttlGender, String address) {
        Div card = new Div();
        card.addClassName("ad-verif-card");

        Div left = new Div();
        left.addClassName("ad-verif-left");

        Div avatar = new Div();
        avatar.addClassName("ad-verif-avatar");

        Div info = new Div();
        info.addClassName("ad-verif-info");

        Span roleBadge = new Span("Daftar Sebagai: " + role);
        roleBadge.addClassName("ad-role-badge-teal");

        Div grid = new Div();
        grid.addClassName("ad-verif-grid");

        // Field 1: Nama Lengkap
        grid.add(createFieldBlock("Nama Lengkap", name));

        // Field 2: Username
        grid.add(createFieldBlock("Username", username));

        // Field 3: NIK KTP
        grid.add(createFieldBlock("NIK KTP", nik));

        // Field 4: Tanggal Lahir & Gender
        grid.add(createFieldBlock("Tanggal Lahir & Gender", ttlGender));

        // Field 5: Area / Alamat (spans full)
        Div addrBlock = createFieldBlock("Area / Alamat", address);
        addrBlock.getStyle().set("grid-column", "span 2");
        grid.add(addrBlock);

        info.add(roleBadge, grid);
        left.add(avatar, info);

        // Right Actions
        Div actions = new Div();
        actions.addClassName("ad-verif-actions");

        Button btnApprove = new Button("✔ Setujui Akun");
        btnApprove.addClassName("ad-btn-approve");
        btnApprove.addClickListener(e -> {
            Notification.show("Akun " + name + " berhasil disetujui!");
            card.removeFromParent();
        });

        Button btnReject = new Button("🗑 Tolak Pendaftaran");
        btnReject.addClassName("ad-btn-reject");
        btnReject.addClickListener(e -> {
            Notification.show("Pendaftaran " + name + " ditolak.");
            card.removeFromParent();
        });

        actions.add(btnApprove, btnReject);
        card.add(left, actions);

        return card;
    }

    private Div createFieldBlock(String label, String value) {
        Div block = new Div();
        block.addClassName("ad-field-block");

        Span lbl = new Span(label);
        lbl.addClassName("ad-field-lbl");

        Span val = new Span(value);
        val.addClassName("ad-field-val");

        block.add(lbl, val);
        return block;
    }
}

package com.example.application.views.admin;

import com.example.application.views.BlankLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "admin/verifikasi", layout = BlankLayout.class)
@PageTitle("Verifikasi Pengguna - Admin Lapor")
public class AdminVerifikasiView extends Div {

    public AdminVerifikasiView() {
        addClassName("a-root");
        add(AdminDasborView.buildSidebar("verifikasi"), buildMain());
    }

    private Div buildMain() {
        Div main = new Div();
        main.addClassName("a-main");

        // Topbar
        Div topbar = new Div();
        topbar.addClassName("a-topbar");
        Span title = new Span("Verifikasi Pengguna");
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

        // Header row
        Div headerRow = new Div();
        headerRow.addClassName("a-verif-header");
        Span subtitle = new Span("Pendaftaran Menunggu Verifikasi");
        subtitle.addClassName("a-verif-subtitle");

        NativeLabel search = new NativeLabel();
        search.getElement().setProperty("innerHTML",
            "<input class='a-search-input' placeholder='Cari nama atau NIK...' />");
        headerRow.add(subtitle, search);
        body.add(headerRow);

        // Verification card
        body.add(buildVerifCard());

        main.add(body);
        return main;
    }

    private Div buildVerifCard() {
        Div card = new Div();
        card.addClassName("a-verif-card");

        // Photo section
        Div photoWrapper = new Div();
        photoWrapper.addClassName("a-verif-photo-wrapper");

        Div photo = new Div();
        photo.addClassName("a-verif-photo");
        Div circle = new Div();
        circle.addClassName("a-verif-photo-circle");
        photo.add(circle);
        photoWrapper.add(photo);

        Span roleBadge = new Span("Daftar Sebagai: Warga");
        roleBadge.addClassName("a-verif-role-badge");
        photoWrapper.add(roleBadge);

        card.add(photoWrapper);

        // Details grid
        Div details = new Div();
        details.addClassName("a-verif-details");

        details.add(verifField("Nama Lengkap", "Rina Wijaya"));
        details.add(verifField("Username", "@rinaw"));
        details.add(verifField("NIK KTP", "3271012345678901"));
        details.add(verifField("Tanggal Lahir & Gender", "05 Mar 1995 • Perempuan"));

        // Full-width address field
        Div addressField = new Div();
        addressField.addClassName("a-verif-field-full");
        Span addressLabel = new Span("Area / Alamat");
        addressLabel.addClassName("a-verif-field-label");
        Span addressValue = new Span("📍 RT 02/RW 01 (Perumahan Indah, Jl. Melati No 5)");
        addressValue.addClassName("a-verif-field-value");
        addressField.add(addressLabel, addressValue);
        details.add(addressField);

        card.add(details);

        // Action buttons
        Div actions = new Div();
        actions.addClassName("a-verif-actions");

        NativeLabel approveBtn = new NativeLabel();
        approveBtn.getElement().setProperty("innerHTML",
            "<button class='a-btn-approve'>✅ Setujui Akun</button>");
        actions.add(approveBtn);

        NativeLabel rejectBtn = new NativeLabel();
        rejectBtn.getElement().setProperty("innerHTML",
            "<button class='a-btn-reject'>🗑 Tolak Pendaftaran</button>");
        actions.add(rejectBtn);

        card.add(actions);
        return card;
    }

    private Div verifField(String label, String value) {
        Div field = new Div();
        field.addClassName("a-verif-field");
        Span lbl = new Span(label);
        lbl.addClassName("a-verif-field-label");
        Span val = new Span(value);
        val.addClassName("a-verif-field-value");
        field.add(lbl, val);
        return field;
    }
}

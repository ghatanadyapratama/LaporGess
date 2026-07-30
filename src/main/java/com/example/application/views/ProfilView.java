package com.example.application.views;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "profil", layout = BlankLayout.class)
@PageTitle("Profil Anda - Lapor Gess")
public class ProfilView extends Div {

    // ── Modal & overlay references ──
    private Div modalOverlay;

    public ProfilView() {
        addClassName("d-root");
        add(buildSidebar(), buildMain());

        // Build and attach modal (hidden by default)
        modalOverlay = buildEditModal();
        add(modalOverlay);
    }

    // ══════════════════════════════════════════
    //  SIDEBAR
    // ══════════════════════════════════════════
    private Div buildSidebar() {
        Div sidebar = new Div();
        sidebar.addClassName("d-sidebar");

        Div logo = new Div();
        logo.addClassName("d-logo");
        Image logoImg = new Image("icons/logoLaporGess.png", "logo");
        logoImg.addClassName("d-logo-img");
        Span logoTxt = new Span("Lapor Gess");
        logoTxt.addClassName("d-logo-txt");
        logo.add(logoImg, logoTxt);
        sidebar.add(logo);

        Div nav = new Div();
        nav.addClassName("d-nav");

        Div homeItem = navItem("icons/home.png", "Beranda", false);
        homeItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("dashboard")));
        nav.add(homeItem);

        Div laporanItem = navItem("icons/laporan.png", "Laporan Saya", false);
        laporanItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("laporan-saya")));
        nav.add(laporanItem);

        Div peringkatItem = navItem("icons/iconPiala.png", "Peringkat", false);
        peringkatItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("peringkat")));
        nav.add(peringkatItem);

        Div hadiahItem = navItem("icons/hadiah.png", "Toko Hadiah", false);
        hadiahItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("toko-hadiah")));
        nav.add(hadiahItem);

        Div edukasiItem = navItem("icons/buku.png", "Edukasi", false);
        edukasiItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("edukasi")));
        nav.add(edukasiItem);

        nav.add(navItem("icons/profile.png", "Profil", true));
        sidebar.add(nav);

        Div sp = new Div();
        sp.addClassName("d-sidebar-spacer");
        sidebar.add(sp);

        Div cta = new Div();
        cta.addClassName("d-cta");
        cta.add(new Span("+ Buat Laporan"));
        cta.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("buat-laporan")));
        sidebar.add(cta);

        return sidebar;
    }

    private Div navItem(String icon, String label, boolean active) {
        Div item = new Div();
        item.addClassName("d-nav-item");
        if (active) item.addClassName("d-nav-active");
        Image img = new Image(icon, label);
        img.addClassName("d-nav-icon");
        Span txt = new Span(label);
        txt.addClassName("d-nav-label");
        item.add(img, txt);
        return item;
    }

    // ══════════════════════════════════════════
    //  MAIN CONTENT
    // ══════════════════════════════════════════
    private Div buildMain() {
        Div main = new Div();
        main.addClassName("d-main");
        main.add(buildTopbar());
        main.add(buildBody());
        return main;
    }

    private Div buildTopbar() {
        Div bar = new Div();
        bar.addClassName("d-topbar");
        Span title = new Span("Profil Anda");
        title.addClassName("d-topbar-title");
        bar.add(title);

        Div right = new Div();
        right.addClassName("d-topbar-right");

        Div badge = new Div();
        badge.addClassName("d-poin-badge");
        Image trophy = new Image("icons/pialaOren.png", "poin");
        trophy.addClassName("d-poin-icon");
        Span poinTxt = new Span("1.250 Poin");
        poinTxt.addClassName("d-poin-txt");
        badge.add(trophy, poinTxt);

        Div bell = new Div();
        bell.addClassName("d-bell");
        Image bellImg = new Image("icons/bell.png", "notif");
        bellImg.addClassName("d-bell-img");
        bell.add(bellImg);
        bell.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("notifikasi")));

        Div av = new Div();
        av.addClassName("d-avatar");
        av.add(new Span("B"));

        right.add(badge, bell, av);
        bar.add(right);
        return bar;
    }

    private Div buildBody() {
        Div body = new Div();
        body.addClassNames("d-body", "pf-body");

        // === Profile Card ===
        Div profileCard = new Div();
        profileCard.addClassName("pf-profile-card");

        // Dark banner
        Div banner = new Div();
        banner.addClassName("pf-banner");
        profileCard.add(banner);

        // Info row
        Div infoRow = new Div();
        infoRow.addClassName("pf-info-row");

        // Avatar (large)
        Div avatarWrapper = new Div();
        avatarWrapper.addClassName("pf-avatar-wrapper");
        Div bigAvatar = new Div();
        bigAvatar.addClassName("pf-big-avatar");
        bigAvatar.add(new Span("B"));
        avatarWrapper.add(bigAvatar);

        Div nameBlock = new Div();
        nameBlock.addClassName("pf-name-block");
        H2 nameTitle = new H2("Budi Santoso");
        nameTitle.addClassName("pf-name");
        Div locRow = new Div();
        locRow.addClassName("pf-loc-row");
        Div locIcon = new Div();
        locIcon.getElement().setProperty("innerHTML",
            "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"14\" height=\"14\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"#888\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\">" +
            "<path d=\"M20 10c0 6-8 12-8 12s-8-6-8-12a8 8 0 0 1 16 0z\"/>" +
            "<circle cx=\"12\" cy=\"10\" r=\"3\"/></svg>"
        );
        Span locTxt = new Span("Warga RT 01 / RW 02");
        locTxt.addClassName("pf-loc-txt");
        locRow.add(locIcon, locTxt);
        nameBlock.add(nameTitle, locRow);

        NativeButton editBtn = new NativeButton("Edit Profil");
        editBtn.addClassName("pf-edit-btn");
        editBtn.addClickListener(e -> showModal());

        infoRow.add(avatarWrapper, nameBlock, editBtn);
        profileCard.add(infoRow);
        body.add(profileCard);

        // === Bottom Two-Column Row ===
        Div bottomRow = new Div();
        bottomRow.addClassName("pf-bottom-row");
        bottomRow.add(buildSettingsCard());
        bottomRow.add(buildStatsCard());
        body.add(bottomRow);

        return body;
    }

    private Div buildSettingsCard() {
        Div card = new Div();
        card.addClassName("pf-settings-card");

        H3 cardTitle = new H3("Pengaturan");
        cardTitle.addClassName("pf-card-title");
        card.add(cardTitle);

        card.add(settingRow("icons/setting.png", "Akun & Sandi", false));
        card.add(buildDivider());
        card.add(settingRow("icons/notif.png", "Notifikasi", false));
        card.add(buildDivider());
        card.add(settingRow("icons/out.png", "Keluar", true));

        return card;
    }

    private Div settingRow(String icon, String label, boolean isDanger) {
        Div row = new Div();
        row.addClassName("pf-setting-row");
        if (isDanger) row.addClassName("pf-setting-danger");

        Image img = new Image(icon, label);
        img.addClassName("pf-setting-icon");

        Span txt = new Span(label);
        txt.addClassName("pf-setting-label");

        Div spacer = new Div();
        spacer.addClassName("pf-setting-spacer");

        if (!isDanger) {
            Div arrow = new Div();
            arrow.getElement().setProperty("innerHTML",
                "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"#aaa\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\">" +
                "<path d=\"M9 18l6-6-6-6\"/></svg>"
            );
            row.add(img, txt, spacer, arrow);
        } else {
            row.add(img, txt);
        }

        row.addClickListener(e -> {
            if (isDanger) {
                getUI().ifPresent(ui -> ui.navigate("login"));
            } else if ("Notifikasi".equalsIgnoreCase(label)) {
                getUI().ifPresent(ui -> ui.navigate("notifikasi"));
            } else if ("Akun & Sandi".equalsIgnoreCase(label)) {
                getUI().ifPresent(ui -> ui.navigate("akun-sandi"));
            }
        });

        return row;
    }

    private Hr buildDivider() {
        Hr hr = new Hr();
        hr.addClassName("pf-divider");
        return hr;
    }

    private Div buildStatsCard() {
        Div card = new Div();
        card.addClassName("pf-stats-card");

        H3 cardTitle = new H3("Statistik Kontribusi");
        cardTitle.addClassName("pf-card-title");
        card.add(cardTitle);

        Div statsGrid = new Div();
        statsGrid.addClassName("pf-stats-grid");

        // Total Laporan
        Div laporanStat = new Div();
        laporanStat.addClassNames("pf-stat-box", "pf-stat-green");
        Image laporanIcon = new Image("icons/laporanHijau.png", "laporan");
        laporanIcon.addClassName("pf-stat-icon");
        Div laporanText = new Div();
        laporanText.addClassName("pf-stat-text");
        Span laporanLabel = new Span("Total Laporan");
        laporanLabel.addClassName("pf-stat-label");
        Span laporanVal = new Span("24");
        laporanVal.addClassNames("pf-stat-value", "pf-stat-value-green");
        laporanText.add(laporanLabel, laporanVal);
        laporanStat.add(laporanIcon, laporanText);

        // Total Poin
        Div poinStat = new Div();
        poinStat.addClassNames("pf-stat-box", "pf-stat-orange");
        Image poinIcon = new Image("icons/pialaOren.png", "poin");
        poinIcon.addClassName("pf-stat-icon");
        Div poinText = new Div();
        poinText.addClassName("pf-stat-text");
        Span poinLabel = new Span("Total Poin");
        poinLabel.addClassNames("pf-stat-label", "pf-stat-label-orange");
        Span poinVal = new Span("1.250");
        poinVal.addClassNames("pf-stat-value", "pf-stat-value-orange");
        poinText.add(poinLabel, poinVal);
        poinStat.add(poinIcon, poinText);

        statsGrid.add(laporanStat, poinStat);
        card.add(statsGrid);

        return card;
    }

    // ══════════════════════════════════════════
    //  EDIT PROFIL MODAL
    // ══════════════════════════════════════════
    private Div buildEditModal() {
        // ── Overlay (backdrop) ──
        Div overlay = new Div();
        overlay.addClassName("ep-overlay");
        overlay.getElement().setAttribute("id", "ep-overlay");

        // ── Dialog box ──
        Div dialog = new Div();
        dialog.addClassName("ep-dialog");

        // ── Header ──
        Div header = new Div();
        header.addClassName("ep-header");
        Span title = new Span("Edit Profil Anda");
        title.addClassName("ep-title");
        NativeButton closeBtn = new NativeButton("×");
        closeBtn.addClassName("ep-close-btn");
        closeBtn.addClickListener(e -> hideModal());
        header.add(title, closeBtn);
        dialog.add(header);

        // ── Scrollable content ──
        Div content = new Div();
        content.addClassName("ep-content");

        // ── Photo section ──
        Div photoSection = new Div();
        photoSection.addClassName("ep-photo-section");

        Div photoAvatar = new Div();
        photoAvatar.addClassName("ep-photo-avatar");
        photoAvatar.add(new Span("B"));

        Div photoUploadBtn = new Div();
        photoUploadBtn.addClassName("ep-photo-upload-icon");
        photoUploadBtn.getElement().setProperty("innerHTML",
            "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"14\" height=\"14\" viewBox=\"0 0 24 24\" fill=\"white\">" +
            "<path d=\"M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 14H9v-2h2v-4H9V8h4v8h-2v2zm1-12c-.55 0-1 .45-1 1s.45 1 1 1 1-.45 1-1-.45-1-1-1z\"/></svg>"
        );
        photoAvatar.add(photoUploadBtn);

        Div photoInfo = new Div();
        photoInfo.addClassName("ep-photo-info");
        Span photoLabel = new Span("Foto Profil");
        photoLabel.addClassName("ep-photo-label");
        Span photoHint = new Span("Disarankan ukuran 1:1, maksimal 2MB (JPG/PNG).");
        photoHint.addClassName("ep-photo-hint");
        Span photoUpload = new Span("Unggah Foto Baru");
        photoUpload.addClassName("ep-photo-upload-link");
        photoInfo.add(photoLabel, photoHint, photoUpload);

        photoSection.add(photoAvatar, photoInfo);
        content.add(photoSection);

        // ── Divider ──
        content.add(buildModalDivider());

        // ── Field: Nama Lengkap (read-only) ──
        content.add(buildFieldGroup(
            "Nama Lengkap",
            buildReadOnlyField("Budi Santoso"),
            "⚠ Nama lengkap tidak dapat diubah demi keamanan. Hubungi Admin RT jika terdapat kesalahan."
        ));

        // ── Field: Username ──
        TextField usernameField = new TextField();
        usernameField.setValue("budis");
        usernameField.setWidthFull();
        usernameField.addClassName("ep-input");
        content.add(buildFieldGroup("Nama Pengguna (Username)", usernameField, null));

        // ── Field: Email ──
        TextField emailField = new TextField();
        emailField.setValue("budi@example.com");
        emailField.setWidthFull();
        emailField.addClassName("ep-input");
        content.add(buildFieldGroup("Email", emailField, null));

        // ── Field: Telepon ──
        TextField teleponField = new TextField();
        teleponField.setValue("-");
        teleponField.setWidthFull();
        teleponField.addClassName("ep-input");
        content.add(buildFieldGroup("Nomor Telepon", teleponField, null));

        // ── Two-column row: Jenis Kelamin + Tanggal Lahir ──
        Div twoCol = new Div();
        twoCol.addClassName("ep-two-col");

        Select<String> jenisKelaminSelect = new Select<>();
        jenisKelaminSelect.setItems("LAKI-LAKI", "PEREMPUAN");
        jenisKelaminSelect.setValue("LAKI-LAKI");
        jenisKelaminSelect.setWidthFull();
        jenisKelaminSelect.addClassName("ep-input");
        Div jenisKelaminGroup = buildFieldGroup("Jenis Kelamin", jenisKelaminSelect, null);
        jenisKelaminGroup.addClassName("ep-two-col-item");

        DatePicker tanggalLahirPicker = new DatePicker();
        tanggalLahirPicker.setWidthFull();
        tanggalLahirPicker.addClassName("ep-input");
        Div tanggalLahirGroup = buildFieldGroup("Tanggal Lahir", tanggalLahirPicker, null);
        tanggalLahirGroup.addClassName("ep-two-col-item");

        twoCol.add(jenisKelaminGroup, tanggalLahirGroup);
        content.add(twoCol);

        // ── Field: Alamat ──
        TextArea alamatArea = new TextArea();
        alamatArea.setValue("-");
        alamatArea.setWidthFull();
        alamatArea.addClassName("ep-input");
        content.add(buildFieldGroup("Alamat", alamatArea, null));

        // ── Two-column row: Nomor Rumah + RT/RW ──
        Div twoCol2 = new Div();
        twoCol2.addClassName("ep-two-col");

        TextField nomorRumahField = new TextField();
        nomorRumahField.setValue("-");
        nomorRumahField.setWidthFull();
        nomorRumahField.addClassName("ep-input");
        Div nomorRumahGroup = buildFieldGroup("Nomor Rumah", nomorRumahField, null);
        nomorRumahGroup.addClassName("ep-two-col-item");

        TextField rtRwField = new TextField();
        rtRwField.setValue("-");
        rtRwField.setWidthFull();
        rtRwField.addClassName("ep-input");
        Div rtRwGroup = buildFieldGroup("RT / RW", rtRwField, null);
        rtRwGroup.addClassName("ep-two-col-item");

        twoCol2.add(nomorRumahGroup, rtRwGroup);
        content.add(twoCol2);

        // ── Field: Kecamatan ──
        TextField kecamatanField = new TextField();
        kecamatanField.setValue("-");
        kecamatanField.setWidthFull();
        kecamatanField.addClassName("ep-input");
        content.add(buildFieldGroup("Kecamatan", kecamatanField, null));

        dialog.add(content);

        // ── Footer ──
        Div footer = new Div();
        footer.addClassName("ep-footer");

        NativeButton cancelBtn = new NativeButton("Batal");
        cancelBtn.addClassName("ep-cancel-btn");
        cancelBtn.addClickListener(e -> hideModal());

        NativeButton saveBtn = new NativeButton("Simpan Perubahan");
        saveBtn.addClassName("ep-save-btn");
        saveBtn.addClickListener(e -> {
            // TODO: Implement actual save logic with PenggunaService
            Notification notif = new Notification("Profil berhasil diperbarui!", 3000, Notification.Position.BOTTOM_CENTER);
            notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notif.open();
            hideModal();
        });

        footer.add(cancelBtn, saveBtn);
        dialog.add(footer);

        overlay.add(dialog);

        // Close on backdrop click
        overlay.addClickListener(e -> {
            // Only close if click is directly on the overlay (backdrop), not the dialog
            hideModal();
        });
        dialog.addClickListener(e -> e.getSource()); // consume click on dialog to prevent bubbling via JS

        return overlay;
    }

    /** Builds a labelled field group with optional helper text. */
    private Div buildFieldGroup(String label, com.vaadin.flow.component.Component input, String helperText) {
        Div group = new Div();
        group.addClassName("ep-field-group");

        Span lbl = new Span(label);
        lbl.addClassName("ep-field-label");
        group.add(lbl);
        group.add(input);

        if (helperText != null) {
            Span helper = new Span(helperText);
            helper.addClassName("ep-field-helper");
            group.add(helper);
        }

        return group;
    }

    /** Builds a disabled/read-only styled text input. */
    private Div buildReadOnlyField(String value) {
        Div field = new Div();
        field.addClassName("ep-readonly-field");
        Span txt = new Span(value);
        txt.addClassName("ep-readonly-text");
        field.add(txt);
        return field;
    }

    private Hr buildModalDivider() {
        Hr hr = new Hr();
        hr.addClassName("ep-modal-divider");
        return hr;
    }

    private void showModal() {
        modalOverlay.addClassName("ep-overlay-visible");
    }

    private void hideModal() {
        modalOverlay.removeClassName("ep-overlay-visible");
    }
}

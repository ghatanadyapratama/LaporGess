package com.example.application.views.warga;

import com.example.application.model.Pengguna;
import com.example.application.repository.PenggunaRepository;
import com.example.application.service.SessionManager;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

@Route(value = "profil", layout = BlankLayout.class)
@PageTitle("Profil Anda - Lapor Gess")
public class ProfilView extends Div {

    private final PenggunaRepository penggunaRepository;
    private Pengguna currentUser;
    private Div modalOverlay;

    public ProfilView(PenggunaRepository penggunaRepository) {
        this.penggunaRepository = penggunaRepository;
        
        String username = SessionManager.getUsername();
        if (username != null) {
            currentUser = penggunaRepository.findByUsername(username).orElse(null);
        }

        addClassName("d-root");
        add(buildSidebar(), buildMain());

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
        int poin = currentUser != null && currentUser.getPoin() != null ? currentUser.getPoin() : 0;
        Span poinTxt = new Span(String.format("%,d Poin", poin));
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
        av.add(getAvatarComponent());

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

        Div avatarWrapper = new Div();
        avatarWrapper.addClassName("pf-avatar-wrapper");
        Div bigAvatar = new Div();
        bigAvatar.addClassName("pf-big-avatar");
        bigAvatar.add(getAvatarComponent());
        avatarWrapper.add(bigAvatar);

        Div nameBlock = new Div();
        nameBlock.addClassName("pf-name-block");
        String name = currentUser != null ? currentUser.getNamaLengkap() : "Pengguna";
        H2 nameTitle = new H2(name);
        nameTitle.addClassName("pf-name");
        
        Div locRow = new Div();
        locRow.addClassName("pf-loc-row");
        Div locIcon = new Div();
        locIcon.getElement().setProperty("innerHTML",
            "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"14\" height=\"14\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"#888\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\">" +
            "<path d=\"M20 10c0 6-8 12-8 12s-8-6-8-12a8 8 0 0 1 16 0z\"/>" +
            "<circle cx=\"12\" cy=\"10\" r=\"3\"/></svg>"
        );
        String rtRw = (currentUser != null && currentUser.getRtRw() != null) ? currentUser.getRtRw() : "-";
        Span locTxt = new Span("Warga " + rtRw);
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
                SessionManager.logout();
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
        int laporanCnt = currentUser != null && currentUser.getTotalLaporan() != null ? currentUser.getTotalLaporan() : 0;
        Span laporanVal = new Span(String.valueOf(laporanCnt));
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
        int poinCnt = currentUser != null && currentUser.getPoin() != null ? currentUser.getPoin() : 0;
        Span poinVal = new Span(String.format("%,d", poinCnt));
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
        Div overlay = new Div();
        overlay.addClassName("ep-overlay");
        overlay.getElement().setAttribute("id", "ep-overlay");

        Div dialog = new Div();
        dialog.addClassName("ep-dialog");

        Div header = new Div();
        header.addClassName("ep-header");
        Span title = new Span("Edit Profil Anda");
        title.addClassName("ep-title");
        NativeButton closeBtn = new NativeButton("×");
        closeBtn.addClassName("ep-close-btn");
        closeBtn.addClickListener(e -> hideModal());
        header.add(title, closeBtn);
        dialog.add(header);

        Div content = new Div();
        content.addClassName("ep-content");

        // Track the photo profile url array to capture it in lambdas
        final String[] tempFotoProfilUrl = new String[] { currentUser != null ? currentUser.getFotoProfil() : null };

        Div photoSection = new Div();
        photoSection.addClassName("ep-photo-section");
        Div photoAvatar = new Div();
        photoAvatar.addClassName("ep-photo-avatar");
        if (currentUser != null && currentUser.getFotoProfil() != null && !currentUser.getFotoProfil().isEmpty()) {
            Image img = new Image(currentUser.getFotoProfil(), "foto");
            img.getStyle().set("width", "100%").set("height", "100%").set("border-radius", "50%").set("object-fit", "cover");
            photoAvatar.add(img);
        } else {
            photoAvatar.add(new Span(getInitials()));
        }

        Div photoInfo = new Div();
        photoInfo.addClassName("ep-photo-info");
        Span photoLabel = new Span("Foto Profil");
        photoLabel.addClassName("ep-photo-label");
        Span photoHint = new Span("Disarankan ukuran 1:1, maksimal 2MB (JPG/PNG).");
        photoHint.addClassName("ep-photo-hint");
        photoInfo.add(photoLabel, photoHint);

        Upload photoUpload = new Upload(event -> {
            try {
                String originalName = event.getFileName();
                String ext = originalName.contains(".") ? originalName.substring(originalName.lastIndexOf('.')) : ".jpg";
                String uniqueName = "avatar_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12) + ext;

                // Coba simpan ke folder static resources agar bisa diakses via URL
                // Pertama coba path development (src/main/resources)
                String projectDir = System.getProperty("user.dir");
                File dir = new File(projectDir, "src/main/resources/META-INF/resources/uploads/");
                if (!dir.exists()) {
                    // Kalau tidak ada (misal running dari JAR), simpan ke folder "uploads" di working dir
                    dir = new File(projectDir, "uploads");
                }
                if (!dir.exists()) dir.mkdirs();

                try (InputStream is = event.getInputStream();
                     FileOutputStream fos = new FileOutputStream(new File(dir, uniqueName))) {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = is.read(buf)) > 0) fos.write(buf, 0, len);
                }
                String newFotoUrl = "uploads/" + uniqueName;
                tempFotoProfilUrl[0] = newFotoUrl;

                UI.getCurrent().access(() -> {
                    photoAvatar.removeAll();
                    Image tempImg = new Image(newFotoUrl, "preview");
                    tempImg.getStyle().set("width", "100%").set("height", "100%").set("border-radius", "50%").set("object-fit", "cover");
                    photoAvatar.add(tempImg);
                    Notification.show("Foto profil berhasil dipilih!");
                });
            } catch (Exception ex) {
                Notification.show("Gagal mengunggah foto: " + ex.getMessage());
            }
        });
        photoUpload.setAcceptedFileTypes("image/jpeg", "image/png");
        photoUpload.setMaxFileSize(2 * 1024 * 1024); // 2 MB
        photoUpload.setMaxFiles(1);

        Button selectBtn = new Button("Unggah Foto");
        selectBtn.getStyle().set("background", "#F1F5F9").set("color", "#334155")
            .set("border-radius", "8px").set("font-size", "0.85rem").set("font-weight", "600").set("cursor", "pointer").set("border", "none").set("padding", "6px 12px");
        photoUpload.setUploadButton(selectBtn);
        photoUpload.setDropLabel(new Span(""));
        photoUpload.getStyle().set("margin-top", "8px");

        photoInfo.add(photoUpload);
        photoSection.add(photoAvatar, photoInfo);
        content.add(photoSection);

        content.add(buildModalDivider());

        String name = currentUser != null ? currentUser.getNamaLengkap() : "";
        content.add(buildFieldGroup(
            "Nama Lengkap",
            buildReadOnlyField(name),
            "⚠ Nama lengkap tidak dapat diubah demi keamanan."
        ));

        TextField usernameField = new TextField();
        usernameField.setValue(currentUser != null ? currentUser.getUsername() : "");
        usernameField.setWidthFull();
        usernameField.addClassName("ep-input");
        content.add(buildFieldGroup("Nama Pengguna (Username)", usernameField, null));

        TextField emailField = new TextField();
        emailField.setValue(currentUser != null && currentUser.getEmail() != null ? currentUser.getEmail() : "");
        emailField.setWidthFull();
        emailField.addClassName("ep-input");
        content.add(buildFieldGroup("Email", emailField, null));

        TextField teleponField = new TextField();
        teleponField.setValue(currentUser != null && currentUser.getTelepon() != null ? currentUser.getTelepon() : "");
        teleponField.setWidthFull();
        teleponField.addClassName("ep-input");
        content.add(buildFieldGroup("Nomor Telepon", teleponField, null));

        Div twoCol = new Div();
        twoCol.addClassName("ep-two-col");

        Select<Pengguna.JenisKelamin> jenisKelaminSelect = new Select<>();
        jenisKelaminSelect.setItems(Pengguna.JenisKelamin.values());
        if (currentUser != null && currentUser.getJenisKelamin() != null) jenisKelaminSelect.setValue(currentUser.getJenisKelamin());
        jenisKelaminSelect.setWidthFull();
        jenisKelaminSelect.addClassName("ep-input");
        Div jenisKelaminGroup = buildFieldGroup("Jenis Kelamin", jenisKelaminSelect, null);
        jenisKelaminGroup.addClassName("ep-two-col-item");

        DatePicker tanggalLahirPicker = new DatePicker();
        if (currentUser != null && currentUser.getTanggalLahir() != null) tanggalLahirPicker.setValue(currentUser.getTanggalLahir());
        tanggalLahirPicker.setWidthFull();
        tanggalLahirPicker.addClassName("ep-input");
        Div tanggalLahirGroup = buildFieldGroup("Tanggal Lahir", tanggalLahirPicker, null);
        tanggalLahirGroup.addClassName("ep-two-col-item");

        twoCol.add(jenisKelaminGroup, tanggalLahirGroup);
        content.add(twoCol);

        TextArea alamatArea = new TextArea();
        alamatArea.setValue(currentUser != null && currentUser.getAlamat() != null ? currentUser.getAlamat() : "");
        alamatArea.setWidthFull();
        alamatArea.addClassName("ep-input");
        content.add(buildFieldGroup("Alamat", alamatArea, null));

        Div twoCol2 = new Div();
        twoCol2.addClassName("ep-two-col");

        TextField nomorRumahField = new TextField();
        nomorRumahField.setValue(currentUser != null && currentUser.getNomorRumah() != null ? currentUser.getNomorRumah() : "");
        nomorRumahField.setWidthFull();
        nomorRumahField.addClassName("ep-input");
        Div nomorRumahGroup = buildFieldGroup("Nomor Rumah", nomorRumahField, null);
        nomorRumahGroup.addClassName("ep-two-col-item");

        TextField rtRwField = new TextField();
        rtRwField.setValue(currentUser != null && currentUser.getRtRw() != null ? currentUser.getRtRw() : "");
        rtRwField.setWidthFull();
        rtRwField.addClassName("ep-input");
        Div rtRwGroup = buildFieldGroup("RT / RW", rtRwField, null);
        rtRwGroup.addClassName("ep-two-col-item");

        twoCol2.add(nomorRumahGroup, rtRwGroup);
        content.add(twoCol2);

        TextField kecamatanField = new TextField();
        kecamatanField.setValue(currentUser != null && currentUser.getKecamatan() != null ? currentUser.getKecamatan() : "");
        kecamatanField.setWidthFull();
        kecamatanField.addClassName("ep-input");
        content.add(buildFieldGroup("Kecamatan", kecamatanField, null));

        dialog.add(content);

        Div footer = new Div();
        footer.addClassName("ep-footer");

        NativeButton cancelBtn = new NativeButton("Batal");
        cancelBtn.addClassName("ep-cancel-btn");
        cancelBtn.addClickListener(e -> hideModal());

        NativeButton saveBtn = new NativeButton("Simpan Perubahan");
        saveBtn.addClassName("ep-save-btn");
        saveBtn.addClickListener(e -> {
            if (currentUser != null) {
                String newUsername = usernameField.getValue().trim();
                
                if (newUsername.isEmpty()) {
                    Notification.show("Username tidak boleh kosong!", 3000, Notification.Position.BOTTOM_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }

                // Check if username changed and is already taken
                if (!newUsername.equalsIgnoreCase(currentUser.getUsername())) {
                    if (penggunaRepository.findByUsername(newUsername).isPresent()) {
                        Notification.show("Username sudah digunakan oleh orang lain!", 3000, Notification.Position.BOTTOM_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                        return;
                    }
                }

                currentUser.setUsername(newUsername);
                currentUser.setEmail(emailField.getValue());
                currentUser.setTelepon(teleponField.getValue());
                currentUser.setJenisKelamin(jenisKelaminSelect.getValue());
                currentUser.setTanggalLahir(tanggalLahirPicker.getValue());
                currentUser.setAlamat(alamatArea.getValue());
                currentUser.setNomorRumah(nomorRumahField.getValue());
                currentUser.setRtRw(rtRwField.getValue());
                currentUser.setKecamatan(kecamatanField.getValue());
                
                if (tempFotoProfilUrl[0] != null) {
                    currentUser.setFotoProfil(tempFotoProfilUrl[0]);
                }
                
                try {
                    penggunaRepository.save(currentUser);
                    SessionManager.updateProfilData(newUsername, currentUser.getNamaLengkap());
                    
                    Notification notif = new Notification("Profil berhasil diperbarui!", 3000, Notification.Position.BOTTOM_CENTER);
                    notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notif.open();
                    
                    getUI().ifPresent(ui -> ui.getPage().reload());
                    hideModal();
                } catch (Exception ex) {
                    Notification notif = new Notification("Gagal menyimpan profil: " + ex.getMessage(), 5000, Notification.Position.BOTTOM_CENTER);
                    notif.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notif.open();
                }
            }
        });

        footer.add(cancelBtn, saveBtn);
        dialog.add(footer);

        overlay.add(dialog);

        return overlay;
    }

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

    private com.vaadin.flow.component.Component getAvatarComponent() {
        if (currentUser != null && currentUser.getFotoProfil() != null && !currentUser.getFotoProfil().isEmpty()) {
            Image img = new Image(currentUser.getFotoProfil(), "foto");
            img.getStyle().set("width", "100%").set("height", "100%").set("border-radius", "50%").set("object-fit", "cover");
            return img;
        } else {
            return new Span(getInitials());
        }
    }
    
    private String getInitials() {
        if (currentUser == null || currentUser.getNamaLengkap() == null || currentUser.getNamaLengkap().isEmpty()) return "U";
        String[] parts = currentUser.getNamaLengkap().trim().split(" ");
        if (parts.length > 1) {
            return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
        }
        return currentUser.getNamaLengkap().substring(0, 1).toUpperCase();
    }
}

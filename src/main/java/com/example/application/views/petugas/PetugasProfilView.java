package com.example.application.views.petugas;

import com.example.application.model.Pengguna;
import com.example.application.repository.PenggunaRepository;
import com.example.application.service.LaporanService;
import com.example.application.service.SessionManager;
import com.example.application.views.warga.BlankLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

@Route(value = "petugas/profil", layout = BlankLayout.class)
@PageTitle("Profil Saya - Petugas LaporGess")
public class PetugasProfilView extends Div implements BeforeEnterObserver {

    private final PenggunaRepository penggunaRepository;
    private final LaporanService laporanService;
    private Pengguna currentUser;
    private Div modalOverlay;

    public PetugasProfilView(PenggunaRepository penggunaRepository, LaporanService laporanService) {
        this.penggunaRepository = penggunaRepository;
        this.laporanService = laporanService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!SessionManager.isLoggedIn() || !SessionManager.isPetugas()) {
            event.rerouteTo("login");
            return;
        }
        String username = SessionManager.getUsername();
        if (username != null) {
            currentUser = penggunaRepository.findByUsername(username).orElse(null);
        }
        buildUI();
    }

    private void buildUI() {
        removeAll();
        addClassName("pt-root");

        Div sidebar = PetugasLayout.buildSidebar("petugas/profil");
        Div main = new Div();
        main.addClassName("pt-main");
        Div topbar = PetugasLayout.buildTopbar("Profil Anda");
        Div body = new Div();
        body.addClassName("pt-body");

        // --- Profile Header Card ---
        Div headerCard = new Div();
        headerCard.addClassName("pt-profile-header-card");

        Div banner = new Div();
        banner.addClassName("pt-profile-banner");
        headerCard.add(banner);

        Div infoRow = new Div();
        infoRow.addClassName("pt-profile-info-row");
        infoRow.getStyle()
            .set("display", "flex").set("align-items", "center")
            .set("justify-content", "space-between").set("gap", "16px");

        // Grup kiri: avatar + teks nama & lokasi berdampingan
        Div leftGroup = new Div();
        leftGroup.getStyle()
            .set("display", "flex").set("align-items", "center").set("gap", "16px");

        // Avatar
        Div avatarWrapper = new Div();
        avatarWrapper.getStyle()
            .set("width", "64px").set("height", "64px")
            .set("border-radius", "50%").set("overflow", "hidden")
            .set("background", "#E2E8F0").set("flex-shrink", "0")
            .set("display", "flex").set("align-items", "center")
            .set("justify-content", "center").set("font-weight", "700")
            .set("font-size", "1.4rem").set("color", "#475569")
            .set("border", "3px solid white").set("box-shadow", "0 2px 8px rgba(0,0,0,0.15)");
        if (currentUser != null && currentUser.getFotoProfil() != null && !currentUser.getFotoProfil().isEmpty()) {
            Image img = new Image(currentUser.getFotoProfil(), "foto");
            img.getStyle().set("width", "100%").set("height", "100%").set("object-fit", "cover");
            avatarWrapper.add(img);
        } else {
            avatarWrapper.add(new Span(getInitials()));
        }

        // Nama & Lokasi langsung di samping avatar
        Div textGroup = new Div();
        textGroup.getStyle().set("display", "flex").set("flex-direction", "column").set("gap", "4px");

        String name = currentUser != null ? currentUser.getNamaLengkap() : "Petugas";
        H2 nameText = new H2(name);
        nameText.addClassName("pt-profile-display-name");
        nameText.getStyle().set("margin", "0");

        Div locRow = new Div();
        locRow.addClassName("pt-profile-display-loc");
        Span locIcon = new Span("📍");
        String area = (currentUser != null && currentUser.getRtRw() != null) ? currentUser.getRtRw() : "Petugas Lapangan";
        Span locText = new Span(area);
        locRow.add(locIcon, locText);

        textGroup.add(nameText, locRow);
        leftGroup.add(avatarWrapper, textGroup);

        Button btnEdit = new Button("Edit Profil");
        btnEdit.addClassName("pt-profile-edit-btn");
        btnEdit.addClickListener(e -> showModal());

        infoRow.add(leftGroup, btnEdit);
        headerCard.add(infoRow);
        body.add(headerCard);

        // --- Grid Layout (Pengaturan & Statistik) ---
        Div grid = new Div();
        grid.getStyle()
            .set("display", "grid")
            .set("grid-template-columns", "1fr 1.5fr")
            .set("gap", "24px")
            .set("width", "100%");

        // Column Left: Pengaturan
        Div settingsCard = new Div();
        settingsCard.addClassName("pt-settings-card");

        H3 settingsTitle = new H3("Pengaturan");
        settingsTitle.addClassName("pt-settings-title");
        settingsCard.add(settingsTitle);

        Div rowAccount = new Div();
        rowAccount.addClassName("pt-settings-row");
        Div accountLeft = new Div();
        accountLeft.addClassName("pt-settings-left");
        Span iconAccount = new Span("⚙️");
        iconAccount.addClassName("pt-settings-icon");
        Span labelAccount = new Span("Akun & Sandi");
        accountLeft.add(iconAccount, labelAccount);
        Span arrowAccount = new Span(">");
        rowAccount.add(accountLeft, arrowAccount);
        rowAccount.addClickListener(e -> UI.getCurrent().navigate("petugas/akun-sandi"));

        Div rowNotif = new Div();
        rowNotif.addClassName("pt-settings-row");
        Div notifLeft = new Div();
        notifLeft.addClassName("pt-settings-left");
        Span iconNotif = new Span("🔔");
        iconNotif.addClassName("pt-settings-icon");
        Span labelNotif = new Span("Notifikasi");
        notifLeft.add(iconNotif, labelNotif);
        Span arrowNotif = new Span(">");
        rowNotif.add(notifLeft, arrowNotif);
        rowNotif.addClickListener(e -> UI.getCurrent().navigate("petugas/notifikasi"));

        // Logout row
        Div rowLogout = new Div();
        rowLogout.addClassName("pt-settings-row");
        rowLogout.getStyle().set("color", "#EF4444");
        Span iconLogout = new Span("🚪");
        Span labelLogout = new Span("Keluar");
        rowLogout.add(iconLogout, labelLogout);
        rowLogout.addClickListener(e -> {
            SessionManager.logout();
            UI.getCurrent().navigate("login");
        });

        settingsCard.add(rowAccount, rowNotif, rowLogout);
        grid.add(settingsCard);

        // Column Right: Statistik Kinerja
        Div statsCard = new Div();
        statsCard.addClassName("pt-stats-card");

        H3 statsTitle = new H3("Statistik Kinerja");
        statsTitle.addClassName("pt-stats-title");
        statsCard.add(statsTitle);

        Div statsRow = new Div();
        statsRow.addClassName("pt-stats-row");

        int totalSelesai = currentUser != null ? laporanService.getLaporanSelesaiByPetugas(currentUser.getUsername()).size() : 0;
        int totalLaporan = currentUser != null ? laporanService.getLaporanByPetugas(currentUser.getUsername()).size() : 0;

        Div boxTeal = new Div();
        boxTeal.addClassName("pt-stat-box");
        boxTeal.addClassName("pt-stat-box-teal");
        Div iconWrapperTeal = new Div();
        iconWrapperTeal.addClassName("pt-stat-box-icon-wrapper");
        Image checkImg = new Image("icons/ceklist.png", "Completed");
        checkImg.getStyle().set("width", "18px").set("height", "18px");
        iconWrapperTeal.add(checkImg);
        Div infoTeal = new Div();
        infoTeal.addClassName("pt-stat-box-info");
        Span labelTeal = new Span("Tugas Selesai");
        labelTeal.addClassName("pt-stat-box-label");
        Span valTeal = new Span(String.valueOf(totalSelesai));
        valTeal.addClassName("pt-stat-box-value");
        infoTeal.add(labelTeal, valTeal);
        boxTeal.add(iconWrapperTeal, infoTeal);

        Div boxOrange = new Div();
        boxOrange.addClassName("pt-stat-box");
        boxOrange.addClassName("pt-stat-box-orange");
        Div iconWrapperOrange = new Div();
        iconWrapperOrange.addClassName("pt-stat-box-icon-wrapper");
        Image clockImg = new Image("icons/jam.png", "Active");
        clockImg.getStyle().set("width", "18px").set("height", "18px");
        iconWrapperOrange.add(clockImg);
        Div infoOrange = new Div();
        infoOrange.addClassName("pt-stat-box-info");
        Span labelOrange = new Span("Total Ditangani");
        labelOrange.addClassName("pt-stat-box-label");
        Span valOrange = new Span(String.valueOf(totalLaporan));
        valOrange.addClassName("pt-stat-box-value");
        infoOrange.add(labelOrange, valOrange);
        boxOrange.add(iconWrapperOrange, infoOrange);

        statsRow.add(boxTeal, boxOrange);
        statsCard.add(statsRow);
        grid.add(statsCard);

        body.add(grid);
        main.add(topbar, body);

        // Build and add modal
        modalOverlay = buildEditModal();
        add(sidebar, main, modalOverlay);
    }

    // ══════════════════════════════════════════
    //  EDIT PROFIL MODAL
    // ══════════════════════════════════════════
    private Div buildEditModal() {
        Div overlay = new Div();
        overlay.getStyle()
            .set("display", "none")
            .set("position", "fixed").set("inset", "0")
            .set("background", "rgba(0,0,0,0.5)")
            .set("z-index", "1000")
            .set("align-items", "center").set("justify-content", "center");
        overlay.getElement().setAttribute("id", "pt-edit-overlay");

        Div dialog = new Div();
        dialog.getStyle()
            .set("background", "white").set("border-radius", "16px")
            .set("padding", "28px").set("width", "480px").set("max-width", "95vw")
            .set("max-height", "90vh").set("overflow-y", "auto")
            .set("box-shadow", "0 20px 60px rgba(0,0,0,0.3)");

        // Header
        Div header = new Div();
        header.getStyle().set("display", "flex").set("justify-content", "space-between")
            .set("align-items", "center").set("margin-bottom", "20px");
        Span title = new Span("Edit Profil Anda");
        title.getStyle().set("font-size", "1.1rem").set("font-weight", "700").set("color", "#1E293B");
        NativeButton closeBtn = new NativeButton("×");
        closeBtn.getStyle().set("background", "none").set("border", "none").set("font-size", "1.5rem")
            .set("cursor", "pointer").set("color", "#94A3B8").set("line-height", "1");
        closeBtn.addClickListener(e -> hideModal());
        header.add(title, closeBtn);
        dialog.add(header);

        // Photo section
        final String[] tempFotoUrl = { currentUser != null ? currentUser.getFotoProfil() : null };

        Div photoSection = new Div();
        photoSection.getStyle().set("display", "flex").set("align-items", "center")
            .set("gap", "16px").set("margin-bottom", "20px").set("padding-bottom", "20px")
            .set("border-bottom", "1px solid #F1F5F9");

        Div photoAvatar = new Div();
        photoAvatar.getStyle()
            .set("width", "72px").set("height", "72px").set("border-radius", "50%")
            .set("overflow", "hidden").set("background", "#E2E8F0").set("flex-shrink", "0")
            .set("display", "flex").set("align-items", "center").set("justify-content", "center")
            .set("font-weight", "700").set("font-size", "1.4rem").set("color", "#475569");
        if (currentUser != null && currentUser.getFotoProfil() != null && !currentUser.getFotoProfil().isEmpty()) {
            Image img = new Image(currentUser.getFotoProfil(), "foto");
            img.getStyle().set("width", "100%").set("height", "100%").set("object-fit", "cover");
            photoAvatar.add(img);
        } else {
            photoAvatar.add(new Span(getInitials()));
        }

        Div photoInfo = new Div();
        Span photoLabel = new Span("Foto Profil");
        photoLabel.getStyle().set("font-weight", "600").set("color", "#334155")
            .set("display", "block").set("margin-bottom", "4px");
        Span photoHint = new Span("JPG/PNG, maks 2MB");
        photoHint.getStyle().set("font-size", "0.78rem").set("color", "#94A3B8")
            .set("display", "block").set("margin-bottom", "8px");

        Upload photoUpload = new Upload(event -> {
            try {
                String originalName = event.getFileName();
                String ext = originalName.contains(".") ? originalName.substring(originalName.lastIndexOf('.')) : ".jpg";
                String uniqueName = "avatar_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12) + ext;
                String projectDir = System.getProperty("user.dir");
                File dir = new File(projectDir, "src/main/resources/META-INF/resources/uploads/");
                if (!dir.exists()) dir = new File(projectDir, "uploads");
                if (!dir.exists()) dir.mkdirs();
                try (InputStream is = event.getInputStream();
                     FileOutputStream fos = new FileOutputStream(new File(dir, uniqueName))) {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = is.read(buf)) > 0) fos.write(buf, 0, len);
                }
                tempFotoUrl[0] = "uploads/" + uniqueName;
                UI.getCurrent().access(() -> {
                    photoAvatar.removeAll();
                    Image prev = new Image(tempFotoUrl[0], "preview");
                    prev.getStyle().set("width", "100%").set("height", "100%").set("object-fit", "cover");
                    photoAvatar.add(prev);
                    Notification.show("Foto berhasil dipilih!", 2000, Notification.Position.BOTTOM_CENTER);
                });
            } catch (Exception ex) {
                Notification.show("Gagal upload: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        photoUpload.setAcceptedFileTypes("image/jpeg", "image/png");
        photoUpload.setMaxFileSize(2 * 1024 * 1024);
        photoUpload.setMaxFiles(1);
        Button uploadBtn = new Button("📷 Unggah Foto");
        uploadBtn.getStyle().set("background", "#F1F5F9").set("color", "#334155")
            .set("border", "none").set("border-radius", "8px").set("font-size", "0.82rem")
            .set("font-weight", "600").set("cursor", "pointer").set("padding", "6px 12px");
        photoUpload.setUploadButton(uploadBtn);
        photoUpload.setDropLabel(new Span(""));

        photoInfo.add(photoLabel, photoHint, photoUpload);
        photoSection.add(photoAvatar, photoInfo);
        dialog.add(photoSection);

        // Fields
        TextField usernameField = buildField("Nama Pengguna (Username)",
            currentUser != null ? currentUser.getUsername() : "");
        TextField emailField = buildField("Email",
            currentUser != null && currentUser.getEmail() != null ? currentUser.getEmail() : "");
        TextField teleponField = buildField("Nomor Telepon",
            currentUser != null && currentUser.getTelepon() != null ? currentUser.getTelepon() : "");

        dialog.add(wrapField("Nama Pengguna (Username)", usernameField));
        dialog.add(wrapField("Email", emailField));
        dialog.add(wrapField("Nomor Telepon", teleponField));

        // Footer
        Div footer = new Div();
        footer.getStyle().set("display", "flex").set("justify-content", "flex-end")
            .set("gap", "12px").set("margin-top", "24px").set("padding-top", "16px")
            .set("border-top", "1px solid #F1F5F9");

        NativeButton cancelBtn = new NativeButton("Batal");
        cancelBtn.getStyle().set("background", "#F1F5F9").set("color", "#475569")
            .set("border", "none").set("border-radius", "8px").set("padding", "9px 20px")
            .set("font-weight", "600").set("cursor", "pointer").set("font-size", "0.9rem");
        cancelBtn.addClickListener(e -> hideModal());

        NativeButton saveBtn = new NativeButton("Simpan Perubahan");
        saveBtn.getStyle().set("background", "#F97316").set("color", "white")
            .set("border", "none").set("border-radius", "8px").set("padding", "9px 20px")
            .set("font-weight", "700").set("cursor", "pointer").set("font-size", "0.9rem");
        saveBtn.addClickListener(e -> {
            if (currentUser == null) return;
            String newUsername = usernameField.getValue().trim();
            if (newUsername.isEmpty()) {
                Notification.show("Username tidak boleh kosong!", 3000, Notification.Position.BOTTOM_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            if (!newUsername.equalsIgnoreCase(currentUser.getUsername())) {
                if (penggunaRepository.findByUsername(newUsername).isPresent()) {
                    Notification.show("Username sudah digunakan!", 3000, Notification.Position.BOTTOM_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }
            }
            currentUser.setUsername(newUsername);
            currentUser.setEmail(emailField.getValue());
            currentUser.setTelepon(teleponField.getValue());
            if (tempFotoUrl[0] != null) {
                currentUser.setFotoProfil(tempFotoUrl[0]);
            }
            penggunaRepository.save(currentUser);
            SessionManager.setUsername(newUsername);
            Notification notif = new Notification("Profil berhasil diperbarui!", 3000, Notification.Position.BOTTOM_CENTER);
            notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notif.open();
            getUI().ifPresent(ui -> ui.getPage().reload());
        });

        footer.add(cancelBtn, saveBtn);
        dialog.add(footer);
        overlay.add(dialog);
        return overlay;
    }

    private TextField buildField(String label, String value) {
        TextField field = new TextField();
        field.setValue(value);
        field.setWidthFull();
        field.getStyle().set("border-radius", "8px");
        return field;
    }

    private Div wrapField(String label, TextField field) {
        Div group = new Div();
        group.getStyle().set("margin-bottom", "16px");
        Span lbl = new Span(label);
        lbl.getStyle().set("font-size", "0.85rem").set("font-weight", "600")
            .set("color", "#475569").set("display", "block").set("margin-bottom", "6px");
        group.add(lbl, field);
        return group;
    }

    private void showModal() {
        modalOverlay.getStyle().set("display", "flex");
    }

    private void hideModal() {
        modalOverlay.getStyle().set("display", "none");
    }

    private String getInitials() {
        if (currentUser == null || currentUser.getNamaLengkap() == null || currentUser.getNamaLengkap().isEmpty()) return "P";
        String[] parts = currentUser.getNamaLengkap().trim().split(" ");
        if (parts.length > 1) {
            return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
        }
        return currentUser.getNamaLengkap().substring(0, 1).toUpperCase();
    }
}

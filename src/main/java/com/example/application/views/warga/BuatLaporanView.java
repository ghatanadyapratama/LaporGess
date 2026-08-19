package com.example.application.views.warga;

import com.example.application.service.LaporanService;
import com.example.application.service.SessionManager;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Route(value = "buat-laporan", layout = BlankLayout.class)
@PageTitle("Buat Laporan - Lapor Gess")
public class BuatLaporanView extends Div implements BeforeEnterObserver {

    private final LaporanService laporanService;
    private String uploadedFileUrl = null;

    public BuatLaporanView(LaporanService laporanService) {
        this.laporanService = laporanService;
        addClassName("d-root");
        add(buildSidebar(), buildMain());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!SessionManager.isLoggedIn() || !SessionManager.isWarga()) {
            event.rerouteTo("login");
        }
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

        Div profilItem = navItem("icons/profile.png", "Profil", false);
        profilItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("profil")));
        nav.add(profilItem);

        sidebar.add(nav);

        Div sp = new Div();
        sp.addClassName("d-sidebar-spacer");
        sidebar.add(sp);

        Div cta = new Div();
        cta.addClassName("d-cta");
        cta.add(new Span("+ Buat Laporan"));
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

        Span title = new Span("Laporan Baru");
        title.addClassName("d-topbar-title");
        bar.add(title);

        Div right = new Div();
        right.addClassName("d-topbar-right");

        Div badge = new Div();
        badge.addClassName("d-poin-badge");
        Image trophy = new Image("icons/pialaOren.png", "poin");
        trophy.addClassName("d-poin-icon");
        Span poinTxt = new Span(SessionManager.getPoin() + " Poin");
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
        String nama = SessionManager.getNama();
        av.add(new Span(nama != null && !nama.isEmpty() ? String.valueOf(nama.charAt(0)).toUpperCase() : "U"));

        right.add(badge, bell, av);
        bar.add(right);
        return bar;
    }

    @SuppressWarnings("deprecation")
    private Div buildBody() {
        Div body = new Div();
        body.addClassName("d-body");

        Div formCard = new Div();
        formCard.addClassName("d-form-card");

        H2 formTitle = new H2("Detail Laporan");
        formTitle.addClassName("d-form-title");
        formCard.add(formTitle);

        // Row 1: Kategori + Lokasi
        Div row1 = new Div();
        row1.addClassName("d-form-row");

        Div catCol = new Div();
        catCol.addClassName("d-form-col");
        Span catLabel = new Span("Kategori Masalah");
        catLabel.addClassName("d-input-label");
        Select<String> categorySelect = new Select<>();
        categorySelect.setItems("Sampah Liar", "Lampu Jalan", "Pohon Tumbang", "Jalan Rusak", "Fasilitas Umum", "Keamanan", "Lainnya");
        categorySelect.setPlaceholder("Pilih kategori");
        categorySelect.setWidthFull();
        categorySelect.addClassName("d-form-select");
        catCol.add(catLabel, categorySelect);

        Div locCol = new Div();
        locCol.addClassName("d-form-col");
        Span locLabel = new Span("Lokasi Spesifik");
        locLabel.addClassName("d-input-label");

        Div locInputWrapper = new Div();
        locInputWrapper.addClassName("d-loc-wrapper");
        TextField locField = new TextField();
        locField.setPlaceholder("Cth. Di depan Rumah no. 12");
        locField.setWidthFull();
        locField.addClassName("d-form-input");

        Button mapBtn = new Button();
        mapBtn.addClassName("d-map-btn");
        Image mapImg = new Image("icons/mapsIcon.png", "maps");
        mapImg.addClassName("d-map-icon");
        mapBtn.setIcon(mapImg);

        locInputWrapper.add(locField, mapBtn);
        locCol.add(locLabel, locInputWrapper);

        row1.add(catCol, locCol);
        formCard.add(row1);

        // Row 2: Judul
        Div judulCol = new Div();
        judulCol.addClassName("d-form-col-full");
        Span judulLabel = new Span("Judul Laporan");
        judulLabel.addClassName("d-input-label");
        TextField judulField = new TextField();
        judulField.setPlaceholder("Ringkasan singkat masalah");
        judulField.setWidthFull();
        judulField.addClassName("d-form-input");
        judulCol.add(judulLabel, judulField);
        formCard.add(judulCol);

        // Row 3: Description
        Div descCol = new Div();
        descCol.addClassName("d-form-col-full");
        Span descLabel = new Span("Deskripsi Lengkap");
        descLabel.addClassName("d-input-label");
        TextArea descArea = new TextArea();
        descArea.setPlaceholder("Jelaskan masalahnya secara detail untuk memudahkan petugas...");
        descArea.setWidthFull();
        descArea.addClassName("d-form-textarea");
        descCol.add(descLabel, descArea);
        formCard.add(descCol);

        // Row 4: Upload Foto (Vaadin Upload)
        Div photoCol = new Div();
        photoCol.addClassName("d-form-col-full");
        Span photoLabel = new Span("Bukti Foto");
        photoLabel.addClassName("d-input-label");

        // Track uploaded file
        Span uploadStatus = new Span("");
        uploadStatus.getStyle().set("color", "#0D9488").set("font-size", "0.88rem").set("font-weight", "600");

        Upload upload = new Upload(event -> {
            try {
                String originalName = event.getFileName();
                String ext = originalName.contains(".") ? originalName.substring(originalName.lastIndexOf('.')) : ".jpg";
                String uniqueName = "laporan_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12) + ext;

                // Save to uploads folder
                String uploadsDir = "src/main/resources/META-INF/resources/uploads/";
                File dir = new File(uploadsDir);
                if (!dir.exists()) dir.mkdirs();

                try (InputStream is = event.getInputStream();
                     FileOutputStream fos = new FileOutputStream(new File(dir, uniqueName))) {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = is.read(buf)) > 0) fos.write(buf, 0, len);
                }
                uploadedFileUrl = "uploads/" + uniqueName;
                UI.getCurrent().access(() -> uploadStatus.setText("✔ Foto berhasil dipilih: " + originalName));
            } catch (Exception ex) {
                UI.getCurrent().access(() -> uploadStatus.setText("❌ Gagal mengunggah: " + ex.getMessage()));
            }
        });
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.setMaxFileSize(5 * 1024 * 1024); // 5 MB
        upload.setMaxFiles(1);
        upload.setWidthFull();
        upload.addClassName("d-upload-zone");
        upload.getElement().executeJs("setTimeout(() => { const input = this.shadowRoot.querySelector('input[type=\"file\"]'); if(input) { input.setAttribute('capture', 'environment'); input.setAttribute('accept', 'image/*'); } }, 100);");

        Div uploadLabel = new Div();
        uploadLabel.getElement().setProperty("innerHTML",
            "<div style='display:flex;flex-direction:column;align-items:center;gap:8px;padding:24px;'>" +
            "<span style='font-size:2rem;'>📷</span>" +
            "<span style='font-weight:600;color:#334155;'>Klik untuk unggah atau seret foto ke sini</span>" +
            "<span style='font-size:0.82rem;color:#94A3B8;'>Maksimal 5MB (JPG, PNG)</span>" +
            "</div>");
        upload.setUploadButton(new Button("Pilih Foto"));
        upload.setDropLabel(new Span("Atau seret foto ke sini"));

        photoCol.add(photoLabel, upload, uploadStatus);
        formCard.add(photoCol);

        // Footer Actions
        Div footer = new Div();
        footer.addClassName("d-form-footer");

        Button cancelBtn = new Button("Batal");
        cancelBtn.getStyle().set("background", "#F1F5F9").set("color", "#334155")
            .set("border-radius", "12px").set("padding", "12px 24px").set("font-weight", "700").set("border", "none").set("cursor", "pointer");
        cancelBtn.addClickListener(e -> UI.getCurrent().navigate("laporan-saya"));

        Button submitBtn = new Button("Kirim Laporan");
        submitBtn.addClassName("d-submit-report-btn");
        submitBtn.addClickListener(e -> {
            String kategori = categorySelect.getValue();
            String judul = judulField.getValue().trim();
            String lokasi = locField.getValue().trim();
            String deskripsi = descArea.getValue().trim();
            String username = SessionManager.getUsername();

            if (kategori == null || kategori.isEmpty()) {
                showNotif("Pilih kategori masalah terlebih dahulu!", NotificationVariant.LUMO_ERROR);
                return;
            }
            if (judul.isEmpty()) {
                showNotif("Judul laporan tidak boleh kosong!", NotificationVariant.LUMO_ERROR);
                return;
            }
            if (lokasi.isEmpty()) {
                showNotif("Lokasi tidak boleh kosong!", NotificationVariant.LUMO_ERROR);
                return;
            }
            if (deskripsi.isEmpty()) {
                showNotif("Deskripsi tidak boleh kosong!", NotificationVariant.LUMO_ERROR);
                return;
            }

            try {
                laporanService.buatLaporan(username, kategori, judul, deskripsi, lokasi, uploadedFileUrl);
                showNotif("Laporan berhasil dikirim! Admin akan meninjau laporan Anda.", NotificationVariant.LUMO_SUCCESS);
                UI.getCurrent().navigate("laporan-saya");
            } catch (Exception ex) {
                showNotif("Gagal mengirim laporan: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
            }
        });

        footer.add(cancelBtn, submitBtn);
        formCard.add(footer);

        body.add(formCard);
        return body;
    }

    private void showNotif(String msg, NotificationVariant variant) {
        Notification n = new Notification(msg, 4000, Notification.Position.BOTTOM_CENTER);
        n.addThemeVariants(variant);
        n.open();
    }
}

package com.example.application.views.petugas;

import com.example.application.model.Laporan;
import com.example.application.service.LaporanService;
import com.example.application.service.SessionManager;
import com.example.application.views.warga.BlankLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

@Route(value = "petugas/selesaikan", layout = BlankLayout.class)
@PageTitle("Selesaikan Tugas - Petugas LaporGess")
public class PetugasSelesaikanView extends Div implements BeforeEnterObserver {

    private final LaporanService laporanService;
    private String uploadedBuktiUrl = null;
    private Integer laporanId = null;

    public PetugasSelesaikanView(LaporanService laporanService) {
        this.laporanService = laporanService;
        addClassName("pt-root");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!SessionManager.isLoggedIn() || !SessionManager.isPetugas()) {
            event.rerouteTo("login");
            return;
        }
        // Get laporan ID from session
        Object idObj = UI.getCurrent().getSession().getAttribute("selectedLaporanId");
        if (idObj != null) {
            laporanId = (Integer) idObj;
        }
        buildUI();
    }

    private void buildUI() {
        removeAll();
        Div sidebar = PetugasLayout.buildSidebar("petugas/tugas-saya");
        Div main = new Div();
        main.addClassName("pt-main");
        Div topbar = PetugasLayout.buildTopbar("Selesaikan Tugas");
        Div body = new Div();
        body.addClassName("pt-body");

        // Back button
        Div backBtn = new Div();
        backBtn.addClassName("pt-detail-back-btn");
        backBtn.add(new Span("←"), new Span("Kembali ke Tugas Saya"));
        backBtn.addClickListener(e -> UI.getCurrent().navigate("petugas/tugas-saya"));
        body.add(backBtn);

        // Load laporan info
        String laporanInfo = "Tugas Lapangan";
        String laporanLokasi = "-";
        if (laporanId != null) {
            var opt = laporanService.getById(laporanId);
            if (opt.isPresent()) {
                Laporan l = opt.get();
                laporanInfo = l.getJudul();
                laporanLokasi = l.getLokasi() != null ? l.getLokasi() : "-";
            }
        }

        // Form Card
        Div formCard = new Div();
        formCard.getStyle().set("background-color", "#FFFFFF").set("border-radius", "24px")
            .set("border", "1px solid #E2E8F0").set("padding", "32px")
            .set("box-shadow", "0 4px 6px -1px rgba(0,0,0,0.05)")
            .set("max-width", "700px").set("width", "100%");

        H2 formTitle = new H2("Laporan Penyelesaian Tugas");
        formTitle.getStyle().set("margin", "0 0 4px 0").set("font-size", "1.3rem").set("font-weight", "800").set("color", "#1E293B");
        Paragraph subTitle = new Paragraph("Tugas: " + laporanInfo + " • " + laporanLokasi);
        subTitle.getStyle().set("margin", "0 0 28px 0").set("color", "#64748B").set("font-size", "0.9rem");
        formCard.add(formTitle, subTitle);

        // Catatan Penyelesaian
        Span catatanLabel = new Span("Catatan Penyelesaian");
        catatanLabel.getStyle().set("font-size", "0.88rem").set("font-weight", "700").set("color", "#334155").set("display", "block").set("margin-bottom", "6px");

        TextArea catatanField = new TextArea();
        catatanField.setPlaceholder("Jelaskan tindakan yang telah dilakukan di lapangan...");
        catatanField.setWidthFull();
        catatanField.setHeight("130px");
        catatanField.addClassName("user-dialog-input");

        formCard.add(catatanLabel, catatanField);

        // Upload Foto Bukti
        Span fotoLabel = new Span("Foto Bukti Penyelesaian");
        fotoLabel.getStyle().set("font-size", "0.88rem").set("font-weight", "700").set("color", "#334155")
            .set("display", "block").set("margin-top", "20px").set("margin-bottom", "6px");

        Span uploadStatus = new Span("");
        uploadStatus.getStyle().set("color", "#0D9488").set("font-size", "0.85rem").set("font-weight", "600").set("display", "block").set("margin-top", "6px");

        Upload upload = new Upload(event -> {
            try {
                String originalName = event.getFileName();
                String ext = originalName.contains(".") ? originalName.substring(originalName.lastIndexOf('.')) : ".jpg";
                String uniqueName = "bukti_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12) + ext;

                String uploadsDir = "src/main/resources/META-INF/resources/uploads/";
                File dir = new File(uploadsDir);
                if (!dir.exists()) dir.mkdirs();

                try (InputStream is = event.getInputStream();
                     FileOutputStream fos = new FileOutputStream(new File(dir, uniqueName))) {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = is.read(buf)) > 0) fos.write(buf, 0, len);
                }
                uploadedBuktiUrl = "uploads/" + uniqueName;
                UI.getCurrent().access(() -> uploadStatus.setText("✔ Foto bukti berhasil dipilih: " + originalName));
            } catch (Exception ex) {
                UI.getCurrent().access(() -> uploadStatus.setText("❌ Gagal upload: " + ex.getMessage()));
            }
        });
        upload.setAcceptedFileTypes("image/jpeg", "image/png");
        upload.setMaxFileSize(5 * 1024 * 1024);
        upload.setMaxFiles(1);
        upload.setWidthFull();
        upload.setUploadButton(new Button("📷 Pilih Foto Bukti"));
        upload.setDropLabel(new Span("Atau seret foto ke sini"));

        formCard.add(fotoLabel, upload, uploadStatus);

        // Footer buttons
        Div footer = new Div();
        footer.addClassName("user-dialog-footer");
        footer.getStyle().set("margin-top", "28px");

        Button cancelBtn = new Button("Batal");
        cancelBtn.addClassName("user-dialog-cancel-btn");
        cancelBtn.addClickListener(e -> UI.getCurrent().navigate("petugas/tugas-saya"));

        Button submitBtn = new Button("Kirim Laporan Selesai");
        submitBtn.addClassName("pt-btn-kirim");
        submitBtn.addClickListener(e -> {
            String catatan = catatanField.getValue().trim();
            if (catatan.isEmpty()) {
                Notification n = new Notification("Catatan penyelesaian tidak boleh kosong!", 3000, Notification.Position.BOTTOM_CENTER);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                n.open();
                return;
            }
            if (laporanId == null) {
                Notification n = new Notification("Data tugas tidak ditemukan!", 3000, Notification.Position.BOTTOM_CENTER);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                n.open();
                return;
            }
            try {
                laporanService.selesaikanLaporan(laporanId, catatan, uploadedBuktiUrl);
                Notification n = new Notification("Laporan penyelesaian berhasil dikirim! +100 poin untuk warga. ✅", 4000, Notification.Position.BOTTOM_CENTER);
                n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                n.open();
                UI.getCurrent().navigate("petugas/riwayat-selesai");
            } catch (Exception ex) {
                Notification n = new Notification("Gagal mengirim: " + ex.getMessage(), 3000, Notification.Position.BOTTOM_CENTER);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                n.open();
            }
        });

        footer.add(cancelBtn, submitBtn);
        formCard.add(footer);
        body.add(formCard);
        main.add(topbar, body);
        add(sidebar, main);
    }
}

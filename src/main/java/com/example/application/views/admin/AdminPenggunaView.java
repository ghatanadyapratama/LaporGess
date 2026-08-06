package com.example.application.views.admin;

import com.example.application.model.Pengguna;
import com.example.application.repository.PenggunaRepository;
import com.example.application.views.warga.BlankLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.datepicker.DatePicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Route(value = "admin/pengguna", layout = BlankLayout.class)
@PageTitle("Kelola Pengguna - Lapor Gess")
public class AdminPenggunaView extends Div {

    private final PenggunaRepository penggunaRepository;
    private List<Pengguna> users;
    private Div tableBody;
    private Span infoText;
    private TextField searchField;
    private ComboBox<String> filterPeran;
    private ComboBox<String> filterStatus;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public AdminPenggunaView(PenggunaRepository penggunaRepository) {
        this.penggunaRepository = penggunaRepository;
        addClassName("ad-root");

        long petugasAktif = penggunaRepository.countByStatusAndPeran(Pengguna.Status.AKTIF, Pengguna.Peran.PETUGAS_LAPANGAN);
        long verifikasiPending = penggunaRepository.countByStatus(Pengguna.Status.PENDING);
        Div sidebar = AdminLayout.buildSidebar("admin/pengguna", 0, petugasAktif, verifikasiPending);

        Div main = new Div();
        main.addClassName("ad-main");

        Div topbar = AdminLayout.buildTopbar("Pengguna");

        Div body = new Div();
        body.addClassName("ad-body");

        // Main Card Container
        Div card = new Div();
        card.addClassName("ad-card");

        // ── Controls Bar ──────────────────────────────
        Div controls = new Div();
        controls.getStyle()
            .set("display", "flex").set("align-items", "center")
            .set("gap", "12px").set("margin-bottom", "20px")
            .set("flex-wrap", "wrap");

        searchField = new TextField();
        searchField.setPlaceholder("Cari nama/username...");
        searchField.getStyle().set("width", "240px");
        searchField.addValueChangeListener(e -> loadDataAndRefresh());

        filterPeran = new ComboBox<>();
        filterPeran.setItems("Semua Peran", "WARGA", "PETUGAS_LAPANGAN", "ADMIN");
        filterPeran.setValue("Semua Peran");
        filterPeran.getStyle().set("width", "145px");
        filterPeran.addValueChangeListener(e -> loadDataAndRefresh());

        filterStatus = new ComboBox<>();
        filterStatus.setItems("Semua Status", "AKTIF", "PENDING", "NONAKTIF");
        filterStatus.setValue("Semua Status");
        filterStatus.getStyle().set("width", "145px");
        filterStatus.addValueChangeListener(e -> loadDataAndRefresh());

        // Tambah button
        Button btnTambah = new Button("+ Tambah Pengguna");
        btnTambah.addClassName("ad-btn-primary-green");
        btnTambah.getStyle().set("margin-left", "auto");
        btnTambah.addClickListener(e -> openAddDialog());

        controls.add(searchField, filterPeran, filterStatus, btnTambah);
        card.add(controls);

        // ── Table Header ──────────────────────────────
        Div tableWrapper = new Div();
        tableWrapper.getStyle().set("overflow-x", "auto");

        StringBuilder thead = new StringBuilder();
        thead.append("<table class='ad-table' style='width:100%;border-collapse:collapse;' id='pengguna-table'>")
             .append("<thead><tr>")
             .append("<th>Data Pengguna</th><th>ID</th><th>Peran</th>")
             .append("<th>Area / Lokasi</th><th>Tanggal Lahir</th><th>Jenis Kelamin</th>")
             .append("<th style='text-align:center'>Aksi</th>")
             .append("</tr></thead></table>");

        Div theadDiv = new Div();
        theadDiv.getElement().setProperty("innerHTML", thead.toString());

        // ── Table Body (dynamic) ──────────────────────
        tableBody = new Div();
        
        tableWrapper.add(theadDiv, tableBody);
        card.add(tableWrapper);

        // ── Pagination Footer ──────────────────────────
        Div pagination = new Div();
        pagination.addClassName("ad-pagination");

        infoText = new Span("Menampilkan 0 pengguna");
        infoText.addClassName("ad-page-info");
        pagination.add(infoText);

        card.add(pagination);
        body.add(card);
        main.add(topbar, body);
        add(sidebar, main);

        // initial load
        loadDataAndRefresh();
    }

    private void loadDataAndRefresh() {
        users = penggunaRepository.findAll();
        
        // Apply filters
        String search = searchField.getValue() != null ? searchField.getValue().toLowerCase() : "";
        String peran = filterPeran.getValue();
        String status = filterStatus.getValue();

        users = users.stream().filter(u -> {
            boolean matchSearch = search.isEmpty() || 
                (u.getNamaLengkap() != null && u.getNamaLengkap().toLowerCase().contains(search)) ||
                (u.getUsername() != null && u.getUsername().toLowerCase().contains(search));
            
            boolean matchPeran = "Semua Peran".equals(peran) || (u.getPeran() != null && u.getPeran().name().equals(peran));
            boolean matchStatus = "Semua Status".equals(status) || (u.getStatus() != null && u.getStatus().name().equals(status));
            
            return matchSearch && matchPeran && matchStatus;
        }).collect(Collectors.toList());

        refreshTable();
        infoText.setText("Menampilkan " + users.size() + " pengguna");
    }

    // ── Refresh table ─────────────────────────────────────────────────────────
    private void refreshTable() {
        tableBody.removeAll();
        for (Pengguna u : users) {
            tableBody.add(buildRow(u));
        }
    }

    private Div buildRow(Pengguna u) {
        Div row = new Div();
        row.getStyle()
            .set("display", "grid")
            .set("grid-template-columns", "2fr 1fr 1fr 1fr 1fr 1fr 120px")
            .set("align-items", "center")
            .set("padding", "12px 16px")
            .set("border-bottom", "1px solid #F1F5F9")
            .set("gap", "12px");

        // Data Pengguna cell
        Div userCell = new Div();
        userCell.getStyle().set("display", "flex").set("align-items", "center").set("gap", "10px");
        Div avatar = new Div();
        avatar.getStyle().set("width", "36px").set("height", "36px").set("border-radius", "50%")
            .set("background-color", "#E2E8F0").set("flex-shrink", "0")
            .set("display", "flex").set("align-items", "center").set("justify-content", "center")
            .set("overflow", "hidden").set("font-weight", "700").set("font-size", "0.85rem").set("color", "#64748B");
        if (u.getFotoProfil() != null && !u.getFotoProfil().isEmpty()) {
            Image fotoImg = new Image(u.getFotoProfil(), "foto");
            fotoImg.getStyle().set("width", "100%").set("height", "100%").set("object-fit", "cover");
            avatar.add(fotoImg);
        } else {
            String initials = "U";
            if (u.getNamaLengkap() != null && !u.getNamaLengkap().isEmpty()) {
                String[] parts = u.getNamaLengkap().trim().split(" ");
                initials = parts.length > 1
                    ? (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase()
                    : parts[0].substring(0, 1).toUpperCase();
            }
            avatar.add(new Span(initials));
        }
        Div nameCol = new Div();
        Span nameTxt = new Span(u.getNamaLengkap() != null ? u.getNamaLengkap() : "-");
        nameTxt.getStyle().set("font-weight", "700").set("color", "#1E293B").set("display", "block");
        Span userTxt = new Span("@" + u.getUsername());
        userTxt.getStyle().set("font-size", "0.76rem").set("color", "#94A3B8");
        nameCol.add(nameTxt, userTxt);
        userCell.add(avatar, nameCol);

        // ID cell
        Span idSpan = new Span("USR-" + u.getId());
        idSpan.getStyle().set("font-weight", "600").set("color", "#64748B").set("font-size", "0.85rem");

        // Peran badge
        String roleStr = u.getPeran() != null ? u.getPeran().name() : "-";
        Span peranBadge = new Span(roleStr);
        String peranColor = "PETUGAS_LAPANGAN".equals(roleStr) ? "#1D4ED8" : "ADMIN".equals(roleStr) ? "#7C3AED" : "#92400E";
        String peranBg = "PETUGAS_LAPANGAN".equals(roleStr) ? "#DBEAFE" : "ADMIN".equals(roleStr) ? "#EDE9FE" : "#FEF3C7";
        peranBadge.getStyle()
            .set("background-color", peranBg).set("color", peranColor)
            .set("padding", "3px 10px").set("border-radius", "20px")
            .set("font-weight", "700").set("font-size", "0.78rem");

        Span areaSpan = new Span(u.getRtRw() != null ? u.getRtRw() : "-");
        areaSpan.getStyle().set("font-size", "0.85rem").set("color", "#475569");

        Span birthSpan = new Span(u.getTanggalLahir() != null ? u.getTanggalLahir().format(FMT) : "-");
        birthSpan.getStyle().set("font-size", "0.85rem").set("color", "#475569");

        Span genderSpan = new Span(u.getJenisKelamin() != null ? u.getJenisKelamin().name() : "-");
        genderSpan.getStyle().set("font-size", "0.85rem").set("color", "#475569");

        // Action buttons
        Div actions = new Div();
        actions.getStyle().set("display", "flex").set("gap", "6px").set("justify-content", "center");

        Button btnEdit = new Button("✏");
        btnEdit.getStyle()
            .set("background", "#3B82F6").set("color", "white")
            .set("border", "none").set("border-radius", "6px")
            .set("padding", "5px 10px").set("cursor", "pointer")
            .set("font-size", "0.78rem").set("font-weight", "600");
        btnEdit.addClickListener(e -> openEditDialog(u));

        Button btnHapus = new Button("🗑");
        btnHapus.getStyle()
            .set("background", "#EF4444").set("color", "white")
            .set("border", "none").set("border-radius", "6px")
            .set("padding", "5px 10px").set("cursor", "pointer")
            .set("font-size", "0.78rem");
        btnHapus.addClickListener(e -> openDeleteConfirm(u));

        actions.add(btnEdit, btnHapus);

        row.add(userCell, idSpan, peranBadge, areaSpan, birthSpan, genderSpan, actions);
        return row;
    }

    private void openAddDialog() {
        Pengguna u = new Pengguna();
        showDialog("Tambah Pengguna Baru", u, true);
    }

    private void openEditDialog(Pengguna u) {
        showDialog("Edit Data Pengguna", u, false);
    }

    private void showDialog(String title, Pengguna u, boolean isNew) {
        Dialog dialog = new Dialog();
        dialog.setWidth("640px");
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        // Header
        Div headerDiv = new Div();
        headerDiv.addClassName("user-dialog-header");
        Span titleSpan = new Span(title);
        titleSpan.addClassName("user-dialog-title");
        Button btnClose = new Button("×", ev -> dialog.close());
        btnClose.addClassName("user-dialog-close-btn");
        headerDiv.add(titleSpan, btnClose);
        dialog.getHeader().add(headerDiv);

        // Avatar row with photo upload
        final String[] tempFotoUrl = { u.getFotoProfil() };

        Div avatarRow = new Div();
        avatarRow.addClassName("user-dialog-avatar-row");

        Div avatarCircle = new Div();
        avatarCircle.addClassName("user-dialog-avatar-circle");
        avatarCircle.getStyle().set("display", "flex").set("align-items", "center").set("justify-content", "center")
            .set("overflow", "hidden").set("font-weight", "700").set("font-size", "1.2rem").set("color", "#64748B");
        // Show current photo or initials
        if (u.getFotoProfil() != null && !u.getFotoProfil().isEmpty()) {
            Image av = new Image(u.getFotoProfil(), "foto");
            av.getStyle().set("width", "100%").set("height", "100%").set("object-fit", "cover");
            avatarCircle.add(av);
        } else {
            String ini = isNew ? "+" : (u.getNamaLengkap() != null && !u.getNamaLengkap().isEmpty()
                ? u.getNamaLengkap().trim().substring(0, 1).toUpperCase() : "U");
            avatarCircle.add(new Span(ini));
        }

        Div avatarInfo = new Div();
        avatarInfo.addClassName("user-dialog-avatar-info");
        Span idText = new Span("ID Pengguna: " + (isNew ? "Baru" : "USR-" + u.getId()));
        idText.addClassName("user-dialog-avatar-id");

        // Upload button untuk foto profil
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
                    avatarCircle.removeAll();
                    Image prev = new Image(tempFotoUrl[0], "preview");
                    prev.getStyle().set("width", "100%").set("height", "100%").set("object-fit", "cover");
                    avatarCircle.add(prev);
                });
            } catch (Exception ex) {
                Notification.show("Gagal upload: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        photoUpload.setAcceptedFileTypes("image/jpeg", "image/png");
        photoUpload.setMaxFileSize(2 * 1024 * 1024);
        photoUpload.setMaxFiles(1);
        Button uploadBtn = new Button("📷 Upload Foto");
        uploadBtn.getStyle().set("background", "#F1F5F9").set("color", "#334155")
            .set("border", "none").set("border-radius", "8px").set("font-size", "0.8rem")
            .set("font-weight", "600").set("cursor", "pointer").set("padding", "5px 10px");
        photoUpload.setUploadButton(uploadBtn);
        photoUpload.setDropLabel(new Span(""));
        photoUpload.getStyle().set("margin-top", "4px");

        avatarInfo.add(idText, photoUpload);
        avatarRow.add(avatarCircle, avatarInfo);

        // Grid fields
        Div grid = new Div();
        grid.addClassName("user-dialog-grid");

        TextField nameField = new TextField("Nama Lengkap");
        nameField.setValue(u.getNamaLengkap() != null ? u.getNamaLengkap() : "");
        nameField.addClassName("user-dialog-input");

        TextField userField = new TextField("Username");
        userField.setValue(u.getUsername() != null ? u.getUsername() : "");
        userField.addClassName("user-dialog-input");

        TextField passField = new TextField("Kata Sandi (Biarkan kosong jika tidak diubah)");
        passField.addClassName("user-dialog-input");
        if (isNew) passField.setLabel("Kata Sandi");

        ComboBox<Pengguna.Peran> roleBox = new ComboBox<>("Peran Akun");
        roleBox.setItems(Pengguna.Peran.values());
        roleBox.setValue(u.getPeran() != null ? u.getPeran() : Pengguna.Peran.WARGA);
        roleBox.addClassName("user-dialog-combo");

        ComboBox<Pengguna.Status> statusBox = new ComboBox<>("Status Akun");
        statusBox.setItems(Pengguna.Status.values());
        statusBox.setValue(u.getStatus() != null ? u.getStatus() : Pengguna.Status.AKTIF);
        statusBox.addClassName("user-dialog-combo");

        DatePicker birthField = new DatePicker("Tanggal Lahir");
        birthField.setValue(u.getTanggalLahir());
        birthField.addClassName("user-dialog-input");

        ComboBox<Pengguna.JenisKelamin> genderBox = new ComboBox<>("Jenis Kelamin");
        genderBox.setItems(Pengguna.JenisKelamin.values());
        genderBox.setValue(u.getJenisKelamin());
        genderBox.addClassName("user-dialog-combo");

        TextField areaField = new TextField("Area / Lokasi (RT/RW)");
        areaField.setValue(u.getRtRw() != null ? u.getRtRw() : "");
        areaField.addClassName("user-dialog-input");

        grid.add(nameField, userField, passField, roleBox, statusBox, birthField, genderBox, areaField);

        VerticalLayout layout = new VerticalLayout(avatarRow, grid);
        layout.setPadding(false);
        layout.setSpacing(false);
        dialog.add(layout);

        // Footer buttons
        Div footer = new Div();
        footer.addClassName("user-dialog-footer");

        Button btnCancel = new Button("Batal", ev -> dialog.close());
        btnCancel.addClassName("user-dialog-cancel-btn");

        Button btnSave = new Button("Simpan");
        btnSave.addClassName("user-dialog-save-btn");
        btnSave.addClickListener(ev -> {
            if (nameField.isEmpty() || userField.isEmpty()) {
                Notification n = new Notification("Nama Lengkap dan Username harus diisi!", 3000, Notification.Position.TOP_CENTER);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                n.open();
                return;
            }
            if (isNew && passField.isEmpty()) {
                Notification n = new Notification("Kata sandi harus diisi untuk pengguna baru!", 3000, Notification.Position.TOP_CENTER);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                n.open();
                return;
            }
            u.setNamaLengkap(nameField.getValue());
            u.setUsername(userField.getValue());
            if (!passField.isEmpty()) {
                u.setKataSandi(passField.getValue());
            }
            u.setPeran(roleBox.getValue());
            u.setStatus(statusBox.getValue());
            u.setTanggalLahir(birthField.getValue());
            u.setJenisKelamin(genderBox.getValue());
            u.setRtRw(areaField.getValue());
            if (tempFotoUrl[0] != null) {
                u.setFotoProfil(tempFotoUrl[0]);
            }
            
            penggunaRepository.save(u);
            loadDataAndRefresh();
            dialog.close();
            Notification n = new Notification("Data pengguna berhasil disimpan!", 3000, Notification.Position.BOTTOM_CENTER);
            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            n.open();
        });

        footer.add(btnCancel, btnSave);
        dialog.getFooter().add(footer);
        dialog.open();
    }

    private void openDeleteConfirm(Pengguna u) {
        Dialog dialog = new Dialog();
        dialog.setWidth("380px");
        dialog.setHeaderTitle("🗑 Hapus Pengguna");

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(false);

        Paragraph msg = new Paragraph("Apakah Anda yakin ingin menghapus pengguna");
        Paragraph name = new Paragraph("\"" + u.getNamaLengkap() + "\" (" + u.getUsername() + ")?");
        name.getStyle().set("font-weight", "700").set("color", "#EF4444").set("margin", "0");
        Paragraph warn = new Paragraph("Tindakan ini tidak dapat dibatalkan.");
        warn.getStyle().set("color", "#94A3B8").set("font-size", "0.85rem");

        content.add(msg, name, warn);
        dialog.add(content);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidthFull();
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        footer.setSpacing(true);

        Button btnCancel = new Button("Batal", ev -> dialog.close());
        btnCancel.getStyle().set("background", "#F1F5F9").set("color", "#475569").set("border", "none").set("border-radius", "8px").set("padding", "8px 20px").set("cursor", "pointer");

        Button btnConfirm = new Button("Ya, Hapus");
        btnConfirm.getStyle().set("background", "#EF4444").set("color", "white").set("border", "none").set("border-radius", "8px").set("padding", "8px 20px").set("cursor", "pointer").set("font-weight", "700");
        btnConfirm.addClickListener(ev -> {
            penggunaRepository.delete(u);
            loadDataAndRefresh();
            dialog.close();
            Notification n = new Notification("Pengguna berhasil dihapus.", 3000, Notification.Position.BOTTOM_CENTER);
            n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            n.open();
        });

        footer.add(btnCancel, btnConfirm);
        dialog.getFooter().add(footer);
        dialog.open();
    }
}

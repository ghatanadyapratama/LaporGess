package com.example.application.views.admin;

import com.example.application.model.Laporan;
import com.example.application.model.Pengguna;
import com.example.application.repository.NotifikasiRepository;
import com.example.application.repository.PenggunaRepository;
import com.example.application.service.LaporanService;
import com.example.application.views.warga.BlankLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "admin/laporan", layout = BlankLayout.class)
@PageTitle("Kelola Laporan - Lapor Gess")
public class AdminLaporanView extends Div {

    private final LaporanService laporanService;
    private final PenggunaRepository penggunaRepository;
    private final NotifikasiRepository notifikasiRepository;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private Div tableBody;
    private List<Laporan> allLaporan;
    private TextField searchField;
    private Select<String> filterStatus;

    public AdminLaporanView(LaporanService laporanService, PenggunaRepository penggunaRepository, NotifikasiRepository notifikasiRepository) {
        this.laporanService = laporanService;
        this.penggunaRepository = penggunaRepository;
        this.notifikasiRepository = notifikasiRepository;
        addClassName("ad-root");

        long laporanPending = laporanService.countByStatus(Laporan.Status.PENDING);
        long petugasAktif = penggunaRepository.countByStatusAndPeran(Pengguna.Status.AKTIF, Pengguna.Peran.PETUGAS_LAPANGAN);
        long verifikasiPending = penggunaRepository.countByStatus(Pengguna.Status.PENDING);
        Div sidebar = AdminLayout.buildSidebar("admin/laporan", laporanPending, petugasAktif, verifikasiPending);

        Div main = new Div();
        main.addClassName("ad-main");

        Div topbar = AdminLayout.buildTopbar("Kelola Laporan", notifikasiRepository);

        Div body = new Div();
        body.addClassName("ad-body");

        Div card = new Div();
        card.addClassName("ad-card");
        card.getStyle().set("padding", "20px 24px");

        // Controls bar
        Div controls = new Div();
        controls.getStyle().set("display", "flex").set("justify-content", "space-between")
            .set("align-items", "center").set("margin-bottom", "20px").set("gap", "12px");

        searchField = new TextField();
        searchField.setPlaceholder("Cari judul atau pelapor...");
        searchField.getStyle().set("width", "280px");
        searchField.addValueChangeListener(e -> renderTable());

        filterStatus = new Select<>();
        filterStatus.setItems("Semua", "PENDING", "DIPROSES", "SELESAI", "DITOLAK");
        filterStatus.setValue("Semua");
        filterStatus.getStyle().set("width", "160px");
        filterStatus.addValueChangeListener(e -> renderTable());

        controls.add(searchField, filterStatus);
        card.add(controls);

        // Table
        Div tableContainer = new Div();
        tableContainer.getStyle().set("overflow-x", "auto");

        // Table header
        StringBuilder headerHtml = new StringBuilder();
        headerHtml.append("<table class='ad-table' style='width:100%;border-collapse:collapse;'>");
        headerHtml.append("<thead><tr>");
        headerHtml.append("<th>ID</th><th>Kategori</th><th>Judul</th><th>Pelapor</th><th>Tanggal</th><th>Status</th><th>Aksi</th>");
        headerHtml.append("</tr></thead></table>");

        Div headerWrapper = new Div();
        headerWrapper.getElement().setProperty("innerHTML", headerHtml.toString());
        tableContainer.add(headerWrapper);

        tableBody = new Div();
        tableContainer.add(tableBody);
        card.add(tableContainer);

        body.add(card);
        main.add(topbar, body);
        add(sidebar, main);

        allLaporan = laporanService.getAllLaporan();
        renderTable();
    }

    private void renderTable() {
        tableBody.removeAll();
        String keyword = searchField.getValue().trim().toLowerCase();
        String status = filterStatus.getValue();

        List<Laporan> filtered = allLaporan.stream()
            .filter(l -> keyword.isEmpty()
                    || l.getJudul().toLowerCase().contains(keyword)
                    || (l.getWarga() != null && l.getWarga().getNamaLengkap().toLowerCase().contains(keyword)))
            .filter(l -> "Semua".equals(status) || l.getStatus().name().equals(status))
            .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            Div empty = new Div();
            empty.getStyle().set("padding", "40px").set("text-align", "center").set("color", "#94A3B8");
            empty.add(new Span("Tidak ada laporan ditemukan."));
            tableBody.add(empty);
            return;
        }

        for (Laporan l : filtered) {
            tableBody.add(buildRow(l));
        }
    }

    private Div buildRow(Laporan laporan) {
        Div row = new Div();
        row.getStyle().set("display", "grid")
            .set("grid-template-columns", "60px 110px 1fr 130px 100px 110px 120px")
            .set("align-items", "center")
            .set("padding", "14px 16px")
            .set("border-bottom", "1px solid #F1F5F9")
            .set("gap", "8px");

        Span idSpan = new Span("#" + laporan.getId());
        idSpan.getStyle().set("font-weight", "700").set("color", "#64748B").set("font-size", "0.85rem");

        Span kategoriSpan = new Span(laporan.getKategori());
        kategoriSpan.getStyle().set("font-size", "0.82rem").set("color", "#475569");

        Span judulSpan = new Span(laporan.getJudul());
        judulSpan.getStyle().set("font-weight", "600").set("color", "#1E293B");

        String pelaporName = laporan.getWarga() != null ? laporan.getWarga().getNamaLengkap() : "-";
        Span pelapor = new Span(pelaporName);
        pelapor.getStyle().set("font-size", "0.88rem").set("color", "#475569");

        Span tanggal = new Span(laporan.getDibuatPada().format(FMT));
        tanggal.getStyle().set("font-size", "0.82rem").set("color", "#94A3B8");

        Span statusSpan = new Span(laporan.getStatus().name());
        String statusClass = switch (laporan.getStatus()) {
            case PENDING -> "ad-status-menunggu";
            case DIPROSES -> "ad-status-diproses";
            case MENUNGGU_KONFIRMASI -> "ad-status-diproses"; // Use same color as diproses or similar for waiting
            case SELESAI -> "ad-status-selesai";
            case DITOLAK -> "ad-status-ditolak";
        };
        statusSpan.addClassName(statusClass);

        Button tinjauBtn = new Button("Tinjau");
        tinjauBtn.addClassName("ad-btn-secondary");
        tinjauBtn.getStyle().set("font-size", "0.82rem").set("padding", "6px 14px");
        tinjauBtn.addClickListener(e -> openDetailDialog(laporan));

        row.add(idSpan, kategoriSpan, judulSpan, pelapor, tanggal, statusSpan, tinjauBtn);
        return row;
    }

    private void openDetailDialog(Laporan laporan) {
        Dialog dialog = new Dialog();
        dialog.setWidth("700px");
        dialog.setModal(true);
        dialog.setDraggable(true);

        Div content = new Div();
        content.getStyle().set("padding", "8px").set("display", "flex").set("flex-direction", "column").set("gap", "16px");

        H2 title = new H2(laporan.getJudul());
        title.getStyle().set("margin", "0").set("font-size", "1.3rem").set("color", "#1E293B");

        Span statusBadge = new Span(laporan.getStatus().name());
        statusBadge.addClassName(switch (laporan.getStatus()) {
            case PENDING -> "ad-status-menunggu";
            case DIPROSES -> "ad-status-diproses";
            case MENUNGGU_KONFIRMASI -> "ad-status-diproses";
            case SELESAI -> "ad-status-selesai";
            case DITOLAK -> "ad-status-ditolak";
        });

        Div metaRow = new Div();
        metaRow.getStyle().set("display", "flex").set("gap", "12px").set("align-items", "center").set("flex-wrap", "wrap");
        metaRow.add(title, statusBadge);
        content.add(metaRow);

        // Photo if exists
        if (laporan.getFotoUrl() != null && !laporan.getFotoUrl().isEmpty()) {
            Image foto = new Image(laporan.getFotoUrl(), "Foto laporan");
            foto.getStyle().set("width", "100%").set("max-height", "220px").set("object-fit", "cover")
                .set("border-radius", "12px");
            content.add(foto);
        }

        // Info grid
        Div infoGrid = new Div();
        infoGrid.getStyle().set("display", "grid").set("grid-template-columns", "1fr 1fr").set("gap", "12px");
        infoGrid.add(buildInfoBlock("Kategori", laporan.getKategori()));
        infoGrid.add(buildInfoBlock("Pelapor", laporan.getWarga() != null ? laporan.getWarga().getNamaLengkap() : "-"));
        infoGrid.add(buildInfoBlock("Lokasi", laporan.getLokasi() != null ? laporan.getLokasi() : "-"));
        infoGrid.add(buildInfoBlock("Tanggal", laporan.getDibuatPada().format(FMT)));
        content.add(infoGrid);

        Div descBox = new Div();
        descBox.getStyle().set("background", "#F8FAFC").set("border-radius", "10px").set("padding", "14px");
        Span descTitle = new Span("Deskripsi:");
        descTitle.getStyle().set("font-weight", "700").set("font-size", "0.85rem").set("color", "#334155").set("display", "block").set("margin-bottom", "6px");
        Span descText = new Span(laporan.getDeskripsi() != null ? laporan.getDeskripsi() : "-");
        descText.getStyle().set("color", "#475569").set("font-size", "0.9rem").set("line-height", "1.6");
        descBox.add(descTitle, descText);
        content.add(descBox);

        // Action section (only if PENDING)
        if (laporan.getStatus() == Laporan.Status.PENDING) {
            Div actionSection = new Div();
            actionSection.getStyle().set("display", "flex").set("flex-direction", "column").set("gap", "12px")
                .set("border-top", "1px solid #E2E8F0").set("padding-top", "16px");

            Span actionTitle = new Span("Tindakan:");
            actionTitle.getStyle().set("font-weight", "700").set("font-size", "0.92rem").set("color", "#1E293B");

            // Assign petugas
            List<Pengguna> petugasList = penggunaRepository.findByPeran(Pengguna.Peran.PETUGAS_LAPANGAN);
            ComboBox<Pengguna> petugasSelect = new ComboBox<>("Assign ke Petugas");
            petugasSelect.setItems(petugasList);
            petugasSelect.setItemLabelGenerator(p -> p.getNamaLengkap() + " (" + p.getUsername() + ")");
            petugasSelect.setPlaceholder("Pilih petugas...");
            petugasSelect.setWidthFull();

            Button btnAssign = new Button("✔ Verifikasi & Assign");
            btnAssign.addClassName("ad-btn-approve");
            btnAssign.getStyle().set("width", "100%");
            btnAssign.addClickListener(e -> {
                Pengguna selected = petugasSelect.getValue();
                if (selected == null) {
                    Notification.show("Pilih petugas terlebih dahulu!", 2000, Notification.Position.MIDDLE);
                    return;
                }
                laporanService.assignPetugas(laporan.getId(), selected.getUsername());
                Notification n = new Notification("Laporan diassign ke " + selected.getNamaLengkap(), 3000, Notification.Position.BOTTOM_CENTER);
                n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                n.open();
                dialog.close();
                allLaporan = laporanService.getAllLaporan();
                renderTable();
            });

            TextArea alasanTolak = new TextArea("Alasan Penolakan");
            alasanTolak.setPlaceholder("Jelaskan alasan penolakan laporan...");
            alasanTolak.setWidthFull();

            Button btnReject = new Button("✖ Tolak Laporan");
            btnReject.addClassName("ad-btn-reject");
            btnReject.getStyle().set("width", "100%");
            btnReject.addClickListener(e -> {
                String alasan = alasanTolak.getValue().trim();
                if (alasan.isEmpty()) alasan = "Laporan tidak memenuhi kriteria.";
                laporanService.tolakLaporan(laporan.getId(), alasan);
                Notification n = new Notification("Laporan ditolak.", 3000, Notification.Position.BOTTOM_CENTER);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                n.open();
                dialog.close();
                allLaporan = laporanService.getAllLaporan();
                renderTable();
            });

            actionSection.add(actionTitle, petugasSelect, btnAssign, alasanTolak, btnReject);
            content.add(actionSection);
        }

        // Catatan if completed
        if ((laporan.getStatus() == Laporan.Status.SELESAI || laporan.getStatus() == Laporan.Status.MENUNGGU_KONFIRMASI) && laporan.getCatatan() != null) {
            Div catatanBox = new Div();
            catatanBox.getStyle().set("background", "#E6F7F5").set("border-radius", "10px").set("padding", "14px");
            Span catatanTitle = new Span("Catatan Petugas:");
            catatanTitle.getStyle().set("font-weight", "700").set("font-size", "0.85rem").set("color", "#0D9488").set("display", "block").set("margin-bottom", "6px");
            Span catatanText = new Span(laporan.getCatatan());
            catatanText.getStyle().set("color", "#134E4A").set("font-size", "0.9rem").set("font-style", "italic");
            catatanBox.add(catatanTitle, catatanText);
            content.add(catatanBox);
        }

        Button closeBtn = new Button("Tutup");
        closeBtn.getStyle().set("width", "100%").set("background", "#F1F5F9").set("color", "#334155")
            .set("font-weight", "700").set("border-radius", "12px").set("padding", "12px");
        closeBtn.addClickListener(e -> dialog.close());
        content.add(closeBtn);

        dialog.add(content);
        dialog.open();
    }

    private Div buildInfoBlock(String label, String value) {
        Div block = new Div();
        block.getStyle().set("display", "flex").set("flex-direction", "column").set("gap", "4px");
        Span lbl = new Span(label);
        lbl.getStyle().set("font-size", "0.78rem").set("font-weight", "700").set("color", "#94A3B8").set("text-transform", "uppercase");
        Span val = new Span(value != null ? value : "-");
        val.getStyle().set("font-size", "0.92rem").set("font-weight", "600").set("color", "#1E293B");
        block.add(lbl, val);
        return block;
    }
}

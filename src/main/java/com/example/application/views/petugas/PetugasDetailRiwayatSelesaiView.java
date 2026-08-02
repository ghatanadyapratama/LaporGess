package com.example.application.views.petugas;

import com.example.application.model.Laporan;
import com.example.application.service.LaporanService;
import com.example.application.service.SessionManager;
import com.example.application.views.warga.BlankLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;

@Route(value = "petugas/detail-riwayat-selesai", layout = BlankLayout.class)
@PageTitle("Detail Riwayat Selesai - Petugas LaporGess")
public class PetugasDetailRiwayatSelesaiView extends Div implements BeforeEnterObserver {

    private final LaporanService laporanService;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

    public PetugasDetailRiwayatSelesaiView(LaporanService laporanService) {
        this.laporanService = laporanService;
        addClassName("pt-root");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!SessionManager.isLoggedIn() || !SessionManager.isPetugas()) {
            event.rerouteTo("login");
            return;
        }
        buildUI();
    }

    private void buildUI() {
        removeAll();
        Div sidebar = PetugasLayout.buildSidebar("petugas/riwayat-selesai");
        Div main = new Div();
        main.addClassName("pt-main");
        Div topbar = PetugasLayout.buildTopbar("Detail Riwayat Selesai");
        Div body = new Div();
        body.addClassName("pt-body");

        // Back button
        Div backBtn = new Div();
        backBtn.addClassName("pt-detail-back-btn");
        backBtn.add(new Span("←"), new Span("Kembali"));
        backBtn.addClickListener(e -> UI.getCurrent().navigate("petugas/riwayat-selesai"));
        body.add(backBtn);

        // Load laporan from session
        Object idObj = UI.getCurrent().getSession().getAttribute("selectedLaporanId");
        if (idObj == null) {
            body.add(new Paragraph("Data tidak ditemukan."));
            main.add(topbar, body);
            add(sidebar, main);
            return;
        }
        Integer laporanId = (Integer) idObj;
        Laporan laporan = laporanService.getById(laporanId).orElse(null);

        if (laporan == null) {
            body.add(new Paragraph("Laporan tidak ditemukan."));
            main.add(topbar, body);
            add(sidebar, main);
            return;
        }

        // Two-column grid
        Div grid = new Div();
        grid.addClassName("pt-detail-grid");

        // ─── LEFT: Job Details ─────────────────────────────────────────────
        Div leftCard = new Div();
        leftCard.addClassName("pt-detail-left-card");

        // Photo
        if (laporan.getFotoUrl() != null && !laporan.getFotoUrl().isEmpty()) {
            Image img = new Image(laporan.getFotoUrl(), "Foto laporan");
            img.addClassName("pt-detail-image");
            leftCard.add(img);
        }

        Div infoBox = new Div();
        infoBox.addClassName("pt-detail-info-box");

        H2 jobTitle = new H2(laporan.getJudul());
        jobTitle.addClassName("pt-detail-title");

        Div rowLoc = new Div();
        rowLoc.addClassName("pt-detail-info-row");
        Span locIcon = new Span("📍");
        Span locText = new Span(laporan.getLokasi() != null ? laporan.getLokasi() : "-");
        rowLoc.add(locIcon, locText);

        Div rowCat = new Div();
        rowCat.addClassName("pt-detail-info-row");
        Span catIcon = new Span("🏷");
        Span catText = new Span("Kategori: " + laporan.getKategori());
        rowCat.add(catIcon, catText);

        Div rowDate = new Div();
        rowDate.addClassName("pt-detail-info-row");
        Span dateIcon = new Span("🗓");
        Span dateText = new Span("Dilaporkan pada " + laporan.getDibuatPada().format(FMT));
        rowDate.add(dateIcon, dateText);

        Div rowWarga = new Div();
        rowWarga.addClassName("pt-detail-info-row");
        Span wargaIcon = new Span("👤");
        String wargaName = laporan.getWarga() != null ? laporan.getWarga().getNamaLengkap() : "-";
        Span wargaText = new Span("Pelapor: " + wargaName);
        rowWarga.add(wargaIcon, wargaText);

        infoBox.add(jobTitle, rowLoc, rowCat, rowDate, rowWarga);
        leftCard.add(infoBox);

        // ─── RIGHT: Completion Info ────────────────────────────────────────
        Div rightCard = new Div();
        rightCard.addClassName("pt-detail-right-card");

        Div iconWrapper = new Div();
        iconWrapper.getStyle()
            .set("width", "64px").set("height", "64px")
            .set("border-radius", "50%").set("background-color", "#E6F7F5")
            .set("display", "flex").set("align-items", "center").set("justify-content", "center")
            .set("margin-bottom", "24px").set("font-size", "2rem");
        iconWrapper.add(new Span("✅"));

        H3 rightTitle = new H3("Pekerjaan Selesai");
        rightTitle.addClassName("pt-detail-right-title");

        String diselesaikanText = laporan.getDiselesaikanPada() != null
                ? "Diselesaikan pada " + laporan.getDiselesaikanPada().format(FMT)
                : "Laporan penyelesaian telah dikirim ke Admin.";

        Paragraph rightDesc = new Paragraph("Anda telah berhasil menyelesaikan pekerjaan ini. " + diselesaikanText);
        rightDesc.addClassName("pt-detail-right-desc");

        // Foto bukti (if exists)
        if (laporan.getFotoBuktiUrl() != null && !laporan.getFotoBuktiUrl().isEmpty()) {
            Span buktiLabel = new Span("Foto Bukti Penyelesaian:");
            buktiLabel.getStyle().set("font-weight", "700").set("color", "#334155").set("font-size", "0.85rem").set("display", "block").set("margin-bottom", "8px");
            Image fotoBukti = new Image(laporan.getFotoBuktiUrl(), "Foto bukti");
            fotoBukti.getStyle().set("width", "100%").set("border-radius", "12px").set("margin-bottom", "16px");
            rightCard.add(iconWrapper, rightTitle, rightDesc, buktiLabel, fotoBukti);
        } else {
            rightCard.add(iconWrapper, rightTitle, rightDesc);
        }

        // Catatan Petugas
        if (laporan.getCatatan() != null && !laporan.getCatatan().isEmpty()) {
            Div notesBox = new Div();
            notesBox.addClassName("pt-completed-note-box");
            Span notesTitle = new Span("Catatan Petugas:");
            notesTitle.getStyle().set("font-weight", "800").set("color", "#334155").set("font-size", "0.85rem").set("display", "block").set("margin-bottom", "6px");
            Span notesText = new Span("\"" + laporan.getCatatan() + "\"");
            notesText.getStyle().set("color", "#475569").set("font-size", "0.88rem").set("font-style", "italic").set("line-height", "1.5");
            notesBox.add(notesTitle, notesText);
            rightCard.add(notesBox);
        }

        grid.add(leftCard, rightCard);
        body.add(grid);
        main.add(topbar, body);
        add(sidebar, main);
    }
}

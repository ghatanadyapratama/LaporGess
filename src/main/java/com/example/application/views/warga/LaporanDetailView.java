package com.example.application.views.warga;

import com.example.application.model.Laporan;
import com.example.application.repository.LaporanRepository;
import com.example.application.service.SessionManager;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Route(value = "laporan-detail/:laporanId", layout = BlankLayout.class)
@PageTitle("Detail Laporan - Lapor Gess")
public class LaporanDetailView extends Div implements BeforeEnterObserver {

    private final LaporanRepository laporanRepository;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");

    public LaporanDetailView(LaporanRepository laporanRepository) {
        this.laporanRepository = laporanRepository;
        addClassName("d-root");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!SessionManager.isLoggedIn()) {
            event.rerouteTo("login");
            return;
        }

        String idStr = event.getRouteParameters().get("laporanId").orElse(null);
        if (idStr == null) {
            event.rerouteTo("laporan-saya");
            return;
        }

        try {
            int laporanId = Integer.parseInt(idStr);
            Optional<Laporan> lapOpt = laporanRepository.findById(laporanId);
            if (lapOpt.isEmpty()) {
                event.rerouteTo("laporan-saya");
                return;
            }
            Laporan laporan = lapOpt.get();
            removeAll();
            add(buildSidebar(), buildMain(laporan));
        } catch (NumberFormatException e) {
            event.rerouteTo("laporan-saya");
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

        Div laporanItem = navItem("icons/laporan.png", "Laporan Saya", true);
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
    private Div buildMain(Laporan laporan) {
        Div main = new Div();
        main.addClassName("d-main");
        main.add(buildTopbar());
        main.add(buildBody(laporan));
        return main;
    }

    private Div buildTopbar() {
        Div bar = new Div();
        bar.addClassName("d-topbar");

        // Back button + title
        Div left = new Div();
        left.getStyle().set("display", "flex").set("align-items", "center").set("gap", "12px");

        Div backBtn = new Div();
        backBtn.getElement().setProperty("innerHTML",
            "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"20\" height=\"20\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\"><path d=\"M19 12H5\"/><path d=\"M12 19l-7-7 7-7\"/></svg>"
        );
        backBtn.getStyle().set("cursor", "pointer").set("display", "flex").set("align-items", "center")
            .set("color", "#64748B").set("padding", "4px");
        backBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("laporan-saya")));

        Span title = new Span("Detail Laporan");
        title.addClassName("d-topbar-title");
        left.add(backBtn, title);
        bar.add(left);

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

    private Div buildBody(Laporan laporan) {
        Div body = new Div();
        body.addClassNames("d-body", "ld-body");

        // Main detail card
        Div card = new Div();
        card.addClassName("ld-card");

        // ── Foto ────────────────────────────────
        if (laporan.getFotoUrl() != null && !laporan.getFotoUrl().isEmpty()) {
            Div imgWrapper = new Div();
            imgWrapper.addClassName("ld-img-wrapper");
            Image img = new Image(laporan.getFotoUrl(), "Foto laporan");
            img.addClassName("ld-img");
            imgWrapper.add(img);
            card.add(imgWrapper);
        }

        // ── Header ──────────────────────────────
        Div headerSection = new Div();
        headerSection.addClassName("ld-header-section");

        Div titleRow = new Div();
        titleRow.addClassName("ld-title-row");

        H2 judulH2 = new H2(laporan.getJudul());
        judulH2.addClassName("ld-judul");

        String statusText = laporan.getStatus().name();
        Span statusBadge = new Span(statusText);
        statusBadge.addClassNames("ld-status-badge", "ls-badge-" + statusText.toLowerCase());

        titleRow.add(judulH2, statusBadge);
        headerSection.add(titleRow);

        // Kode laporan
        if (laporan.getKodeLaporan() != null) {
            Span kodeSpan = new Span("Kode: " + laporan.getKodeLaporan());
            kodeSpan.addClassName("ld-kode");
            headerSection.add(kodeSpan);
        }

        card.add(headerSection);

        // ── Info Grid ───────────────────────────
        Div infoGrid = new Div();
        infoGrid.addClassName("ld-info-grid");

        infoGrid.add(infoItem("📍", "Lokasi", laporan.getLokasi() != null ? laporan.getLokasi() : "-"));
        infoGrid.add(infoItem("🏷", "Kategori", laporan.getKategori() != null ? laporan.getKategori() : "-"));
        infoGrid.add(infoItem("📅", "Tanggal Lapor", laporan.getDibuatPada().format(FMT)));
        if (laporan.getDiselesaikanPada() != null) {
            infoGrid.add(infoItem("✅", "Diselesaikan", laporan.getDiselesaikanPada().format(FMT)));
        }

        card.add(infoGrid);

        // ── Deskripsi ───────────────────────────
        if (laporan.getDeskripsi() != null && !laporan.getDeskripsi().isEmpty()) {
            Div descSection = new Div();
            descSection.addClassName("ld-desc-section");

            H3 descTitle = new H3("Deskripsi");
            descTitle.addClassName("ld-section-title");

            Paragraph descText = new Paragraph(laporan.getDeskripsi());
            descText.addClassName("ld-desc-text");

            descSection.add(descTitle, descText);
            card.add(descSection);
        }

        // ── Foto Bukti ──────────────────────────
        if (laporan.getFotoBuktiUrl() != null && !laporan.getFotoBuktiUrl().isEmpty()) {
            Div buktiSection = new Div();
            buktiSection.addClassName("ld-desc-section");

            H3 buktiTitle = new H3("Foto Bukti Penyelesaian");
            buktiTitle.addClassName("ld-section-title");

            Div buktiImgWrapper = new Div();
            buktiImgWrapper.addClassName("ld-bukti-wrapper");
            Image buktiImg = new Image(laporan.getFotoBuktiUrl(), "Foto Bukti");
            buktiImg.addClassName("ld-img");
            buktiImgWrapper.add(buktiImg);

            buktiSection.add(buktiTitle, buktiImgWrapper);
            card.add(buktiSection);
        }

        // ── Catatan / Alasan Tolak ──────────────
        if (laporan.getCatatanTolak() != null && !laporan.getCatatanTolak().isEmpty()) {
            Div catatanSection = new Div();
            catatanSection.addClassName("ld-catatan-section");

            H3 catatanTitle = new H3("Alasan Penolakan");
            catatanTitle.addClassName("ld-section-title");

            Paragraph catatanText = new Paragraph(laporan.getCatatanTolak());
            catatanText.addClassName("ld-desc-text");

            catatanSection.add(catatanTitle, catatanText);
            card.add(catatanSection);
        }

        body.add(card);
        return body;
    }

    private Div infoItem(String emoji, String label, String value) {
        Div item = new Div();
        item.addClassName("ld-info-item");

        Span icon = new Span(emoji);
        icon.addClassName("ld-info-icon");

        Div texts = new Div();
        texts.addClassName("ld-info-texts");

        Span lbl = new Span(label);
        lbl.addClassName("ld-info-label");

        Span val = new Span(value);
        val.addClassName("ld-info-value");

        texts.add(lbl, val);
        item.add(icon, texts);
        return item;
    }
}

package com.example.application.views.warga;

import com.example.application.model.Laporan;
import com.example.application.model.Pengguna;
import com.example.application.repository.PenggunaRepository;
import com.example.application.service.LaporanService;
import com.example.application.service.SessionManager;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "dashboard", layout = BlankLayout.class)
@PageTitle("Beranda - Lapor Gess")
public class WargaDashboardView extends Div implements BeforeEnterObserver {

    private final LaporanService laporanService;
    private final PenggunaRepository penggunaRepository;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

    public WargaDashboardView(LaporanService laporanService, PenggunaRepository penggunaRepository) {
        this.laporanService = laporanService;
        this.penggunaRepository = penggunaRepository;
        addClassName("d-root");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!SessionManager.isLoggedIn()) {
            event.rerouteTo("login");
            return;
        }
        removeAll();
        buildUI();
    }

    private void buildUI() {
        add(buildSidebar(), buildMain());
    }

    // ══════════════════════════════════════════
    //  SIDEBAR
    // ══════════════════════════════════════════
    private Div buildSidebar() {
        Div sidebar = new Div();
        sidebar.addClassName("d-sidebar");

        // Logo
        Div logo = new Div();
        logo.addClassName("d-logo");
        Image logoImg = new Image("icons/logoLaporGess.png", "logo");
        logoImg.addClassName("d-logo-img");
        Span logoTxt = new Span("Lapor Gess");
        logoTxt.addClassName("d-logo-txt");
        logo.add(logoImg, logoTxt);
        sidebar.add(logo);

        // Nav
        Div nav = new Div();
        nav.addClassName("d-nav");
        nav.add(navItem("icons/home.png",      "Beranda",     true, "dashboard"));
        nav.add(navItem("icons/laporan.png",   "Laporan Saya",false, "laporan-saya"));
        nav.add(navItem("icons/iconPiala.png", "Peringkat",   false, "peringkat"));
        nav.add(navItem("icons/hadiah.png", "Toko Hadiah", false, "toko-hadiah"));
        nav.add(navItem("icons/buku.png", "Edukasi", false, "edukasi"));
        nav.add(navItem("icons/profile.png", "Profil", false, "profil"));
        sidebar.add(nav);

        // Spacer
        Div sp = new Div();
        sp.addClassName("d-sidebar-spacer");
        sidebar.add(sp);

        // CTA button
        Div cta = new Div();
        cta.addClassName("d-cta");
        cta.add(new Span("+ Buat Laporan"));
        cta.addClickListener(e -> UI.getCurrent().navigate("buat-laporan"));
        sidebar.add(cta);

        return sidebar;
    }

    private Div navItem(String icon, String label, boolean active, String route) {
        Div item = new Div();
        item.addClassName("d-nav-item");
        if (active) item.addClassName("d-nav-active");
        Image img = new Image(icon, label);
        img.addClassName("d-nav-icon");
        Span txt = new Span(label);
        txt.addClassName("d-nav-label");
        item.add(img, txt);
        item.addClickListener(e -> UI.getCurrent().navigate(route));
        return item;
    }

    // ══════════════════════════════════════════
    //  MAIN CONTENT
    // ══════════════════════════════════════════
    private Div buildMain() {
        Div main = new Div();
        main.addClassName("d-main");
        
        Pengguna pengguna = penggunaRepository.findByUsername(SessionManager.getUsername()).orElse(null);
        String name = pengguna != null ? pengguna.getNamaLengkap() : SessionManager.getNama();
        List<Laporan> laporanList = laporanService.getLaporanByWarga(SessionManager.getUsername());

        int totalLaporan = laporanList.size();
        // Pakai poin dari DB, bukan hasil hitungan manual
        int totalPoin = pengguna != null && pengguna.getPoin() != null ? pengguna.getPoin() : 0;

        main.add(buildTopbar(name, totalPoin));
        main.add(buildBody(name, totalPoin, totalLaporan, laporanList));
        return main;
    }

    private Div buildTopbar(String name, int totalPoin) {
        Div bar = new Div();
        bar.addClassName("d-topbar");

        Span title = new Span("Beranda");
        title.addClassName("d-topbar-title");
        bar.add(title);

        Div right = new Div();
        right.addClassName("d-topbar-right");

        // Points badge
        Div badge = new Div();
        badge.addClassName("d-poin-badge");
        Image trophy = new Image("icons/pialaOren.png", "poin");
        trophy.addClassName("d-poin-icon");
        Span poinTxt = new Span(String.format("%,d Poin", totalPoin).replace(',', '.'));
        poinTxt.addClassName("d-poin-txt");
        badge.add(trophy, poinTxt);

        // Bell
        Div bell = new Div();
        bell.addClassName("d-bell");
        Image bellImg = new Image("icons/bell.png", "notif");
        bellImg.addClassName("d-bell-img");
        bell.add(bellImg);
        bell.addClickListener(e -> UI.getCurrent().navigate("notifikasi"));

        // Avatar
        Div av = new Div();
        av.addClassName("d-avatar");
        av.add(new Span(name != null && !name.isEmpty() ? name.substring(0, 1).toUpperCase() : "U"));

        right.add(badge, bell, av);
        bar.add(right);
        return bar;
    }

    private Div buildBody(String name, int totalPoin, int totalLaporan, List<Laporan> laporanList) {
        Div body = new Div();
        body.addClassName("d-body");
        body.add(buildWelcome(name, totalPoin, totalLaporan));
        body.add(buildLower(laporanList));
        return body;
    }

    // ── Welcome card ──────────────────────────
    private Div buildWelcome(String name, int totalPoin, int totalLaporan) {
        Div card = new Div();
        card.addClassName("d-welcome");

        Div left = new Div();
        left.addClassName("d-welcome-left");
        Span greeting = new Span("Selamat Datang Kembali, " + (name != null ? name : "Warga") + "!");
        greeting.addClassName("d-welcome-title");
        Span sub = new Span("Ayo bantu jaga lingkungan tetap bersih dan aman.");
        sub.addClassName("d-welcome-sub");
        left.add(greeting, sub);
        card.add(left);

        Div stats = new Div();
        stats.addClassName("d-stats");
        stats.add(statCard("icons/piala.png",   "d-icon-orange", "Total Poin", String.format("%,d", totalPoin).replace(',', '.')));
        stats.add(statCard("icons/ceklist.png", "d-icon-white",  "Total\nLaporan", String.valueOf(totalLaporan)));
        card.add(stats);

        return card;
    }

    private Div statCard(String iconSrc, String iconClass, String label, String value) {
        Div card = new Div();
        card.addClassName("d-stat");

        Div iconBox = new Div();
        iconBox.addClassName("d-stat-icon");
        iconBox.addClassName(iconClass);
        Image img = new Image(iconSrc, label);
        img.addClassName("d-stat-img");
        iconBox.add(img);
        card.add(iconBox);

        Div info = new Div();
        info.addClassName("d-stat-info");
        Span lbl = new Span(label);
        lbl.addClassName("d-stat-label");
        Span val = new Span(value);
        val.addClassName("d-stat-value");
        info.add(lbl, val);
        card.add(info);

        return card;
    }

    // ── Lower row ─────────────────────────────
    private Div buildLower(List<Laporan> laporanList) {
        Div lower = new Div();
        lower.addClassName("d-lower");
        lower.add(buildLaporan(laporanList));
        lower.add(buildTrash());
        return lower;
    }

    private Div buildLaporan(List<Laporan> laporanList) {
        Div section = new Div();
        section.addClassName("d-laporan");

        Div header = new Div();
        header.addClassName("d-sec-header");
        Span title = new Span("Laporan Terbaru Anda");
        title.addClassName("d-sec-title");
        Span lihat = new Span("Lihat Semua");
        lihat.addClassName("d-lihat");
        lihat.addClickListener(e -> UI.getCurrent().navigate("laporan-saya"));
        lihat.getStyle().set("cursor", "pointer");
        header.add(title, lihat);
        section.add(header);

        if (laporanList == null || laporanList.isEmpty()) {
            Div empty = new Div(new Span("Anda belum memiliki laporan."));
            empty.getStyle().set("color", "#94A3B8").set("padding", "20px");
            section.add(empty);
        } else {
            // Tampilkan max 3 laporan terbaru
            int count = Math.min(laporanList.size(), 3);
            for (int i = 0; i < count; i++) {
                Laporan lap = laporanList.get(i);
                section.add(laporanCard(lap));
            }
        }
        return section;
    }

    private Div laporanCard(Laporan laporan) {
        Div card = new Div();
        card.addClassName("d-lap-card");
        card.getStyle().set("cursor", "pointer");

        Div imgBox = new Div();
        imgBox.addClassName("ls-card-img-box");
        imgBox.getStyle().set("width", "70px").set("height", "70px").set("border-radius", "10px").set("flex-shrink", "0");
        if (laporan.getFotoUrl() != null && !laporan.getFotoUrl().isEmpty()) {
            Image image = new Image(laporan.getFotoUrl(), "Foto Laporan");
            image.addClassName("ls-card-img");
            imgBox.add(image);
        } else {
            Span noImg = new Span("🖼");
            noImg.getStyle().set("font-size", "1.5rem").set("opacity", "0.5");
            imgBox.add(noImg);
        }
        card.add(imgBox);

        Div content = new Div();
        content.addClassName("d-lap-content");

        Div topRow = new Div();
        topRow.addClassName("d-lap-top");
        Span judulSpan = new Span(laporan.getJudul());
        judulSpan.addClassName("d-lap-judul");

        String statusText = laporan.getStatus().name();
        String badgeCls = "ls-badge-" + statusText.toLowerCase();
        Span badge = new Span(statusText);
        badge.addClassName(badgeCls);

        topRow.add(judulSpan, badge);

        Div meta = new Div();
        meta.addClassName("d-lap-meta");
        Span lokasiSpan = new Span("📍 " + (laporan.getLokasi() != null ? laporan.getLokasi() : "-"));
        lokasiSpan.addClassName("d-lap-meta-row");
        Span tglSpan = new Span("🕐 Dilaporkan pada " + laporan.getDibuatPada().format(FMT));
        tglSpan.addClassName("d-lap-meta-row");
        meta.add(lokasiSpan, tglSpan);

        content.add(topRow, meta);
        card.add(content);

        // Click to view detail
        card.addClickListener(e -> UI.getCurrent().navigate("laporan-detail/" + laporan.getId()));

        return card;
    }

    private Div buildTrash() {
        Div section = new Div();
        section.addClassName("d-trash");

        Span title = new Span("Trash-Pedia 📚");
        title.addClassName("d-sec-title");
        section.add(title);

        Div article = new Div();
        article.addClassName("d-article");

        Span newBadge = new Span("Artikel Baru");
        newBadge.addClassName("d-article-badge");

        Span artTitle = new Span("Dasar Pemilahan Sampah");
        artTitle.addClassName("d-article-title");

        Span artSub = new Span("Pelajari cara memisahkan sampah organik dan anorganik dengan benar.");
        artSub.addClassName("d-article-sub");

        Div bacaBtn = new Div();
        bacaBtn.addClassName("d-baca-btn");
        bacaBtn.add(new Span("Baca Selengkapnya"));
        bacaBtn.addClickListener(e -> UI.getCurrent().navigate("edukasi"));

        article.add(newBadge, artTitle, artSub, bacaBtn);
        section.add(article);
        return section;
    }
}

package com.example.application.views;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "dashboard", layout = BlankLayout.class)
@PageTitle("Beranda - Lapor Gess")
public class WargaDashboardView extends Div {

    public WargaDashboardView() {
        addClassName("d-root");
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
        nav.add(navItem("icons/home.png",      "Beranda",     true));
        Div laporanNav = navItem("icons/laporan.png",   "Laporan Saya",false);
        laporanNav.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("laporan-saya")));
        nav.add(laporanNav);
        Div peringkatNav = navItem("icons/iconPiala.png", "Peringkat",   false);
        peringkatNav.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("peringkat")));
        nav.add(peringkatNav);
        Div hadiahNav = navItem("icons/hadiah.png", "Toko Hadiah", false);
        hadiahNav.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("toko-hadiah")));
        nav.add(hadiahNav);

        Div edukasiNav = navItem("icons/buku.png", "Edukasi", false);
        edukasiNav.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("edukasi")));
        nav.add(edukasiNav);

        Div profilNav = navItem("icons/profile.png", "Profil", false);
        profilNav.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("profil")));
        nav.add(profilNav);
        sidebar.add(nav);

        // Spacer
        Div sp = new Div();
        sp.addClassName("d-sidebar-spacer");
        sidebar.add(sp);

        // CTA button
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
        Span poinTxt = new Span("1.250 Poin");
        poinTxt.addClassName("d-poin-txt");
        badge.add(trophy, poinTxt);

        // Bell
        Div bell = new Div();
        bell.addClassName("d-bell");
        Image bellImg = new Image("icons/bell.png", "notif");
        bellImg.addClassName("d-bell-img");
        bell.add(bellImg);
        bell.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("notifikasi")));

        // Avatar
        Div av = new Div();
        av.addClassName("d-avatar");
        av.add(new Span("B"));

        right.add(badge, bell, av);
        bar.add(right);
        return bar;
    }

    private Div buildBody() {
        Div body = new Div();
        body.addClassName("d-body");
        body.add(buildWelcome());
        body.add(buildLower());
        return body;
    }

    // ── Welcome card ──────────────────────────
    private Div buildWelcome() {
        Div card = new Div();
        card.addClassName("d-welcome");

        Div left = new Div();
        left.addClassName("d-welcome-left");
        Span greeting = new Span("Selamat Datang Kembali, Budi!");
        greeting.addClassName("d-welcome-title");
        Span sub = new Span("Lingkungan RT 01 / RW 02 dalam keadaan aman hari ini.");
        sub.addClassName("d-welcome-sub");
        left.add(greeting, sub);
        card.add(left);

        Div stats = new Div();
        stats.addClassName("d-stats");
        stats.add(statCard("icons/piala.png",   "d-icon-orange", "Total Poin",      "1.250"));
        stats.add(statCard("icons/ceklist.png", "d-icon-white",  "Laporan\nSelesai","14"));
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
    private Div buildLower() {
        Div lower = new Div();
        lower.addClassName("d-lower");
        lower.add(buildLaporan());
        lower.add(buildTrash());
        return lower;
    }

    private Div buildLaporan() {
        Div section = new Div();
        section.addClassName("d-laporan");

        Div header = new Div();
        header.addClassName("d-sec-header");
        Span title = new Span("Laporan Terbaru Anda");
        title.addClassName("d-sec-title");
        Span lihat = new Span("Lihat Semua");
        lihat.addClassName("d-lihat");
        header.add(title, lihat);
        section.add(header);

        section.add(laporanCard("Pohon Tumbang di Jl. Utama", "Jl. Sudirman, RT 01/02", "2026-07-16", "Diproses", "d-badge-proses"));
        section.add(laporanCard("Lampu Jalan Mati", "Jl. Merdeka, RT 03/02", "2026-07-15", "Selesai", "d-badge-selesai"));
        return section;
    }

    private Div laporanCard(String judul, String lokasi, String tgl, String status, String badgeCls) {
        Div card = new Div();
        card.addClassName("d-lap-card");

        Div img = new Div();
        img.addClassName("d-lap-img");
        card.add(img);

        Div content = new Div();
        content.addClassName("d-lap-content");

        Div topRow = new Div();
        topRow.addClassName("d-lap-top");
        Span judulSpan = new Span(judul);
        judulSpan.addClassName("d-lap-judul");
        Span badge = new Span(status);
        badge.addClassName("d-badge");
        badge.addClassName(badgeCls);
        topRow.add(judulSpan, badge);

        Div meta = new Div();
        meta.addClassName("d-lap-meta");
        Span lokasiSpan = new Span("📍 " + lokasi);
        lokasiSpan.addClassName("d-lap-meta-row");
        Span tglSpan = new Span("🕐 Dilaporkan pada " + tgl);
        tglSpan.addClassName("d-lap-meta-row");
        meta.add(lokasiSpan, tglSpan);

        content.add(topRow, meta);
        card.add(content);
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
        bacaBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("edukasi")));

        article.add(newBadge, artTitle, artSub, bacaBtn);
        section.add(article);
        return section;
    }
}

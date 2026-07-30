package com.example.application.views;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "peringkat", layout = BlankLayout.class)
@PageTitle("Peringkat - Lapor Gess")
public class PeringkatView extends Div {

    public PeringkatView() {
        addClassName("d-root");
        add(buildSidebar(), buildMain());
    }

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
        Div laporanItem = navItem("icons/laporan.png",   "Laporan Saya", false);
        laporanItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("laporan-saya")));
        nav.add(laporanItem);
        nav.add(navItem("icons/iconPiala.png", "Peringkat", true));
        Div hadiahItemP = navItem("icons/hadiah.png", "Toko Hadiah", false);
        hadiahItemP.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("toko-hadiah")));
        nav.add(hadiahItemP);

        Div edukasiItemP = navItem("icons/buku.png", "Edukasi", false);
        edukasiItemP.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("edukasi")));
        nav.add(edukasiItemP);

        Div profilNavP = navItem("icons/profile.png", "Profil", false);
        profilNavP.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("profil")));
        nav.add(profilNavP);
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
        Span title = new Span("Peringkat");
        title.addClassName("d-topbar-title");
        bar.add(title);

        Div right = new Div();
        right.addClassName("d-topbar-right");

        Div badge = new Div();
        badge.addClassName("d-poin-badge");
        Image trophy = new Image("icons/pialaOren.png", "poin");
        trophy.addClassName("d-poin-icon");
        Span poinTxt = new Span("1.250 Poin");
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
        av.add(new Span("B"));

        right.add(badge, bell, av);
        bar.add(right);
        return bar;
    }

    private Div buildBody() {
        Div body = new Div();
        body.addClassName("d-body");

        // Hero banner
        Div hero = new Div();
        hero.addClassName("pr-hero");

        // Trophy SVG icon (matches reference design - orange outline cup)
        Div trophyIcon = new Div();
        trophyIcon.getElement().setProperty("innerHTML",
            "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"80\" height=\"80\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"#fff\" stroke-width=\"1.8\" stroke-linecap=\"round\" stroke-linejoin=\"round\">" +
            "<path d=\"M6 9H3.5a2.5 2.5 0 0 0 0 5H6\"/>" +
            "<path d=\"M18 9h2.5a2.5 2.5 0 0 1 0 5H18\"/>" +
            "<path d=\"M4 22h16\"/>" +
            "<path d=\"M10 14.66V17c0 .55-.47.98-.97 1.21C7.85 18.75 7 20.24 7 22\"/>" +
            "<path d=\"M14 14.66V17c0 .55.47.98.97 1.21C16.15 18.75 17 20.24 17 22\"/>" +
            "<path d=\"M18 2H6v7a6 6 0 0 0 12 0V2z\"/>" +
            "</svg>"
        );
        trophyIcon.getStyle().set("display", "flex").set("align-items", "center").set("justify-content", "center");

        H2 heroTitle = new H2("Pahlawan Lingkungan");
        heroTitle.addClassName("pr-hero-title");

        Paragraph heroSub = new Paragraph("Kumpulkan poin dari laporan yang diselesaikan dan jadi yang terdepan!");
        heroSub.addClassName("pr-hero-sub");

        hero.add(trophyIcon, heroTitle, heroSub);
        body.add(hero);

        // Podium section
        Div podiumSection = new Div();
        podiumSection.addClassName("pr-podium-section");

        Paragraph sectionTitle = new Paragraph("Top 3 Warga Terbaik");
        sectionTitle.addClassName("pr-section-title");
        podiumSection.add(sectionTitle);

        // Podium
        Div podium = new Div();
        podium.addClassName("pr-podium");

        // #2 (left)
        podium.add(buildPodiumCard("2", "Siti Aminah", "980 Poin", "SA", false));
        // #1 (center, elevated)
        podium.add(buildPodiumCard("1", "Budi Santoso", "1.250 Poin", "BS", true));
        // #3 (right)
        podium.add(buildPodiumCard("3", "Agus Pratama", "720 Poin", "AP", false));

        podiumSection.add(podium);
        body.add(podiumSection);

        // Leaderboard table
        Div tableSection = new Div();
        tableSection.addClassName("pr-table-section");

        Paragraph tableTitle = new Paragraph("Daftar Peringkat Lengkap");
        tableTitle.addClassName("pr-section-title");
        tableSection.add(tableTitle);

        Div table = new Div();
        table.addClassName("pr-table");

        // Header
        Div header = new Div();
        header.addClassName("pr-table-header");
        header.add(span("Rank", "pr-th"), span("Nama", "pr-th"), span("Poin", "pr-th"), span("Laporan", "pr-th"));
        table.add(header);

        // Rows
        table.add(tableRow("1", "🥇", "Budi Santoso",  "1.250", "18", true));
        table.add(tableRow("2", "🥈", "Siti Aminah",   "980",   "15", false));
        table.add(tableRow("3", "🥉", "Agus Pratama",  "720",   "12", false));
        table.add(tableRow("4", "4",  "Dewi Lestari",  "650",   "10", false));
        table.add(tableRow("5", "5",  "Eko Wijaya",    "580",   "9",  false));

        tableSection.add(table);
        body.add(tableSection);

        return body;
    }

    private Div buildPodiumCard(String rank, String name, String poin, String initials, boolean isFirst) {
        Div card = new Div();
        card.addClassName("pr-podium-card");
        if (isFirst) card.addClassName("pr-podium-first");

        Div av = new Div();
        av.addClassName("pr-podium-av");
        if (isFirst) av.addClassName("pr-podium-av-first");
        av.add(new Span(initials));

        Span rankBadge = new Span("#" + rank);
        rankBadge.addClassName("pr-podium-rank");
        if (isFirst) rankBadge.addClassName("pr-podium-rank-first");

        Span nameSpan = new Span(name);
        nameSpan.addClassName("pr-podium-name");

        Span poinSpan = new Span(poin);
        poinSpan.addClassName("pr-podium-poin");

        Div pedestal = new Div();
        pedestal.addClassName("pr-pedestal");
        if (isFirst) pedestal.addClassName("pr-pedestal-first");

        card.add(rankBadge, av, nameSpan, poinSpan, pedestal);
        return card;
    }

    private Div tableRow(String rank, String medal, String name, String poin, String laporan, boolean highlight) {
        Div row = new Div();
        row.addClassName("pr-table-row");
        if (highlight) row.addClassName("pr-table-row-hl");

        Div rankCell = new Div();
        rankCell.addClassName("pr-td");
        rankCell.add(new Span(medal));

        Div nameCell = new Div();
        nameCell.addClassName("pr-td");
        Div av = new Div();
        av.addClassName("pr-row-av");
        av.add(new Span(name.substring(0,1)));
        Span nameSpan = new Span(name);
        nameSpan.addClassName("pr-row-name");
        nameCell.add(av, nameSpan);

        Div poinCell = new Div();
        poinCell.addClassNames("pr-td", "pr-td-poin");
        poinCell.add(new Span(poin + " pts"));

        Div laporanCell = new Div();
        laporanCell.addClassName("pr-td");
        laporanCell.add(new Span(laporan + " laporan"));

        row.add(rankCell, nameCell, poinCell, laporanCell);
        return row;
    }

    private Span span(String text, String cls) {
        Span s = new Span(text);
        s.addClassName(cls);
        return s;
    }
}

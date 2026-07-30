package com.example.application.views;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "toko-hadiah", layout = BlankLayout.class)
@PageTitle("Toko Hadiah - Lapor Gess")
public class TokoHadiahView extends Div {

    public TokoHadiahView() {
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
        
        Div laporanItem = navItem("icons/laporan.png", "Laporan Saya", false);
        laporanItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("laporan-saya")));
        nav.add(laporanItem);
        
        Div peringkatItem = navItem("icons/iconPiala.png", "Peringkat", false);
        peringkatItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("peringkat")));
        nav.add(peringkatItem);
        
        Div hadiahItem = navItem("icons/hadiah.png", "Toko Hadiah", true);
        nav.add(hadiahItem);

        Div edukasiItemTH = navItem("icons/buku.png", "Edukasi", false);
        edukasiItemTH.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("edukasi")));
        nav.add(edukasiItemTH);

        Div profilNavTH = navItem("icons/profile.png", "Profil", false);
        profilNavTH.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("profil")));
        nav.add(profilNavTH);
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
        Span title = new Span("Toko Hadiah");
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
        body.addClassName("th-body");

        // Header Card
        Div headerCard = new Div();
        headerCard.addClassName("th-header-card");
        
        Div headerText = new Div();
        headerText.addClassName("th-header-text");
        H2 hTitle = new H2("Toko Hadiah");
        hTitle.addClassName("th-title");
        Paragraph hSub = new Paragraph("Tukarkan poin komunitas Anda dengan hadiah menarik dari mitra kami.");
        hSub.addClassName("th-sub");
        headerText.add(hTitle, hSub);
        
        Div balanceCard = new Div();
        balanceCard.addClassName("th-balance-card");
        Span bLabel = new Span("Saldo Poin Anda");
        bLabel.addClassName("th-balance-label");
        
        Div bPointsWrapper = new Div();
        bPointsWrapper.addClassName("th-balance-points-wrapper");
        
        Div trophySvg = new Div();
        trophySvg.getElement().setProperty("innerHTML",
            "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"28\" height=\"28\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"#e58e26\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\">" +
            "<path d=\"M6 9H3.5a2.5 2.5 0 0 0 0 5H6\"/>" +
            "<path d=\"M18 9h2.5a2.5 2.5 0 0 1 0 5H18\"/>" +
            "<path d=\"M4 22h16\"/>" +
            "<path d=\"M10 14.66V17c0 .55-.47.98-.97 1.21C7.85 18.75 7 20.24 7 22\"/>" +
            "<path d=\"M14 14.66V17c0 .55.47.98.97 1.21C16.15 18.75 17 20.24 17 22\"/>" +
            "<path d=\"M18 2H6v7a6 6 0 0 0 12 0V2z\"/>" +
            "</svg>"
        );
        trophySvg.addClassName("th-balance-icon");
        
        Span bPoints = new Span("1.250");
        bPoints.addClassName("th-balance-points");
        
        bPointsWrapper.add(trophySvg, bPoints);
        balanceCard.add(bLabel, bPointsWrapper);
        
        headerCard.add(headerText, balanceCard);
        body.add(headerCard);

        // Grid
        Div grid = new Div();
        grid.addClassName("th-grid");
        
        grid.add(buildRewardCard("icons/HadiahIjo.png", "Paket Sembako", "1000 poin"));
        grid.add(buildRewardCard("icons/HadiahOren.png", "Token Listrik Rp 50rb", "800 poin"));
        grid.add(buildRewardCard("icons/HadiahKuning.png", "Voucher Minimarket", "500 poin"));
        
        body.add(grid);

        return body;
    }
    
    private Div buildRewardCard(String iconPath, String title, String points) {
        Div card = new Div();
        card.addClassName("th-card");
        
        Div iconCircle = new Div();
        iconCircle.addClassName("th-card-icon-circle");
        Image icon = new Image(iconPath, title);
        icon.addClassName("th-card-icon");
        iconCircle.add(icon);
        
        Span titleSpan = new Span(title);
        titleSpan.addClassName("th-card-title");
        
        Span pointsSpan = new Span(points);
        pointsSpan.addClassName("th-card-points");
        
        NativeButton btn = new NativeButton("Tukar Sekarang");
        btn.addClassName("th-card-btn");
        
        card.add(iconCircle, titleSpan, pointsSpan, btn);
        return card;
    }
}

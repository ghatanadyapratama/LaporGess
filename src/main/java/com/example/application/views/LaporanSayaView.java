package com.example.application.views;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "laporan-saya", layout = BlankLayout.class)
@PageTitle("Laporan Saya - Lapor Gess")
public class LaporanSayaView extends Div {

    public LaporanSayaView() {
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
        nav.add(navItem("icons/laporan.png",   "Laporan Saya",  true));
        Div peringkatItem = navItem("icons/iconPiala.png", "Peringkat", false);
        peringkatItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("peringkat")));
        nav.add(peringkatItem);
        Div hadiahItemL = navItem("icons/hadiah.png", "Toko Hadiah", false);
        hadiahItemL.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("toko-hadiah")));
        nav.add(hadiahItemL);

        Div edukasiItemL = navItem("icons/buku.png", "Edukasi", false);
        edukasiItemL.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("edukasi")));
        nav.add(edukasiItemL);

        Div profilNavL = navItem("icons/profile.png", "Profil", false);
        profilNavL.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("profil")));
        nav.add(profilNavL);
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
        Span title = new Span("Laporan Saya");
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

        // Search & Filter
        Div filterBar = new Div();
        filterBar.addClassName("ls-filter-bar");

        TextField searchField = new TextField();
        searchField.setPlaceholder("Cari laporan Anda...");
        searchField.addClassName("ls-search");
        searchField.setWidthFull();

        Select<String> statusFilter = new Select<>();
        statusFilter.setItems("Semua Status", "Diproses", "Selesai", "Ditolak");
        statusFilter.setValue("Semua Status");
        statusFilter.addClassName("ls-filter-select");

        filterBar.add(searchField, statusFilter);
        body.add(filterBar);

        // Cards Grid
        Div grid = new Div();
        grid.addClassName("ls-grid");

        grid.add(reportCard("Pohon Tumbang di Jl. Utama", "Jl. Sudirman, RT 01/02", "Lingkungan", "2026-07-16", "Diproses", "d-badge-proses", null));
        grid.add(reportCard("Lampu Jalan Mati", "Jl. Merdeka, RT 03/02", "Infrastruktur", "2026-07-15", "Selesai", "d-badge-selesai", null));
        grid.add(reportCard("Pemilahan Sampah Organik", "Jl. Veteran, RT 02/01", "Lingkungan", "2026-07-12", "Selesai", "d-badge-selesai", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6d/Good_Food_Display_-_NCI_Visuals_Online.jpg/220px-Good_Food_Display_-_NCI_Visuals_Online.jpg"));

        body.add(grid);
        return body;
    }

    private Div reportCard(String judul, String lokasi, String kategori, String tgl, String status, String badgeCls, String imgUrl) {
        Div card = new Div();
        card.addClassName("ls-card");

        Div imgBox = new Div();
        imgBox.addClassName("ls-card-img");
        if (imgUrl != null) {
            Image img = new Image(imgUrl, judul);
            img.addClassName("ls-card-img-el");
            imgBox.add(img);
        }
        card.add(imgBox);

        Div info = new Div();
        info.addClassName("ls-card-info");

        Div topRow = new Div();
        topRow.addClassName("ls-card-top");
        Span judulSpan = new Span(judul);
        judulSpan.addClassName("ls-card-judul");
        topRow.add(judulSpan);

        Span lokasiSpan = new Span("📍 " + lokasi);
        lokasiSpan.addClassName("ls-card-lokasi");

        Div bottomRow = new Div();
        bottomRow.addClassName("ls-card-bottom");
        Span kat = new Span("Kategori: " + kategori);
        kat.addClassName("ls-card-kat");
        Span tglSpan = new Span(tgl);
        tglSpan.addClassName("ls-card-tgl");
        bottomRow.add(kat, tglSpan);

        info.add(topRow, lokasiSpan, bottomRow);
        card.add(info);

        return card;
    }
}

package com.example.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "buat-laporan", layout = BlankLayout.class)
@PageTitle("Buat Laporan - Lapor Gess")
public class BuatLaporanView extends Div {

    public BuatLaporanView() {
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
        
        Div homeItem = navItem("icons/home.png", "Beranda", false);
        homeItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("dashboard")));
        nav.add(homeItem);
        
        Div laporanItem = navItem("icons/laporan.png",   "Laporan Saya", false);
        laporanItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("laporan-saya")));
        nav.add(laporanItem);

        Div peringkatItem = navItem("icons/iconPiala.png", "Peringkat",   false);
        peringkatItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("peringkat")));
        nav.add(peringkatItem);

        Div hadiahItem = navItem("icons/hadiah.png",    "Toko Hadiah", false);
        hadiahItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("toko-hadiah")));
        nav.add(hadiahItem);

        Div edukasiItem = navItem("icons/buku.png",     "Edukasi",     false);
        edukasiItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("edukasi")));
        nav.add(edukasiItem);

        Div profilItem = navItem("icons/profile.png",   "Profil",      false);
        profilItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("profil")));
        nav.add(profilItem);

        sidebar.add(nav);

        // Spacer
        Div sp = new Div();
        sp.addClassName("d-sidebar-spacer");
        sidebar.add(sp);

        // CTA button (active/clicked state style is normal since we are on it)
        Div cta = new Div();
        cta.addClassName("d-cta");
        cta.add(new Span("+ Buat Laporan"));
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

        Span title = new Span("Laporan Baru");
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

        Div formCard = new Div();
        formCard.addClassName("d-form-card");

        H2 formTitle = new H2("Detail Laporan");
        formTitle.addClassName("d-form-title");
        formCard.add(formTitle);

        // Row 1: Kategori + Lokasi
        Div row1 = new Div();
        row1.addClassName("d-form-row");

        // Category Select
        Div catCol = new Div();
        catCol.addClassName("d-form-col");
        Span catLabel = new Span("Kategori Masalah");
        catLabel.addClassName("d-input-label");
        Select<String> categorySelect = new Select<>();
        categorySelect.setItems("Sampah", "Fasilitas Umum", "Keamanan", "Pohon Tumbang", "Jalan Rusak", "Lainnya");
        categorySelect.setPlaceholder("Pilih kategori");
        categorySelect.setWidthFull();
        categorySelect.addClassName("d-form-select");
        catCol.add(catLabel, categorySelect);

        // Specific Location
        Div locCol = new Div();
        locCol.addClassName("d-form-col");
        Span locLabel = new Span("Lokasi Spesifik");
        locLabel.addClassName("d-input-label");
        
        Div locInputWrapper = new Div();
        locInputWrapper.addClassName("d-loc-wrapper");
        TextField locField = new TextField();
        locField.setPlaceholder("Cth. Di depan Rumah no. 12");
        locField.setWidthFull();
        locField.addClassName("d-form-input");
        
        Button mapBtn = new Button();
        mapBtn.addClassName("d-map-btn");
        Image mapImg = new Image("icons/mapsIcon.png", "maps");
        mapImg.addClassName("d-map-icon");
        mapBtn.setIcon(mapImg);
        
        locInputWrapper.add(locField, mapBtn);
        locCol.add(locLabel, locInputWrapper);

        row1.add(catCol, locCol);
        formCard.add(row1);

        // Row 2: Full Description
        Div descCol = new Div();
        descCol.addClassName("d-form-col-full");
        Span descLabel = new Span("Deskripsi Lengkap");
        descLabel.addClassName("d-input-label");
        TextArea descArea = new TextArea();
        descArea.setPlaceholder("Jelaskan masalahnya secara detail untuk memudahkan petugas...");
        descArea.setWidthFull();
        descArea.addClassName("d-form-textarea");
        descCol.add(descLabel, descArea);
        formCard.add(descCol);

        // Row 3: Image Evidence
        Div photoCol = new Div();
        photoCol.addClassName("d-form-col-full");
        Span photoLabel = new Span("Bukti Foto");
        photoLabel.addClassName("d-input-label");
        
        Div uploadZone = new Div();
        uploadZone.addClassName("d-upload-zone");
        
        Image uploadImg = new Image("icons/imageIcon.png", "upload");
        uploadImg.addClassName("d-upload-img");
        
        Span uploadText1 = new Span("Klik untuk unggah atau seret foto ke sini");
        uploadText1.addClassName("d-upload-text-main");
        
        Span uploadText2 = new Span("Maksimal ukuran file: 5MB (JPG, PNG)");
        uploadText2.addClassName("d-upload-text-sub");
        
        uploadZone.add(uploadImg, uploadText1, uploadText2);
        photoCol.add(photoLabel, uploadZone);
        formCard.add(photoCol);

        // Footer Actions (Submit Button)
        Div footer = new Div();
        footer.addClassName("d-form-footer");
        Button submitBtn = new Button("Kirim Laporan");
        submitBtn.addClassName("d-submit-report-btn");
        footer.add(submitBtn);
        formCard.add(footer);

        body.add(formCard);
        return body;
    }
}

package com.example.application.views.warga;

import com.example.application.model.Hadiah;
import com.example.application.model.Pengguna;
import com.example.application.repository.HadiahRepository;
import com.example.application.repository.PenggunaRepository;
import com.example.application.service.SessionManager;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "toko-hadiah", layout = BlankLayout.class)
@PageTitle("Toko Hadiah - Lapor Gess")
public class TokoHadiahView extends Div {

    private final HadiahRepository hadiahRepository;
    private final PenggunaRepository penggunaRepository;
    private Pengguna currentUser;
    private Span bPoints;

    public TokoHadiahView(HadiahRepository hadiahRepository, PenggunaRepository penggunaRepository) {
        this.hadiahRepository = hadiahRepository;
        this.penggunaRepository = penggunaRepository;
        
        String username = SessionManager.getUsername();
        if (username != null) {
            currentUser = penggunaRepository.findByUsername(username).orElse(null);
        }

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
        
        int poin = currentUser != null && currentUser.getPoin() != null ? currentUser.getPoin() : 0;
        Span poinTxt = new Span(String.format("%,d Poin", poin));
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
        av.add(new Span(getInitials()));

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
        
        int userPoin = currentUser != null && currentUser.getPoin() != null ? currentUser.getPoin() : 0;
        bPoints = new Span(String.format("%,d", userPoin));
        bPoints.addClassName("th-balance-points");
        
        bPointsWrapper.add(trophySvg, bPoints);
        balanceCard.add(bLabel, bPointsWrapper);
        
        headerCard.add(headerText, balanceCard);
        body.add(headerCard);

        // Grid
        Div grid = new Div();
        grid.addClassName("th-grid");
        
        List<Hadiah> hadiahList = hadiahRepository.findAll();
        if (hadiahList.isEmpty()) {
            grid.add(new Span("Belum ada hadiah yang tersedia."));
        } else {
            for (Hadiah h : hadiahList) {
                grid.add(buildRewardCard(h));
            }
        }
        
        body.add(grid);

        return body;
    }
    
    private Div buildRewardCard(Hadiah hadiah) {
        Div card = new Div();
        card.addClassName("th-card");
        
        Div iconCircle = new Div();
        iconCircle.addClassName("th-card-icon-circle");
        
        // Use emoji as icon - clean and always visible
        Span emojiIcon = new Span("🎁");
        emojiIcon.getStyle().set("font-size", "2.2rem").set("line-height", "1");
        iconCircle.add(emojiIcon);
        
        Span titleSpan = new Span(hadiah.getNama());
        titleSpan.addClassName("th-card-title");
        
        Span pointsSpan = new Span(String.format("%,d poin", hadiah.getHargaPoin()));
        pointsSpan.addClassName("th-card-points");
        
        NativeButton btn = new NativeButton("Tukar Sekarang");
        btn.addClassName("th-card-btn");
        
        if (hadiah.getStok() <= 0) {
            btn.setText("Stok Habis");
            btn.getStyle().set("background", "#94A3B8").set("cursor", "not-allowed");
            btn.setEnabled(false);
        } else {
            btn.addClickListener(e -> openTukarDialog(hadiah));
        }
        
        card.add(iconCircle, titleSpan, pointsSpan, btn);
        return card;
    }

    private void openTukarDialog(Hadiah hadiah) {
        if (currentUser == null) return;
        
        int userPoin = currentUser.getPoin() != null ? currentUser.getPoin() : 0;
        if (userPoin < hadiah.getHargaPoin()) {
            Notification n = new Notification("Poin Anda tidak cukup untuk menukar hadiah ini.", 4000, Notification.Position.BOTTOM_CENTER);
            n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            n.open();
            return;
        }

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Konfirmasi Penukaran");
        dialog.setWidth("380px");

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        
        Paragraph msg = new Paragraph("Anda akan menukarkan poin untuk hadiah:");
        Paragraph namaHadiah = new Paragraph(hadiah.getNama());
        namaHadiah.getStyle().set("font-weight", "bold").set("font-size", "1.1rem").set("color", "#F97316");
        Paragraph hargaPoin = new Paragraph("Harga: " + hadiah.getHargaPoin() + " poin");
        
        layout.add(msg, namaHadiah, hargaPoin);
        dialog.add(layout);
        
        NativeButton cancelBtn = new NativeButton("Batal");
        cancelBtn.getStyle().set("padding", "8px 16px").set("border-radius", "8px").set("border", "none").set("cursor", "pointer").set("background", "#F1F5F9");
        cancelBtn.addClickListener(e -> dialog.close());

        NativeButton okBtn = new NativeButton("Tukar");
        okBtn.getStyle().set("padding", "8px 16px").set("border-radius", "8px").set("border", "none").set("cursor", "pointer").set("background", "#F97316").set("color", "white").set("font-weight", "bold");
        
        okBtn.addClickListener(e -> {
            currentUser.setPoin(userPoin - hadiah.getHargaPoin());
            penggunaRepository.save(currentUser);
            
            hadiah.setStok(hadiah.getStok() - 1);
            hadiahRepository.save(hadiah);
            
            SessionManager.login(currentUser.getUsername(), currentUser.getPeran().name(), currentUser.getNamaLengkap(), currentUser.getPoin());
            
            Notification n = new Notification("Penukaran berhasil! Silakan cek email Anda untuk detail pengiriman.", 5000, Notification.Position.BOTTOM_CENTER);
            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            n.open();
            
            dialog.close();
            UI.getCurrent().getPage().reload();
        });

        HorizontalLayout footer = new HorizontalLayout(cancelBtn, okBtn);
        footer.setJustifyContentMode(HorizontalLayout.JustifyContentMode.END);
        dialog.getFooter().add(footer);

        dialog.open();
    }

    private String getInitials() {
        if (currentUser == null || currentUser.getNamaLengkap() == null || currentUser.getNamaLengkap().isEmpty()) return "U";
        String[] parts = currentUser.getNamaLengkap().trim().split(" ");
        if (parts.length > 1) {
            return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
        }
        return currentUser.getNamaLengkap().substring(0, 1).toUpperCase();
    }
}

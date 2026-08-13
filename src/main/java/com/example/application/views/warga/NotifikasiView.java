package com.example.application.views.warga;

import com.example.application.model.Notifikasi;
import com.example.application.model.Pengguna;
import com.example.application.repository.NotifikasiRepository;
import com.example.application.repository.PenggunaRepository;
import com.example.application.service.SessionManager;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Route(value = "notifikasi", layout = BlankLayout.class)
@PageTitle("Notifikasi - Lapor Gess")
public class NotifikasiView extends Div {

    private final NotifikasiRepository notifikasiRepository;
    private final PenggunaRepository penggunaRepository;
    private Pengguna currentUser;
    private Div notifListContainer;
    private List<Notifikasi> notifList;

    public NotifikasiView(NotifikasiRepository notifikasiRepository, PenggunaRepository penggunaRepository) {
        this.notifikasiRepository = notifikasiRepository;
        this.penggunaRepository = penggunaRepository;

        String username = SessionManager.getUsername();
        if (username != null) {
            currentUser = penggunaRepository.findByUsername(username).orElse(null);
        }

        addClassName("d-root");
        add(buildSidebar(), buildMain());
        loadAndRender();
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

        Div laporanItem = navItem("icons/laporan.png", "Laporan Saya", false);
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
        Span title = new Span("Notifikasi");
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
        bell.getElement().getStyle().set("position", "relative");
        Image bellImg = new Image("icons/bell.png", "notif");
        bellImg.addClassName("d-bell-img");
        bell.add(bellImg);

        String initials = "U";
        if (currentUser != null && currentUser.getNamaLengkap() != null && !currentUser.getNamaLengkap().isEmpty()) {
            initials = currentUser.getNamaLengkap().substring(0, 1).toUpperCase();
        }
        Div av = new Div();
        av.addClassName("d-avatar");
        av.add(new Span(initials));

        right.add(badge, bell, av);
        bar.add(right);
        return bar;
    }

    private Div buildBody() {
        Div body = new Div();
        body.addClassNames("d-body", "nt-body");

        Div headerRow = new Div();
        headerRow.addClassName("nt-header-row");

        H2 subTitle = new H2("Notifikasi Anda");
        subTitle.addClassName("nt-subtitle");

        Span markAllRead = new Span("Tandai semua dibaca");
        markAllRead.addClassName("nt-mark-read");
        markAllRead.addClickListener(e -> markAllAsRead());

        headerRow.add(subTitle, markAllRead);
        body.add(headerRow);

        notifListContainer = new Div();
        notifListContainer.addClassName("nt-list-container");
        body.add(notifListContainer);

        return body;
    }

    private void loadAndRender() {
        notifListContainer.removeAll();

        if (currentUser == null) {
            notifListContainer.add(new Span("Silakan login untuk melihat notifikasi."));
            return;
        }

        notifList = notifikasiRepository.findByPenggunaOrderByDibuatPadaDesc(currentUser);

        if (notifList.isEmpty()) {
            Div empty = new Div(new Span("Belum ada notifikasi untuk Anda."));
            empty.getStyle().set("padding", "40px").set("text-align", "center").set("color", "#94A3B8");
            notifListContainer.add(empty);
            return;
        }

        for (int i = 0; i < notifList.size(); i++) {
            Notifikasi notif = notifList.get(i);
            Div card = new Div();
            card.addClassName("nt-card");
            if (i == notifList.size() - 1) card.addClassName("nt-card-last");

            // Unread indicator
            Div leftIndicator = new Div();
            leftIndicator.addClassName("nt-left-indicator");
            if (!notif.isDibaca()) {
                Div redDot = new Div();
                redDot.addClassName("nt-red-dot");
                leftIndicator.add(redDot);
            }
            card.add(leftIndicator);

            // Icon circle  
            Div iconCircle = new Div();
            iconCircle.addClassName("nt-icon-circle");
            String tipe = notif.getTipe();
            if ("SUCCESS".equals(tipe)) {
                iconCircle.addClassName("nt-circle-green");
            } else if ("WARNING".equals(tipe)) {
                iconCircle.addClassName("nt-circle-yellow");
            } else if ("ERROR".equals(tipe)) {
                iconCircle.addClassName("nt-circle-orange");
            } else {
                iconCircle.addClassName("nt-circle-grey");
            }
            String iconEmoji = "SUCCESS".equals(tipe) ? "✅" : "WARNING".equals(tipe) ? "⚠️" : "ℹ️";
            iconCircle.add(new Span(iconEmoji));
            card.add(iconCircle);

            // Content Block
            Div contentBlock = new Div();
            contentBlock.addClassName("nt-content-block");

            H3 itemTitle = new H3(tipe);
            itemTitle.addClassName("nt-card-title");

            Paragraph itemDesc = new Paragraph(notif.getPesan());
            itemDesc.addClassName("nt-card-desc");

            String timeAgo = formatTimeAgo(notif.getDibuatPada());
            Span itemTime = new Span(timeAgo);
            itemTime.addClassName("nt-card-time");

            contentBlock.add(itemTitle, itemDesc, itemTime);
            card.add(contentBlock);
            
            if (notif.getLaporanId() != null) {
                card.getStyle().set("cursor", "pointer");
                card.addClickListener(e -> {
                    notif.setDibaca(true);
                    notifikasiRepository.save(notif);
                    getUI().ifPresent(ui -> ui.navigate("laporan-detail/" + notif.getLaporanId()));
                });
            }
            
            notifListContainer.add(card);
        }
    }

    private void markAllAsRead() {
        if (notifList == null) return;
        notifList.forEach(n -> n.setDibaca(true));
        notifikasiRepository.saveAll(notifList);
        loadAndRender();
        Notification notif = new Notification("Semua notifikasi ditandai telah dibaca", 3000, Notification.Position.BOTTOM_CENTER);
        notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notif.open();
    }

    private String formatTimeAgo(LocalDateTime time) {
        if (time == null) return "Baru saja";
        Duration d = Duration.between(time, LocalDateTime.now());
        if (d.toMinutes() < 1) return "Baru saja";
        if (d.toMinutes() < 60) return d.toMinutes() + " menit yang lalu";
        if (d.toHours() < 24) return d.toHours() + " jam yang lalu";
        return d.toDays() + " hari yang lalu";
    }
}

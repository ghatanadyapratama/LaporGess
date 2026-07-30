package com.example.application.views;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.List;

@Route(value = "notifikasi", layout = BlankLayout.class)
@PageTitle("Notifikasi - Lapor Gess")
public class NotifikasiView extends Div {

    private Div notifListContainer;
    private List<NotifItem> notifItems = new ArrayList<>();

    public NotifikasiView() {
        addClassName("d-root");
        add(buildSidebar(), buildMain());
        
        // Load default mock notification data
        initData();
        renderNotifications();
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
        Span poinTxt = new Span("1.250 Poin");
        poinTxt.addClassName("d-poin-txt");
        badge.add(trophy, poinTxt);

        Div bell = new Div();
        bell.addClassName("d-bell");
        // Bell icon has notification dot in this view or active styling
        bell.getElement().getStyle().set("position", "relative");
        Image bellImg = new Image("icons/bell.png", "notif");
        bellImg.addClassName("d-bell-img");
        bell.add(bellImg);

        // Optional red dot indicator
        Div dot = new Div();
        dot.addClassName("d-bell-dot"); // We will style this in styles.css
        bell.add(dot);

        Div av = new Div();
        av.addClassName("d-avatar");
        av.add(new Span("B"));

        right.add(badge, bell, av);
        bar.add(right);
        return bar;
    }

    private Div buildBody() {
        Div body = new Div();
        body.addClassNames("d-body", "nt-body");

        // Header Row for "Notifikasi Anda" and "Tandai semua dibaca"
        Div headerRow = new Div();
        headerRow.addClassName("nt-header-row");

        H2 subTitle = new H2("Notifikasi Anda");
        subTitle.addClassName("nt-subtitle");

        Span markAllRead = new Span("Tandai semua dibaca");
        markAllRead.addClassName("nt-mark-read");
        markAllRead.addClickListener(e -> markAllAsRead());

        headerRow.add(subTitle, markAllRead);
        body.add(headerRow);

        // Notifications List Wrapper
        notifListContainer = new Div();
        notifListContainer.addClassName("nt-list-container");
        body.add(notifListContainer);

        return body;
    }

    private void initData() {
        notifItems.clear();
        
        // 1. Laporan Diselesaikan! (Unread)
        notifItems.add(new NotifItem(
            "icons/iconCeklist.png",
            "Laporan Diselesaikan!",
            "Laporan Anda mengenai \"Lampu Jalan Mati\" telah selesai ditangani oleh petugas lapangan.",
            "2 jam yang lalu",
            true
        ));

        // 2. Poin Bertambah (Unread)
        notifItems.add(new NotifItem(
            "icons/pialaPeeringkat.png",
            "Poin Bertambah",
            "Anda mendapatkan +100 poin dari partisipasi pelaporan fasilitas umum.",
            "2 jam yang lalu",
            true
        ));

        // 3. Laporan Diproses (Read)
        notifItems.add(new NotifItem(
            "icons/iconWaktu.png",
            "Laporan Diproses",
            "Laporan \"Pohon Tumbang\" Anda saat ini sedang diproses oleh petugas.",
            "1 hari yang lalu",
            false
        ));

        // 4. Akun Disetujui (Read)
        notifItems.add(new NotifItem(
            "icons/ceklistAbu.png",
            "Akun Disetujui",
            "Selamat! Pendaftaran akun Anda telah diverifikasi oleh Admin RT.",
            "3 hari yang lalu",
            false
        ));
    }

    private void renderNotifications() {
        notifListContainer.removeAll();

        for (int i = 0; i < notifItems.size(); i++) {
            NotifItem item = notifItems.get(i);
            Div card = new Div();
            card.addClassName("nt-card");
            if (i == notifItems.size() - 1) {
                card.addClassName("nt-card-last");
            }

            // Unread Dot layout
            Div leftIndicator = new Div();
            leftIndicator.addClassName("nt-left-indicator");
            if (item.isUnread()) {
                Div redDot = new Div();
                redDot.addClassName("nt-red-dot");
                leftIndicator.add(redDot);
            }
            card.add(leftIndicator);

            // Icon circle
            Div iconCircle = new Div();
            iconCircle.addClassName("nt-icon-circle");
            // Set background dynamically depending on type
            if (item.getTitle().contains("Diselesaikan")) {
                iconCircle.addClassName("nt-circle-green");
            } else if (item.getTitle().contains("Poin")) {
                iconCircle.addClassName("nt-circle-orange");
            } else if (item.getTitle().contains("Diproses")) {
                iconCircle.addClassName("nt-circle-yellow");
            } else {
                iconCircle.addClassName("nt-circle-grey");
            }

            Image img = new Image(item.getIconPath(), item.getTitle());
            img.addClassName("nt-card-icon");
            iconCircle.add(img);
            card.add(iconCircle);

            // Content Block
            Div contentBlock = new Div();
            contentBlock.addClassName("nt-content-block");

            H3 itemTitle = new H3(item.getTitle());
            itemTitle.addClassName("nt-card-title");

            Paragraph itemDesc = new Paragraph();
            itemDesc.addClassName("nt-card-desc");
            
            // Format bold text elements nicely
            String descText = item.getDescription();
            if (descText.contains("\"Lampu Jalan Mati\"")) {
                itemDesc.add("Laporan Anda mengenai ");
                Span boldSpan = new Span("\"Lampu Jalan Mati\"");
                boldSpan.getStyle().set("font-weight", "700");
                itemDesc.add(boldSpan);
                itemDesc.add(" telah selesai ditangani oleh petugas lapangan.");
            } else if (descText.contains("+100 poin")) {
                itemDesc.add("Anda mendapatkan ");
                Span boldSpan = new Span("+100 poin");
                boldSpan.getStyle().set("font-weight", "700");
                itemDesc.add(boldSpan);
                itemDesc.add(" dari partisipasi pelaporan fasilitas umum.");
            } else if (descText.contains("\"Pohon Tumbang\"")) {
                itemDesc.add("Laporan ");
                Span boldSpan = new Span("\"Pohon Tumbang\"");
                boldSpan.getStyle().set("font-weight", "700");
                itemDesc.add(boldSpan);
                itemDesc.add(" Anda saat ini sedang diproses oleh petugas.");
            } else {
                itemDesc.setText(descText);
            }

            Span itemTime = new Span(item.getTimeAgo());
            itemTime.addClassName("nt-card-time");

            contentBlock.add(itemTitle, itemDesc, itemTime);
            card.add(contentBlock);

            notifListContainer.add(card);
        }
    }

    private void markAllAsRead() {
        boolean updated = false;
        for (NotifItem item : notifItems) {
            if (item.isUnread()) {
                item.setUnread(false);
                updated = true;
            }
        }
        if (updated) {
            renderNotifications();
            Notification notif = new Notification("Semua notifikasi ditandai telah dibaca", 3000, Notification.Position.BOTTOM_CENTER);
            notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notif.open();
        }
    }

    // Helper model representation
    private static class NotifItem {
        private String iconPath;
        private String title;
        private String description;
        private String timeAgo;
        private boolean unread;

        public NotifItem(String iconPath, String title, String description, String timeAgo, boolean unread) {
            this.iconPath = iconPath;
            this.title = title;
            this.description = description;
            this.timeAgo = timeAgo;
            this.unread = unread;
        }

        public String getIconPath() { return iconPath; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getTimeAgo() { return timeAgo; }
        public boolean isUnread() { return unread; }
        public void setUnread(boolean unread) { this.unread = unread; }
    }
}

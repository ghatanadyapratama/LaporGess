package com.example.application.views.warga;

import com.example.application.model.Laporan;
import com.example.application.service.LaporanService;
import com.example.application.service.SessionManager;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "laporan-saya", layout = BlankLayout.class)
@PageTitle("Laporan Saya - Lapor Gess")
public class LaporanSayaView extends Div implements BeforeEnterObserver {

    private final LaporanService laporanService;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public LaporanSayaView(LaporanService laporanService) {
        this.laporanService = laporanService;
        addClassName("d-root");
        add(buildSidebar(), buildMain());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!SessionManager.isLoggedIn() || !SessionManager.isWarga()) {
            event.rerouteTo("login");
        }
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
        nav.add(navItem("icons/laporan.png", "Laporan Saya", true));
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

    private Div buildBody() {
        Div body = new Div();
        body.addClassName("d-body");

        // Search + Filter row (matches screenshot design)
        Div filterRow = new Div();
        filterRow.addClassName("ls-filter-row");

        TextField searchField = new TextField();
        searchField.setPlaceholder("Cari laporan Anda...");
        searchField.addClassName("ls-search-field");

        Select<String> filterSelect = new Select<>();
        filterSelect.setItems("Semua Status", "PENDING", "DIPROSES", "MENUNGGU_KONFIRMASI", "SELESAI", "DITOLAK");
        filterSelect.setValue("Semua Status");
        filterSelect.addClassName("ls-filter-select");

        filterRow.add(searchField, filterSelect);
        body.add(filterRow);

        // Laporan grid container
        Div listContainer = new Div();
        listContainer.addClassName("ls-list-container");

        String username = SessionManager.getUsername();
        List<Laporan> laporanList = laporanService.getLaporanByWarga(username);

        Runnable refresh = () -> {
            listContainer.removeAll();
            String query = searchField.getValue().trim().toLowerCase();
            String filter = filterSelect.getValue();
            laporanList.stream()
                .filter(l -> ("Semua Status".equals(filter) || l.getStatus().name().equals(filter))
                    && (query.isEmpty() || l.getJudul().toLowerCase().contains(query)
                        || (l.getLokasi() != null && l.getLokasi().toLowerCase().contains(query))))
                .forEach(l -> listContainer.add(buildLaporanCard(l)));
            if (listContainer.getChildren().count() == 0) {
                listContainer.add(buildEmptyState());
            }
        };

        searchField.addValueChangeListener(ev -> refresh.run());
        filterSelect.addValueChangeListener(ev -> refresh.run());

        if (laporanList.isEmpty()) {
            listContainer.add(buildEmptyState());
        } else {
            laporanList.forEach(l -> listContainer.add(buildLaporanCard(l)));
        }

        body.add(listContainer);
        return body;
    }

    private Div buildLaporanCard(Laporan laporan) {
        Div card = new Div();
        card.addClassName("ls-card");
        card.getStyle().set("cursor", "pointer");

        // Left side: image
        Div imgBox = new Div();
        imgBox.addClassName("ls-card-img-box");
        if (laporan.getFotoUrl() != null && !laporan.getFotoUrl().isEmpty()) {
            Image img = new Image(laporan.getFotoUrl(), "Foto laporan");
            img.addClassName("ls-card-img");
            imgBox.add(img);
        } else {
            Span noImg = new Span("📋");
            noImg.getStyle().set("font-size", "2rem");
            imgBox.add(noImg);
        }

        // Right side: info
        Div info = new Div();
        info.addClassName("ls-card-info");

        Div topRow = new Div();
        topRow.addClassName("ls-card-top-row");

        Span judulSpan = new Span(laporan.getJudul());
        judulSpan.addClassName("ls-card-judul");

        Span statusBadge = new Span(laporan.getStatus().name());
        statusBadge.addClassName("ls-badge-" + laporan.getStatus().name().toLowerCase());

        topRow.add(judulSpan, statusBadge);

        Div locRow = new Div();
        locRow.addClassName("ls-card-loc-row");
        Span locIcon = new Span("📍");
        Span locText = new Span(laporan.getLokasi() != null ? laporan.getLokasi() : "-");
        locText.getStyle().set("font-size", "0.85rem").set("color", "#64748B");
        locRow.add(locIcon, locText);

        Div metaRow = new Div();
        metaRow.addClassName("ls-card-meta");
        Span kategoriSpan = new Span("Kategori: " + laporan.getKategori());
        kategoriSpan.getStyle().set("font-size", "0.8rem").set("color", "#94A3B8");
        Span dateSpan = new Span(laporan.getDibuatPada().format(FMT));
        dateSpan.getStyle().set("font-size", "0.8rem").set("color", "#94A3B8");
        metaRow.add(kategoriSpan, dateSpan);

        info.add(topRow, locRow, metaRow);
        card.add(imgBox, info);

        // Click to view detail
        card.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("laporan-detail/" + laporan.getId())));

        return card;
    }

    private Div buildEmptyState() {
        Div empty = new Div();
        empty.getStyle().set("text-align", "center").set("padding", "60px 20px").set("color", "#94A3B8");
        Span icon = new Span("📋");
        icon.getStyle().set("font-size", "3rem").set("display", "block").set("margin-bottom", "12px");
        Span msg = new Span("Belum ada laporan. Buat laporan pertama Anda!");
        msg.getStyle().set("font-weight", "600");
        empty.add(icon, msg);
        return empty;
    }
}

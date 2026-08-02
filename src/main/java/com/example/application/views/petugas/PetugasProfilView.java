package com.example.application.views.petugas;

import com.example.application.model.Pengguna;
import com.example.application.repository.PenggunaRepository;
import com.example.application.service.LaporanService;
import com.example.application.service.SessionManager;
import com.example.application.views.warga.BlankLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "petugas/profil", layout = BlankLayout.class)
@PageTitle("Profil Saya - Petugas LaporGess")
public class PetugasProfilView extends Div implements BeforeEnterObserver {

    private final PenggunaRepository penggunaRepository;
    private final LaporanService laporanService;
    private Pengguna currentUser;

    public PetugasProfilView(PenggunaRepository penggunaRepository, LaporanService laporanService) {
        this.penggunaRepository = penggunaRepository;
        this.laporanService = laporanService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!SessionManager.isLoggedIn() || !SessionManager.isPetugas()) {
            event.rerouteTo("login");
            return;
        }
        String username = SessionManager.getUsername();
        if (username != null) {
            currentUser = penggunaRepository.findByUsername(username).orElse(null);
        }
        buildUI();
    }

    private void buildUI() {
        removeAll();
        addClassName("pt-root");

        Div sidebar = PetugasLayout.buildSidebar("petugas/profil");
        Div main = new Div();
        main.addClassName("pt-main");
        Div topbar = PetugasLayout.buildTopbar("Profil Anda");
        Div body = new Div();
        body.addClassName("pt-body");

        // --- Profile Header Card ---
        Div headerCard = new Div();
        headerCard.addClassName("pt-profile-header-card");

        Div banner = new Div();
        banner.addClassName("pt-profile-banner");
        headerCard.add(banner);

        Div infoRow = new Div();
        infoRow.addClassName("pt-profile-info-row");

        Div textGroup = new Div();
        textGroup.addClassName("pt-profile-text-group");

        String name = currentUser != null ? currentUser.getNamaLengkap() : "Petugas";
        H2 nameText = new H2(name);
        nameText.addClassName("pt-profile-display-name");
        nameText.getStyle().set("margin", "0");

        Div locRow = new Div();
        locRow.addClassName("pt-profile-display-loc");
        Span locIcon = new Span("📍");
        String area = (currentUser != null && currentUser.getRtRw() != null) ? currentUser.getRtRw() : "Petugas Lapangan";
        Span locText = new Span(area);
        locRow.add(locIcon, locText);

        textGroup.add(nameText, locRow);

        Button btnEdit = new Button("Edit Profil");
        btnEdit.addClassName("pt-profile-edit-btn");
        btnEdit.addClickListener(e -> {
            Notification n = new Notification("Untuk mengubah profil, hubungi Admin.", 3000, Notification.Position.BOTTOM_CENTER);
            n.open();
        });

        infoRow.add(textGroup, btnEdit);
        headerCard.add(infoRow);
        body.add(headerCard);

        // --- Grid Layout (Pengaturan & Statistik) ---
        Div grid = new Div();
        grid.getStyle()
            .set("display", "grid")
            .set("grid-template-columns", "1fr 1.5fr")
            .set("gap", "24px")
            .set("width", "100%");

        // Column Left: Pengaturan
        Div settingsCard = new Div();
        settingsCard.addClassName("pt-settings-card");

        H3 settingsTitle = new H3("Pengaturan");
        settingsTitle.addClassName("pt-settings-title");
        settingsCard.add(settingsTitle);

        Div rowAccount = new Div();
        rowAccount.addClassName("pt-settings-row");
        Div accountLeft = new Div();
        accountLeft.addClassName("pt-settings-left");
        Span iconAccount = new Span("⚙️");
        iconAccount.addClassName("pt-settings-icon");
        Span labelAccount = new Span("Akun & Sandi");
        accountLeft.add(iconAccount, labelAccount);
        Span arrowAccount = new Span(">");
        rowAccount.add(accountLeft, arrowAccount);
        rowAccount.addClickListener(e -> UI.getCurrent().navigate("petugas/akun-sandi"));

        Div rowNotif = new Div();
        rowNotif.addClassName("pt-settings-row");
        Div notifLeft = new Div();
        notifLeft.addClassName("pt-settings-left");
        Span iconNotif = new Span("🔔");
        iconNotif.addClassName("pt-settings-icon");
        Span labelNotif = new Span("Notifikasi");
        notifLeft.add(iconNotif, labelNotif);
        Span arrowNotif = new Span(">");
        rowNotif.add(notifLeft, arrowNotif);
        rowNotif.addClickListener(e -> UI.getCurrent().navigate("petugas/notifikasi"));

        // Logout row
        Div rowLogout = new Div();
        rowLogout.addClassName("pt-settings-row");
        rowLogout.getStyle().set("color", "#EF4444");
        Span iconLogout = new Span("🚪");
        Span labelLogout = new Span("Keluar");
        rowLogout.add(iconLogout, labelLogout);
        rowLogout.addClickListener(e -> {
            SessionManager.logout();
            UI.getCurrent().navigate("login");
        });

        settingsCard.add(rowAccount, rowNotif, rowLogout);
        grid.add(settingsCard);

        // Column Right: Statistik Kinerja
        Div statsCard = new Div();
        statsCard.addClassName("pt-stats-card");

        H3 statsTitle = new H3("Statistik Kinerja");
        statsTitle.addClassName("pt-stats-title");
        statsCard.add(statsTitle);

        Div statsRow = new Div();
        statsRow.addClassName("pt-stats-row");

        // Total Selesai from DB
        int totalSelesai = currentUser != null ? laporanService.getLaporanSelesaiByPetugas(currentUser.getUsername()).size() : 0;
        int totalLaporan = currentUser != null ? laporanService.getLaporanByPetugas(currentUser.getUsername()).size() : 0;

        // Box 1: Tugas Selesai (Teal)
        Div boxTeal = new Div();
        boxTeal.addClassName("pt-stat-box");
        boxTeal.addClassName("pt-stat-box-teal");
        Div iconWrapperTeal = new Div();
        iconWrapperTeal.addClassName("pt-stat-box-icon-wrapper");
        Image checkImg = new Image("icons/ceklist.png", "Completed");
        checkImg.getStyle().set("width", "18px").set("height", "18px");
        iconWrapperTeal.add(checkImg);
        Div infoTeal = new Div();
        infoTeal.addClassName("pt-stat-box-info");
        Span labelTeal = new Span("Tugas Selesai");
        labelTeal.addClassName("pt-stat-box-label");
        Span valTeal = new Span(String.valueOf(totalSelesai));
        valTeal.addClassName("pt-stat-box-value");
        infoTeal.add(labelTeal, valTeal);
        boxTeal.add(iconWrapperTeal, infoTeal);

        // Box 2: Total Ditangani (Orange)
        Div boxOrange = new Div();
        boxOrange.addClassName("pt-stat-box");
        boxOrange.addClassName("pt-stat-box-orange");
        Div iconWrapperOrange = new Div();
        iconWrapperOrange.addClassName("pt-stat-box-icon-wrapper");
        Image clockImg = new Image("icons/jam.png", "Active");
        clockImg.getStyle().set("width", "18px").set("height", "18px");
        iconWrapperOrange.add(clockImg);
        Div infoOrange = new Div();
        infoOrange.addClassName("pt-stat-box-info");
        Span labelOrange = new Span("Total Ditangani");
        labelOrange.addClassName("pt-stat-box-label");
        Span valOrange = new Span(String.valueOf(totalLaporan));
        valOrange.addClassName("pt-stat-box-value");
        infoOrange.add(labelOrange, valOrange);
        boxOrange.add(iconWrapperOrange, infoOrange);

        statsRow.add(boxTeal, boxOrange);
        statsCard.add(statsRow);
        grid.add(statsCard);

        body.add(grid);
        main.add(topbar, body);
        add(sidebar, main);
    }
}

package com.example.application.views.warga;

import com.example.application.model.Pengguna;
import com.example.application.repository.PenggunaRepository;
import com.example.application.service.SessionManager;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "akun-sandi", layout = BlankLayout.class)
@PageTitle("Akun & Sandi - Lapor Gess")
public class AkunSandiView extends Div {

    private final PenggunaRepository penggunaRepository;
    private Pengguna currentUser;

    public AkunSandiView(PenggunaRepository penggunaRepository) {
        this.penggunaRepository = penggunaRepository;
        String username = SessionManager.getUsername();
        if (username != null) {
            currentUser = penggunaRepository.findByUsername(username).orElse(null);
        }

        addClassName("d-root");
        add(buildSidebar(), buildMain());
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

        Div profilItem = navItem("icons/profile.png", "Profil", true); // Highlight Profil as parent category
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
        Span title = new Span("Akun & Sandi");
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
        body.addClassNames("d-body", "as-body");

        // Main Settings Card container
        Div formCard = new Div();
        formCard.addClassName("as-card");

        H2 title = new H2("Ubah Kata Sandi");
        title.addClassName("as-card-title");
        Paragraph subtitle = new Paragraph("Pastikan kata sandi baru Anda menggunakan kombinasi karakter yang kuat agar tetap aman.");
        subtitle.addClassName("as-card-subtitle");
        formCard.add(title, subtitle);

        // Fields Container
        Div fieldsContainer = new Div();
        fieldsContainer.addClassName("as-fields-container");

        // Username (Read-only styled box)
        String usernameStr = currentUser != null ? currentUser.getUsername() : "";
        fieldsContainer.add(buildFieldGroup("Nama Pengguna (Username)", buildReadOnlyField(usernameStr)));

        // Current Password Field
        PasswordField currentPwd = new PasswordField();
        currentPwd.setPlaceholder("Masukkan kata sandi saat ini");
        currentPwd.setWidthFull();
        currentPwd.addClassName("as-input");
        fieldsContainer.add(buildFieldGroup("Kata Sandi Saat Ini", currentPwd));

        // New Password Field
        PasswordField newPwd = new PasswordField();
        newPwd.setPlaceholder("Masukkan kata sandi baru");
        newPwd.setWidthFull();
        newPwd.addClassName("as-input");
        fieldsContainer.add(buildFieldGroup("Kata Sandi Baru", newPwd));

        // Confirm Password Field
        PasswordField confirmPwd = new PasswordField();
        confirmPwd.setPlaceholder("Konfirmasi kata sandi baru");
        confirmPwd.setWidthFull();
        confirmPwd.addClassName("as-input");
        fieldsContainer.add(buildFieldGroup("Konfirmasi Kata Sandi Baru", confirmPwd));

        formCard.add(fieldsContainer);

        // Actions Footer Row
        Div footer = new Div();
        footer.addClassName("as-footer");

        NativeButton cancelBtn = new NativeButton("Batal");
        cancelBtn.addClassName("as-cancel-btn");
        cancelBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("profil")));

        NativeButton saveBtn = new NativeButton("Simpan Sandi");
        saveBtn.addClassName("as-save-btn");
        saveBtn.addClickListener(e -> {
            String currVal = currentPwd.getValue();
            String newVal = newPwd.getValue();
            String confVal = confirmPwd.getValue();

            if (currVal.isEmpty() || newVal.isEmpty() || confVal.isEmpty()) {
                Notification notif = new Notification("Semua field kata sandi harus diisi!", 3000, Notification.Position.BOTTOM_CENTER);
                notif.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notif.open();
                return;
            }
            
            if (currentUser != null && !currVal.equals(currentUser.getKataSandi())) {
                Notification notif = new Notification("Kata sandi saat ini salah!", 3000, Notification.Position.BOTTOM_CENTER);
                notif.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notif.open();
                return;
            }

            if (!newVal.equals(confVal)) {
                Notification notif = new Notification("Konfirmasi kata sandi baru tidak cocok!", 3000, Notification.Position.BOTTOM_CENTER);
                notif.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notif.open();
                return;
            }

            if (currentUser != null) {
                currentUser.setKataSandi(newVal);
                penggunaRepository.save(currentUser);
            }

            // Success saving
            Notification notif = new Notification("Kata sandi berhasil diperbarui!", 3000, Notification.Position.BOTTOM_CENTER);
            notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notif.open();

            // Clear inputs
            currentPwd.clear();
            newPwd.clear();
            confirmPwd.clear();

            // Redirect back to profile page
            getUI().ifPresent(ui -> ui.navigate("profil"));
        });

        footer.add(cancelBtn, saveBtn);
        formCard.add(footer);

        body.add(formCard);
        return body;
    }

    private Div buildFieldGroup(String label, com.vaadin.flow.component.Component input) {
        Div group = new Div();
        group.addClassName("as-field-group");

        Span lbl = new Span(label);
        lbl.addClassName("as-field-label");
        group.add(lbl, input);

        return group;
    }

    private Div buildReadOnlyField(String value) {
        Div field = new Div();
        field.addClassName("as-readonly-field");
        Span txt = new Span(value);
        txt.addClassName("as-readonly-text");
        field.add(txt);
        return field;
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

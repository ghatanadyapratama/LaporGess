package com.example.application.views.petugas;

import com.example.application.model.Pengguna;
import com.example.application.repository.PenggunaRepository;
import com.example.application.service.SessionManager;
import com.example.application.views.warga.BlankLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "petugas/akun-sandi", layout = BlankLayout.class)
@PageTitle("Akun & Sandi - Petugas LaporGess")
public class PetugasAkunSandiView extends Div {

    private final PenggunaRepository penggunaRepository;

    public PetugasAkunSandiView(PenggunaRepository penggunaRepository) {
        this.penggunaRepository = penggunaRepository;
        addClassName("pt-root");

        // 1. Sidebar (active is Profil)
        Div sidebar = PetugasLayout.buildSidebar("petugas/profil");

        // 2. Main content container
        Div main = new Div();
        main.addClassName("pt-main");

        // 3. Topbar
        Div topbar = PetugasLayout.buildTopbar("Akun & Sandi");

        // 4. Body Content
        Div body = new Div();
        body.addClassName("pt-body");

        // Back link (← Kembali ke Profil)
        Div backBtn = new Div();
        backBtn.addClassName("pt-detail-back-btn");
        Span backArrow = new Span("←");
        Span backText = new Span("Kembali ke Profil");
        backBtn.add(backArrow, backText);
        backBtn.addClickListener(e -> UI.getCurrent().navigate("petugas/profil"));
        body.add(backBtn);

        // Main Settings Card
        Div formCard = new Div();
        formCard.addClassName("as-card");
        formCard.getStyle().set("max-width", "600px").set("background-color", "#FFFFFF").set("border-radius", "24px").set("padding", "32px").set("border", "1px solid #E2E8F0");

        H2 title = new H2("Ubah Kata Sandi");
        title.addClassName("as-card-title");
        title.getStyle().set("margin", "0 0 8px 0").set("font-size", "1.35rem").set("font-weight", "800").set("color", "#1E293B");

        Paragraph subtitle = new Paragraph("Pastikan kata sandi baru Anda menggunakan kombinasi karakter yang kuat agar tetap aman.");
        subtitle.addClassName("as-card-subtitle");
        subtitle.getStyle().set("margin", "0 0 28px 0").set("color", "#64748B").set("font-size", "0.92rem");
        formCard.add(title, subtitle);

        // Fields Container
        Div fieldsContainer = new Div();
        fieldsContainer.getStyle().set("display", "flex").set("flex-direction", "column").set("gap", "20px").set("margin-bottom", "28px");

        // Username (Read-only box)
        String currentUsername = SessionManager.getUsername() != null ? SessionManager.getUsername() : "petugas";
        fieldsContainer.add(buildFieldGroup("Nama Pengguna (Username)", buildReadOnlyField(currentUsername)));

        // Current Password Field
        PasswordField currentPwd = new PasswordField();
        currentPwd.setPlaceholder("Masukkan kata sandi saat ini");
        currentPwd.setWidthFull();
        currentPwd.addClassName("user-dialog-input");
        fieldsContainer.add(buildFieldGroup("Kata Sandi Saat Ini", currentPwd));

        // New Password Field
        PasswordField newPwd = new PasswordField();
        newPwd.setPlaceholder("Masukkan kata sandi baru");
        newPwd.setWidthFull();
        newPwd.addClassName("user-dialog-input");
        fieldsContainer.add(buildFieldGroup("Kata Sandi Baru", newPwd));

        // Confirm Password Field
        PasswordField confirmPwd = new PasswordField();
        confirmPwd.setPlaceholder("Konfirmasi kata sandi baru");
        confirmPwd.setWidthFull();
        confirmPwd.addClassName("user-dialog-input");
        fieldsContainer.add(buildFieldGroup("Konfirmasi Kata Sandi Baru", confirmPwd));

        formCard.add(fieldsContainer);

        // Actions Footer Row
        Div footer = new Div();
        footer.addClassName("user-dialog-footer");

        Button cancelBtn = new Button("Batal");
        cancelBtn.addClassName("user-dialog-cancel-btn");
        cancelBtn.addClickListener(e -> UI.getCurrent().navigate("petugas/profil"));

        Button saveBtn = new Button("Simpan Sandi");
        saveBtn.addClassName("user-dialog-save-btn");
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

            // Retrieve current user DB password
            String lookupUsername = SessionManager.getUsername() != null ? SessionManager.getUsername() : "petugas";
            Pengguna p = penggunaRepository.findByUsername(lookupUsername).orElse(null);
            if (p == null) {
                Notification notif = new Notification("Data pengguna tidak ditemukan!", 3000, Notification.Position.BOTTOM_CENTER);
                notif.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notif.open();
                return;
            }

            if (!p.getKataSandi().equals(currVal)) {
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

            // Save new password to DB
            p.setKataSandi(newVal);
            penggunaRepository.save(p);

            Notification notif = new Notification("Kata sandi berhasil diperbarui!", 3000, Notification.Position.BOTTOM_CENTER);
            notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notif.open();

            // Clear inputs and navigate
            currentPwd.clear();
            newPwd.clear();
            confirmPwd.clear();
            UI.getCurrent().navigate("petugas/profil");
        });

        footer.add(cancelBtn, saveBtn);
        formCard.add(footer);
        body.add(formCard);

        main.add(topbar, body);
        add(sidebar, main);
    }

    private Div buildFieldGroup(String label, com.vaadin.flow.component.Component input) {
        Div group = new Div();
        group.getStyle().set("display", "flex").set("flex-direction", "column").set("gap", "6px");

        Span lbl = new Span(label);
        lbl.getStyle().set("font-size", "0.88rem").set("font-weight", "700").set("color", "#334155");
        group.add(lbl, input);

        return group;
    }

    private Div buildReadOnlyField(String value) {
        Div field = new Div();
        field.getStyle()
            .set("background-color", "#F1F5F9")
            .set("border", "1px solid #E2E8F0")
            .set("border-radius", "12px")
            .set("padding", "12px 16px");
        Span txt = new Span(value);
        txt.getStyle().set("color", "#64748B").set("font-weight", "600").set("font-size", "0.95rem");
        field.add(txt);
        return field;
    }
}

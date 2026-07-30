package com.example.application.views;

import com.example.application.model.Pengguna;
import com.example.application.service.PenggunaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route(value = "register", layout = BlankLayout.class)
@PageTitle("Daftar - Lapor Gess")
public class RegisterView extends HorizontalLayout {

    private final PenggunaService penggunaService;

    public RegisterView(PenggunaService penggunaService) {
        this.penggunaService = penggunaService;

        setSizeFull();
        setPadding(false);
        setSpacing(false);
        addClassName("login-split-view");

        // ── Left Panel ──────────────────────────────────────────────────────────
        VerticalLayout leftPanel = new VerticalLayout();
        leftPanel.addClassName("login-split-left");
        leftPanel.setAlignItems(Alignment.CENTER);
        leftPanel.setJustifyContentMode(JustifyContentMode.CENTER);

        Image leafIcon = new Image("icons/logoLaporGess.png", "LaporGess icon");
        leafIcon.addClassName("login-split-icon");

        H1 leftTitle = new H1("Bergabung dengan\nKomunitas");
        leftTitle.addClassName("login-split-title");

        Paragraph leftSubtitle = new Paragraph(
                "Lapor Gess memudahkan Anda untuk melaporkan masalah lingkungan dan fasilitas umum di RT/RW Anda.");
        leftSubtitle.addClassName("login-split-subtitle");

        leftPanel.add(leafIcon, leftTitle, leftSubtitle);
        leftPanel.setWidth("45%");
        leftPanel.setHeightFull();

        // ── Right Panel (scrollable) ─────────────────────────────────────────────
        VerticalLayout rightPanel = new VerticalLayout();
        rightPanel.addClassNames("login-split-right", "register-right-scroll");
        rightPanel.setAlignItems(Alignment.CENTER);
        rightPanel.setJustifyContentMode(JustifyContentMode.START);
        rightPanel.setWidth("55%");
        rightPanel.setHeightFull();
        rightPanel.getStyle().set("overflow-y", "auto");
        rightPanel.setPadding(true);

        VerticalLayout registerCard = new VerticalLayout();
        registerCard.addClassNames("login-split-card", "register-card-wide");
        registerCard.setWidth("520px");

        H2 welcomeTitle = new H2("Buat Akun Baru");
        welcomeTitle.addClassName("login-split-welcome");

        Paragraph welcomeSubtitle = new Paragraph("Silakan lengkapi data Anda di bawah ini.");
        welcomeSubtitle.addClassName("login-split-welcome-subtitle");

        // ── Nama Pengguna ────────────────────────────────────────────────────────
        TextField username = new TextField("Nama Pengguna");
        username.setPlaceholder("Masukkan nama pengguna");
        username.setWidthFull();
        username.addClassName("login-split-input");

        // ── Baris: Tanggal Lahir & NIK ───────────────────────────────────────────
        DatePicker tanggalLahir = new DatePicker("Tanggal Lahir");
        tanggalLahir.setPlaceholder("DD/MM/YYYY");
        tanggalLahir.setWidthFull();
        tanggalLahir.addClassName("login-split-input");

        TextField nik = new TextField("NIK");
        nik.setPlaceholder("16 digit NIK");
        nik.setWidthFull();
        nik.addClassName("login-split-input");

        HorizontalLayout tglNikRow = new HorizontalLayout(tanggalLahir, nik);
        tglNikRow.setWidthFull();
        tglNikRow.setSpacing(true);

        // ── Alamat ───────────────────────────────────────────────────────────────
        TextArea alamat = new TextArea("Alamat");
        alamat.setPlaceholder("Masukkan alamat lengkap Anda");
        alamat.setWidthFull();
        alamat.setMinHeight("90px");
        alamat.addClassName("login-split-input");

        // ── Jenis Kelamin ─────────────────────────────────────────────────────────
        ComboBox<Pengguna.JenisKelamin> jenisKelamin = new ComboBox<>("Jenis Kelamin");
        jenisKelamin.setItems(Pengguna.JenisKelamin.values());
        jenisKelamin.setItemLabelGenerator(
                jk -> jk == Pengguna.JenisKelamin.LAKI_LAKI ? "Laki-laki" : "Perempuan");
        jenisKelamin.setPlaceholder("Pilih jenis kelamin");
        jenisKelamin.setWidthFull();
        jenisKelamin.addClassName("login-split-input");

        // ── Baris: Kata Sandi & Konfirmasi ───────────────────────────────────────
        PasswordField password = new PasswordField("Kata Sandi");
        password.setPlaceholder("Masukkan kata sandi");
        password.setWidthFull();
        password.addClassName("login-split-input");

        PasswordField confirmPassword = new PasswordField("Konfirmasi Sandi");
        confirmPassword.setPlaceholder("Ulangi kata sandi");
        confirmPassword.setWidthFull();
        confirmPassword.addClassName("login-split-input");

        HorizontalLayout sandiRow = new HorizontalLayout(password, confirmPassword);
        sandiRow.setWidthFull();
        sandiRow.setSpacing(true);

        // ── Tombol Daftar ────────────────────────────────────────────────────────
        Button registerButton = new Button("Daftar");
        registerButton.addClassName("login-split-button");
        registerButton.setWidthFull();
        registerButton.addClickListener(e -> {
            String usernameVal        = username.getValue().trim();
            java.time.LocalDate tglLahirVal = tanggalLahir.getValue();
            String nikVal             = nik.getValue().trim();
            String alamatVal          = alamat.getValue().trim();
            Pengguna.JenisKelamin jkVal = jenisKelamin.getValue();
            String passwordVal        = password.getValue();
            String confirmPasswordVal = confirmPassword.getValue();

            if (usernameVal.isEmpty() || tglLahirVal == null || nikVal.isEmpty()
                    || alamatVal.isEmpty() || jkVal == null
                    || passwordVal.isEmpty() || confirmPasswordVal.isEmpty()) {
                showNotification("Semua field harus diisi!", NotificationVariant.LUMO_ERROR);
                return;
            }

            if (!passwordVal.equals(confirmPasswordVal)) {
                showNotification("Kata sandi dan konfirmasi sandi tidak cocok!", NotificationVariant.LUMO_ERROR);
                return;
            }

            try {
                penggunaService.registerNewUser(
                        usernameVal, tglLahirVal, nikVal, alamatVal, jkVal, passwordVal);
                showNotification("Registrasi berhasil!", NotificationVariant.LUMO_SUCCESS);
                getUI().ifPresent(ui -> ui.navigate("waiting-verification"));
            } catch (Exception ex) {
                showNotification("Registrasi gagal: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
            }
        });

        // ── Link Login ───────────────────────────────────────────────────────────
        HorizontalLayout loginLayout = new HorizontalLayout();
        loginLayout.addClassName("login-split-register");
        loginLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        loginLayout.setWidthFull();
        Span alreadyRegistered = new Span("Sudah punya akun? ");
        RouterLink loginLink = new RouterLink("Masuk di sini", LoginView.class);
        loginLink.addClassName("login-split-link");
        loginLayout.add(alreadyRegistered, loginLink);

        registerCard.add(
                welcomeTitle, welcomeSubtitle,
                username,
                tglNikRow,
                alamat,
                jenisKelamin,
                sandiRow,
                registerButton,
                loginLayout);
        rightPanel.add(registerCard);

        add(leftPanel, rightPanel);
    }

    private void showNotification(String text, NotificationVariant variant) {
        Notification notification = new Notification(text, 3000, Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(variant);
        notification.open();
    }
}

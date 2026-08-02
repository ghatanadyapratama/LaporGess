package com.example.application.views.warga;

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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.util.Locale;

@Route(value = "register", layout = BlankLayout.class)
@PageTitle("Daftar Akun Baru - Lapor Gess")
public class RegisterView extends HorizontalLayout {

    private final PenggunaService penggunaService;
    private Pengguna.Peran selectedPeran = Pengguna.Peran.WARGA;

    private Button btnWarga;
    private Button btnPetugas;

    // Form fields
    private TextField namaLengkap;
    private TextField nik;
    private TextField username;
    private PasswordField password;
    private ComboBox<Pengguna.JenisKelamin> jenisKelamin;
    private DatePicker tanggalLahir;
    private TextField telepon; // Emergency contact for Petugas
    private ComboBox<String> keahlian; // Specialization for Petugas
    private TextArea alamat;
    private TextField rt;
    private TextField rw;

    private HorizontalLayout petugasRow;

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

        H1 leftTitle = new H1("Daftar Akun Baru");
        leftTitle.addClassName("login-split-title");

        Paragraph leftSubtitle = new Paragraph(
                "Lengkapi data diri Anda untuk mulai melaporkan masalah di lingkungan RT/RW Anda.");
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
        registerCard.getStyle().set("padding", "40px");

        H2 welcomeTitle = new H2("Pendaftaran Akun");
        welcomeTitle.addClassName("login-split-welcome");
        welcomeTitle.getStyle().set("margin-bottom", "4px");

        Paragraph welcomeSubtitle = new Paragraph("Pilih tipe akun dan lengkapi data diri Anda.");
        welcomeSubtitle.addClassName("login-split-welcome-subtitle");
        welcomeSubtitle.getStyle().set("margin-bottom", "24px");

        // ── Toggle Buttons (Daftar Warga / Daftar Petugas) ───────────────────────
        HorizontalLayout toggleLayout = new HorizontalLayout();
        toggleLayout.setWidthFull();
        toggleLayout.setSpacing(true);
        toggleLayout.getStyle().set("margin-bottom", "24px");

        btnWarga = new Button("Daftar Warga");
        btnWarga.setHeight("48px");
        btnWarga.getStyle().set("flex", "1");
        btnWarga.addClickListener(e -> selectRole(Pengguna.Peran.WARGA));

        btnPetugas = new Button("Daftar Petugas");
        btnPetugas.setHeight("48px");
        btnPetugas.getStyle().set("flex", "1");
        btnPetugas.addClickListener(e -> selectRole(Pengguna.Peran.PETUGAS_LAPANGAN));

        toggleLayout.add(btnWarga, btnPetugas);

        // ── Form Inputs ──────────────────────────────────────────────────────────
        // Row 1: Nama Lengkap & NIK
        namaLengkap = new TextField("Nama Lengkap");
        namaLengkap.setPlaceholder("Sesuai KTP");
        namaLengkap.addClassName("login-split-input");
        namaLengkap.getStyle().set("flex", "1");

        nik = new TextField("NIK");
        nik.setPlaceholder("Nomor Induk Kependudukan");
        nik.addClassName("login-split-input");
        nik.getStyle().set("flex", "1");

        HorizontalLayout row1 = new HorizontalLayout(namaLengkap, nik);
        row1.setWidthFull();
        row1.setSpacing(true);

        // Row 2: Username & Kata Sandi
        username = new TextField("Username");
        username.setPlaceholder("Untuk login");
        username.addClassName("login-split-input");
        username.getStyle().set("flex", "1");

        password = new PasswordField("Kata Sandi");
        password.setPlaceholder("Buat kata sandi");
        password.addClassName("login-split-input");
        password.getStyle().set("flex", "1");

        HorizontalLayout row2 = new HorizontalLayout(username, password);
        row2.setWidthFull();
        row2.setSpacing(true);

        // Row 3: Jenis Kelamin & Tanggal Lahir
        jenisKelamin = new ComboBox<>("Jenis Kelamin");
        jenisKelamin.setItems(Pengguna.JenisKelamin.values());
        jenisKelamin.setItemLabelGenerator(
                jk -> jk == Pengguna.JenisKelamin.LAKI_LAKI ? "Laki-laki" : "Perempuan");
        jenisKelamin.setPlaceholder("Pilih Jenis Kelamin");
        jenisKelamin.addClassName("login-split-input");
        jenisKelamin.getStyle().set("flex", "1");

        tanggalLahir = new DatePicker("Tanggal Lahir");
        tanggalLahir.setPlaceholder("Pilih Tanggal Lahir");
        tanggalLahir.setLocale(new Locale("id", "ID"));
        tanggalLahir.addClassName("login-split-input");
        tanggalLahir.getStyle().set("flex", "1");

        HorizontalLayout row3 = new HorizontalLayout(jenisKelamin, tanggalLahir);
        row3.setWidthFull();
        row3.setSpacing(true);

        // Row 4 (Petugas Only): Nomor Telepon Darurat & Spesialisasi
        telepon = new TextField("Nomor Telepon Darurat");
        telepon.setPlaceholder("Cth: 08123...");
        telepon.addClassName("login-split-input");
        telepon.getStyle().set("flex", "1");

        keahlian = new ComboBox<>("Spesialisasi / Keahlian");
        keahlian.setItems("Umum (Kebersihan)", "Infrastruktur (Jalan)", "Kelistrikan / Penerangan", "Lingkungan Hidup");
        keahlian.setPlaceholder("Pilih Keahlian");
        keahlian.setValue("Umum (Kebersihan)");
        keahlian.addClassName("login-split-input");
        keahlian.getStyle().set("flex", "1");

        petugasRow = new HorizontalLayout(telepon, keahlian);
        petugasRow.setWidthFull();
        petugasRow.setSpacing(true);
        petugasRow.setVisible(false); // Hidden by default (since Warga is default)

        // Row 5: Alamat Lengkap
        alamat = new TextArea("Alamat Lengkap");
        alamat.setPlaceholder("Nama jalan, nomor rumah...");
        alamat.setWidthFull();
        alamat.setMinHeight("90px");
        alamat.addClassName("login-split-input");

        // Row 6: RT & RW
        rt = new TextField("RT");
        rt.setPlaceholder("Contoh: 01");
        rt.addClassName("login-split-input");
        rt.getStyle().set("flex", "1");

        rw = new TextField("RW");
        rw.setPlaceholder("Contoh: 02");
        rw.addClassName("login-split-input");
        rw.getStyle().set("flex", "1");

        HorizontalLayout row6 = new HorizontalLayout(rt, rw);
        row6.setWidthFull();
        row6.setSpacing(true);

        // Row 7: Buttons (Batal & Daftar Sekarang)
        Button btnCancel = new Button("Batal");
        btnCancel.setHeight("48px");
        btnCancel.getStyle().set("background-color", "#F1F5F9").set("color", "#475569").set("border-radius", "12px").set("font-weight", "bold").set("flex", "1");
        btnCancel.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("login")));

        Button btnRegister = new Button("Daftar Sekarang");
        btnRegister.setHeight("48px");
        btnRegister.getStyle().set("background-color", "#F97316").set("color", "white").set("border-radius", "12px").set("font-weight", "bold").set("flex", "1");
        btnRegister.addClickListener(e -> handleRegistration());

        HorizontalLayout btnRow = new HorizontalLayout(btnCancel, btnRegister);
        btnRow.setWidthFull();
        btnRow.setSpacing(true);
        btnRow.getStyle().set("margin-top", "24px");

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
                toggleLayout,
                row1,
                row2,
                row3,
                petugasRow,
                alamat,
                row6,
                btnRow,
                loginLayout);
        rightPanel.add(registerCard);

        add(leftPanel, rightPanel);

        // Set default toggle state
        selectRole(Pengguna.Peran.WARGA);
    }

    private void selectRole(Pengguna.Peran peran) {
        this.selectedPeran = peran;

        if (peran == Pengguna.Peran.WARGA) {
            // Style btnWarga active
            btnWarga.getStyle()
                .set("background-color", "#FFF7ED")
                .set("color", "#F97316")
                .set("border", "2px solid #F97316")
                .set("font-weight", "bold");
            // Style btnPetugas inactive
            btnPetugas.getStyle()
                .set("background-color", "#FFFFFF")
                .set("color", "#64748B")
                .set("border", "1px solid #E2E8F0")
                .set("font-weight", "500");

            petugasRow.setVisible(false);
        } else {
            // Style btnPetugas active
            btnPetugas.getStyle()
                .set("background-color", "#FFF7ED")
                .set("color", "#F97316")
                .set("border", "2px solid #F97316")
                .set("font-weight", "bold");
            // Style btnWarga inactive
            btnWarga.getStyle()
                .set("background-color", "#FFFFFF")
                .set("color", "#64748B")
                .set("border", "1px solid #E2E8F0")
                .set("font-weight", "500");

            petugasRow.setVisible(true);
        }
    }

    private void handleRegistration() {
        String namaLengkapVal = namaLengkap.getValue().trim();
        String nikVal = nik.getValue().trim();
        String usernameVal = username.getValue().trim();
        String passwordVal = password.getValue();
        Pengguna.JenisKelamin jkVal = jenisKelamin.getValue();
        java.time.LocalDate tglLahirVal = tanggalLahir.getValue();
        String alamatVal = alamat.getValue().trim();
        String rtVal = rt.getValue().trim();
        String rwVal = rw.getValue().trim();

        // Validation
        if (namaLengkapVal.isEmpty() || nikVal.isEmpty() || usernameVal.isEmpty() || passwordVal.isEmpty()
                || jkVal == null || tglLahirVal == null || alamatVal.isEmpty() || rtVal.isEmpty() || rwVal.isEmpty()) {
            showNotification("Semua field wajib diisi!", NotificationVariant.LUMO_ERROR);
            return;
        }

        String teleponVal = "";
        String keahlianVal = "";
        if (selectedPeran == Pengguna.Peran.PETUGAS_LAPANGAN) {
            teleponVal = telepon.getValue().trim();
            keahlianVal = keahlian.getValue();
            if (teleponVal.isEmpty() || keahlianVal == null || keahlianVal.isEmpty()) {
                showNotification("Field nomor telepon darurat & keahlian wajib diisi untuk petugas!", NotificationVariant.LUMO_ERROR);
                return;
            }
        }

        String rtRwVal = rtVal + "/" + rwVal;

        try {
            penggunaService.registerNewUser(
                    usernameVal,
                    tglLahirVal,
                    nikVal,
                    alamatVal,
                    jkVal,
                    passwordVal,
                    namaLengkapVal,
                    rtRwVal,
                    teleponVal,
                    selectedPeran,
                    keahlianVal
            );
            showNotification("Registrasi berhasil! Menunggu verifikasi admin.", NotificationVariant.LUMO_SUCCESS);
            getUI().ifPresent(ui -> ui.navigate("waiting-verification"));
        } catch (Exception ex) {
            showNotification("Registrasi gagal: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
        }
    }

    private void showNotification(String text, NotificationVariant variant) {
        Notification notification = new Notification(text, 3000, Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(variant);
        notification.open();
    }
}

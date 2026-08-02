package com.example.application.views.warga;

import com.example.application.model.Pengguna;
import com.example.application.service.PenggunaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route(value = "login", layout = BlankLayout.class)
@PageTitle("Login - Lapor Gess")
public class LoginView extends HorizontalLayout {

    private final PenggunaService penggunaService;

    public LoginView(PenggunaService penggunaService) {
        this.penggunaService = penggunaService;
        
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        addClassName("login-split-view");

        // Left Panel
        VerticalLayout leftPanel = new VerticalLayout();
        leftPanel.addClassName("login-split-left");
        leftPanel.setAlignItems(Alignment.CENTER);
        leftPanel.setJustifyContentMode(JustifyContentMode.CENTER);
        
        Image leafIcon = new Image("icons/logoLaporGess.png", "LaporGess icon");
        leafIcon.addClassName("login-split-icon");
        
        H1 leftTitle = new H1("Bergabung dengan\nKomunitas");
        leftTitle.addClassName("login-split-title");
        
        Paragraph leftSubtitle = new Paragraph("Lapor Gess memudahkan Anda untuk melaporkan masalah lingkungan dan fasilitas umum di RT/RW Anda.");
        leftSubtitle.addClassName("login-split-subtitle");
        
        leftPanel.add(leafIcon, leftTitle, leftSubtitle);
        leftPanel.setWidth("50%");
        leftPanel.setHeightFull();

        // Right Panel
        VerticalLayout rightPanel = new VerticalLayout();
        rightPanel.addClassName("login-split-right");
        rightPanel.setAlignItems(Alignment.CENTER);
        rightPanel.setJustifyContentMode(JustifyContentMode.CENTER);
        rightPanel.setWidth("50%");
        rightPanel.setHeightFull();

        VerticalLayout loginCard = new VerticalLayout();
        loginCard.addClassName("login-split-card");
        loginCard.setWidth("440px");
        
        H2 welcomeTitle = new H2("Selamat Datang!");
        welcomeTitle.addClassName("login-split-welcome");
        
        Paragraph welcomeSubtitle = new Paragraph("Masuk untuk melanjutkan pelaporan.");
        welcomeSubtitle.addClassName("login-split-welcome-subtitle");

        TextField username = new TextField("Nama Pengguna");
        username.setPlaceholder("Masukkan nama pengguna");
        username.setWidthFull();
        username.addClassName("login-split-input");

        PasswordField password = new PasswordField("Kata Sandi");
        password.setPlaceholder("Masukkan kata sandi");
        password.setWidthFull();
        password.addClassName("login-split-input");

        Button loginButton = new Button("Masuk");
        loginButton.addClassName("login-split-button");
        loginButton.setWidthFull();
        loginButton.addClickListener(e -> {
            String usernameVal = username.getValue().trim();
            String passwordVal = password.getValue();

            if (usernameVal.isEmpty() || passwordVal.isEmpty()) {
                showNotification("Nama pengguna dan kata sandi harus diisi!", NotificationVariant.LUMO_ERROR);
                return;
            }

            try {
                Pengguna authenticated = penggunaService.authenticateUser(usernameVal, passwordVal);

                // Save to session
                com.example.application.service.SessionManager.login(
                    authenticated.getUsername(),
                    authenticated.getPeran().name(),
                    authenticated.getNamaLengkap(),
                    authenticated.getPoin() != null ? authenticated.getPoin() : 0
                );

                showNotification("Selamat datang, " + authenticated.getNamaLengkap() + "!", NotificationVariant.LUMO_SUCCESS);
                // Redirect based on role
                if (authenticated.getPeran() == Pengguna.Peran.ADMIN) {
                    getUI().ifPresent(ui -> ui.navigate("admin/dashboard"));
                } else if (authenticated.getPeran() == Pengguna.Peran.PETUGAS_LAPANGAN) {
                    getUI().ifPresent(ui -> ui.navigate("petugas/dashboard"));
                } else {
                    getUI().ifPresent(ui -> ui.navigate("dashboard"));
                }
            } catch (IllegalStateException ex) {
                showNotification(ex.getMessage(), NotificationVariant.LUMO_PRIMARY);
            } catch (Exception ex) {
                showNotification(ex.getMessage(), NotificationVariant.LUMO_ERROR);
            }
        });

        HorizontalLayout registerLayout = new HorizontalLayout();
        registerLayout.addClassName("login-split-register");
        registerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        registerLayout.setWidthFull();
        Span notRegistered = new Span("Belum punya akun? ");
        RouterLink registerLink = new RouterLink("Daftar di sini", RegisterView.class);
        registerLink.addClassName("login-split-link");
        registerLayout.add(notRegistered, registerLink);

        loginCard.add(welcomeTitle, welcomeSubtitle, username, password, loginButton, registerLayout);
        rightPanel.add(loginCard);

        add(leftPanel, rightPanel);
    }

    private void showNotification(String text, NotificationVariant variant) {
        Notification notification = new Notification(text, 4000, Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(variant);
        notification.open();
    }
}

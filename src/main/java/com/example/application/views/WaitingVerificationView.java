package com.example.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "waiting-verification", layout = BlankLayout.class)
@PageTitle("Menunggu Verifikasi - Lapor Gess")
public class WaitingVerificationView extends VerticalLayout {

    public WaitingVerificationView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("waiting-view-bg");

        VerticalLayout container = new VerticalLayout();
        container.setAlignItems(Alignment.CENTER);
        container.addClassName("waiting-container");

        Image shieldIcon = new Image("icons/Container_margin (1).png", "Shield icon");
        shieldIcon.addClassName("waiting-icon");

        H1 title = new H1("Akun Sedang Menunggu Verifikasi");
        title.addClassName("waiting-title");

        Paragraph subtitle = new Paragraph("Pendaftaran Anda telah dikirim dan sedang dalam antrean. Harap tunggu administrator RT/RW memverifikasi dan menyetujui akun Anda sebelum Anda dapat mengakses aplikasi.");
        subtitle.addClassName("waiting-subtitle");

        Button backButton = new Button("Kembali ke Halaman Awal");
        backButton.addClassName("login-split-button"); // Reuse the orange button style from login
        backButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("")));

        container.add(shieldIcon, title, subtitle, backButton);
        add(container);
    }
}

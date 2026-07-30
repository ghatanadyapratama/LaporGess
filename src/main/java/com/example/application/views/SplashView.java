package com.example.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = BlankLayout.class)
@PageTitle("Lapor Gess")
public class SplashView extends VerticalLayout {

    public SplashView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("splash-view");

        VerticalLayout container = new VerticalLayout();
        container.setAlignItems(Alignment.CENTER);
        container.addClassName("splash-container");

        Image leafIcon = new Image("icons/logoLaporGess.png", "LaporGess icon");
        leafIcon.addClassName("splash-icon");

        H1 title = new H1("Lapor Gess");
        title.addClassName("splash-title");

        Paragraph subtitle = new Paragraph("Platform pelaporan lingkungan untuk warga. Mari membangun lingkungan yang lebih baik bersama-sama.");
        subtitle.addClassName("splash-subtitle");

        Button startButton = new Button("Mulai Sekarang");
        startButton.addClassName("splash-button");
        startButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("login")));

        container.add(leafIcon, title, subtitle, startButton);
        add(container);
    }
}

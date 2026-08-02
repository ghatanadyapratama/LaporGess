package com.example.application.views.petugas;

import com.example.application.views.warga.BlankLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "petugas/detail-pekerjaan", layout = BlankLayout.class)
@PageTitle("Detail Pekerjaan - Petugas LaporGess")
public class PetugasDetailView extends Div {

    public PetugasDetailView() {
        addClassName("pt-root");

        // 1. Sidebar
        Div sidebar = PetugasLayout.buildSidebar("petugas/dashboard");

        // 2. Main Container
        Div main = new Div();
        main.addClassName("pt-main");

        // 3. Topbar
        Div topbar = PetugasLayout.buildTopbar("Detail Pekerjaan Tersedia");

        // 4. Body Content
        Div body = new Div();
        body.addClassName("pt-body");

        // Back button link (← Kembali)
        Div backBtn = new Div();
        backBtn.addClassName("pt-detail-back-btn");
        Span backArrow = new Span("←");
        Span backText = new Span("Kembali");
        backBtn.add(backArrow, backText);
        backBtn.addClickListener(e -> UI.getCurrent().navigate("petugas/dashboard"));

        body.add(backBtn);

        // Columns Layout (Grid)
        Div grid = new Div();
        grid.addClassName("pt-detail-grid");

        // --- Left Card (Job Details) ---
        Div leftCard = new Div();
        leftCard.addClassName("pt-detail-left-card");

        // Card image (trash bins)
        Image mainImg = new Image("icons/sampah_liar.png", "Pembuangan Sampah Liar");
        mainImg.addClassName("pt-detail-image");
        leftCard.add(mainImg);

        // Info details wrapper
        Div infoBox = new Div();
        infoBox.addClassName("pt-detail-info-box");

        H2 jobTitle = new H2("Pembuangan Sampah Liar");
        jobTitle.addClassName("pt-detail-title");

        // Info 1: Location
        Div rowLoc = new Div();
        rowLoc.addClassName("pt-detail-info-row");
        Image locIcon = new Image("icons/mapsIcon.png", "Lokasi");
        locIcon.addClassName("pt-detail-info-icon");
        Span locText = new Span("Area Taman, RT 05/01");
        rowLoc.add(locIcon, locText);

        // Info 2: Category
        Div rowCat = new Div();
        rowCat.addClassName("pt-detail-info-row");
        Image catIcon = new Image("icons/laporan.png", "Kategori");
        catIcon.addClassName("pt-detail-info-icon");
        Span catText = new Span("Kategori: Kebersihan");
        rowCat.add(catIcon, catText);

        // Info 3: Reported date
        Div rowDate = new Div();
        rowDate.addClassName("pt-detail-info-row");
        Image dateIcon = new Image("icons/jam.png", "Tanggal");
        dateIcon.addClassName("pt-detail-info-icon");
        Span dateText = new Span("Dilaporkan pada 2026-07-17");
        rowDate.add(dateIcon, dateText);

        infoBox.add(jobTitle, rowLoc, rowCat, rowDate);
        leftCard.add(infoBox);

        // --- Right Card (Take Action) ---
        Div rightCard = new Div();
        rightCard.addClassName("pt-detail-right-card");

        // Circular checklist icon
        Div iconWrapper = new Div();
        iconWrapper.addClassName("pt-detail-right-icon-wrapper");
        Image checklistIcon = new Image("icons/pekerjaan.png", "Checklist");
        checklistIcon.addClassName("pt-detail-right-icon");
        iconWrapper.add(checklistIcon);

        H3 rightTitle = new H3("Ambil Pekerjaan Ini");
        rightTitle.addClassName("pt-detail-right-title");

        Paragraph rightDesc = new Paragraph(
            "Pekerjaan ini masih berstatus \"Menunggu\". Pastikan Anda memiliki peralatan yang cukup sebelum mengambil pekerjaan ini."
        );
        rightDesc.addClassName("pt-detail-right-desc");

        Button btnTake = new Button("Ya, Saya Ambil Tugas Ini");
        btnTake.addClassName("pt-detail-take-btn");
        btnTake.addClickListener(e -> {
            Notification n = new Notification("Tugas berhasil diambil! Buka menu 'Tugas Saya' untuk melihat detail pengerjaan.", 4000, Notification.Position.BOTTOM_CENTER);
            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            n.open();
            UI.getCurrent().navigate("petugas/dashboard");
        });

        rightCard.add(iconWrapper, rightTitle, rightDesc, btnTake);

        grid.add(leftCard, rightCard);
        body.add(grid);

        main.add(topbar, body);
        add(sidebar, main);
    }
}

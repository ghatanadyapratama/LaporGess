package com.example.application.views.petugas;

import com.example.application.model.Laporan;
import com.example.application.service.LaporanService;
import com.example.application.service.SessionManager;
import com.example.application.views.warga.BlankLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "petugas/tugas-saya", layout = BlankLayout.class)
@PageTitle("Tugas Saya - Petugas LaporGess")
public class PetugasTugasSayaView extends Div implements BeforeEnterObserver {

    private final LaporanService laporanService;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public PetugasTugasSayaView(LaporanService laporanService) {
        this.laporanService = laporanService;
        addClassName("pt-root");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!SessionManager.isLoggedIn() || !SessionManager.isPetugas()) {
            event.rerouteTo("login");
            return;
        }
        buildUI();
    }

    private void buildUI() {
        removeAll();
        Div sidebar = PetugasLayout.buildSidebar("petugas/tugas-saya");
        Div main = new Div();
        main.addClassName("pt-main");
        Div topbar = PetugasLayout.buildTopbar("Tugas Saya");
        Div body = new Div();
        body.addClassName("pt-body");

        String username = SessionManager.getUsername();
        List<Laporan> tugasList = laporanService.getLaporanDiprosesByPetugas(username);

        if (tugasList.isEmpty()) {
            Div empty = new Div();
            empty.getStyle().set("text-align", "center").set("padding", "80px 20px").set("color", "#94A3B8");
            Span icon = new Span("📋");
            icon.getStyle().set("font-size", "3.5rem").set("display", "block").set("margin-bottom", "16px");
            Span msg = new Span("Tidak ada tugas yang di-assign saat ini.");
            msg.getStyle().set("font-weight", "600").set("font-size", "1rem");
            empty.add(icon, msg);
            body.add(empty);
        } else {
            Div grid = new Div();
            grid.addClassName("pt-job-grid");

            for (Laporan laporan : tugasList) {
                Div card = new Div();
                card.addClassName("pt-job-card");

                Div headerRow = new Div();
                headerRow.addClassName("pt-card-header-row");
                Span titleSpan = new Span(laporan.getJudul());
                titleSpan.addClassName("pt-job-title");
                Span badge = new Span("DIPROSES");
                badge.addClassName("pt-badge-diproses");
                badge.getStyle().set("background-color", "#FFF0E0").set("color", "#FF7A00")
                    .set("font-size", "0.72rem").set("font-weight", "800").set("padding", "4px 10px")
                    .set("border-radius", "6px").set("letter-spacing", "0.05em");
                headerRow.add(titleSpan, badge);

                Div locRow = new Div();
                locRow.addClassName("pt-job-location-row");
                Span locIcon = new Span("📍");
                Span locText = new Span(laporan.getLokasi() != null ? laporan.getLokasi() : "-");
                locRow.add(locIcon, locText);

                Div metaRow = new Div();
                metaRow.getStyle().set("font-size", "0.8rem").set("color", "#94A3B8").set("margin-top", "6px");
                metaRow.add(new Span("🏷 " + laporan.getKategori() + "  •  🗓 " + laporan.getDibuatPada().format(FMT)));

                Div actions = new Div();
                actions.addClassName("pt-card-actions");

                Button btnDetail = new Button("Lihat Detail");
                btnDetail.addClassName("pt-btn-detail");
                btnDetail.getStyle().set("flex", "1");
                btnDetail.addClickListener(e -> {
                    UI.getCurrent().getSession().setAttribute("selectedLaporanId", laporan.getId());
                    UI.getCurrent().navigate("petugas/detail-tugas");
                });

                Button btnSelesai = new Button("Selesaikan");
                btnSelesai.addClassName("pt-btn-kirim");
                btnSelesai.getStyle().set("flex", "1");
                btnSelesai.addClickListener(e -> {
                    UI.getCurrent().getSession().setAttribute("selectedLaporanId", laporan.getId());
                    UI.getCurrent().navigate("petugas/selesaikan");
                });

                actions.add(btnDetail, btnSelesai);
                card.add(headerRow, locRow, metaRow, actions);
                grid.add(card);
            }
            body.add(grid);
        }

        main.add(topbar, body);
        add(sidebar, main);
    }
}

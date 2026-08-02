package com.example.application.views.petugas;

import com.example.application.model.Notifikasi;
import com.example.application.model.Pengguna;
import com.example.application.repository.NotifikasiRepository;
import com.example.application.repository.PenggunaRepository;
import com.example.application.service.SessionManager;
import com.example.application.views.warga.BlankLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Route(value = "petugas/notifikasi", layout = BlankLayout.class)
@PageTitle("Notifikasi Tugas - Petugas LaporGess")
public class PetugasNotifikasiView extends Div implements BeforeEnterObserver {

    private final NotifikasiRepository notifikasiRepository;
    private final PenggunaRepository penggunaRepository;
    private Pengguna currentUser;

    public PetugasNotifikasiView(NotifikasiRepository notifikasiRepository, PenggunaRepository penggunaRepository) {
        this.notifikasiRepository = notifikasiRepository;
        this.penggunaRepository = penggunaRepository;
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

        Div sidebar = PetugasLayout.buildSidebar("petugas/notifikasi");
        Div main = new Div();
        main.addClassName("pt-main");
        Div topbar = PetugasLayout.buildTopbar("Notifikasi Tugas");
        Div body = new Div();
        body.addClassName("pt-body");

        // Header block
        Div headerBlock = new Div();
        headerBlock.addClassName("ad-notif-card-header");

        H2 sectionTitle = new H2("Pemberitahuan Sistem");
        sectionTitle.addClassName("ad-notif-card-title");

        Div listCard = new Div();
        listCard.addClassName("ad-notif-page-card");
        Div listContainer = new Div();
        listContainer.addClassName("ad-notif-list-container");

        List<Notifikasi> notifList = currentUser != null
                ? notifikasiRepository.findByPenggunaOrderByDibuatPadaDesc(currentUser)
                : List.of();

        Span markRead = new Span("Tandai semua dibaca");
        markRead.addClassName("ad-notif-mark-all");
        markRead.addClickListener(e -> {
            notifList.forEach(n -> n.setDibaca(true));
            notifikasiRepository.saveAll(notifList);
            Notification n = new Notification("Semua pemberitahuan ditandai telah dibaca.", 3000, Notification.Position.BOTTOM_CENTER);
            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            n.open();
        });

        headerBlock.add(sectionTitle, markRead);
        body.add(headerBlock);

        if (notifList.isEmpty()) {
            Div empty = new Div(new Span("Belum ada notifikasi untuk Anda."));
            empty.getStyle().set("padding", "40px").set("text-align", "center").set("color", "#94A3B8");
            listContainer.add(empty);
        } else {
            for (Notifikasi notif : notifList) {
                Div row = new Div();
                row.addClassName("ad-notif-item-row");

                String tipe = notif.getTipe();
                String icon = "INFO".equals(tipe) ? "ℹ️" : "SUCCESS".equals(tipe) ? "✔️" : "⚠️";
                String badgeClass = "WARNING".equals(tipe) ? "ad-notif-avatar-red" : "SUCCESS".equals(tipe) ? "ad-notif-avatar-teal" : "ad-notif-avatar-gray";

                Div badge = new Div(new Span(icon));
                badge.addClassName("ad-notif-avatar-box");
                badge.addClassName(badgeClass);

                Div bodyDiv = new Div();
                bodyDiv.addClassName("ad-notif-body-col");
                Span t = new Span(tipe);
                t.addClassName("ad-notif-item-title");
                Span d = new Span(notif.getPesan());
                d.addClassName("ad-notif-item-desc");
                Span time = new Span(formatTimeAgo(notif.getDibuatPada()));
                time.addClassName("ad-notif-item-time");
                bodyDiv.add(t, d, time);

                row.add(badge, bodyDiv);
                listContainer.add(row);
            }
        }

        listCard.add(listContainer);
        body.add(listCard);
        main.add(topbar, body);
        add(sidebar, main);
    }

    private String formatTimeAgo(LocalDateTime time) {
        if (time == null) return "Baru saja";
        Duration d = Duration.between(time, LocalDateTime.now());
        if (d.toMinutes() < 1) return "Baru saja";
        if (d.toMinutes() < 60) return d.toMinutes() + " menit yang lalu";
        if (d.toHours() < 24) return d.toHours() + " jam yang lalu";
        return d.toDays() + " hari yang lalu";
    }
}

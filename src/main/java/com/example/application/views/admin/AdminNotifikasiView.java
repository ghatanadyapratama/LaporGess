package com.example.application.views.admin;

import com.example.application.views.BlankLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route(value = "admin/notifikasi", layout = BlankLayout.class)
@PageTitle("Notifikasi Sistem - Lapor Gess")
public class AdminNotifikasiView extends Div {

    private final List<Div> unreadDots = new ArrayList<>();

    public AdminNotifikasiView() {
        addClassName("ad-root");

        // Build Sidebar
        Div sidebar = AdminLayout.buildSidebar("admin/notifikasi");

        // Main Content Container
        Div main = new Div();
        main.addClassName("ad-main");

        // Topbar
        Div topbar = AdminLayout.buildTopbar("Notifikasi Sistem");

        // Scrollable Body
        Div body = new Div();
        body.addClassName("ad-body");

        // Main Card Container
        Div card = new Div();
        card.addClassName("ad-notif-page-card");

        // Card Header
        Div header = new Div();
        header.addClassName("ad-notif-card-header");

        Span title = new Span("Semua Notifikasi Admin");
        title.addClassName("ad-notif-card-title");

        Span markAllRead = new Span("Tandai semua dibaca");
        markAllRead.addClassName("ad-notif-mark-all");
        markAllRead.addClickListener(e -> {
            for (Div dot : unreadDots) {
                dot.getStyle().set("visibility", "hidden");
            }
            Notification.show("Semua notifikasi telah ditandai sebagai dibaca.");
        });

        header.add(title, markAllRead);
        card.add(header);

        // Notifications List Container
        Div list = new Div();
        list.addClassName("ad-notif-list-container");

        // 1. Laporan Darurat Baru Masuk!
        list.add(createNotifRow(
            true,
            "ad-notif-avatar-red",
            "⚠️",
            "Laporan Darurat Baru Masuk!",
            "Pohon tumbang menutup jalan utama di area RT 02 / RW 01. Pelapor: Budi Santoso.",
            "Baru saja",
            "Tinjau Laporan",
            () -> UI.getCurrent().navigate("admin/laporan")
        ));

        // 2. Pendaftaran Pengguna Baru
        list.add(createNotifRow(
            true,
            "ad-notif-avatar-gray",
            "👤",
            "Pendaftaran Pengguna Baru",
            "\"Rina Wijaya\" (Warga - RT 02/RW 01) baru saja mendaftar dan menunggu persetujuan Anda.",
            "1 jam yang lalu",
            "Verifikasi",
            () -> UI.getCurrent().navigate("admin/verifikasi")
        ));

        // 3. Tugas Diselesaikan oleh Petugas
        list.add(createNotifRow(
            false,
            "ad-notif-avatar-teal",
            "✓",
            "Tugas Diselesaikan oleh Petugas",
            "Agus Pratama telah menandai laporan \"Lampu Jalan Mati\" sebagai Selesai.",
            "3 jam yang lalu",
            null,
            null
        ));

        // 4. Updates / Notification System
        list.add(createNotifRow(
            false,
            "ad-notif-avatar-teal",
            "ℹ️",
            "Pembaruan Sistem Berhasil",
            "Pembaruan modul verifikasi otomatis RT/RW telah berhasil dipasang.",
            "1 hari yang lalu",
            null,
            null
        ));

        card.add(list);
        body.add(card);

        main.add(topbar, body);
        add(sidebar, main);
    }

    private Div createNotifRow(
        boolean isUnread,
        String avatarBgClass,
        String iconSymbol,
        String titleText,
        String descriptionText,
        String timeText,
        String actionButtonLabel,
        Runnable actionHandler
    ) {
        Div row = new Div();
        row.addClassName("ad-notif-item-row");

        // Left Dot (Unread indicator)
        Div dot = new Div();
        dot.addClassName("ad-notif-dot-unread");
        if (!isUnread) {
            dot.getStyle().set("visibility", "hidden");
        } else {
            unreadDots.add(dot);
        }

        // Icon Box / Avatar
        Div avatar = new Div(new Span(iconSymbol));
        avatar.addClassName("ad-notif-avatar-box");
        avatar.addClassName(avatarBgClass);

        // Content (Title, Description, Time)
        Div content = new Div();
        content.addClassName("ad-notif-body-col");

        Span itemTitle = new Span(titleText);
        itemTitle.addClassName("ad-notif-item-title");

        Span itemDesc = new Span(descriptionText);
        itemDesc.addClassName("ad-notif-item-desc");

        Span itemTime = new Span(timeText);
        itemTime.addClassName("ad-notif-item-time");

        content.add(itemTitle, itemDesc, itemTime);

        row.add(dot, avatar, content);

        // Action Button (if any)
        if (actionButtonLabel != null && !actionButtonLabel.isEmpty()) {
            Button actionBtn = new Button(actionButtonLabel);
            actionBtn.addClassName("ad-notif-action-btn");
            if (actionHandler != null) {
                actionBtn.addClickListener(e -> actionHandler.run());
            }
            row.add(actionBtn);
        }

        return row;
    }
}

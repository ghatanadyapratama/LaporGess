package com.example.application.views.admin;

import com.example.application.model.Notifikasi;
import com.example.application.model.Pengguna;
import com.example.application.repository.NotifikasiRepository;
import com.example.application.views.warga.BlankLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Route(value = "admin/notifikasi", layout = BlankLayout.class)
@PageTitle("Notifikasi Sistem - Lapor Gess")
public class AdminNotifikasiView extends Div {

    private final NotifikasiRepository notifikasiRepository;
    private final List<Div> unreadDots = new ArrayList<>();
    private List<Notifikasi> notifications;
    private Div list;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

    public AdminNotifikasiView(NotifikasiRepository notifikasiRepository) {
        this.notifikasiRepository = notifikasiRepository;
        addClassName("ad-root");

        // Build Sidebar
        Div sidebar = AdminLayout.buildSidebar("admin/notifikasi");

        // Main Content Container
        Div main = new Div();
        main.addClassName("ad-main");

        // Topbar
        Div topbar = AdminLayout.buildTopbar("Notifikasi Sistem", notifikasiRepository);

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
        markAllRead.addClickListener(e -> markAllAsRead());

        header.add(title, markAllRead);
        card.add(header);

        // Notifications List Container
        list = new Div();
        list.addClassName("ad-notif-list-container");
        card.add(list);
        body.add(card);

        main.add(topbar, body);
        add(sidebar, main);

        loadNotifications();
    }

    private void loadNotifications() {
        list.removeAll();
        unreadDots.clear();
        
        notifications = notifikasiRepository.findByPengguna_PeranOrderByDibuatPadaDesc(Pengguna.Peran.ADMIN);
        
        if (notifications.isEmpty()) {
            Div emptyMsg = new Div(new Span("Belum ada notifikasi baru."));
            emptyMsg.getStyle().set("padding", "20px").set("text-align", "center").set("color", "#64748B");
            list.add(emptyMsg);
            return;
        }

        for (Notifikasi notif : notifications) {
            String bgClass = "ad-notif-avatar-gray";
            String icon = "ℹ️";
            
            if ("WARNING".equals(notif.getTipe())) {
                bgClass = "ad-notif-avatar-red";
                icon = "⚠️";
            } else if ("SUCCESS".equals(notif.getTipe())) {
                bgClass = "ad-notif-avatar-teal";
                icon = "✓";
            }

            list.add(createNotifRow(
                !notif.isDibaca(),
                bgClass,
                icon,
                notif.getTipe(),
                notif.getPesan(),
                notif.getDibuatPada() != null ? notif.getDibuatPada().format(FMT) : "Baru saja",
                null,
                null
            ));
        }
    }

    private void markAllAsRead() {
        for (Notifikasi notif : notifications) {
            notif.setDibaca(true);
        }
        notifikasiRepository.saveAll(notifications);
        
        for (Div dot : unreadDots) {
            dot.getStyle().set("visibility", "hidden");
        }
        Notification.show("Semua notifikasi telah ditandai sebagai dibaca.");
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

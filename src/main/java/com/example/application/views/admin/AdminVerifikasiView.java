package com.example.application.views.admin;

import com.example.application.model.Pengguna;
import com.example.application.repository.PenggunaRepository;
import com.example.application.views.warga.BlankLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "admin/verifikasi", layout = BlankLayout.class)
@PageTitle("Verifikasi Pengguna - Lapor Gess")
public class AdminVerifikasiView extends Div {

    private final PenggunaRepository penggunaRepository;
    private Div verifList;
    private TextField search;

    public AdminVerifikasiView(PenggunaRepository penggunaRepository) {
        this.penggunaRepository = penggunaRepository;
        addClassName("ad-root");

        long petugasAktif = penggunaRepository.countByStatusAndPeran(Pengguna.Status.AKTIF, Pengguna.Peran.PETUGAS_LAPANGAN);
        long verifikasiPending = penggunaRepository.countByStatus(Pengguna.Status.PENDING);
        Div sidebar = AdminLayout.buildSidebar("admin/verifikasi", 0, petugasAktif, verifikasiPending);

        Div main = new Div();
        main.addClassName("ad-main");

        Div topbar = AdminLayout.buildTopbar("Verifikasi Pengguna");

        Div body = new Div();
        body.addClassName("ad-body");

        Div card = new Div();
        card.addClassName("ad-card");

        Div header = new Div();
        header.addClassName("ad-card-header");
        Span title = new Span("Pendaftaran Menunggu Verifikasi");
        title.addClassName("ad-card-title");

        search = new TextField();
        search.setPlaceholder("Cari nama atau NIK...");
        search.getStyle().set("width", "260px");
        search.addValueChangeListener(e -> refreshList());

        header.add(title, search);
        card.add(header);

        verifList = new Div();
        verifList.addClassName("ad-verif-list");
        card.add(verifList);

        body.add(card);
        main.add(topbar, body);
        add(sidebar, main);

        refreshList();
    }

    private void refreshList() {
        verifList.removeAll();
        List<Pengguna> pending = penggunaRepository.findByStatus(Pengguna.Status.PENDING);

        String keyword = search.getValue().trim().toLowerCase();
        pending.stream()
            .filter(p -> keyword.isEmpty()
                    || p.getNamaLengkap().toLowerCase().contains(keyword)
                    || (p.getNik() != null && p.getNik().toLowerCase().contains(keyword))
                    || p.getUsername().toLowerCase().contains(keyword))
            .forEach(p -> verifList.add(createVerifCard(p)));

        if (verifList.getChildren().count() == 0) {
            Div empty = new Div();
            empty.getStyle().set("padding", "40px").set("text-align", "center").set("color", "#94A3B8");
            empty.add(new Span("✅ Tidak ada pendaftaran yang menunggu verifikasi."));
            verifList.add(empty);
        }
    }

    private Div createVerifCard(Pengguna pengguna) {
        Div card = new Div();
        card.addClassName("ad-verif-card");

        Div left = new Div();
        left.addClassName("ad-verif-left");

        Div avatar = new Div();
        avatar.addClassName("ad-verif-avatar");
        avatar.add(new Span(pengguna.getNamaLengkap() != null && !pengguna.getNamaLengkap().isEmpty()
                ? String.valueOf(pengguna.getNamaLengkap().charAt(0)).toUpperCase() : "?"));

        Div info = new Div();
        info.addClassName("ad-verif-info");

        Span roleBadge = new Span();
        if (pengguna.getPeran() == Pengguna.Peran.PETUGAS_LAPANGAN) {
            roleBadge.setText("Daftar Sebagai: Petugas Lapangan");
            roleBadge.addClassName("ad-role-badge-orange");
            roleBadge.getStyle().set("background-color", "#FFF0E0").set("color", "#FF7A00")
                .set("font-size", "0.72rem").set("font-weight", "800").set("padding", "4px 10px")
                .set("border-radius", "6px").set("letter-spacing", "0.05em");
        } else {
            roleBadge.setText("Daftar Sebagai: Warga");
            roleBadge.addClassName("ad-role-badge-teal");
        }

        Div grid = new Div();
        grid.addClassName("ad-verif-grid");

        grid.add(createFieldBlock("Nama Lengkap", pengguna.getNamaLengkap()));
        grid.add(createFieldBlock("Username", "@" + pengguna.getUsername()));
        grid.add(createFieldBlock("NIK KTP", pengguna.getNik() != null ? pengguna.getNik() : "-"));
        grid.add(createFieldBlock("Jenis Kelamin",
            pengguna.getJenisKelamin() != null ? pengguna.getJenisKelamin().name().replace("_", " ") : "-"));

        String alamatFull = (pengguna.getRtRw() != null ? "RT " + pengguna.getRtRw() : "")
                + (pengguna.getAlamat() != null ? " - " + pengguna.getAlamat() : "");
        Div addrBlock = createFieldBlock("Area / Alamat", alamatFull);
        addrBlock.getStyle().set("grid-column", "span 2");
        grid.add(addrBlock);

        if (pengguna.getPeran() == Pengguna.Peran.PETUGAS_LAPANGAN) {
            grid.add(createFieldBlock("No. Telepon Darurat", pengguna.getTelepon()));
            grid.add(createFieldBlock("Spesialisasi / Keahlian", pengguna.getKeahlian() != null ? pengguna.getKeahlian() : "Umum"));
        }

        info.add(roleBadge, grid);
        left.add(avatar, info);

        Div actions = new Div();
        actions.addClassName("ad-verif-actions");

        Button btnApprove = new Button("✔ Setujui Akun");
        btnApprove.addClassName("ad-btn-approve");
        btnApprove.addClickListener(e -> {
            pengguna.setStatus(Pengguna.Status.AKTIF);
            penggunaRepository.save(pengguna);
            Notification n = new Notification("Akun " + pengguna.getNamaLengkap() + " berhasil disetujui!", 3000, Notification.Position.BOTTOM_CENTER);
            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            n.open();
            refreshList();
        });

        Button btnReject = new Button("🗑 Tolak Pendaftaran");
        btnReject.addClassName("ad-btn-reject");
        btnReject.addClickListener(e -> {
            penggunaRepository.delete(pengguna);
            Notification n = new Notification("Pendaftaran " + pengguna.getNamaLengkap() + " ditolak.", 3000, Notification.Position.BOTTOM_CENTER);
            n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            n.open();
            refreshList();
        });

        actions.add(btnApprove, btnReject);
        card.add(left, actions);

        return card;
    }

    private Div createFieldBlock(String label, String value) {
        Div block = new Div();
        block.addClassName("ad-field-block");
        Span lbl = new Span(label);
        lbl.addClassName("ad-field-lbl");
        Span val = new Span(value != null ? value : "-");
        val.addClassName("ad-field-val");
        block.add(lbl, val);
        return block;
    }
}

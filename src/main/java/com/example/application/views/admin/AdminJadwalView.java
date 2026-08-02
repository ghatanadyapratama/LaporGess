package com.example.application.views.admin;

import com.example.application.model.JadwalShift;
import com.example.application.model.Pengguna;
import com.example.application.repository.JadwalShiftRepository;
import com.example.application.repository.PenggunaRepository;
import com.example.application.views.warga.BlankLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "admin/jadwal", layout = BlankLayout.class)
@PageTitle("Jadwal Petugas - Admin Lapor")
public class AdminJadwalView extends Div {

    private final JadwalShiftRepository jadwalShiftRepository;
    private final PenggunaRepository penggunaRepository;
    private Grid<JadwalShift> grid = new Grid<>(JadwalShift.class, false);

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public AdminJadwalView(JadwalShiftRepository jadwalShiftRepository, PenggunaRepository penggunaRepository) {
        this.jadwalShiftRepository = jadwalShiftRepository;
        this.penggunaRepository = penggunaRepository;
        
        addClassName("a-root");
        add(AdminLayout.buildSidebar("admin/jadwal"), buildMain());
    }

    private Div buildMain() {
        Div main = new Div();
        main.addClassName("a-main");
        main.add(AdminLayout.buildTopbar("Jadwal Petugas Lapangan"));

        Div body = new Div();
        body.addClassName("a-body");
        
        // Stats
        Div stats = new Div();
        stats.addClassName("a-stats-row");
        long petugasCount = penggunaRepository.findAll().stream().filter(p -> p.getPeran() == Pengguna.Peran.PETUGAS_LAPANGAN).count();
        long shiftCount = jadwalShiftRepository.count();
        stats.add(statCard("👥", "a-stat-icon-blue", "Total Petugas", String.valueOf(petugasCount)));
        stats.add(statCard("📋", "a-stat-icon-orange", "Total Shift", String.valueOf(shiftCount)));
        body.add(stats);

        // Toolbar
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(HorizontalLayout.JustifyContentMode.END);
        toolbar.setPadding(false);
        
        Button btnTambah = new Button("+ Tambah Jadwal Shift", e -> openAddDialog());
        btnTambah.getStyle().set("background-color", "#10B981").set("color", "white").set("border-radius", "8px").set("font-weight", "600");
        toolbar.add(btnTambah);
        body.add(toolbar);

        // Grid
        grid.addColumn(s -> s.getTanggal() != null ? s.getTanggal().format(DATE_FMT) : "-").setHeader("Tanggal").setSortable(true);
        grid.addColumn(JadwalShift::getJenisShift).setHeader("Waktu Shift").setSortable(true);
        grid.addColumn(s -> s.getJamMulai().format(TIME_FMT) + " - " + s.getJamSelesai().format(TIME_FMT)).setHeader("Jam");
        grid.addColumn(s -> s.getPetugas() != null ? s.getPetugas().getNamaLengkap() : "-").setHeader("Petugas").setSortable(true);
        grid.addColumn(JadwalShift::getZona).setHeader("Zona / Lokasi");
        
        grid.addComponentColumn(s -> {
            Button delBtn = new Button("Hapus", e -> {
                jadwalShiftRepository.delete(s);
                refreshGrid();
                Notification n = new Notification("Shift dihapus.", 3000, Notification.Position.BOTTOM_CENTER);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                n.open();
            });
            delBtn.getStyle().set("color", "#EF4444");
            return delBtn;
        }).setHeader("Aksi");

        grid.getStyle().set("margin-top", "20px").set("border-radius", "12px").set("border", "none").set("box-shadow", "0 1px 3px rgba(0,0,0,0.1)");
        body.add(grid);

        main.add(body);
        
        refreshGrid();
        return main;
    }

    private Div statCard(String icon, String iconClass, String label, String value) {
        Div card = new Div();
        card.addClassName("a-stat-card");
        Div iconBox = new Div();
        iconBox.addClassName("a-stat-icon");
        iconBox.addClassName(iconClass);
        iconBox.add(new Span(icon));
        Div info = new Div();
        info.addClassName("a-stat-info");
        Span lbl = new Span(label);
        lbl.addClassName("a-stat-label");
        Span val = new Span(value);
        val.addClassName("a-stat-value");
        info.add(lbl, val);
        card.add(iconBox, info);
        return card;
    }

    private void refreshGrid() {
        grid.setItems(jadwalShiftRepository.findAll());
    }

    private void openAddDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Tambah Jadwal Shift");
        dialog.setWidth("400px");

        VerticalLayout layout = new VerticalLayout();
        
        DatePicker datePicker = new DatePicker("Tanggal");
        datePicker.setWidthFull();

        ComboBox<JadwalShift.JenisShift> jenisBox = new ComboBox<>("Jenis Shift");
        jenisBox.setItems(JadwalShift.JenisShift.values());
        jenisBox.setWidthFull();

        TimePicker jamMulai = new TimePicker("Jam Mulai");
        jamMulai.setWidthFull();
        
        TimePicker jamSelesai = new TimePicker("Jam Selesai");
        jamSelesai.setWidthFull();

        ComboBox<Pengguna> petugasBox = new ComboBox<>("Petugas");
        List<Pengguna> petugasList = penggunaRepository.findAll().stream().filter(p -> p.getPeran() == Pengguna.Peran.PETUGAS_LAPANGAN).toList();
        petugasBox.setItems(petugasList);
        petugasBox.setItemLabelGenerator(Pengguna::getNamaLengkap);
        petugasBox.setWidthFull();

        ComboBox<String> zonaBox = new ComboBox<>("Zona / Lokasi");
        zonaBox.setItems("Zona A (Pusat)", "Zona B (Utara)", "Zona C (Timur)", "Zona D (Selatan)", "Zona E (Barat)");
        zonaBox.setWidthFull();

        layout.add(datePicker, jenisBox, jamMulai, jamSelesai, petugasBox, zonaBox);
        dialog.add(layout);

        Button saveBtn = new Button("Simpan", e -> {
            if (datePicker.isEmpty() || jenisBox.isEmpty() || jamMulai.isEmpty() || jamSelesai.isEmpty() || petugasBox.isEmpty() || zonaBox.isEmpty()) {
                Notification n = new Notification("Harap isi semua kolom!", 3000, Notification.Position.TOP_CENTER);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                n.open();
                return;
            }
            JadwalShift shift = new JadwalShift();
            shift.setTanggal(datePicker.getValue());
            shift.setJenisShift(jenisBox.getValue());
            shift.setJamMulai(jamMulai.getValue());
            shift.setJamSelesai(jamSelesai.getValue());
            shift.setPetugas(petugasBox.getValue());
            shift.setZona(zonaBox.getValue());
            jadwalShiftRepository.save(shift);
            refreshGrid();
            dialog.close();
            Notification n = new Notification("Jadwal shift berhasil ditambahkan!", 3000, Notification.Position.BOTTOM_CENTER);
            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            n.open();
        });
        saveBtn.addThemeName("primary");
        
        Button cancelBtn = new Button("Batal", e -> dialog.close());

        HorizontalLayout footer = new HorizontalLayout(cancelBtn, saveBtn);
        footer.setJustifyContentMode(HorizontalLayout.JustifyContentMode.END);
        dialog.getFooter().add(footer);

        dialog.open();
    }
}

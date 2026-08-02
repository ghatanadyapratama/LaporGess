package com.example.application.views.admin;

import com.example.application.model.JadwalShift;
import com.example.application.model.Laporan;
import com.example.application.model.Pengguna;
import com.example.application.repository.PenggunaRepository;
import com.example.application.service.JadwalShiftService;
import com.example.application.service.LaporanService;
import com.example.application.views.warga.BlankLayout;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Route(value = "admin/jadwal-petugas", layout = BlankLayout.class)
@PageTitle("Jadwal Petugas - Lapor Gess")
public class AdminJadwalPetugasView extends Div {

    private final JadwalShiftService jadwalShiftService;
    private final LaporanService laporanService;
    private final PenggunaRepository penggunaRepository;

    private LocalDate selectedDate;
    private boolean isEditMode = false;
    private List<Pengguna> activePetugas;
    private List<JadwalShift> currentJadwal;
    
    // Map to hold comboboxes during edit mode: key = "Time_Zone"
    private Map<String, ComboBox<Pengguna>> editFields = new HashMap<>();

    private Div gridContainer;
    private Div legendContainer;
    
    private ComboBox<LocalDate> daySelect;
    private TextField searchField;
    private Button btnEdit;
    private Button btnSave;

    public AdminJadwalPetugasView(JadwalShiftService jadwalShiftService, LaporanService laporanService, PenggunaRepository penggunaRepository) {
        this.jadwalShiftService = jadwalShiftService;
        this.laporanService = laporanService;
        this.penggunaRepository = penggunaRepository;
        
        this.activePetugas = penggunaRepository.findByStatusAndPeran(Pengguna.Status.AKTIF, Pengguna.Peran.PETUGAS_LAPANGAN);
        // Default to today
        this.selectedDate = LocalDate.now();
        
        addClassName("ad-root");
        long laporanPending = laporanService.countByStatus(Laporan.Status.PENDING);
        long petugasAktif = activePetugas.size();
        long verifikasiPending = penggunaRepository.countByStatus(Pengguna.Status.PENDING);
        Div sidebar = AdminLayout.buildSidebar("admin/jadwal-petugas", laporanPending, petugasAktif, verifikasiPending);
        Div main = new Div();
        main.addClassName("ad-main");
        Div topbar = AdminLayout.buildTopbar("Jadwal Petugas Lapangan");
        Div body = new Div();
        body.addClassName("ad-body");

        body.add(buildStatCards());
        body.add(buildControlsBar());
        
        Div wrap = new Div();
        wrap.addClassName("ad-schedule-grid-wrap");
        gridContainer = new Div();
        gridContainer.addClassName("ad-card");
        gridContainer.getStyle().set("padding", "0").set("overflow", "hidden");
        
        legendContainer = new Div();
        legendContainer.addClassName("ad-card");
        
        wrap.add(gridContainer, legendContainer);
        body.add(wrap);

        main.add(topbar, body);
        add(sidebar, main);

        refreshDataAndUI();
    }

    private Div buildStatCards() {
        Div grid = new Div();
        grid.addClassName("ad-stats-grid");

        long totalPetugas = activePetugas.size();
        long diproses = laporanService.countByStatus(Laporan.Status.DIPROSES);
        long selesai = laporanService.countByStatus(Laporan.Status.SELESAI);

        grid.add(createStatCard("Petugas Aktif", String.valueOf(totalPetugas), "👥", "ad-stat-bg-blue"));
        grid.add(createStatCard("Laporan Diproses", String.valueOf(diproses), "⏱", "ad-stat-bg-yellow"));
        grid.add(createStatCard("Laporan Selesai", String.valueOf(selesai), "✔", "ad-stat-bg-teal"));

        return grid;
    }

    private Div createStatCard(String label, String value, String iconSymbol, String bgClass) {
        Div card = new Div();
        card.addClassName("ad-stat-card");
        Div iconBox = new Div(new Span(iconSymbol));
        iconBox.addClassName("ad-stat-icon-wrapper");
        iconBox.addClassName(bgClass);
        Div info = new Div();
        info.addClassName("ad-stat-info");
        Span lbl = new Span(label);
        lbl.addClassName("ad-stat-label");
        Span val = new Span(value);
        val.addClassName("ad-stat-value");
        info.add(lbl, val);
        card.add(iconBox, info);
        return card;
    }

    private Div buildControlsBar() {
        Div bar = new Div();
        bar.addClassName("ad-controls-bar");

        Div left = new Div();
        left.addClassName("ad-controls-left");

        searchField = new TextField();
        searchField.setPlaceholder("Cari nama petugas...");
        searchField.getStyle().set("width", "240px").set("margin-right", "16px");
        searchField.addValueChangeListener(e -> buildLegend());

        // We use a combobox to select a date in the current week
        daySelect = new ComboBox<>();
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        List<LocalDate> weekDays = new ArrayList<>();
        for(int i=0; i<7; i++) {
            weekDays.add(startOfWeek.plusDays(i));
        }
        daySelect.setItems(weekDays);
        daySelect.setValue(LocalDate.now());
        daySelect.setItemLabelGenerator(date -> date.format(DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy", new Locale("id", "ID"))));
        daySelect.getStyle().set("width", "260px");
        daySelect.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                selectedDate = e.getValue();
                isEditMode = false;
                refreshDataAndUI();
            }
        });

        left.add(searchField, daySelect);

        Div right = new Div();
        right.addClassName("ad-controls-right");

        btnEdit = new Button("⚙ Edit Jadwal");
        btnEdit.addClassName("ad-btn-secondary");
        btnEdit.addClickListener(e -> {
            isEditMode = true;
            refreshUI();
        });

        btnSave = new Button("📝 Simpan");
        btnSave.getStyle().set("background-color", "#087F6B").set("color", "white").set("border-radius", "8px").set("font-weight", "600").set("padding", "8px 20px");
        btnSave.addClickListener(e -> saveSchedules());

        right.add(btnEdit, btnSave);

        bar.add(left, right);
        return bar;
    }


    private void saveSchedules() {
        if (!isEditMode) return;
        
        // Remove existing schedules for this date
        jadwalShiftService.deleteAllByRentang(selectedDate, selectedDate);
        
        List<JadwalShift> toSave = new ArrayList<>();
        
        LocalTime[][] slots = {
            {LocalTime.of(6, 0), LocalTime.of(8, 0)},
            {LocalTime.of(8, 0), LocalTime.of(10, 0)},
            {LocalTime.of(10, 0), LocalTime.of(12, 0)},
            {LocalTime.of(13, 0), LocalTime.of(15, 0)},
            {LocalTime.of(15, 0), LocalTime.of(17, 0)}
        };
        String[] zones = {"Zona A", "Zona B", "Zona C", "Zona D", "Zona E"};

        for (LocalTime[] slot : slots) {
            for (String zone : zones) {
                String key = slot[0].toString() + "_" + zone;
                ComboBox<Pengguna> cb = editFields.get(key);
                if (cb != null && cb.getValue() != null) {
                    JadwalShift js = new JadwalShift();
                    js.setTanggal(selectedDate);
                    js.setJamMulai(slot[0]);
                    js.setJamSelesai(slot[1]);
                    js.setZona(zone);
                    js.setPetugas(cb.getValue());
                    js.setJenisShift(slot[0].isBefore(LocalTime.of(12, 0)) ? JadwalShift.JenisShift.PAGI : JadwalShift.JenisShift.SIANG);
                    js.setKeterangan("Manual");
                    toSave.add(js);
                }
            }
        }
        
        jadwalShiftService.saveAll(toSave);
        Notification.show("Jadwal hari ini berhasil disimpan!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        isEditMode = false;
        refreshDataAndUI();
    }

    private void refreshDataAndUI() {
        currentJadwal = jadwalShiftService.getJadwalTanggal(selectedDate);
        refreshUI();
    }

    private void refreshUI() {
        btnEdit.setVisible(!isEditMode);
        btnSave.setVisible(isEditMode);
        daySelect.setEnabled(!isEditMode);
        
        buildGrid();
        buildLegend();
    }

    private void buildGrid() {
        gridContainer.removeAll();
        editFields.clear();
        
        HtmlContainer table = new HtmlContainer("table");
        table.addClassName("ad-table");
        table.getStyle().set("width", "100%").set("border-collapse", "collapse");

        HtmlContainer thead = new HtmlContainer("thead");
        HtmlContainer trHead = new HtmlContainer("tr");
        trHead.add(new HtmlContainer("th")); // Jam
        String[] zones = {"Zona A", "Zona B", "Zona C", "Zona D", "Zona E"};
        
        HtmlContainer thJam = new HtmlContainer("th");
        thJam.setText("Jam");
        trHead.add(thJam);
        
        for (String z : zones) {
            HtmlContainer thZ = new HtmlContainer("th");
            thZ.setText(z);
            trHead.add(thZ);
        }
        thead.add(trHead);
        table.add(thead);

        HtmlContainer tbody = new HtmlContainer("tbody");

        LocalTime[][] slots = {
            {LocalTime.of(6, 0), LocalTime.of(8, 0)},
            {LocalTime.of(8, 0), LocalTime.of(10, 0)},
            {LocalTime.of(10, 0), LocalTime.of(12, 0)},
            {LocalTime.of(13, 0), LocalTime.of(15, 0)},
            {LocalTime.of(15, 0), LocalTime.of(17, 0)}
        };

        for (int i=0; i<slots.length; i++) {
            if (i == 3) {
                // Break row
                HtmlContainer breakRow = new HtmlContainer("tr");
                breakRow.addClassName("ad-row-break");
                HtmlContainer td = new HtmlContainer("td");
                td.setText("I S T I R A H A T");
                td.getElement().setAttribute("colspan", "6");
                td.getStyle().set("text-align", "center").set("font-weight", "600").set("padding", "8px");
                breakRow.add(td);
                tbody.add(breakRow);
            }

            LocalTime[] slot = slots[i];
            HtmlContainer tr = new HtmlContainer("tr");
            String timeStr = String.format("%02d.00 - %02d.00", slot[0].getHour(), slot[1].getHour());
            HtmlContainer tdJam = new HtmlContainer("td");
            tdJam.setText(timeStr);
            tr.add(tdJam);

            for (String zone : zones) {
                HtmlContainer td = new HtmlContainer("td");
                
                // Find existing shift
                JadwalShift shift = currentJadwal.stream()
                        .filter(j -> j.getJamMulai().equals(slot[0]) && j.getZona().equals(zone))
                        .findFirst().orElse(null);

                if (isEditMode) {
                    ComboBox<Pengguna> cb = new ComboBox<>();
                    cb.setItems(activePetugas);
                    cb.setItemLabelGenerator(Pengguna::getNamaLengkap);
                    cb.getStyle().set("width", "120px");
                    if (shift != null) {
                        cb.setValue(shift.getPetugas());
                    }
                    editFields.put(slot[0].toString() + "_" + zone, cb);
                    td.add(cb);
                } else {
                    if (shift != null && shift.getPetugas() != null) {
                        String name = shift.getPetugas().getNamaLengkap();
                        String initial = (name != null && name.length() > 1) ? name.substring(0, 2).toUpperCase() : "?";
                        Span pill = new Span(initial);
                        pill.addClassName("ad-pill");
                        pill.addClassName(getPillClass(shift.getPetugas().getId()));
                        td.add(pill);
                    } else {
                        td.add(new Span("-"));
                    }
                }
                tr.add(td);
            }
            tbody.add(tr);
        }

        table.add(tbody);
        gridContainer.add(table);
    }

    private void buildLegend() {
        legendContainer.removeAll();
        
        Span title = new Span("Daftar Petugas");
        title.addClassName("ad-card-title");

        Div list = new Div();
        list.addClassName("ad-officer-list");

        String keyword = searchField != null && searchField.getValue() != null ? searchField.getValue().toLowerCase() : "";

        for (Pengguna p : activePetugas) {
            String name = p.getNamaLengkap();
            if (!keyword.isEmpty() && name != null && !name.toLowerCase().contains(keyword)) {
                continue; // Skip if it doesn't match search
            }
            
            String initial = (name != null && name.length() > 1) ? name.substring(0, 2).toUpperCase() : "?";
            
            Div item = new Div();
            item.addClassName("ad-officer-item");

            Span pill = new Span(initial);
            pill.addClassName("ad-pill");
            pill.addClassName(getPillClass(p.getId()));

            Span nameTxt = new Span(name);
            nameTxt.addClassName("ad-officer-name");

            item.add(pill, nameTxt);
            list.add(item);
        }

        legendContainer.add(title, list);
    }
    
    private String getPillClass(Integer petugasId) {
        String[] classes = {"ad-pill-blue", "ad-pill-pink", "ad-pill-teal", "ad-pill-orange", "ad-pill-purple", "ad-pill-green", "ad-pill-yellow"};
        if (petugasId == null) return classes[0];
        return classes[petugasId % classes.length];
    }
}

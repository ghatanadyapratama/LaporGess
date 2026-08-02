package com.example.application.views.warga;

import com.example.application.model.Pengguna;
import com.example.application.repository.PenggunaRepository;
import com.example.application.service.SessionManager;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.List;

@Route(value = "edukasi", layout = BlankLayout.class)
@PageTitle("Pusat Edukasi Lingkungan - Lapor Gess")
public class EdukasiView extends Div {

    private final PenggunaRepository penggunaRepository;
    private Div articlesGrid;
    private Div articleReaderModal;
    private List<Article> articles = new ArrayList<>();
    private String activeCategory = "Semua";
    private String searchQuery = "";
    private Pengguna currentUser;

    public EdukasiView(PenggunaRepository penggunaRepository) {
        this.penggunaRepository = penggunaRepository;
        String username = SessionManager.getUsername();
        if (username != null) {
            currentUser = penggunaRepository.findByUsername(username).orElse(null);
        }
        addClassName("d-root");
        add(buildSidebar(), buildMain());

        // Initialize articles mock data
        initData();
        renderArticles();

        // Build and attach reader modal (hidden by default)
        articleReaderModal = buildReaderModal();
        add(articleReaderModal);
    }

    // ══════════════════════════════════════════
    //  SIDEBAR
    // ══════════════════════════════════════════
    private Div buildSidebar() {
        Div sidebar = new Div();
        sidebar.addClassName("d-sidebar");

        Div logo = new Div();
        logo.addClassName("d-logo");
        Image logoImg = new Image("icons/logoLaporGess.png", "logo");
        logoImg.addClassName("d-logo-img");
        Span logoTxt = new Span("Lapor Gess");
        logoTxt.addClassName("d-logo-txt");
        logo.add(logoImg, logoTxt);
        sidebar.add(logo);

        Div nav = new Div();
        nav.addClassName("d-nav");

        Div homeItem = navItem("icons/home.png", "Beranda", false);
        homeItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("dashboard")));
        nav.add(homeItem);

        Div laporanItem = navItem("icons/laporan.png", "Laporan Saya", false);
        laporanItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("laporan-saya")));
        nav.add(laporanItem);

        Div peringkatItem = navItem("icons/iconPiala.png", "Peringkat", false);
        peringkatItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("peringkat")));
        nav.add(peringkatItem);

        Div hadiahItem = navItem("icons/hadiah.png", "Toko Hadiah", false);
        hadiahItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("toko-hadiah")));
        nav.add(hadiahItem);

        // Edukasi Menu (Active in this view) with icons/buku.png icon
        Div edukasiItem = navItem("icons/buku.png", "Edukasi", true);
        nav.add(edukasiItem);

        Div profilItem = navItem("icons/profile.png", "Profil", false);
        profilItem.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("profil")));
        nav.add(profilItem);

        sidebar.add(nav);

        Div sp = new Div();
        sp.addClassName("d-sidebar-spacer");
        sidebar.add(sp);

        Div cta = new Div();
        cta.addClassName("d-cta");
        cta.add(new Span("+ Buat Laporan"));
        cta.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("buat-laporan")));
        sidebar.add(cta);

        return sidebar;
    }

    private Div navItem(String icon, String label, boolean active) {
        Div item = new Div();
        item.addClassName("d-nav-item");
        if (active) item.addClassName("d-nav-active");
        Image img = new Image(icon, label);
        img.addClassName("d-nav-icon");
        Span txt = new Span(label);
        txt.addClassName("d-nav-label");
        item.add(img, txt);
        return item;
    }

    // ══════════════════════════════════════════
    //  MAIN CONTENT
    // ══════════════════════════════════════════
    private Div buildMain() {
        Div main = new Div();
        main.addClassName("d-main");
        main.add(buildTopbar());
        main.add(buildBody());
        return main;
    }

    private Div buildTopbar() {
        Div bar = new Div();
        bar.addClassName("d-topbar");
        Span title = new Span("Pusat Edukasi Lingkungan");
        title.addClassName("d-topbar-title");
        bar.add(title);

        Div right = new Div();
        right.addClassName("d-topbar-right");

        Div badge = new Div();
        badge.addClassName("d-poin-badge");
        Image trophy = new Image("icons/pialaOren.png", "poin");
        trophy.addClassName("d-poin-icon");
        int poin = currentUser != null && currentUser.getPoin() != null ? currentUser.getPoin() : 0;
        Span poinTxt = new Span(String.format("%,d Poin", poin).replace(',', '.'));
        poinTxt.addClassName("d-poin-txt");
        badge.add(trophy, poinTxt);

        Div bell = new Div();
        bell.addClassName("d-bell");
        Image bellImg = new Image("icons/bell.png", "notif");
        bellImg.addClassName("d-bell-img");
        bell.add(bellImg);
        bell.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("notifikasi")));

        Div av = new Div();
        av.addClassName("d-avatar");
        String initials = "U";
        if (currentUser != null && currentUser.getNamaLengkap() != null && !currentUser.getNamaLengkap().isEmpty()) {
            String[] parts = currentUser.getNamaLengkap().trim().split(" ");
            initials = parts.length > 1
                ? (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase()
                : currentUser.getNamaLengkap().substring(0, 1).toUpperCase();
        }
        av.add(new Span(initials));

        right.add(badge, bell, av);
        bar.add(right);
        return bar;
    }

    private Div buildBody() {
        Div body = new Div();
        body.addClassNames("d-body", "ed-body");

        // Welcome Header Banner for Edukasi
        Div headerBanner = new Div();
        headerBanner.addClassName("ed-banner");
        
        Div bannerLeft = new Div();
        bannerLeft.addClassName("ed-banner-left");
        H2 bannerTitle = new H2("Trash-Pedia 📚");
        bannerTitle.addClassName("ed-banner-title");
        Paragraph bannerSub = new Paragraph("Temukan tips, panduan, dan artikel menarik seputar pengelolaan sampah dan pelestarian lingkungan di sekitar kita.");
        bannerSub.addClassName("ed-banner-sub");
        bannerLeft.add(bannerTitle, bannerSub);
        headerBanner.add(bannerLeft);
        body.add(headerBanner);

        // Search and Filter controls row
        Div controlsRow = new Div();
        controlsRow.addClassName("ed-controls");

        // Filter chips
        Div categories = new Div();
        categories.addClassName("ed-categories");
        categories.add(buildCategoryChip("Semua"));
        categories.add(buildCategoryChip("Pemilahan"));
        categories.add(buildCategoryChip("Kompos"));
        categories.add(buildCategoryChip("Daur Ulang"));
        categories.add(buildCategoryChip("Gaya Hidup"));

        // Search Input
        TextField search = new TextField();
        search.addClassName("ed-search");
        search.setPlaceholder("Cari artikel...");
        search.addValueChangeListener(e -> {
            searchQuery = e.getValue().trim().toLowerCase();
            renderArticles();
        });

        controlsRow.add(categories, search);
        body.add(controlsRow);

        // Articles Grid List
        articlesGrid = new Div();
        articlesGrid.addClassName("ed-grid");
        body.add(articlesGrid);

        return body;
    }

    private Div buildCategoryChip(String name) {
        Div chip = new Div();
        chip.addClassName("ed-chip");
        if (name.equals(activeCategory)) {
            chip.addClassName("ed-chip-active");
        }
        chip.add(new Span(name));
        chip.addClickListener(e -> {
            activeCategory = name;
            // update classes on chips dynamically
            chip.getParent().ifPresent(parent -> {
                parent.getChildren().forEach(child -> {
                    if (child instanceof Div) {
                        ((Div) child).removeClassName("ed-chip-active");
                    }
                });
            });
            chip.addClassName("ed-chip-active");
            renderArticles();
        });
        return chip;
    }

    private void initData() {
        articles.clear();

        articles.add(new Article(
            "Dasar Pemilahan Sampah",
            "Pelajari cara memisahkan sampah organik dan anorganik dengan benar untuk menjaga lingkungan dan mempermudah proses daur ulang.",
            "Pemilahan",
            "5 Min Baca",
            "icons/iconCeklist.png",
            "<h2>Mengapa Memilah Sampah itu Penting?</h2>" +
            "<p>Memilah sampah adalah langkah awal yang sangat krusial dalam siklus daur ulang. Ketika sampah tercampur, bahan yang sebenarnya dapat didaur ulang seperti kertas atau kardus menjadi kotor dan tidak lagi bernilai ekonomis.</p>" +
            "<h3>Kategori Utama Sampah Rumah Tangga</h3>" +
            "<ul>" +
            "<li><strong>Sampah Organik:</strong> Sisa makanan, sayuran, buah-buahan, daun kering. Sampah jenis ini mudah membusuk dan bisa diolah menjadi kompos.</li>" +
            "<li><strong>Sampah Anorganik:</strong> Plastik, kertas, kaca, logam. Bahan-bahan ini sulit membusuk namun memiliki nilai daur ulang yang tinggi.</li>" +
            "<li><strong>Sampah B3 (Bahan Berbahaya & Beracun):</strong> Baterai bekas, lampu neon, kemasan obat-obatan, barang elektronik. Sampah ini memerlukan penanganan khusus agar tidak mencemari tanah.</li>" +
            "</ul>" +
            "<h3>Langkah Sederhana Memulai dari Rumah</h3>" +
            "<p>Sediakan minimal dua tempat sampah terpisah di dapur Anda: satu untuk organik, dan satu lagi untuk anorganik bersih. Pastikan botol plastik atau kaleng dibilas terlebih dahulu sebelum dibuang agar tidak mengundang semut atau serangga.</p>"
        ));

        articles.add(new Article(
            "Membuat Kompos Sendiri",
            "Ubah sampah dapur organik menjadi pupuk organik cair dan padat yang kaya akan nutrisi bagi tanaman Anda.",
            "Kompos",
            "8 Min Baca",
            "icons/pialaOren.png",
            "<h2>Cara Membuat Kompos Rumah Tangga dengan Metode Takakura</h2>" +
            "<p>Membuat kompos dari sisa dapur adalah cara terbaik untuk mengurangi jumlah sampah yang dibuang ke Tempat Pembuangan Akhir (TPA).</p>" +
            "<h3>Bahan yang Dibutuhkan:</h3>" +
            "<ul>" +
            "<li>Sisa sayuran dan buah (cincang kasar)</li>" +
            "<li>Sekam padi atau serbuk gergaji (sebagai karbon)</li>" +
            "<li>Starter bakteri (EM4) atau kompos matang sebagai ragi</li>" +
            "<li>Wadah berlubang/komposter</li>" +
            "</ul>" +
            "<h3>Langkah Pembuatan:</h3>" +
            "<p>Masukkan starter/ragi ke dasar wadah komposter. Tambahkan sampah dapur organik yang sudah dipotong kecil-kecil secara berkala. Setiap memasukkan sampah baru, lapisi atasnya dengan sekam atau serbuk kayu untuk menyerap kelembaban dan mencegah bau tidak sedap. Aduk seminggu sekali untuk sirkulasi oksigen.</p>"
        ));

        articles.add(new Article(
            "Bahaya Sampah Plastik",
            "Dampak negatif penggunaan plastik sekali pakai bagi kelestarian laut, ekosistem satwa, dan kesehatan manusia.",
            "Gaya Hidup",
            "6 Min Baca",
            "icons/iconWaktu.png",
            "<h2>Melawan Gelombang Mikroplastik</h2>" +
            "<p>Plastik memerlukan waktu ratusan tahun untuk terurai. Alih-alih hilang sepenuhnya, plastik hancur menjadi partikel super kecil yang disebut mikroplastik. Mikroplastik ini kini telah ditemukan di air minum, ikan di laut, bahkan di dalam tubuh manusia.</p>" +
            "<h3>Gerakan 3R (Reduce, Reuse, Recycle)</h3>" +
            "<p>Langkah terbaik adalah mengurangi konsumsi (Reduce). Bawa botol minum sendiri (tumbler), gunakan tas belanja kain yang bisa dipakai ulang, dan hindari pemakaian sedotan plastik sekali pakai. Dukung kebijakan pelarangan kantong plastik di wilayah RT/RW Anda.</p>"
        ));

        articles.add(new Article(
            "Panduan Bank Sampah",
            "Dapatkan keuntungan finansial dan poin Lapor Gess dengan menabungkan sampah plastik dan kertas Anda.",
            "Daur Ulang",
            "4 Min Baca",
            "icons/ceklistAbu.png",
            "<h2>Mengubah Sampah Menjadi Emas dan Poin</h2>" +
            "<p>Bank Sampah RT/RW adalah wadah bagi warga untuk menyetorkan sampah anorganik yang sudah dipilah terlebih dahulu di rumah.</p>" +
            "<h3>Bagaimana Cara Kerjanya?</h3>" +
            "<p>Bersihkan sampah anorganik Anda (seperti botol plastik PET, kardus, koran, kaleng aluminium). Bawa ke loket Bank Sampah terdekat pada jadwal penyetoran. Sampah akan ditimbang dan dihargai sesuai ketentuan berat per kilogram. Hasil penjualan bisa ditabung dalam bentuk uang kas RT atau langsung dikonversikan ke dalam saldo poin Lapor Gess Anda!</p>"
        ));
    }

    private void renderArticles() {
        articlesGrid.removeAll();

        for (Article art : articles) {
            // Apply category filter
            if (!activeCategory.equals("Semua") && !art.getCategory().equalsIgnoreCase(activeCategory)) {
                continue;
            }

            // Apply search filter
            if (!searchQuery.isEmpty() && 
                !art.getTitle().toLowerCase().contains(searchQuery) && 
                !art.getDescription().toLowerCase().contains(searchQuery)) {
                continue;
            }

            Div card = new Div();
            card.addClassName("ed-card");

            Div imgBox = new Div();
            imgBox.addClassName("ed-card-img-box");
            
            // Set dynamic background color based on category
            if (art.getCategory().equals("Pemilahan")) {
                imgBox.addClassName("ed-img-green");
            } else if (art.getCategory().equals("Kompos")) {
                imgBox.addClassName("ed-img-orange");
            } else if (art.getCategory().equals("Daur Ulang")) {
                imgBox.addClassName("ed-img-blue");
            } else {
                imgBox.addClassName("ed-img-teal");
            }

            Image icon = new Image(art.getIconPath(), art.getTitle());
            icon.addClassName("ed-card-icon");
            imgBox.add(icon);
            card.add(imgBox);

            Div content = new Div();
            content.addClassName("ed-card-content");

            Div meta = new Div();
            meta.addClassName("ed-card-meta");
            Span cat = new Span(art.getCategory());
            cat.addClassName("ed-card-category");
            Span readTime = new Span(art.getReadTime());
            readTime.addClassName("ed-card-time");
            meta.add(cat, readTime);

            H3 title = new H3(art.getTitle());
            title.addClassName("ed-card-title");

            Paragraph desc = new Paragraph(art.getDescription());
            desc.addClassName("ed-card-desc");

            Div footer = new Div();
            footer.addClassName("ed-card-footer");
            
            Span readBtn = new Span("Baca Artikel →");
            readBtn.addClassName("ed-read-btn");
            footer.add(readBtn);

            content.add(meta, title, desc, footer);
            card.add(content);

            // Open reader modal on card click
            card.addClickListener(e -> openReader(art));

            articlesGrid.add(card);
        }
    }

    // ══════════════════════════════════════════
    //  ARTICLE READER MODAL
    // ══════════════════════════════════════════
    private Div buildReaderModal() {
        Div overlay = new Div();
        overlay.addClassName("ed-overlay");
        overlay.getElement().setAttribute("id", "ed-overlay");

        Div dialog = new Div();
        dialog.addClassName("ed-dialog");

        Div header = new Div();
        header.addClassName("ed-modal-header");
        
        Span modalMeta = new Span();
        modalMeta.addClassName("ed-modal-meta");
        modalMeta.getElement().setAttribute("id", "ed-modal-meta");

        NativeButton closeBtn = new NativeButton("×");
        closeBtn.addClassName("ed-close-btn");
        closeBtn.addClickListener(e -> closeReader());
        header.add(modalMeta, closeBtn);
        dialog.add(header);

        Div body = new Div();
        body.addClassName("ed-modal-body");

        H1 modalTitle = new H1();
        modalTitle.addClassName("ed-modal-title");
        modalTitle.getElement().setAttribute("id", "ed-modal-title");
        body.add(modalTitle);

        Div modalHtml = new Div();
        modalHtml.addClassName("ed-modal-html");
        modalHtml.getElement().setAttribute("id", "ed-modal-html");
        body.add(modalHtml);

        dialog.add(body);
        overlay.add(dialog);

        overlay.addClickListener(e -> closeReader());
        dialog.addClickListener(e -> e.getSource());

        return overlay;
    }

    private void openReader(Article art) {
        articleReaderModal.getElement().executeJs(
            "document.getElementById('ed-modal-meta').innerText = $0 + ' • ' + $1;" +
            "document.getElementById('ed-modal-title').innerText = $2;" +
            "document.getElementById('ed-modal-html').innerHTML = $3;",
            art.getCategory().toUpperCase(), art.getReadTime(),
            art.getTitle(), art.getContentHtml()
        );
        articleReaderModal.addClassName("ed-overlay-visible");
    }

    private void closeReader() {
        articleReaderModal.removeClassName("ed-overlay-visible");
    }

    // Helper data model representing an article
    private static class Article {
        private String title;
        private String description;
        private String category;
        private String readTime;
        private String iconPath;
        private String contentHtml;

        public Article(String title, String description, String category, String readTime, String iconPath, String contentHtml) {
            this.title = title;
            this.description = description;
            this.category = category;
            this.readTime = readTime;
            this.iconPath = iconPath;
            this.contentHtml = contentHtml;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getCategory() { return category; }
        public String getReadTime() { return readTime; }
        public String getIconPath() { return iconPath; }
        public String getContentHtml() { return contentHtml; }
    }
}

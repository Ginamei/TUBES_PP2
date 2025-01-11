package view;

import controller.KonversiPoinController;
import model.JenisSampah;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import model.Database;

import controller.KonversiPoinPDFExporter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class KonversiPoinView extends JPanel {
    private KonversiPoinController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<ComboItem> cbJenisSampah;
    private JTextField txtPoin;
    private JComboBox<String> cbStatus;
    private int selectedId = -1;
    private JButton btnExportPDF;

    // Inner class untuk combo box item
    private class ComboItem {
        private int id;
        private String nama;
        
        public ComboItem(int id, String nama) {
            this.id = id;
            this.nama = nama;
        }
        
        @Override
        public String toString() {
            return nama;
        }
        
        public int getId() {
            return id;
        }
    }

    public KonversiPoinView() {
        controller = new KonversiPoinController();
        setLayout(new BorderLayout());
        initComponents();
        loadJenisSampah();
        loadData();
    }

    private void initComponents() {
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Jenis Sampah:"));
        cbJenisSampah = new JComboBox<>();
        formPanel.add(cbJenisSampah);

        formPanel.add(new JLabel("Poin:"));
        txtPoin = new JTextField();
        formPanel.add(txtPoin);

        formPanel.add(new JLabel("Status:"));
        cbStatus = new JComboBox<>(new String[]{"Aktif", "Nonaktif"});
        formPanel.add(cbStatus);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        JButton btnClear = new JButton("Clear");
        btnExportPDF = new JButton("Export PDF");

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnExportPDF);
        
        // Table
        String[] columns = {"ID", "Jenis Sampah", "Kategori", "Poin", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add components to panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Event Listeners
        btnTambah.addActionListener(e -> {
            try {
                ComboItem selectedItem = (ComboItem) cbJenisSampah.getSelectedItem();
                if (selectedItem == null) {
                    JOptionPane.showMessageDialog(this, "Pilih jenis sampah terlebih dahulu!");
                    return;
                }
                
                double poin = Double.parseDouble(txtPoin.getText());
                controller.createKonversiPoin(selectedItem.getId(), poin);
                loadData();
                clearForm();
                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Input poin tidak valid!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error database: " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnEdit.addActionListener(e -> {
            if (selectedId != -1) {
                try {
                    ComboItem selectedItem = (ComboItem) cbJenisSampah.getSelectedItem();
                    if (selectedItem == null) {
                        JOptionPane.showMessageDialog(this, "Pilih jenis sampah terlebih dahulu!");
                        return;
                    }
                    
                    double poin = Double.parseDouble(txtPoin.getText());
                    String status = cbStatus.getSelectedItem().toString();
                    controller.updateKonversiPoin(selectedId, selectedItem.getId(), poin, status);
                    loadData();
                    clearForm();
                    JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Input poin tidak valid!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error database: " + ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });

        btnHapus.addActionListener(e -> {
            if (selectedId != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Apakah Anda yakin ingin menghapus data ini?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        controller.deleteKonversiPoin(selectedId);
                        loadData();
                        clearForm();
                        JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Error database: " + ex.getMessage());
                    }
                }
            }
        });

        btnClear.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    selectedId = (int) table.getValueAt(row, 0);
                    String jenisNama = (String) table.getValueAt(row, 1);
                    // Cari dan set jenis sampah di combo box
                    for (int i = 0; i < cbJenisSampah.getItemCount(); i++) {
                        ComboItem item = cbJenisSampah.getItemAt(i);
                        if (item.toString().equals(jenisNama)) {
                            cbJenisSampah.setSelectedIndex(i);
                            break;
                        }
                    }
                    txtPoin.setText(table.getValueAt(row, 3).toString());
                    cbStatus.setSelectedItem(table.getValueAt(row, 4));
                }
            }
        });
        
        btnExportPDF.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Simpan PDF");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".pdf")) {
                    filePath += ".pdf";
                }

                try {
                    List<Map<String, Object>> dataList = controller.getKonversiPoinWithJenisSampah();
                    KonversiPoinPDFExporter exporter = new KonversiPoinPDFExporter(dataList);
                    exporter.export(filePath);

                    JOptionPane.showMessageDialog(this,
                        "PDF berhasil disimpan!",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                        "Gagal menyimpan PDF: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void loadJenisSampah() {
        try {
            cbJenisSampah.removeAllItems();
            List<JenisSampah> jenisList = controller.getAllJenisSampah();
            for (JenisSampah jenis : jenisList) {
                cbJenisSampah.addItem(new ComboItem(jenis.getId(), jenis.getNama()));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading jenis sampah: " + ex.getMessage());
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            List<Map<String, Object>> list = Database.readKonversiPoinWithJenisSampah();
            for (Map<String, Object> row : list) {
                Object[] tableRow = {
                    row.get("id"),
                    row.get("jenis_nama"),
                    row.get("kategori"),
                    row.get("poin"),
                    row.get("status")
                };
                tableModel.addRow(tableRow);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
        }
    }

    private void clearForm() {
        cbJenisSampah.setSelectedIndex(-1);
        txtPoin.setText("");
        cbStatus.setSelectedIndex(0);
        selectedId = -1;
        table.clearSelection();
    }
}
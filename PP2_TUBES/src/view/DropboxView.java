package view;

import controller.DropboxController;
import model.Dropbox;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import controller.DropboxPDFExporter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DropboxView extends JPanel {
    private DropboxController controller;
    private JTextField namaField;
    private JTextField lokasiField;
    private JTextField kapasitasField;
    private JComboBox<String> statusCombo;
    private JTable table;
    private DefaultTableModel tableModel;
    private int selectedId = -1;
    private JButton btnExportPDF;
    
    // Constructor
    public DropboxView() {
        controller = new DropboxController();
        setLayout(new BorderLayout());
        initComponents();
        refreshTable();
    }

    // Inisialisasi semua komponen
    private void initComponents() {
        createFormPanel();
        createButtonPanel();
        createTable();
        setupLayout();
        setupListeners();
    }

    // Membuat panel form input
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Nama field
        addFormField(formPanel, gbc, "Nama:", namaField = new JTextField(20), 0);

        // Lokasi field
        addFormField(formPanel, gbc, "Lokasi:", lokasiField = new JTextField(20), 1);

        // Kapasitas field
        addFormField(formPanel, gbc, "Kapasitas:", kapasitasField = new JTextField(20), 2);

        // Status combo box
        String[] statusOptions = {"Tersedia", "Penuh", "Rusak"};
        statusCombo = new JComboBox<>(statusOptions);
        addFormField(formPanel, gbc, "Status:", statusCombo, 3);

        return formPanel;
    }

    // Helper method untuk menambahkan field ke form
    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent component, int gridy) {
        gbc.gridx = 0;
        gbc.gridy = gridy;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    // Membuat panel button
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton addButton = createButton("Tambah", e -> handleAdd());
        JButton updateButton = createButton("Update", e -> handleUpdate());
        JButton deleteButton = createButton("Hapus", e -> handleDelete());
        JButton clearButton = createButton("Clear", e -> clearFields());
        JButton btnExportPDF = createButton("Export PDF", e -> setupExportPDFListener());
        

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(btnExportPDF); 

        return buttonPanel;
    }

    // Helper method untuk membuat button
    private JButton createButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        return button;
    }

    // Membuat tabel
    private void createTable() {
        String[] columns = {"ID", "Nama", "Lokasi", "Kapasitas", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        table = new JTable(tableModel);
    }

    // Mengatur layout utama
    private void setupLayout() {
        add(createFormPanel(), BorderLayout.NORTH);
        add(createButtonPanel(), BorderLayout.CENTER);
        add(new JScrollPane(table), BorderLayout.SOUTH);
    }

    // Mengatur listeners
    private void setupListeners() {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleTableSelection();
            }
        });
    }

    // Validasi input
    private boolean validateInput() {
        // Validasi nama
        if (namaField.getText().trim().isEmpty()) {
            showError("Nama tidak boleh kosong!");
            namaField.requestFocus();
            return false;
        }

        // Validasi lokasi
        if (lokasiField.getText().trim().isEmpty()) {
            showError("Lokasi tidak boleh kosong!");
            lokasiField.requestFocus();
            return false;
        }

        // Validasi kapasitas
        String kapasitasText = kapasitasField.getText().trim();
        if (kapasitasText.isEmpty()) {
            showError("Kapasitas tidak boleh kosong!");
            kapasitasField.requestFocus();
            return false;
        }

        try {
            int kapasitas = Integer.parseInt(kapasitasText);
            if (kapasitas <= 0) {
                showError("Kapasitas harus lebih besar dari 0!");
                kapasitasField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Kapasitas harus berupa angka!");
            kapasitasField.requestFocus();
            return false;
        }

        return true;
    }

    // Handler untuk tombol Tambah
    private void handleAdd() {
        if (validateInput()) {
            try {
                int kapasitas = Integer.parseInt(kapasitasField.getText().trim());
                controller.addDropbox(
                    namaField.getText().trim(),
                    lokasiField.getText().trim(),
                    kapasitas,
                    statusCombo.getSelectedItem().toString()
                );
                refreshTable();
                clearFields();
                showSuccess("Data berhasil ditambahkan!");
            } catch (Exception e) {
                showError("Error saat menambah data: " + e.getMessage());
            }
        }
    }

    // Handler untuk tombol Update
    private void handleUpdate() {
        if (selectedId == -1) {
            showError("Pilih data yang akan diupdate!");
            return;
        }

        if (validateInput()) {
            try {
                int kapasitas = Integer.parseInt(kapasitasField.getText().trim());
                controller.updateDropbox(
                    selectedId,
                    namaField.getText().trim(),
                    lokasiField.getText().trim(),
                    kapasitas,
                    statusCombo.getSelectedItem().toString()
                );
                refreshTable();
                clearFields();
                showSuccess("Data berhasil diupdate!");
            } catch (Exception e) {
                showError("Error saat mengupdate data: " + e.getMessage());
            }
        }
    }

    // Handler untuk tombol Delete
    private void handleDelete() {
        if (selectedId == -1) {
            showError("Pilih data yang akan dihapus!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus data ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.deleteDropbox(selectedId);
                refreshTable();
                clearFields();
                showSuccess("Data berhasil dihapus!");
            } catch (Exception e) {
                showError("Error saat menghapus data: " + e.getMessage());
            }
        }
    }

    // Handler untuk seleksi tabel
    private void handleTableSelection() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            selectedId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
            namaField.setText(table.getValueAt(selectedRow, 1).toString());
            lokasiField.setText(table.getValueAt(selectedRow, 2).toString());
            kapasitasField.setText(table.getValueAt(selectedRow, 3).toString());
            statusCombo.setSelectedItem(table.getValueAt(selectedRow, 4).toString());
        }
    }

    // Refresh tabel
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Dropbox> dropboxList = controller.getAllDropbox();
        for (Dropbox dropbox : dropboxList) {
            Object[] row = {
                dropbox.getId(),
                dropbox.getNama(),
                dropbox.getLokasi(),
                dropbox.getKapasitas(),
                dropbox.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    // Clear semua field
    private void clearFields() {
        namaField.setText("");
        lokasiField.setText("");
        kapasitasField.setText("");
        statusCombo.setSelectedIndex(0);
        table.clearSelection();
        selectedId = -1;
    }

    // Helper method untuk menampilkan pesan error
    private void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    // Helper method untuk menampilkan pesan sukses
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Sukses",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void setupExportPDFListener() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan PDF");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }

            try {
                List<Dropbox> dataList = controller.getAllDropbox();
                DropboxPDFExporter exporter = new DropboxPDFExporter(dataList);
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
    }
}
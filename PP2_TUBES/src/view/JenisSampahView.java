package view;

import controller.JenisSampahController;
import model.JenisSampah;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import controller.JenisSampahPDFExporter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JenisSampahView extends JPanel {
    private JenisSampahController controller;
    private JTextField txtNama;
    private JTextField txtKategori;
    private JTextArea txtDeskripsi;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnSave, btnUpdate, btnDelete, btnClear;
    private int selectedId = -1;
    private JButton btnExportPDF;

    public JenisSampahView() {
        controller = new JenisSampahController(this);
        initComponents();
        controller.loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        add(createFormPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        initEventListeners();
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGridBagConstraints();
        
        createInputFields(formPanel, gbc);
        createButtons(formPanel, gbc);
        
        return formPanel;
    }

    private GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }

    private void createInputFields(JPanel panel, GridBagConstraints gbc) {
        // Nama
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nama:"), gbc);
        gbc.gridx = 1;
        txtNama = new JTextField(20);
        panel.add(txtNama, gbc);

        // Kategori
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Kategori:"), gbc);
        gbc.gridx = 1;
        txtKategori = new JTextField(20);
        panel.add(txtKategori, gbc);

        // Deskripsi
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Deskripsi:"), gbc);
        gbc.gridx = 1;
        txtDeskripsi = new JTextArea(3, 20);
        txtDeskripsi.setLineWrap(true);
        panel.add(new JScrollPane(txtDeskripsi), gbc);
    }

    private void createButtons(JPanel panel, GridBagConstraints gbc) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnSave = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Hapus");
        btnClear = new JButton("Clear");
        btnExportPDF = new JButton("Export PDF");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnExportPDF); 

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
    }

    private JScrollPane createTablePanel() {
        String[] columns = {"ID", "Nama", "Kategori", "Deskripsi"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        return new JScrollPane(table);
    }

    private void initEventListeners() {
        setupSaveListener();
        setupUpdateListener();
        setupDeleteListener();
        setupClearListener();
        setupTableListener();
        
        setupExportPDFListener();
    }

    private void setupSaveListener() {
        btnSave.addActionListener(e -> {
            if (validateInput()) {
                controller.save(
                    txtNama.getText().trim(),
                    txtKategori.getText().trim(),
                    txtDeskripsi.getText().trim()
                );
            }
        });
    }

    private void setupUpdateListener() {
        btnUpdate.addActionListener(e -> {
            if (!validateSelection("update")) return;
            if (validateInput()) {
                controller.update(
                    selectedId,
                    txtNama.getText().trim(),
                    txtKategori.getText().trim(),
                    txtDeskripsi.getText().trim()
                );
            }
        });
    }

    private void setupDeleteListener() {
        btnDelete.addActionListener(e -> {
            if (!validateSelection("delete")) return;
            if (showDeleteConfirmation()) {
                controller.delete(selectedId);
            }
        });
    }

    private void setupClearListener() {
        btnClear.addActionListener(e -> clearForm());
    }

    private void setupTableListener() {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                loadSelectedData();
            }
        });
    }

    private void loadSelectedData() {
        selectedId = (int) table.getValueAt(table.getSelectedRow(), 0);
        txtNama.setText((String) table.getValueAt(table.getSelectedRow(), 1));
        txtKategori.setText((String) table.getValueAt(table.getSelectedRow(), 2));
        txtDeskripsi.setText((String) table.getValueAt(table.getSelectedRow(), 3));
    }

    private boolean validateSelection(String action) {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih data yang akan " + action + " terlebih dahulu",
                "Validasi",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean showDeleteConfirmation() {
        return JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus data ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder("Mohon lengkapi data berikut:\n");
        boolean isValid = true;
        
        if (txtNama.getText().trim().isEmpty()) {
            errorMessage.append("- Nama\n");
            isValid = false;
        }
        
        if (txtKategori.getText().trim().isEmpty()) {
            errorMessage.append("- Kategori\n");
            isValid = false;
        }
        
        if (txtDeskripsi.getText().trim().isEmpty()) {
            errorMessage.append("- Deskripsi\n");
            isValid = false;
        }
        
        if (!isValid) {
            JOptionPane.showMessageDialog(this,
                errorMessage.toString(),
                "Validasi",
                JOptionPane.WARNING_MESSAGE);
        }
        
        return isValid;
    }
    
    public void displayData(List<JenisSampah> list) {
        tableModel.setRowCount(0);
        for (JenisSampah jenis : list) {
            tableModel.addRow(new Object[]{
                jenis.getId(),
                jenis.getNama(),
                jenis.getKategori(),
                jenis.getDeskripsi()
            });
        }
    }
    
    public void clearForm() {
        txtNama.setText("");
        txtKategori.setText("");
        txtDeskripsi.setText("");
        selectedId = -1;
        table.clearSelection();
    }
    
    private void setupExportPDFListener() {
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
                    List<JenisSampah> dataList = controller.getAllJenisSampah();
                    JenisSampahPDFExporter exporter = new JenisSampahPDFExporter(dataList);
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
}
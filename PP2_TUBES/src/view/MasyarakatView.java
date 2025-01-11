package view;

import controller.MasyarakatController;
import model.Masyarakat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import controller.MasyarakatPDFExporter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MasyarakatView extends JPanel {
    private MasyarakatController controller;
    private JTextField txtNama, txtNoTelp;
    private JTextArea txtAlamat;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnSave, btnVerifikasi, btnTolak, btnClear;
    private int selectedId = -1;
    private JButton btnExportPDF;

    public MasyarakatView() {
        controller = new MasyarakatController(this);
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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Nama
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nama:"), gbc);
        gbc.gridx = 1;
        txtNama = new JTextField(20);
        formPanel.add(txtNama, gbc);

        // No Telp
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("No. Telp:"), gbc);
        gbc.gridx = 1;
        txtNoTelp = new JTextField(20);
        formPanel.add(txtNoTelp, gbc);

        // Alamat
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Alamat:"), gbc);
        gbc.gridx = 1;
        txtAlamat = new JTextArea(3, 20);
        txtAlamat.setLineWrap(true);
        formPanel.add(new JScrollPane(txtAlamat), gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSave = new JButton("Daftar");
        btnVerifikasi = new JButton("Verifikasi");
        btnTolak = new JButton("Tolak");
        btnClear = new JButton("Clear");
        btnExportPDF = new JButton("Export PDF");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnVerifikasi);
        buttonPanel.add(btnTolak);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnExportPDF);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        return formPanel;
    }

    private JScrollPane createTablePanel() {
        String[] columns = {"ID", "Nama", "No. Telp", "Alamat", "Status"};
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
        btnSave.addActionListener(e -> {
            if (validateInput()) {
                controller.save(
                    txtNama.getText().trim(),
                    txtNoTelp.getText().trim(),
                    txtAlamat.getText().trim()
                );
            }
        });

        btnVerifikasi.addActionListener(e -> {
            if (validateSelection()) {
                controller.updateStatus(selectedId, "Verifikasi");
            }
        });

        btnTolak.addActionListener(e -> {
            if (validateSelection()) {
                controller.updateStatus(selectedId, "Ditolak");
            }
        });

        btnClear.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                selectedId = (int) table.getValueAt(table.getSelectedRow(), 0);
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
                    List<Masyarakat> dataList = controller.getAllMasyarakat();
                    MasyarakatPDFExporter exporter = new MasyarakatPDFExporter(dataList);
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

    private boolean validateSelection() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih data terlebih dahulu",
                "Validasi",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validateInput() {
        if (txtNama.getText().trim().isEmpty() ||
            txtNoTelp.getText().trim().isEmpty() ||
            txtAlamat.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this,
                "Semua field harus diisi",
                "Validasi",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public void displayData(List<Masyarakat> list) {
        tableModel.setRowCount(0);
        for (Masyarakat m : list) {
            tableModel.addRow(new Object[]{
                m.getId(),
                m.getNama(),
                m.getNoTelp(),
                m.getAlamat(),
                m.getStatus()
            });
        }
    }

    public void clearForm() {
        txtNama.setText("");
        txtNoTelp.setText("");
        txtAlamat.setText("");
        selectedId = -1;
        table.clearSelection();
    }
}
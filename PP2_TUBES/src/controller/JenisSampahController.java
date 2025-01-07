package controller;

import model.Database;
import model.JenisSampah;
import view.JenisSampahView;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

public class JenisSampahController {
    private JenisSampahView view;
    
    public JenisSampahController(JenisSampahView view) {
        this.view = view;
    }
    
    public void loadData() {
        try {
            List<JenisSampah> list = Database.readAllJenisSampah();
            view.displayData(list);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error loading data: " + e.getMessage());
        }
    }
    
    public void save(String nama, String kategori, String deskripsi) {
        try {
            JenisSampah jenis = new JenisSampah(0, nama, kategori, deskripsi);
            Database.createJenisSampah(jenis);
            loadData();
            view.clearForm();
            JOptionPane.showMessageDialog(view, "Data berhasil disimpan");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error saving data: " + e.getMessage());
        }
    }
    
    public void update(int id, String nama, String kategori, String deskripsi) {
        try {
            JenisSampah jenis = new JenisSampah(id, nama, kategori, deskripsi);
            Database.updateJenisSampah(jenis);
            loadData();
            view.clearForm();
            JOptionPane.showMessageDialog(view, "Data berhasil diupdate");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error updating data: " + e.getMessage());
        }
    }
    
    public void delete(int id) {
        try {
            Database.deleteJenisSampah(id);
            loadData();
            view.clearForm();
            JOptionPane.showMessageDialog(view, "Data berhasil dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error deleting data: " + e.getMessage());
        }
    }
}
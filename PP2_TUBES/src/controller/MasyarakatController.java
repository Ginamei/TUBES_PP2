package controller;

import model.Database;
import model.Masyarakat;
import view.MasyarakatView;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class MasyarakatController {
    private List<Masyarakat> dataMasyarakat;
    private MasyarakatView view;

    public MasyarakatController(MasyarakatView view) {
        this.view = view;
        this.dataMasyarakat = new ArrayList<>();
    }

    public void loadData() {
        try {
            dataMasyarakat = Database.readAllMasyarakat();
            view.displayData(dataMasyarakat);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view,
                "Error loading data: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean save(String nama, String noTelp, String alamat) {
        try {
            Masyarakat masyarakatBaru = new Masyarakat(0, nama, noTelp, alamat, "Menunggu Verifikasi");
            Database.createMasyarakat(masyarakatBaru);
            loadData(); // Refresh data from database
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view,
                "Error saving data: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateStatus(int id, String statusBaru) {
        try {
            Database.updateMasyarakatStatus(id, statusBaru);
            loadData(); // Refresh data from database
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view,
                "Error updating status: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public List<Masyarakat> getAllMasyarakat() {
        try {
            return Database.readAllMasyarakat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error mengambil data: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }
}
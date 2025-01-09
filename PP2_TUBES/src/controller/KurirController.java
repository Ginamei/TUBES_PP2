package controller;

import model.Database;
import model.Kurir;
import view.KurirView;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KurirController {
    private List<Kurir> dataKurir;
    private KurirView view;

    public KurirController(KurirView view) {
        this.view = view;
        this.dataKurir = new ArrayList<>();
    }

    public void loadData() {
        try {
            dataKurir = Database.readAllKurir();
            view.displayData(dataKurir);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view,
                "Error loading data: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean save(String nama, String noTelp, String alamat) {
        try {
            Kurir kurirBaru = new Kurir(0, nama, noTelp, alamat, "Menunggu Verifikasi");
            Database.createKurir(kurirBaru);
            loadData();
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
            Database.updateKurirStatus(id, statusBaru);
            loadData();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view,
                "Error updating status: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
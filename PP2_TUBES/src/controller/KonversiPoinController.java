package controller;

import model.Database;
import model.KonversiPoin;
import model.JenisSampah;
import java.sql.SQLException;
import java.util.List;

public class KonversiPoinController {
    
    public List<KonversiPoin> getAllKonversiPoin() throws SQLException {
        return Database.readAllKonversiPoin();
    }
    
    public List<JenisSampah> getAllJenisSampah() throws SQLException {
        return Database.readAllJenisSampah();
    }
    
    public void createKonversiPoin(int jenisId, double poin) throws SQLException {
        if (poin <= 0) {
            throw new IllegalArgumentException("Poin harus lebih besar dari 0");
        }
        KonversiPoin konversi = new KonversiPoin(0, jenisId, poin, "Aktif");
        Database.createKonversiPoin(konversi);
    }
    
    public void updateKonversiPoin(int id, int jenisId, double poin, String status) throws SQLException {
        if (poin <= 0) {
            throw new IllegalArgumentException("Poin harus lebih besar dari 0");
        }
        if (!status.equals("Aktif") && !status.equals("Nonaktif")) {
            throw new IllegalArgumentException("Status harus 'Aktif' atau 'Nonaktif'");
        }
        KonversiPoin konversi = new KonversiPoin(id, jenisId, poin, status);
        Database.updateKonversiPoin(konversi);
    }
    
    public void deleteKonversiPoin(int id) throws SQLException {
        Database.deleteKonversiPoin(id);
    }
}
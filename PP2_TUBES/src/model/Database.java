package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/pp2_tubes";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static void createJenisSampah(JenisSampah jenis) throws SQLException {
        String sql = "INSERT INTO jenis_sampah (nama, kategori, deskripsi) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, jenis.getNama());
            stmt.setString(2, jenis.getKategori());
            stmt.setString(3, jenis.getDeskripsi());
            stmt.executeUpdate();
        }
    }
    
    public static List<JenisSampah> readAllJenisSampah() throws SQLException {
        List<JenisSampah> list = new ArrayList<>();
        String sql = "SELECT * FROM jenis_sampah";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new JenisSampah(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("kategori"),
                    rs.getString("deskripsi")
                ));
            }
        }
        return list;
    }
    
    public static void updateJenisSampah(JenisSampah jenis) throws SQLException {
        String sql = "UPDATE jenis_sampah SET nama=?, kategori=?, deskripsi=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, jenis.getNama());
            stmt.setString(2, jenis.getKategori());
            stmt.setString(3, jenis.getDeskripsi());
            stmt.setInt(4, jenis.getId());
            stmt.executeUpdate();
        }
    }
    
    public static void deleteJenisSampah(int id) throws SQLException {
        String sql = "DELETE FROM jenis_sampah WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public static void createKonversiPoin(KonversiPoin konversi) throws SQLException {
        // Set semua status menjadi Nonaktif untuk jenis_id yang sama
        String updateSql = "UPDATE konversi_poin SET status='Nonaktif' WHERE jenis_id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setInt(1, konversi.getJenisId());
            stmt.executeUpdate();
        }

        // Insert data baru dengan status Aktif
        String insertSql = "INSERT INTO konversi_poin (jenis_id, poin, status) VALUES (?, ?, 'Aktif')";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            stmt.setInt(1, konversi.getJenisId());
            stmt.setDouble(2, konversi.getPoin());
            stmt.executeUpdate();
        }
    }

    public static List<KonversiPoin> readAllKonversiPoin() throws SQLException {
        List<KonversiPoin> list = new ArrayList<>();
        String sql = "SELECT * FROM konversi_poin ORDER BY jenis_id, id DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new KonversiPoin(
                    rs.getInt("id"),
                    rs.getInt("jenis_id"),
                    rs.getDouble("poin"),
                    rs.getString("status")
                ));
            }
        }
        return list;
    }

    public static void updateKonversiPoin(KonversiPoin konversi) throws SQLException {
        String sql = "UPDATE konversi_poin SET jenis_id=?, poin=?, status=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, konversi.getJenisId());
            stmt.setDouble(2, konversi.getPoin());
            stmt.setString(3, konversi.getStatus());
            stmt.setInt(4, konversi.getId());
            stmt.executeUpdate();
        }
    }

    public static void deleteKonversiPoin(int id) throws SQLException {
        String sql = "DELETE FROM konversi_poin WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public static List<Map<String, Object>> readKonversiPoinWithJenisSampah() throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT kp.*, js.nama as jenis_nama, js.kategori, js.deskripsi " +
                     "FROM konversi_poin kp " +
                     "JOIN jenis_sampah js ON kp.jenis_id = js.id " +
                     "ORDER BY kp.jenis_id, kp.id DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("jenis_id", rs.getInt("jenis_id"));
                row.put("jenis_nama", rs.getString("jenis_nama"));
                row.put("kategori", rs.getString("kategori"));
                row.put("poin", rs.getDouble("poin"));
                row.put("status", rs.getString("status"));
                list.add(row);
            }
        }
        return list;
    }
}
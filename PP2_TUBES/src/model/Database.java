package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

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
    
    public static void addDropbox(Dropbox dropbox) {
        String sql = "INSERT INTO dropbox (nama, lokasi, kapasitas, status) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, dropbox.getNama());
            pstmt.setString(2, dropbox.getLokasi());
            pstmt.setInt(3, dropbox.getKapasitas());
            pstmt.setString(4, dropbox.getStatus());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding dropbox: " + e.getMessage());
        }
    }

    public static List<Dropbox> getAllDropbox() {
        List<Dropbox> dropboxList = new ArrayList<>();
        String sql = "SELECT * FROM dropbox";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Dropbox dropbox = new Dropbox(
                    rs.getString("nama"),
                    rs.getString("lokasi"),
                    rs.getInt("kapasitas"),
                    rs.getString("status")
                );
                dropbox.setId(rs.getInt("id"));
                dropboxList.add(dropbox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching dropbox data: " + e.getMessage());
        }
        return dropboxList;
    }

    public static void updateDropbox(int id, Dropbox dropbox) {
        String sql = "UPDATE dropbox SET nama=?, lokasi=?, kapasitas=?, status=? WHERE id=?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, dropbox.getNama());
            pstmt.setString(2, dropbox.getLokasi());
            pstmt.setInt(3, dropbox.getKapasitas());
            pstmt.setString(4, dropbox.getStatus());
            pstmt.setInt(5, id);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating dropbox: " + e.getMessage());
        }
    }

    public static void deleteDropbox(int id) {
        String sql = "DELETE FROM dropbox WHERE id=?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting dropbox: " + e.getMessage());
        }
    }
}
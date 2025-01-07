package model;

public class JenisSampah {
    private int id;
    private String nama;
    private String kategori;
    private String deskripsi;
    
    public JenisSampah(int id, String nama, String kategori, String deskripsi) {
        this.id = id;
        this.nama = nama;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
    }
    
    // Getters and Setters
    public int getId()
    { 
        return id; 
    }
    
    public void setId(int id) 
    { 
        this.id = id; 
    }
    
    public String getNama() 
    { 
        return nama; 
    }
    
    public void setNama(String nama) 
    { 
        this.nama = nama; 
    }
    
    public String getKategori() 
    { 
        return kategori; 
    }
    
    public void setKategori(String kategori) 
    { 
        this.kategori = kategori; 
    }
    
    public String getDeskripsi() 
    { 
        return deskripsi; 
    }
    
    public void setDeskripsi(String deskripsi) 
    { 
        this.deskripsi = deskripsi; 
    }
}
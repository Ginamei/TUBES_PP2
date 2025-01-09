package model;

public class Masyarakat {
    private int id;
    private String nama;
    private String noTelp;
    private String alamat;
    private String status;
    
    public Masyarakat(int id, String nama, String noTelp, String alamat, String status) {
        this.id = id;
        this.nama = nama;
        this.noTelp = noTelp;
        this.alamat = alamat;
        this.status = status;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getNoTelp() { return noTelp; }
    public void setNoTelp(String noTelp) { this.noTelp = noTelp; }
    
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
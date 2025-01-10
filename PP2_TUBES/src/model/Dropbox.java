package model;

public class Dropbox {
    private int id;
    private String nama;
    private String lokasi;
    private int kapasitas;
    private String status;

    public Dropbox(String nama, String lokasi, int kapasitas, String status) {
        this.nama = nama;
        this.lokasi = lokasi;
        this.kapasitas = kapasitas;
        this.status = status;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }
    
    public int getKapasitas() { return kapasitas; }
    public void setKapasitas(int kapasitas) { this.kapasitas = kapasitas; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

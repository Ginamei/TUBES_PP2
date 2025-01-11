package model;

public class KonversiPoin {
    private int id;
    private int jenisId;
    private double poin;
    private String status;

    public KonversiPoin(int id, int jenisId, double poin, String status) {
        this.id = id;
        this.jenisId = jenisId;
        this.poin = poin;
        this.status = status;
    }

    // Getters
    public int getId() { return id; }
    public int getJenisId() { return jenisId; }
    public double getPoin() { return poin; }
    public String getStatus() { return status; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setJenisId(int jenisId) { this.jenisId = jenisId; }
    public void setPoin(double poin) { this.poin = poin; }
    public void setStatus(String status) { this.status = status; }
}

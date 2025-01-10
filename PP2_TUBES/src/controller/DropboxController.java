// DropboxController.java
package controller;

import model.Database;
import model.Dropbox;
import java.util.List;

public class DropboxController {
    public void addDropbox(String nama, String lokasi, int kapasitas, String status) {
        Dropbox dropbox = new Dropbox(nama, lokasi, kapasitas, status);
        Database.addDropbox(dropbox);
    }

    public List<Dropbox> getAllDropbox() {
        return Database.getAllDropbox();
    }

    public void updateDropbox(int id, String nama, String lokasi, int kapasitas, String status) {
        Dropbox dropbox = new Dropbox(nama, lokasi, kapasitas, status);
        Database.updateDropbox(id, dropbox);
    }

    public void deleteDropbox(int id) {
        Database.deleteDropbox(id);
    }
}

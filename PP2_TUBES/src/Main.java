import view.JenisSampahView;
import view.DropboxView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Modul Admin - Submodul Registrasi dan Data Master");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Jenis Sampah", new JenisSampahView());
            tabbedPane.addTab("Dropbox", new DropboxView());
            // Tambahkan tab lain di sini ya gesss
            
            frame.add(tabbedPane);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

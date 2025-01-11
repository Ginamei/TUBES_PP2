import view.JenisSampahView;
import view.DropboxView;
import view.MasyarakatView;
import view.KurirView;
import javax.swing.*;
import view.KonversiPoinView;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Modul Admin - Submodul Registrasi dan Data Master");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Jenis Sampah", new JenisSampahView());
            tabbedPane.addTab("Dropbox", new DropboxView());
            tabbedPane.addTab("Konversi Poin", new KonversiPoinView());
            tabbedPane.addTab("Pendaftaran Masyarakat", new MasyarakatView());
            tabbedPane.addTab("Pendaftaran Kurir", new KurirView());
            
            frame.add(tabbedPane);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

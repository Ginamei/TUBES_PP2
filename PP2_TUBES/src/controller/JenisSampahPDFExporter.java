package controller;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import model.JenisSampah;

import java.io.FileOutputStream;
import java.util.List;
import java.awt.Color;

public class JenisSampahPDFExporter {
    private List<JenisSampah> jenisSampahList;
    
    public JenisSampahPDFExporter(List<JenisSampah> jenisSampahList) {
        this.jenisSampahList = jenisSampahList;
    }
    
    public void export(String filePath) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        
        document.open();
        
        // Ini buat title ges
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        titleFont.setSize(18);
        
        Paragraph title = new Paragraph("Daftar Jenis Sampah", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        // ini buat table ges
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setWidths(new float[]{1, 2, 2, 3});
        
        // ini buat header ges
        addTableHeader(table);
        
        // dan ini buat rows atau isinya ges
        for (JenisSampah jenisSampah : jenisSampahList) {
            table.addCell(String.valueOf(jenisSampah.getId()));
            table.addCell(jenisSampah.getNama());
            table.addCell(jenisSampah.getKategori());
            table.addCell(jenisSampah.getDeskripsi());
        }
        
        document.add(table);
        document.close();
    }
    
    private void addTableHeader(PdfPTable table) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        headerFont.setColor(Color.WHITE);
        
        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(Color.DARK_GRAY);
        headerCell.setPadding(5);
        
        String[] headers = {"ID", "Nama", "Kategori", "Deskripsi"};
        for (String header : headers) {
            headerCell.setPhrase(new Phrase(header, headerFont));
            table.addCell(headerCell);
        }
    }
}
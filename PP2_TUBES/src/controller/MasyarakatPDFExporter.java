package controller;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import model.Masyarakat;

import java.io.FileOutputStream;
import java.util.List;
import java.awt.Color;

public class MasyarakatPDFExporter {
    private List<Masyarakat> masyarakatList;
    
    public MasyarakatPDFExporter(List<Masyarakat> masyarakatList) {
        this.masyarakatList = masyarakatList;
    }
    
    public void export(String filePath) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        
        document.open();
        
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        titleFont.setSize(18);
        
        Paragraph title = new Paragraph("Pendaftaran Masyarakat", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setWidths(new float[]{1, 2, 2, 2, 2});
        
        addTableHeader(table);
        
        for (Masyarakat masyarakat : masyarakatList) {
            table.addCell(String.valueOf(masyarakat.getId()));
            table.addCell(masyarakat.getNama());
            table.addCell(masyarakat.getNoTelp());
            table.addCell(masyarakat.getAlamat());
            table.addCell(masyarakat.getStatus());
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
        
        String[] headers = {"ID", "Nama", "No. Telp", "Alamat", "Status"};
        for (String header : headers) {
            headerCell.setPhrase(new Phrase(header, headerFont));
            table.addCell(headerCell);
        }
    }
}
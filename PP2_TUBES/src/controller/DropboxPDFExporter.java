package controller;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import model.Dropbox;

import java.io.FileOutputStream;
import java.util.List;
import java.awt.Color;

public class DropboxPDFExporter {
    private List<Dropbox> dropboxList;
    
    public DropboxPDFExporter(List<Dropbox> dropboxList) {
        this.dropboxList = dropboxList;
    }
    
    public void export(String filePath) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        
        document.open();
        
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        titleFont.setSize(18);
        
        Paragraph title = new Paragraph("Daftar Dropbox", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setWidths(new float[]{1, 2, 2, 2, 2});
        
        addTableHeader(table);
        
        for (Dropbox dropbox : dropboxList) {
            table.addCell(String.valueOf(dropbox.getId()));
            table.addCell(dropbox.getNama());
            table.addCell(dropbox.getLokasi());
            table.addCell(String.valueOf(dropbox.getKapasitas()));
            table.addCell(dropbox.getStatus());
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
        
        String[] headers = {"ID", "Nama", "Lokasi", "Kapasitas", "Status"};
        for (String header : headers) {
            headerCell.setPhrase(new Phrase(header, headerFont));
            table.addCell(headerCell);
        }
    }
}
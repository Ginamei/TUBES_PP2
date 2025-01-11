package controller;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import model.Kurir;

import java.io.FileOutputStream;
import java.util.List;
import java.awt.Color;

public class KurirPDFExporter {
    private List<Kurir> kurirList;
    
    public KurirPDFExporter(List<Kurir> kurirList) {
        this.kurirList = kurirList;
    }
    
    public void export(String filePath) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        
        document.open();
        
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        titleFont.setSize(18);
        
        Paragraph title = new Paragraph("Pendaftaran Kurir", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setWidths(new float[]{1, 2, 2, 2, 2});
        
        addTableHeader(table);
        
        for (Kurir kurir : kurirList) {
            table.addCell(String.valueOf(kurir.getId()));
            table.addCell(kurir.getNama());
            table.addCell(kurir.getNoTelp());
            table.addCell(kurir.getAlamat());
            table.addCell(kurir.getStatus());
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
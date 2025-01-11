package controller;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.awt.Color;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class KonversiPoinPDFExporter {
    private List<Map<String, Object>> konversiPoinList;
    
    public KonversiPoinPDFExporter(List<Map<String, Object>> konversiPoinList) {
        this.konversiPoinList = konversiPoinList;
    }
    
    public void export(String filePath) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        
        document.open();
        
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        titleFont.setSize(18);
        
        Paragraph title = new Paragraph("Daftar Konversi Poin", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setWidths(new float[]{1, 2, 2, 2, 2});
        
        addTableHeader(table);
        
        for (Map<String, Object> konversi : konversiPoinList) {
            table.addCell(String.valueOf(konversi.get("id")));
            table.addCell(String.valueOf(konversi.get("jenis_nama")));
            table.addCell(String.valueOf(konversi.get("kategori")));
            table.addCell(String.valueOf(konversi.get("poin")));
            table.addCell(String.valueOf(konversi.get("status")));
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
        
        String[] headers = {
            "ID", 
            "Nama Jenis Sampah", 
            "Kategori",
            "Poin",
            "Status",
        };
        
        for (String header : headers) {
            headerCell.setPhrase(new Phrase(header, headerFont));
            table.addCell(headerCell);
        }
    }
}
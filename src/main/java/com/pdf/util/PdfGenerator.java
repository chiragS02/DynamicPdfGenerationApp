package com.pdf.util;



import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pdf.model.InvoiceRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfGenerator {

    public static String generateInvoicePdf(InvoiceRequest request, String pdfFilePath) throws Exception {
        // Ensure the directory exists
        File file = new File(pdfFilePath);
        File directory = file.getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }

        if (file.exists()) {
            return pdfFilePath;
        }

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
        document.open();


        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Paragraph title = new Paragraph("Invoice", font);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" ")); // Adding space


        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.addCell(getCell("Seller: " + request.getSeller() + "\nAddress: " + request.getSellerAddress() +
                "\nGSTIN: " + request.getSellerGstin(), PdfPCell.ALIGN_LEFT));
        table.addCell(getCell("Buyer: " + request.getBuyer() + "\nAddress: " + request.getBuyerAddress() +
                "\nGSTIN: " + request.getBuyerGstin(), PdfPCell.ALIGN_LEFT));
        document.add(table);

        document.add(new Paragraph(" ")); // Adding space


        PdfPTable itemTable = new PdfPTable(4);
        itemTable.setWidthPercentage(100);
        itemTable.addCell("Item");
        itemTable.addCell("Quantity");
        itemTable.addCell("Rate");
        itemTable.addCell("Amount");

        request.getItems().forEach(item -> {
            itemTable.addCell(item.getName());
            itemTable.addCell(item.getQuantity());
            itemTable.addCell(item.getRate().toString());
            itemTable.addCell(item.getAmount().toString());
        });

        document.add(itemTable);
        document.close();
        return pdfFilePath;
    }

    private static PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
}
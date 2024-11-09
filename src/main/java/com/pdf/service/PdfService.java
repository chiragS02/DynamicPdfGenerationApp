package com.pdf.service;




import com.pdf.model.InvoiceRequest;
import com.pdf.util.PdfGenerator;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
public class PdfService {

    private static final String PDF_DIRECTORY = "invoices/";

    public String generateAndStorePdf(InvoiceRequest request) throws Exception {
        String fileName = request.getSeller() + "_" + request.getBuyer() + ".pdf";
        String pdfFilePath = Paths.get(PDF_DIRECTORY, fileName).toString();

        // Generate the PDF
        return PdfGenerator.generateInvoicePdf(request, pdfFilePath);
    }
}

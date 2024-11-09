package com.pdf.controller;

import com.pdf.model.InvoiceRequest;
import com.pdf.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private static final Logger logger = LoggerFactory.getLogger(PdfController.class);

    @Autowired
    private PdfService pdfService;

    @PostMapping("/generate")
    public ResponseEntity<InputStreamResource> generateInvoicePdf(@RequestBody InvoiceRequest request)
            throws Exception {

        logger.info("Received request to generate PDF for invoice: {}", request);

        // Generate PDF and get the file path
        String pdfFilePath = pdfService.generateAndStorePdf(request);

        // Validate the generated file
        File pdfFile = new File(pdfFilePath);
        if (!pdfFile.exists()) {
            logger.error("PDF file not found at path: {}", pdfFilePath);
            throw new FileNotFoundException("The generated PDF file does not exist: " + pdfFilePath);
        }

        // Create InputStreamResource
        InputStreamResource resource = new InputStreamResource(new FileInputStream(pdfFile));

        // Encode filename for Content-Disposition header
        String encodedFileName = URLEncoder.encode(pdfFile.getName(), StandardCharsets.UTF_8.toString());

        logger.info("PDF file successfully generated: {}", pdfFilePath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}

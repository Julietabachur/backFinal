package com.backendIntegrador.controller;

import com.backendIntegrador.report.PDFReportGenerator;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/admin/report")
public class ReportController {

    private final PDFReportGenerator pdfReportGenerator;

    public ReportController(PDFReportGenerator pdfReportGenerator) {
        this.pdfReportGenerator = pdfReportGenerator;
    }

    @GetMapping("/products-category")
    public void generateReport(HttpServletResponse response) {
        try {
            // Configurar respuesta como PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=reporte_cant_productos_por_categoria.pdf");

            // Obtener el flujo de salida y generar el reporte
            pdfReportGenerator.generateAmountProductsByCategory(response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte", e);
        }
    }
}
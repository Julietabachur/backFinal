package com.backendIntegrador.report;

import com.backendIntegrador.DTO.ReportePpCDto;
import com.backendIntegrador.service.impl.ProductService;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import jakarta.servlet.ServletOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PDFReportGenerator {

    @Autowired
    private ProductService productService;

    public void generateAmountProductsByCategory(ServletOutputStream outputStream) throws Exception {
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Paragraph title = new Paragraph("Cantidad de Productos por Categoría")
                .setBold()
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        document.add(new Paragraph("\n"));

        Table table = new Table(new float[]{3, 2});
        table.setWidth(UnitValue.createPercentValue(80)); // Ajustar ancho de la tabla al 80% del documento
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        // Definir colores para los encabezados
        Color headerBackgroundColor = new DeviceRgb(225, 188, 106);
        Color headerTextColor = new DeviceRgb(0, 0, 0);

        // Encabezados con estilos
        Cell categoryHeader = new Cell().add(new Paragraph("Categoría"))
                .setBackgroundColor(headerBackgroundColor)
                .setFontColor(headerTextColor)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);


        Cell amountHeader = new Cell().add(new Paragraph("Cantidad de Productos"))
                .setBackgroundColor(headerBackgroundColor)
                .setFontColor(headerTextColor)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        table.addHeaderCell(categoryHeader);
        table.addHeaderCell(amountHeader);

        List<ReportePpCDto> productCounts = productService.getProductCountByCategory();

        for (ReportePpCDto productCount : productCounts) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(productCount.getName()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(productCount.getAmount()))));
        }


        document.add(table);
        document.close();
    }

}

package com.backendIntegrador.report;

import com.backendIntegrador.DTO.ProductDto;
import com.backendIntegrador.DTO.ReportePpCDto;
import com.backendIntegrador.model.Sale;
import com.backendIntegrador.service.impl.ProductService;
import com.backendIntegrador.service.impl.SaleService;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.ListItem;
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

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
public class PDFReportGenerator {

    @Autowired
    private ProductService productService;

    @Autowired
    private SaleService saleService;

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
        table.setWidth(UnitValue.createPercentValue(80));
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        Color headerBackgroundColor = new DeviceRgb(225, 188, 106);
        Color headerTextColor = new DeviceRgb(0, 0, 0);

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


    public void generateSalesReport(ServletOutputStream outputStream, LocalDate from, LocalDate to) throws Exception {
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Título principal del reporte
        Paragraph title = new Paragraph("Resumen de Ventas")
                .setBold()
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        document.add(new Paragraph("\n"));

        // Subtítulo
        Paragraph subtitle = new Paragraph("Listado de ventas desde " + from
                .toString() + " hasta " + to.toString())
                .setItalic()
                .setBold()
                .setFontSize(12)
                .setTextAlignment(TextAlignment.LEFT);
        document.add(subtitle);

        document.add(new Paragraph("\n"));

        List<Sale> sales = saleService.findAllByDateRangeWithoutPage(from, to);


        if (sales.isEmpty()) {
            document.add(new Paragraph("No se encontraron ventas en el rango de fechas especificado.")
                    .setFontSize(12)
                    .setMargin(0)
                    .setPadding(0));
        } else {
            com.itextpdf.layout.element.List list = new com.itextpdf.layout.element.List()
                    .setSymbolIndent(12)
                    .setListSymbol("\u2022")
                    .setFontSize(14)
                    .setMargin(0)  // Sin márgenes en la lista
                    .setPadding(0);

            for (Sale sale : sales) {
                ListItem item = new ListItem();

                item.setMargin(0).setPadding(0);

                // Agregar contenido
                item.add(new Paragraph("Id Venta: " + sale.getId()).setMargin(0).setPadding(0));
                item.add(new Paragraph("Fecha: " + sale.getSaleDate()).setMargin(0).setPadding(0));
                item.add(new Paragraph("Total: $" + String.format("%.2f", sale.getTotalPrice())).setMargin(0).setPadding(0));

                if (sale.getProductList() != null && !sale.getProductList().isEmpty()) {
                    item.add(new Paragraph("Productos:").setMargin(0).setPadding(0)); // Título sin espacio extra

                    // Procesar productos
                    for (ProductDto product : sale.getProductList()) {
                        // Formatear texto del producto
                        String productText = String.format(
                                "  - %s (Cantidad: %d, Talle: %s, Precio: $%.2f)",
                                product.getProductName().trim(),
                                product.getAmount(),
                                product.getSize() != null ? product.getSize().trim() : "N/A",
                                product.getPrice()
                        );

                        item.add(new Paragraph(productText).setMargin(0).setPadding(0));
                    }
                } else {
                    item.add(new Paragraph("Productos: No tiene registros").setMargin(0).setPadding(0));
                }

                if (sale.getEntrega() != null) {
                    item.add(new Paragraph("Entrega: " + sale.getEntrega()).setMargin(0).setPadding(0));
                }

                if (sale.getMedioDePago() != null) {
                    item.add(new Paragraph("Medio de Pago: " + sale.getMedioDePago()).setMargin(0).setPadding(0));
                }

                item.add(new Paragraph("\n"));

                list.add(item);
            }


            document.add(list);
        }



        document.close();
    }


    public void generateSalesTableReport(ServletOutputStream outputStream, LocalDate from, LocalDate to) throws Exception {
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Título principal del reporte
        Paragraph title = new Paragraph("Resumen de Ventas")
                .setBold()
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        document.add(new Paragraph("\n"));

        // Subtítulo
        Paragraph subtitle = new Paragraph("Listado de ventas desde " + from
                .toString() + " hasta " + to.toString())
                .setItalic()
                .setBold()
                .setFontSize(12)
                .setTextAlignment(TextAlignment.LEFT);
        document.add(subtitle);

        document.add(new Paragraph("\n"));

        List<Sale> sales = saleService.findAllByDateRangeWithoutPage(from, to);

        if (sales.isEmpty()) {
            document.add(new Paragraph("No se encontraron ventas en el rango de fechas especificado.")
                    .setFontSize(12));
        } else {
            // Crear tabla con columnas dinámicas
            float[] columnWidths = {2, 2, 2, 4}; // Ajusta los anchos según tus necesidades
            Table table = new Table(columnWidths)
                    .setWidth(UnitValue.createPercentValue(100)) // Ancho completo
                    .setMargin(0);

            // Encabezados de la tabla
            table.addHeaderCell(new Cell().add(new Paragraph("Id Venta").setBold()).setBackgroundColor(new DeviceGray(0.75f)));
            table.addHeaderCell(new Cell().add(new Paragraph("Fecha").setBold()).setBackgroundColor(new DeviceGray(0.75f)));
            table.addHeaderCell(new Cell().add(new Paragraph("Total").setBold()).setBackgroundColor(new DeviceGray(0.75f)));
            table.addHeaderCell(new Cell().add(new Paragraph("Detalle").setBold()).setBackgroundColor(new DeviceGray(0.75f)));

            // Iterar sobre las ventas
            for (Sale sale : sales) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(sale.getId())).setMargin(0).setPadding(0)));
                table.addCell(new Cell().add(new Paragraph(sale.getSaleDate().toString()).setMargin(0).setPadding(0)));
                table.addCell(new Cell().add(new Paragraph("$" + String.format("%.2f", sale.getTotalPrice())).setMargin(0).setPadding(0)));

                // Crear una celda para el detalle
                StringBuilder detailBuilder = new StringBuilder();

                // Productos
                if (sale.getProductList() != null && !sale.getProductList().isEmpty()) {
                    detailBuilder.append("Productos:\n");
                    for (ProductDto product : sale.getProductList()) {
                        detailBuilder.append(String.format("  - %s (Cantidad: %d, Talle: %s, Precio: $%.2f)\n",
                                product.getProductName().trim(),
                                product.getAmount(),
                                product.getSize() != null ? product.getSize().trim() : "N/A",
                                product.getPrice()));
                    }
                } else {
                    detailBuilder.append("Productos: No tiene registros\n");
                }

                // Entrega
                if (sale.getEntrega() != null) {
                    detailBuilder.append("Entrega: ").append(sale.getEntrega()).append("\n");
                }

                // Medio de Pago
                if (sale.getMedioDePago() != null) {
                    detailBuilder.append("Medio de Pago: ").append(sale.getMedioDePago()).append("\n");
                }

                // Agregar el detalle a la celda
                table.addCell(new Cell().add(new Paragraph(detailBuilder.toString().trim()).setMargin(0).setPadding(0)));
            }


            document.add(table);
        }

        document.close();
    }



}

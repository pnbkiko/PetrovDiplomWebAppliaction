// src/main/java/com/petrov/maintenance/service/CustomPdfPageEventHelper.java
package com.petrov.maintenance.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomPdfPageEventHelper extends PdfPageEventHelper {

    private Font footerFont;

    public CustomPdfPageEventHelper() {
        try {
            BaseFont baseFont = BaseFont.createFont(
                    "assets/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED
            );
            footerFont = new Font(baseFont, 8, Font.ITALIC, BaseColor.GRAY);
        } catch (Exception e) {
            try {
                BaseFont baseFont = BaseFont.createFont(
                        BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED
                );
                footerFont = new Font(baseFont, 8, Font.ITALIC, BaseColor.GRAY);
            } catch (Exception ex) {
                footerFont = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY);
            }
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        Phrase footer = new Phrase(
                "Страница " + writer.getPageNumber() +
                        " | Отчет о просроченных ТО | " +
                        LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                footerFont
        );
        ColumnText.showTextAligned(
                cb, Element.ALIGN_CENTER,
                footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 10,
                0
        );
    }
}
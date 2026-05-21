// src/main/java/com/petrov/maintenance/service/ReportService.java
package com.petrov.maintenance.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.petrov.maintenance.model.MaintenanceSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private MaintenanceScheduleService scheduleService;

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    private BaseFont loadArialFont() throws Exception {
        // Пробуем загрузить шрифт из resources
        try {
            InputStream inputStream = getClass().getResourceAsStream("assets/fonts/arial.ttf");
            if (inputStream != null) {
                File tempFontFile = File.createTempFile("arial", ".ttf");
                tempFontFile.deleteOnExit();
                java.nio.file.Files.copy(inputStream, tempFontFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                return BaseFont.createFont(tempFontFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            }
        } catch (Exception ignored) {}

        // Пробуем загрузить из файловой системы
        String[] possiblePaths = {
                "assets/fonts/arial.ttf",
                ".assets/fonts/arial.ttf",
                "src/main/resources/assets/fonts/arial.ttf"
        };

        for (String path : possiblePaths) {
            try {
                File fontFile = new File(path);
                if (fontFile.exists()) {
                    return BaseFont.createFont(fontFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                }
            } catch (Exception ignored) {}
        }

        // Запасной вариант
        return BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
    }

    private void addStatsRowVertical(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        labelCell.setPadding(5);
        labelCell.setBorderWidth(0);
        labelCell.setBackgroundColor(new BaseColor(240, 240, 240));
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        valueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        valueCell.setPadding(8);
        valueCell.setBorderWidth(0);
        valueCell.setBackgroundColor(new BaseColor(220, 237, 200));
        valueCell.setMinimumHeight(30);
        table.addCell(valueCell);
    }

    private void addColoredHeader(PdfPTable table, String text, Font font, BaseColor color) {
        PdfPCell header = new PdfPCell(new Phrase(text, font));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setVerticalAlignment(Element.ALIGN_MIDDLE);
        header.setPadding(8);
        header.setMinimumHeight(25);
        header.setBackgroundColor(color);
        table.addCell(header);
    }

    private PdfPCell createCell(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        cell.setMinimumHeight(20);
        return cell;
    }

    // ==================== ГЛАВНЫЙ МЕТОД ГЕНЕРАЦИИ ОТЧЁТА ====================

    public void generateOverdueReport(HttpServletResponse response) throws IOException {
        List<MaintenanceSchedule> overdueList = scheduleService.getAllSchedules().stream()
                .filter(ms -> ms.getNextDue() != null && ms.getNextDue().isBefore(LocalDate.now()))
                .sorted(Comparator.comparing(MaintenanceSchedule::getNextDue))
                .collect(Collectors.toList());

        if (overdueList.isEmpty()) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write(
                    "<script>alert('Нет просроченных технических обслуживаний для отчета.'); window.history.back();</script>"
            );
            return;
        }

        response.setContentType("application/pdf");
        String filename = "Отчет_просроченные_ТО_" +
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy")) + ".pdf";

        String encodedFilename = URLEncoder.encode(filename, "UTF-8")
                .replace("+", "%20");

        response.setHeader("Content-Disposition",
                "attachment; filename*=UTF-8''" + encodedFilename);

        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setPageEvent(new CustomPdfPageEventHelper());
            document.open();

            BaseFont baseFont;
            try {
                baseFont = loadArialFont();
            } catch (Exception e) {
                // Если шрифт не загрузился — используем стандартный
                baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
            }

            Font titleFont = new Font(baseFont, 18, Font.BOLD);
            Font subtitleFont = new Font(baseFont, 14, Font.BOLD);
            Font headerFont = new Font(baseFont, 11, Font.BOLD);
            Font contentFont = new Font(baseFont, 10, Font.NORMAL);
            Font warningFont = new Font(baseFont, 10, Font.BOLD, BaseColor.RED);
            Font infoFont = new Font(baseFont, 9, Font.ITALIC, BaseColor.DARK_GRAY);

            // ==================== СТРАНИЦА 1: ОСНОВНАЯ ИНФОРМАЦИЯ ====================

            // === 1. ЗАГОЛОВОК ===
            Paragraph title = new Paragraph("ОТЧЕТ О ПРОСРОЧЕННЫХ ТЕХНИЧЕСКИХ ОБСЛУЖИВАНИЯХ", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            document.add(title);

            // === 2. ИНФОРМАЦИЯ О ФОРМИРОВАНИИ ===
            Paragraph reportInfo = new Paragraph(
                    "Дата формирования отчета: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                            "   Время: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
                    infoFont
            );
            reportInfo.setAlignment(Element.ALIGN_CENTER);
            reportInfo.setSpacingAfter(15);
            document.add(reportInfo);

            // === 3. СВОДНАЯ СТАТИСТИКА ===
            Paragraph summaryTitle = new Paragraph("СВОДНАЯ ИНФОРМАЦИЯ", subtitleFont);
            summaryTitle.setSpacingAfter(10);
            document.add(summaryTitle);

            PdfPTable statsTable = new PdfPTable(1);
            statsTable.setWidthPercentage(60);
            statsTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            statsTable.setSpacingBefore(5);
            statsTable.setSpacingAfter(15);

            double avgOverdue = overdueList.stream()
                    .filter(ms -> ms.getNextDue() != null)
                    .mapToLong(ms -> ChronoUnit.DAYS.between(ms.getNextDue(), LocalDate.now()))
                    .average()
                    .orElse(0.0);

            OptionalLong maxOverdue = overdueList.stream()
                    .filter(ms -> ms.getNextDue() != null)
                    .mapToLong(ms -> ChronoUnit.DAYS.between(ms.getNextDue(), LocalDate.now()))
                    .max();

            addStatsRowVertical(statsTable, "Всего просроченных ТО:",
                    String.valueOf(overdueList.size()),
                    new Font(baseFont, 10, Font.BOLD),
                    new Font(baseFont, 12, Font.BOLD, new BaseColor(0, 102, 204)));

            addStatsRowVertical(statsTable, "Средний срок просрочки:",
                    String.format("%.1f дней", avgOverdue),
                    new Font(baseFont, 10, Font.BOLD),
                    new Font(baseFont, 12, Font.BOLD, new BaseColor(0, 102, 204)));

            if (maxOverdue.isPresent()) {
                Font warningValueFont = new Font(baseFont, 12, Font.BOLD, BaseColor.RED);
                addStatsRowVertical(statsTable, "Максимальная просрочка:",
                        maxOverdue.getAsLong() + " дней",
                        new Font(baseFont, 10, Font.BOLD),
                        warningValueFont);
            }

            document.add(statsTable);

            // === 4. ПРЕДУПРЕЖДЕНИЕ ===
            Paragraph warning = new Paragraph("ВНИМАНИЕ! Имеются просроченные технические обслуживания", warningFont);
            warning.setSpacingBefore(10);
            warning.setSpacingAfter(15);
            document.add(warning);

            // === 5. ДЕТАЛЬНАЯ ТАБЛИЦА ===
            Paragraph tableTitle = new Paragraph("ДЕТАЛЬНЫЙ СПИСОК ПРОСРОЧЕННЫХ ТО", subtitleFont);
            tableTitle.setSpacingAfter(10);
            document.add(tableTitle);

            PdfPTable mainTable = new PdfPTable(6);
            mainTable.setWidthPercentage(100);
            mainTable.setSpacingBefore(5);
            mainTable.setSpacingAfter(20);

            addColoredHeader(mainTable, "№", headerFont, BaseColor.LIGHT_GRAY);
            addColoredHeader(mainTable, "Модель станка", headerFont, BaseColor.LIGHT_GRAY);
            addColoredHeader(mainTable, "Тип ТО", headerFont, BaseColor.LIGHT_GRAY);
            addColoredHeader(mainTable, "Дата следующего ТО", headerFont, BaseColor.LIGHT_GRAY);
            addColoredHeader(mainTable, "Дата последнего ТО", headerFont, BaseColor.LIGHT_GRAY);
            addColoredHeader(mainTable, "Дней просрочки", headerFont, BaseColor.LIGHT_GRAY);

            int counter = 1;
            LocalDate today = LocalDate.now();

            for (MaintenanceSchedule ms : overdueList) {
                PdfPCell cellNum = createCell(String.valueOf(counter++), contentFont, Element.ALIGN_CENTER);

                String machineModel = ms.getMachine() != null ? ms.getMachine().getModel() : "Не указано";
                PdfPCell cellModel = createCell(machineModel, contentFont, Element.ALIGN_LEFT);

                String typeName = ms.getMaintenanceType() != null ? ms.getMaintenanceType().getName() : "Не указано";
                PdfPCell cellType = createCell(typeName, contentFont, Element.ALIGN_LEFT);

                String nextDueStr = "Не указано";
                if (ms.getNextDue() != null) {
                    nextDueStr = ms.getNextDue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                }
                PdfPCell cellNextDue = createCell(nextDueStr, contentFont, Element.ALIGN_CENTER);

                String lastDoneStr = "Не указано";
                if (ms.getLastDone() != null) {
                    lastDoneStr = ms.getLastDone().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                }
                PdfPCell cellLastDone = createCell(lastDoneStr, contentFont, Element.ALIGN_CENTER);

                long daysOverdue = 0;
                if (ms.getNextDue() != null) {
                    daysOverdue = ChronoUnit.DAYS.between(ms.getNextDue(), today);
                }
                PdfPCell cellOverdue = createCell(daysOverdue + " дн.", contentFont, Element.ALIGN_CENTER);

                if (daysOverdue > 30) {
                    cellOverdue.setBackgroundColor(new BaseColor(255, 200, 200));
                } else if (daysOverdue > 7) {
                    cellOverdue.setBackgroundColor(new BaseColor(255, 255, 200));
                } else {
                    cellOverdue.setBackgroundColor(new BaseColor(200, 255, 200));
                }

                mainTable.addCell(cellNum);
                mainTable.addCell(cellModel);
                mainTable.addCell(cellType);
                mainTable.addCell(cellNextDue);
                mainTable.addCell(cellLastDone);
                mainTable.addCell(cellOverdue);
            }

            document.add(mainTable);

            // ==================== СТРАНИЦА 2: АНАЛИЗ И РЕКОМЕНДАЦИИ ====================
            document.newPage();

            // === 6. АНАЛИЗ ПО ТИПАМ ТО ===
            Paragraph analysisTitle = new Paragraph("АНАЛИЗ ПО ТИПАМ ТЕХНИЧЕСКОГО ОБСЛУЖИВАНИЯ", subtitleFont);
            analysisTitle.setAlignment(Element.ALIGN_CENTER);
            analysisTitle.setSpacingAfter(15);
            document.add(analysisTitle);

            Map<String, Long> byType = overdueList.stream()
                    .filter(ms -> ms.getMaintenanceType() != null)
                    .collect(Collectors.groupingBy(
                            ms -> ms.getMaintenanceType().getName(),
                            Collectors.counting()
                    ));

            if (!byType.isEmpty()) {
                PdfPTable typeTable = new PdfPTable(2);
                typeTable.setWidthPercentage(70);
                typeTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                typeTable.setSpacingBefore(10);
                typeTable.setSpacingAfter(20);

                addColoredHeader(typeTable, "Тип ТО", headerFont, new BaseColor(52, 152, 219));
                addColoredHeader(typeTable, "Количество", headerFont, new BaseColor(52, 152, 219));

                byType.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .forEach(entry -> {
                            typeTable.addCell(createCell(entry.getKey(), contentFont, Element.ALIGN_LEFT));
                            typeTable.addCell(createCell(String.valueOf(entry.getValue()), contentFont, Element.ALIGN_CENTER));
                        });

                document.add(typeTable);
            }

            // === 7. АНАЛИЗ ПО СТАНКАМ ===
            Paragraph machinesTitle = new Paragraph("АНАЛИЗ ПО СТАНКАМ", subtitleFont);
            machinesTitle.setAlignment(Element.ALIGN_CENTER);
            machinesTitle.setSpacingAfter(15);
            document.add(machinesTitle);

            Map<String, Long> byMachine = overdueList.stream()
                    .filter(ms -> ms.getMachine() != null)
                    .collect(Collectors.groupingBy(
                            ms -> ms.getMachine().getModel(),
                            Collectors.counting()
                    ));

            if (!byMachine.isEmpty()) {
                PdfPTable machineTable = new PdfPTable(2);
                machineTable.setWidthPercentage(70);
                machineTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                machineTable.setSpacingBefore(10);
                machineTable.setSpacingAfter(20);

                addColoredHeader(machineTable, "Модель станка", headerFont, new BaseColor(46, 204, 113));
                addColoredHeader(machineTable, "Количество просрочек", headerFont, new BaseColor(46, 204, 113));

                byMachine.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .forEach(entry -> {
                            machineTable.addCell(createCell(entry.getKey(), contentFont, Element.ALIGN_LEFT));
                            machineTable.addCell(createCell(String.valueOf(entry.getValue()), contentFont, Element.ALIGN_CENTER));
                        });

                document.add(machineTable);
            }

            // === 8. РАСПРЕДЕЛЕНИЕ ПО СРОКУ ПРОСРОЧКИ ===
            Paragraph daysTitle = new Paragraph("РАСПРЕДЕЛЕНИЕ ПО СРОКУ ПРОСРОЧКИ", subtitleFont);
            daysTitle.setAlignment(Element.ALIGN_CENTER);
            daysTitle.setSpacingAfter(15);
            document.add(daysTitle);

            long countLess7 = overdueList.stream()
                    .filter(ms -> ChronoUnit.DAYS.between(ms.getNextDue(), LocalDate.now()) <= 7)
                    .count();

            long count7To30 = overdueList.stream()
                    .filter(ms -> {
                        long days = ChronoUnit.DAYS.between(ms.getNextDue(), LocalDate.now());
                        return days > 7 && days <= 30;
                    })
                    .count();

            long countMore30 = overdueList.stream()
                    .filter(ms -> ChronoUnit.DAYS.between(ms.getNextDue(), LocalDate.now()) > 30)
                    .count();

            PdfPTable daysTable = new PdfPTable(2);
            daysTable.setWidthPercentage(60);
            daysTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            daysTable.setSpacingBefore(10);
            daysTable.setSpacingAfter(25);

            addColoredHeader(daysTable, "Срок просрочки", headerFont, new BaseColor(155, 89, 182));
            addColoredHeader(daysTable, "Количество", headerFont, new BaseColor(155, 89, 182));

            daysTable.addCell(createCell("До 7 дней (зеленый)", contentFont, Element.ALIGN_LEFT));
            daysTable.addCell(createCell(String.valueOf(countLess7), contentFont, Element.ALIGN_CENTER));

            daysTable.addCell(createCell("От 7 до 30 дней (желтый)", contentFont, Element.ALIGN_LEFT));
            daysTable.addCell(createCell(String.valueOf(count7To30), contentFont, Element.ALIGN_CENTER));

            daysTable.addCell(createCell("Более 30 дней (красный)", contentFont, Element.ALIGN_LEFT));
            daysTable.addCell(createCell(String.valueOf(countMore30), contentFont, Element.ALIGN_CENTER));

            document.add(daysTable);

            // === 9. РЕКОМЕНДАЦИИ ===
            Paragraph recommendationsTitle = new Paragraph("РЕКОМЕНДАЦИИ", subtitleFont);
            recommendationsTitle.setAlignment(Element.ALIGN_CENTER);
            recommendationsTitle.setSpacingBefore(10);
            recommendationsTitle.setSpacingAfter(15);
            document.add(recommendationsTitle);

            List<String> recommendations = Arrays.asList(
                    "⚠️ Немедленно выполнить просроченные ТО согласно графику",
                    "🔍 Проверить причины просрочки (отсутствие запчастей, занятость персонала)",
                    "📅 Пересмотреть график ТО для предотвращения повторных просрочек",
                    "📧 Уведомить ответственных лиц о необходимости срочного выполнения",
                    "⚙️ Внести изменения в систему планирования ТО",
                    "📊 Провести анализ загруженности оборудования",
                    "👨‍🔧 Обучить персонал правилам соблюдения графика ТО"
            );

            for (String rec : recommendations) {
                Paragraph recPara = new Paragraph(rec, contentFont);
                recPara.setSpacingBefore(5);
                recPara.setIndentationLeft(20);
                document.add(recPara);
            }

            document.newPage();

            // === 10. ВЫВОДЫ ===
            Paragraph conclusionsTitle = new Paragraph("ВЫВОДЫ", subtitleFont);
            conclusionsTitle.setAlignment(Element.ALIGN_CENTER);
            conclusionsTitle.setSpacingBefore(20);
            conclusionsTitle.setSpacingAfter(15);
            document.add(conclusionsTitle);

            String topType = byType.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("не определен");

            String conclusionText = String.format(
                    "На момент формирования отчета обнаружено %d просроченных технических обслуживаний. " +
                            "Средний срок просрочки составляет %.1f дней. " +
                            "Наибольшее количество просрочек приходится на тип ТО: %s. " +
                            "Требуется незамедлительное выполнение отложенных работ для обеспечения " +
                            "бесперебойной работы оборудования и соблюдения регламентов технического обслуживания.",
                    overdueList.size(), avgOverdue, topType
            );

            Paragraph conclusion = new Paragraph(conclusionText, contentFont);
            conclusion.setSpacingAfter(15);
            conclusion.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(conclusion);

            // === 11. ПОДПИСИ ===
            Paragraph signatureTitle = new Paragraph("ПОДПИСИ", subtitleFont);
            signatureTitle.setAlignment(Element.ALIGN_CENTER);
            signatureTitle.setSpacingBefore(20);
            signatureTitle.setSpacingAfter(15);
            document.add(signatureTitle);

            PdfPTable signatureTable = new PdfPTable(2);
            signatureTable.setWidthPercentage(80);
            signatureTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            signatureTable.setSpacingBefore(10);

            signatureTable.addCell(createCell("Главный инженер:", contentFont, Element.ALIGN_LEFT));
            signatureTable.addCell(createCell("___________________", contentFont, Element.ALIGN_LEFT));

            signatureTable.addCell(createCell("Начальник отдела ТО:", contentFont, Element.ALIGN_LEFT));
            signatureTable.addCell(createCell("___________________", contentFont, Element.ALIGN_LEFT));

            signatureTable.addCell(createCell("Дата:", contentFont, Element.ALIGN_LEFT));
            signatureTable.addCell(createCell(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), contentFont, Element.ALIGN_LEFT));

            document.add(signatureTable);

            // === 12. ИНФОРМАЦИЯ О СИСТЕМЕ ===
            Paragraph systemInfo = new Paragraph(
                    "Отчет сформирован автоматически системой управления ТО",
                    infoFont
            );
            systemInfo.setAlignment(Element.ALIGN_CENTER);
            systemInfo.setSpacingBefore(30);
            document.add(systemInfo);

            document.close();

        } catch (DocumentException e) {
            throw new IOException("Ошибка создания PDF: " + e.getMessage(), e);
        }
    }
}
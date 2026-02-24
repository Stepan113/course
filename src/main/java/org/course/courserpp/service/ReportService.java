package org.course.courserpp.service;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.course.courserpp.model.CashWithdrawal;
import org.course.courserpp.model.MoneyTransfer;
import org.course.courserpp.model.Score;
import org.course.courserpp.repository.ScoreRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ScoreRepository scoreRepository;

    public byte[] generateExcel(List<String> scoreIds) throws IOException {
        List<Score> scores = scoreRepository.findAllById(scoreIds);
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Снятия наличных");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Счёт");
            header.createCell(1).setCellValue("Сумма снятия");
            header.createCell(2).setCellValue("Дата");
            header.createCell(3).setCellValue("Заявка ID");
            int rowNum = 1;
            for (Score score : scores) {
                for (CashWithdrawal cw : score.getCashWithdrawals()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(score.getNumber());
                    row.createCell(1).setCellValue(cw.getWithdrawalAmount() != null ? cw.getWithdrawalAmount() : 0);
                    row.createCell(2).setCellValue(cw.getCreatedAt() != null ? new SimpleDateFormat("dd.MM.yyyy").format(cw.getCreatedAt()) : "");
                    row.createCell(3).setCellValue(cw.getApplicationForCash() != null ? cw.getApplicationForCash().getId() : "—");
                }
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] generateWord(List<String> scoreIds) throws IOException {
        List<Score> scores = scoreRepository.findAllById(scoreIds);
        try (XWPFDocument doc = new XWPFDocument()) {
            XWPFParagraph title = doc.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setBold(true);
            titleRun.setFontSize(16);
            titleRun.setText("Отчёт по снятиям наличных");

            for (Score score : scores) {
                XWPFParagraph scorePar = doc.createParagraph();
                XWPFRun scoreRun = scorePar.createRun();
                scoreRun.setBold(true);
                scoreRun.setText("Счёт: " + score.getNumber() + " (баланс: " + score.getBalance() + ")");

                XWPFTable table = doc.createTable();
                XWPFTableRow headerRow = table.getRow(0);
                headerRow.getCell(0).setText("Сумма снятия");
                headerRow.addNewTableCell().setText("Дата");
                headerRow.addNewTableCell().setText("Заявка ID");

                for (CashWithdrawal cw : score.getCashWithdrawals()) {
                    XWPFTableRow row = table.createRow();
                    row.getCell(0).setText(String.valueOf(cw.getWithdrawalAmount() != null ? cw.getWithdrawalAmount() : 0));
                    row.getCell(1).setText(cw.getCreatedAt() != null ? new SimpleDateFormat("dd.MM.yyyy").format(cw.getCreatedAt()) : "");
                    row.getCell(2).setText(cw.getApplicationForCash() != null ? cw.getApplicationForCash().getId() : "—");
                }
                doc.createParagraph();
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.write(out);
            return out.toByteArray();
        }
    }

    public byte[] generatePdf(List<String> scoreIds, Date from, Date to) throws IOException, DocumentException {
        List<Score> scores = scoreRepository.findAllById(scoreIds);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        com.lowagie.text.Document document = new com.lowagie.text.Document();
        PdfWriter.getInstance(document, out);
        document.open();

        BaseFont baseFont;
        try {
            baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            baseFont = BaseFont.createFont();
        }
        com.lowagie.text.Font titleFont = new com.lowagie.text.Font(baseFont, 16, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font normalFont = new com.lowagie.text.Font(baseFont, 12, com.lowagie.text.Font.NORMAL);
        com.lowagie.text.Font boldFont = new com.lowagie.text.Font(baseFont, 12, com.lowagie.text.Font.BOLD);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String period = "с " + (from != null ? sdf.format(from) : "начала") + " по " + (to != null ? sdf.format(to) : "сегодня");

        document.add(new Paragraph("Отчет по счетам: " + period, titleFont));
        document.add(new Paragraph(" "));

        for (Score score : scores) {
            document.add(new Paragraph("Счет: " + score.getNumber() + " | Баланс: " + score.getBalance(), boldFont));

            document.add(new Paragraph("Снятия наличных:", boldFont));
            PdfPTable cwTable = new PdfPTable(3);
            cwTable.setWidthPercentage(100);
            cwTable.addCell(new PdfPCell(new Phrase("Сумма", boldFont)));
            cwTable.addCell(new PdfPCell(new Phrase("Дата", boldFont)));
            cwTable.addCell(new PdfPCell(new Phrase("Заявка", boldFont)));
            boolean hasCw = false;
            for (CashWithdrawal cw : score.getCashWithdrawals()) {
                if (from != null && cw.getCreatedAt() != null && cw.getCreatedAt().before(from)) continue;
                if (to != null && cw.getCreatedAt() != null && cw.getCreatedAt().after(to)) continue;
                hasCw = true;
                cwTable.addCell(new Phrase(String.valueOf(cw.getWithdrawalAmount() != null ? cw.getWithdrawalAmount() : 0), normalFont));
                cwTable.addCell(new Phrase(cw.getCreatedAt() != null ? sdf.format(cw.getCreatedAt()) : "", normalFont));
                cwTable.addCell(new Phrase(cw.getApplicationForCash() != null ? "Есть" : "Нет", normalFont));
            }
            if (hasCw) document.add(cwTable);
            else document.add(new Paragraph("  Нет снятий в указанный период", normalFont));

            document.add(new Paragraph("Переводы:", boldFont));
            PdfPTable mtTable = new PdfPTable(2);
            mtTable.setWidthPercentage(100);
            mtTable.addCell(new PdfPCell(new Phrase("Сумма", boldFont)));
            mtTable.addCell(new PdfPCell(new Phrase("Дата", boldFont)));
            boolean hasMt = false;
            for (MoneyTransfer mt : score.getMoneyTransfers()) {
                if (from != null && mt.getCreatedAt() != null && mt.getCreatedAt().before(from)) continue;
                if (to != null && mt.getCreatedAt() != null && mt.getCreatedAt().after(to)) continue;
                hasMt = true;
                mtTable.addCell(new Phrase(String.valueOf(mt.getSum() != null ? mt.getSum() : 0), normalFont));
                mtTable.addCell(new Phrase(mt.getCreatedAt() != null ? sdf.format(mt.getCreatedAt()) : "", normalFont));
            }
            if (hasMt) document.add(mtTable);
            else document.add(new Paragraph("  Нет переводов в указанный период", normalFont));

            document.add(new Paragraph(" "));
        }

        document.close();
        return out.toByteArray();
    }
}

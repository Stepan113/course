package org.course.courserpp.controller;

import lombok.RequiredArgsConstructor;
import org.course.courserpp.service.EmailService;
import org.course.courserpp.service.ReportService;
import org.course.courserpp.service.ScoreService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ScoreService scoreService;
    private final EmailService emailService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("scores", scoreService.findAll());
        return "report/index";
    }

    @PostMapping("/download-excel")
    public ResponseEntity<byte[]> downloadExcel(@RequestParam(required = false) List<String> scoreIds) throws Exception {
        if (scoreIds == null) scoreIds = List.of();
        byte[] data = reportService.generateExcel(scoreIds);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }

    @PostMapping("/download-word")
    public ResponseEntity<byte[]> downloadWord(@RequestParam(required = false) List<String> scoreIds) throws Exception {
        if (scoreIds == null) scoreIds = List.of();
        byte[] data = reportService.generateWord(scoreIds);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.docx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .body(data);
    }

    @PostMapping("/download-pdf")
    public ResponseEntity<byte[]> downloadPdf(
            @RequestParam(required = false) List<String> scoreIds,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) throws Exception {
        if (scoreIds == null) scoreIds = List.of();
        byte[] data = reportService.generatePdf(scoreIds, from, to);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }

    @PostMapping("/send-pdf-email")
    public String sendPdfEmail(
            @RequestParam(required = false) List<String> scoreIds,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
            @RequestParam String recipientEmail,
            RedirectAttributes redirectAttributes) {
        if (scoreIds == null) scoreIds = List.of();
        try {
            byte[] pdfData = reportService.generatePdf(scoreIds, from, to);
            emailService.sendPdfReport(recipientEmail, pdfData, "report.pdf");
            redirectAttributes.addFlashAttribute("emailSuccess",
                    "PDF-отчёт успешно отправлен на адрес " + recipientEmail);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("emailError",
                    "Ошибка при отправке письма: " + e.getMessage());
        }
        return "redirect:/reports";
    }
}

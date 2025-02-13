package uni.cafemanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.cafemanagement.model.Report;
import uni.cafemanagement.service.ReportService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/daily")
    public ResponseEntity<Report> generateDailyReport() {
        return ResponseEntity.ok(reportService.generateDailyReport());
    }

    @GetMapping("/monthly")
    public ResponseEntity<Report> generateMonthlyReport() {
        return ResponseEntity.ok(reportService.generateMonthlyReport());
    }

    @GetMapping("/yearly")
    public ResponseEntity<Report> generateYearlyReport() {
        return ResponseEntity.ok(reportService.generateYearlyReport());
    }

    @GetMapping("/general")
    public ResponseEntity<Report> generateGeneralReport() {
        return ResponseEntity.ok(reportService.generateGeneralReport());
    }

    @GetMapping("/specific")
    public ResponseEntity<Report> generateSpecificDateReport(@RequestParam("date") String date) {
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            Report report = reportService.generateSpecificDateReport(parsedDate);
            return ResponseEntity.ok(report);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return reports.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(reports);
    }
}
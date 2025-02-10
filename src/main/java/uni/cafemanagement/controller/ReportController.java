package uni.cafemanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uni.cafemanagement.model.Report;
import uni.cafemanagement.service.ReportService;

import java.time.LocalDate;
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
    public Report generateDailyReport() {
        return reportService.generateDailyReport();
    }

    @GetMapping("/monthly")
    public Report generateMonthlyReport() {
        return reportService.generateMonthlyReport();
    }

    @GetMapping("/yearly")
    public Report generateYearlyReport() {
        return reportService.generateYearlyReport();
    }

    @GetMapping("/general")
    public Report generateGeneralReport() {
        return reportService.generateGeneralReport();
    }

    @GetMapping("/specific")
    public Report generateSpecificDateReport(@RequestParam("date") String date) {
        return reportService.generateSpecificDateReport(LocalDate.parse(date));
    }

    @GetMapping
    public List<Report> getAllReports() {
        return reportService.getAllReports();
    }
}
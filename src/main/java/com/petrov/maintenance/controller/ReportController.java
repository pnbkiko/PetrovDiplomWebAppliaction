// src/main/java/com/petrov/maintenance/controller/ReportController.java
package com.petrov.maintenance.controller;

import com.petrov.maintenance.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/reports")
@PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER')")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/overdue")
    public void downloadOverdueReport(HttpServletResponse response) throws IOException {
        reportService.generateOverdueReport(response);
    }
}
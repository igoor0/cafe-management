package dev.cafemanagement.server.controller;

import dev.cafemanagement.server.service.TestRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRoleController {

    @Autowired
    private TestRoleService testRoleService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminInfo() {
        return testRoleService.performAdminTask();
    }


    @GetMapping("/user")
    public String userInfo() {
        return testRoleService.performUserTask();
    }

    @GetMapping("/manager")
    public String managerInfo() {
        return testRoleService.performManagerTask();
    }

    @GetMapping("/public")
    public String publicInfo() {
        return "Public Information";
    }

}

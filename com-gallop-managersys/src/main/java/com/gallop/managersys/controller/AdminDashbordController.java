package com.gallop.managersys.controller;

import com.gallop.managersys.service.FileStorageService;
import com.gallop.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * author gallop
 * date 2020-04-21 20:37
 * Description:
 * Modified By:
 */
@RestController
@RequestMapping("/admin/dashboard")
public class AdminDashbordController {
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("")
    public Object info() {
        int userTotal = fileStorageService.count();
        Map<String, Integer> data = new HashMap<>();
        data.put("filesTotal", userTotal);

        return JSONResult.ok(data);
    }
}

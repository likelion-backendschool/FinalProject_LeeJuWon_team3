package com.project.Week_Mission.app.rebate.controller;

import com.project.Week_Mission.app.rebate.service.RebateService;
import com.project.Week_Mission.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/adm/rebate")
@RequiredArgsConstructor
public class AdmRebateController {

    private final RebateService rebateService;

    @GetMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showMakeData() {
        return "adm/rebate/makeData";
    }

    @PostMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public String makeData(String yearMonth) {

        rebateService.makeDate(yearMonth);

        return "성공";
    }

}

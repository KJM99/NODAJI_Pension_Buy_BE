package com.example.pensionBuying.controller;

import com.example.pensionBuying.service.PensionRoundSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*"
    ,methods = {
    RequestMethod.GET,
    RequestMethod.POST,
    RequestMethod.DELETE,
    RequestMethod.PUT,
    RequestMethod.OPTIONS}, allowedHeaders = "*")
@RequestMapping("/api/v1/pension/round")
public class PensionRoundSchedulerController {

    private final PensionRoundSchedulerService pensionRoundSchedulerService;

    @GetMapping
    public Integer getRound(){
        return pensionRoundSchedulerService.getRound();
    }
}

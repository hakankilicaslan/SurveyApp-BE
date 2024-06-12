package com.bilgeadam.banket.controller;

import com.bilgeadam.banket.dto.request.MailSurveyDto;
import com.bilgeadam.banket.service.MailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.bilgeadam.banket.constant.Endpoint.*;

@CrossOrigin(maxAge = 3600, allowedHeaders = "*")
@RestController
@RequestMapping(ROOT + MAIL)
@AllArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping(SEND_SURVEY)
    public ResponseEntity<String> sendSurvey(@RequestBody MailSurveyDto dto){
        return ResponseEntity.ok(mailService.prepareSurveyMailContent(dto));
    }

}

package com.bilgeadam.banket.controller;

import com.bilgeadam.banket.dto.request.SaveSurveyRequestDto;
import com.bilgeadam.banket.dto.request.UpdateSurveyRequestDto;
import com.bilgeadam.banket.entity.Survey;
import com.bilgeadam.banket.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.bilgeadam.banket.constant.Endpoint.*;

@CrossOrigin
@RestController
@RequestMapping(ROOT+SURVEY)
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;
    @PostMapping(SAVE)
    public ResponseEntity<String> saveSurvey(@RequestBody SaveSurveyRequestDto dto){
        return ResponseEntity.ok(surveyService.saveSurvey(dto));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<String> updateSurvey(UpdateSurveyRequestDto dto){
        return ResponseEntity.ok(surveyService.updateSurvey(dto));
    }
    @GetMapping(GET_BY_UUID)
    public ResponseEntity<Survey> getSurvey(@PathVariable UUID uuid){
        return ResponseEntity.ok((surveyService.getSurvey(uuid)));
    }

    @GetMapping(GET_ALL)
    public ResponseEntity<List<Survey>> getAllSurveys(){
        return ResponseEntity.ok((surveyService.findAll()));
    }


    @DeleteMapping(DELETE_BY_UUID)
    public ResponseEntity<String> deleteSurvey(@PathVariable UUID uuid){
        return ResponseEntity.ok(surveyService.deleteSurvey(uuid));
    }

    @DeleteMapping(REMOVE_BY_UUID)
    public ResponseEntity<String> removeSurvey(@PathVariable UUID uuid){
        return ResponseEntity.ok(surveyService.removeSurvey(uuid));
    }


}

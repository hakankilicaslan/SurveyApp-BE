package com.bilgeadam.banket.service;

import com.bilgeadam.banket.dto.request.AddQuestionToSurveyDto;
import com.bilgeadam.banket.dto.request.SaveSurveyRequestDto;
import com.bilgeadam.banket.dto.request.UpdateSurveyRequestDto;
import com.bilgeadam.banket.entity.Survey;
import com.bilgeadam.banket.exception.BanketApplicationException;
import com.bilgeadam.banket.exception.ErrorType;
import com.bilgeadam.banket.mapper.ISurveyMapper;
import com.bilgeadam.banket.repository.SurveyRepository;
import com.bilgeadam.banket.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class SurveyService extends ServiceManager<Survey,String> {

    private final SurveyRepository surveyRepository;
    public SurveyService(SurveyRepository surveyRepository) {
        super(surveyRepository);
        this.surveyRepository = surveyRepository;
    }
    public String saveSurvey(SaveSurveyRequestDto dto) {
        if (surveyRepository.existsByName(dto.getName())) {
            throw new BanketApplicationException(ErrorType.SURVEY_ALREADY_EXISTS);
        }
        List<AddQuestionToSurveyDto> questions = new ArrayList<>();
        for (AddQuestionToSurveyDto questionDto : dto.getQuestions()) {
            AddQuestionToSurveyDto addQuestionDto = new AddQuestionToSurveyDto();
            addQuestionDto.setQuestionId(questionDto.getQuestionId());
            addQuestionDto.setRequired(questionDto.isRequired());
            questions.add(addQuestionDto);
        }
        dto.setQuestions(questions);
        Survey survey = Survey.builder()
                .name(dto.getName())
                .questions(dto.getQuestions())
                .build();
        save(survey);
        return "Save survey successfully.";
    }

    public String updateSurvey(UpdateSurveyRequestDto dto) {
        Optional<Survey> optionalSurvey = surveyRepository.findByUuid(dto.getUuid());
        if (optionalSurvey.isEmpty()){
            throw new BanketApplicationException(ErrorType.SURVEY_NOT_FOUND);
        }
        if (dto.getName() != null){
            if (surveyRepository.existsByName(dto.getName())){
                throw new BanketApplicationException(ErrorType.SURVEY_ALREADY_EXISTS);
            }else {
                optionalSurvey.get().setName(dto.getName());
            }
        }
        if (dto.getQuestions() != null){
            optionalSurvey.get().getQuestions().addAll(dto.getQuestions());
        }
        update(optionalSurvey.get());
        return "Update survey successfully.";
    }

    public Survey getSurvey(UUID uuid) {
        Optional<Survey> optionalSurvey=surveyRepository.findByUuid(uuid);
        if (optionalSurvey.isEmpty()){
            throw new BanketApplicationException(ErrorType.SURVEY_NOT_FOUND);
        }
        return optionalSurvey.get();
    }

    public String deleteSurvey(UUID uuid) {
        Optional<Survey> optionalSurvey=surveyRepository.findByUuid(uuid);
        if (optionalSurvey.isEmpty()){
            throw new BanketApplicationException(ErrorType.SURVEY_NOT_FOUND);
        }
        if (optionalSurvey.get().isDeleted()) {
        throw new BanketApplicationException(ErrorType.SURVEY_ALREADY_DELETED);
        }
        softDelete(optionalSurvey.get());
        return "Delete survey successfully.";
    }

    public String removeSurvey(UUID uuid) {
        Optional<Survey> optionalSurvey=surveyRepository.findByUuid(uuid);
        if (optionalSurvey.isEmpty()){
            throw new BanketApplicationException(ErrorType.SURVEY_NOT_FOUND);
        }
        hardDelete(optionalSurvey.get());
        return "Remove survey successfully.";
    }
}

package com.bilgeadam.banket.service;

import com.bilgeadam.banket.entity.SendedSurvey;
import com.bilgeadam.banket.repository.SendedSurveyRepository;
import com.bilgeadam.banket.utility.ServiceManager;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SendedSurveyService extends ServiceManager<SendedSurvey, String> {

    private final SendedSurveyRepository sendedSurveyRepository;

    public SendedSurveyService(MongoRepository<SendedSurvey, String> mongoRepository, SendedSurveyRepository sendedSurveyRepository) {
        super(mongoRepository);
        this.sendedSurveyRepository = sendedSurveyRepository;
    }

    public void saveSendedSurvey(UUID groupUuid, UUID surveyUuid) {
        SendedSurvey sendedSurvey = SendedSurvey.builder()
                .groupUuid(groupUuid)
                .surveyUuid(surveyUuid)
                .sendedAt(LocalDateTime.now())
                .build();
        save(sendedSurvey);
    }

}

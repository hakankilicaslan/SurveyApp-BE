package com.bilgeadam.banket.repository;

import com.bilgeadam.banket.entity.SendedSurvey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendedSurveyRepository extends MongoRepository<SendedSurvey, String> {
}

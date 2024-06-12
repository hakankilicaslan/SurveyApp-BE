package com.bilgeadam.banket.mapper;

import com.bilgeadam.banket.dto.request.SaveSurveyRequestDto;
import com.bilgeadam.banket.entity.Survey;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ISurveyMapper {

    ISurveyMapper INSTANCE = Mappers.getMapper(ISurveyMapper.class);

}

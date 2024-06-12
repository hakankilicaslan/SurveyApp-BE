package com.bilgeadam.banket.service;

import com.bilgeadam.banket.dto.request.MailSurveyDto;
import com.bilgeadam.banket.entity.Group;
import com.bilgeadam.banket.entity.Student;
import com.bilgeadam.banket.entity.Survey;
import com.bilgeadam.banket.entity.User;
import com.bilgeadam.banket.exception.BanketApplicationException;
import com.bilgeadam.banket.exception.ErrorType;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    private final Configuration freemarkerConfiguration;

    private final GroupService groupService;

    private final StudentService studentService;

    private final SurveyService surveyService;

    private final SendedSurveyService sendedSurveyService;

    private void configureFreemarker() {
        freemarkerConfiguration.setDefaultEncoding("UTF-8");
        freemarkerConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        freemarkerConfiguration.setLogTemplateExceptions(false);
        freemarkerConfiguration.setWrapUncheckedExceptions(true);
        freemarkerConfiguration.setFallbackOnNullLoopVariable(false);
        freemarkerConfiguration.setSQLDateAndTimeTimeZone(TimeZone.getDefault());
    }

    public String prepareSurveyMailContent(MailSurveyDto dto) {
        Survey survey = surveyService.getSurvey(dto.getSurveyUuid());
        List<Group> selectedGroups = getGroupsFromMailSurveyDto(dto);
        List<Long> selectedGroupIds = selectedGroups.stream()
                .map(group -> Long.parseLong(group.getId()))
                .toList();
        List<Student> selectedStudentsFromAllGroups = selectedGroupIds.stream()
                .flatMap(groupId -> studentService.getBaseAPIStudentsFromBaseAPIGroupId(groupId).stream())
                .toList();
        configureFreemarker();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setSubject("BilgeAdam Banket Uygulaması Anket Paylaşımı");
            mimeMessageHelper.setFrom("java12banket@gmail.com");
        } catch (MessagingException e) {
            throw new RuntimeException("Error while preparing mimeMessage for javaMailSender: " + e);
        }
        sendSurveyToSelectedGroupsStudents(survey, mimeMessage, mimeMessageHelper, selectedStudentsFromAllGroups);
        selectedGroups.forEach(group -> sendedSurveyService.saveSendedSurvey(dto.getSurveyUuid(), group.getUuid()));
        return "Survey link mail sent to each student email address successfully!";
    }

    private List<Group> getGroupsFromMailSurveyDto(MailSurveyDto dto) {
        List<Group> selectedGroups = new ArrayList<>();
        for (String groupName : dto.getGroupNames()) {
            List<Group> foundGroup = groupService.findByName(groupName);
            if (!foundGroup.isEmpty()) selectedGroups.add(foundGroup.get(0)); // Volkan: findByName methoduna tekrar bakılacak. isLike değil de netlik lazım.
        }
        if (selectedGroups.isEmpty()) throw new BanketApplicationException(ErrorType.GROUP_NOT_FOUND);
        return selectedGroups;
    }

    private void sendSurveyToSelectedGroupsStudents(Survey survey, MimeMessage mimeMessage,
                                                    MimeMessageHelper mimeMessageHelper, List<Student> selectedStudentsFromAllGroups) {
        Map<String, String> templateValues = new HashMap<>();
        try {
            Template template = freemarkerConfiguration.getTemplate("survey-mail-template.ftl");
            templateValues.put("survey_name", survey.getName());
            templateValues.put("survey_uuid", survey.getUuid().toString());
            for (Student student : selectedStudentsFromAllGroups) {
                templateValues.put("name", student.getName());
                templateValues.put("student_id", student.getId()); // Volkan: baseapida id tutuluyor o yüzden...
                mimeMessageHelper.setTo(student.getEmail());
                mimeMessageHelper.setText(FreeMarkerTemplateUtils.processTemplateIntoString(template, templateValues), true);
                javaMailSender.send(mimeMessage);
            }
        } catch (TemplateException | IOException | MessagingException e) {
            throw new RuntimeException("Error while preparing mail content for survey link" + e);
        }
    }

    public void sendLoginCredentialsMailToManager(User user) {
        configureFreemarker();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            Template template = freemarkerConfiguration.getTemplate("manager-register-mail-template.ftl");
            mimeMessageHelper.setSubject("BilgeAdam Banket Uygulamasına Kaydınız Yapıldı.");
            mimeMessageHelper.setFrom("java12banket@gmail.com");
            mimeMessageHelper.setTo(user.getEmail());
            Map<String, String> templateValues = Map.ofEntries(
                    Map.entry("name", user.getName()),
                    Map.entry("surname", user.getSurname()),
                    Map.entry("email", user.getEmail()),
                    Map.entry("password", user.getPassword())
            );
            mimeMessageHelper.setText(FreeMarkerTemplateUtils.processTemplateIntoString(template, templateValues), true);
        } catch (MessagingException | IOException | TemplateException e) {
            throw new RuntimeException("Error while preparing mimeMessage for javaMailSender: " + e);
        }
        javaMailSender.send(mimeMessage);
    }

}
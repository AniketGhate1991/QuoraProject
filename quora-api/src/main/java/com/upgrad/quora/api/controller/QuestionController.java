package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.business.QuestionBusinessService;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.upgrad.quora.api.model.QuestionResponse;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    private QuestionBusinessService questionBusinessService;

    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> CreateQuestion(final String content,@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {


        String [] bearerToken = authorization.split("Bearer ");

        final QuestionEntity questionEntity = new QuestionEntity();

       questionEntity.setContent(content);
       questionEntity.setDate(ZonedDateTime.now());

        final QuestionEntity createdquestionEntity = questionBusinessService.CreateQuestion(questionEntity,bearerToken[0]);
        QuestionResponse questionResponse = new QuestionResponse().id(createdquestionEntity.getUuid()).status("QUESTION CREATED");

        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }
}

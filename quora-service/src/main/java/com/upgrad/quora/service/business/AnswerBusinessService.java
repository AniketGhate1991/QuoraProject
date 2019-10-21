package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.AnswerEntity;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;

public class AnswerBusinessService {
    @Autowired
    private AnswerDao answerDao;


    public AnswerEntity CreateAnswer(final AnswerEntity answerEntity,final String questionId, final String authorizationToken) throws AuthorizationFailedException {


        UserAuthTokenEntity userAuthTokenEntity = answerDao.getUserAuthToken(authorizationToken);

        if(userAuthTokenEntity != null){
            ZonedDateTime logout = userAuthTokenEntity.getLogoutAt();
            if (logout != null) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post an answer");
            }
                QuestionEntity questionEntity1 = answerDao.getQuestionById(questionId);
            if(questionEntity1 != null){
                answerEntity.setQuestion(questionEntity1);
                UserEntity userEntity = answerDao.getUserByUUID(userAuthTokenEntity.getUuid());
                if(userEntity != null){
                    answerEntity.setUser(userEntity);
                    return answerDao.createAnswer(answerEntity);
                }
            }else{
                throw new AuthorizationFailedException("QUES-001", "The question entered is invalid");

            }

        }

        throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    }
}

package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class QuestionBusinessService {
    @Autowired
    private QuestionDao questionDao;

    public QuestionEntity CreateQuestion(final QuestionEntity questionEntity,final String authorizationToken) throws AuthorizationFailedException{
        UserAuthTokenEntity userAuthTokenEntity = questionDao.getUserAuthToken(authorizationToken);

        if(userAuthTokenEntity != null){
            ZonedDateTime logout = userAuthTokenEntity.getLogoutAt();
            if (logout != null) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
            }

            UserEntity userEntity = questionDao.getUserByUUID(userAuthTokenEntity.getUuid());
            if(userEntity != null){
               questionEntity.setUser(userEntity);
               questionEntity.setUuid(userEntity.getUuid());
               return questionDao.createquestion(questionEntity);
            }
        }

        throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    }
}

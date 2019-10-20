package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

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
               return questionDao.createquestion(questionEntity);
            }
        }

        throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    }

    public List<QuestionEntity> getAllQuestion(final String authorizationToken) throws AuthorizationFailedException{
        UserAuthTokenEntity userAuthTokenEntity = questionDao.getUserAuthToken(authorizationToken);

        if(userAuthTokenEntity != null){
            ZonedDateTime logout = userAuthTokenEntity.getLogoutAt();
            if (logout != null) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
            }
            else
            {
                return questionDao.getAllQuestion();
            }
        }

        throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    }

    public QuestionEntity EditQuestion(final QuestionEntity questionEntity,final String questionid,final String authorizationToken) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthTokenEntity = questionDao.getUserAuthToken(authorizationToken);

        if(userAuthTokenEntity != null){
            ZonedDateTime logout = userAuthTokenEntity.getLogoutAt();
            if (logout != null) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");
            }

            QuestionEntity Entity = questionDao.getQuestionById(questionid);
            if (Entity == null)
            {
                throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
            }
            else
            {
                if (Entity.getUser() != userAuthTokenEntity.getUser()){
                    throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
                }
            }

            questionEntity.setId(Entity.getId());
            questionEntity.setUuid(Entity.getUuid());
            questionEntity.setDate(Entity.getDate());
            questionEntity.setUser(Entity.getUser());
            return questionDao.editquestion(questionEntity);
        }

        throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    }
}
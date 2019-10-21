package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public AnswerEntity createAnswer(AnswerEntity answerEntity){
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public UserEntity getUserByUUID(final String UUID) {
        try {
            return entityManager.createNamedQuery("userByUUID", UserEntity.class).setParameter("UUID", UUID).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    public QuestionEntity getQuestionById(final String id) {
        try {
            return entityManager.createNamedQuery("getAllQuestionByID", QuestionEntity.class).setParameter("QUESTIONID", id).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    public UserAuthTokenEntity getUserAuthToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthTokenEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre) {

            return null;
        }

    }
}

package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class UserAdminBusinessService {
    @Autowired
    private UserDao userDao;

    public UserEntity DeleteUser(final String userUuid, final String authorizationToken) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorizationToken);
        if(userAuthTokenEntity != null){
            ZonedDateTime logout = userAuthTokenEntity.getLogoutAt();
            if (logout != null) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out");
            }

            UserEntity userEntity = userDao.getUserByUUID(userAuthTokenEntity.getUuid());
            if (userEntity == null){
                throw new UserNotFoundException("USR-001", "User with entered uuid to be deleted does not exist");
            }
            else
            {
                String RoleName = userEntity.getRole();
                if (!RoleName.equals("admin")){
                    throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access,Entered user is not an admin");
                }

            }

            return userDao.DeleteUser(userEntity);
        }

        throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    }


}
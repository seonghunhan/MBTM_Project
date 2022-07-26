package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }


    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {

        ArrayList<Integer> checkList = new ArrayList<Integer>(userProvider.check(postUserReq.getId(),postUserReq.getNickName(),postUserReq.getEmail()));

        // id, nickName, email 중복체크
        if(checkList.get(0) == 1){
            throw new BaseException(POST_USERS_EXISTS_ID);
        }
        else if (checkList.get(1) == 1){
            throw new BaseException(POST_USERS_EXISTS_NICKNAME);
        }
        else if (checkList.get(2) == 1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL2);
        }

        // 비밀번호와 비밀번호 확인이 다를경우 발생
        if(postUserReq.getPassword().equals(postUserReq.getPasswordForCheck()) == false){
            throw new BaseException(POST_USERS_UNMATCH_PASSWORD);
        }

        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);
            int userIdx = userDao.createUser(postUserReq);
            //System.out.println(userIdx);
            return new PostUserRes(userIdx);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
//        프롬투에서는 회원가입말고 로그인할때 jwt발급했음
//        try{
//            int userIdx = userDao.createUser(postUserReq);
//            //jwt 발급.
//            // TODO: jwt는 다음주차에서 배울 내용입니다!
//            String jwt = jwtService.createJwt(userIdx);
//            return new PostUserRes(jwt,userIdx);
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
    }

//    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
//        try{
//            int result = userDao.modifyUserName(patchUserReq);
//            if(result == 0){
//                throw new BaseException(MODIFY_FAIL_USERNAME);
//            }
//        } catch(Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }

}

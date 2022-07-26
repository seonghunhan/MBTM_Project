package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;
import static com.example.demo.utils.ValidationRegex.isRegexPhoneNumber;

@RestController
@RequestMapping("/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원가입 API (users)
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("") // (POST) localhost:9000/users
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!

        //System.out.println(postUserReq.getId().getClass().getName());

        // 회원가입 정보누락 Body Check
        if(postUserReq.getId().length() == 0 || postUserReq.getPassword().length() == 0 || postUserReq.getPasswordForCheck().length() == 0 || postUserReq.getName().length() == 0 || postUserReq.getNickName().length() == 0 || postUserReq.getPhone().length() == 0 || postUserReq.getEmail().length() == 0){
            return new BaseResponse<>(POST_USERS_EMPTY_INFO);
        }
        // 이메일 정규표현
        if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        // 전화번호 정규표현
        if(!isRegexPhoneNumber(postUserReq.getPhone())){
            return new BaseResponse<>(POST_USERS_INVALID_PHONE);
        }
        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
//        if(postUserReq.getId() == null || postUserReq.getPassword() == null || postUserReq.getPasswordForCheck() == null || postUserReq.getName() == null || postUserReq.getNickName() == null || postUserReq.getPhone() == null || postUserReq.getEmail() == null){
//        return new BaseResponse<>(POST_USERS_EMPTY_INFO);
//    }
//    /**
//     * 유저피드조회 API
//     * [GET] /users/:userIdx
//     * @return BaseResponse<PostUserRes>
//     */
//    @ResponseBody
//    @GetMapping("/{userIdx}")
//    public BaseResponse<GetUserFeedRes> getUserFeed(@PathVariable("userIdx")int userIdx) {
//        try{
//
//            GetUserFeedRes getUserFeedRes = userProvider.retrieveUserFeed(userIdx,userIdx); //조회 담당인 Provider로 넘긴다
//            return new BaseResponse<>(getUserFeedRes);
//        } catch(BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }



}

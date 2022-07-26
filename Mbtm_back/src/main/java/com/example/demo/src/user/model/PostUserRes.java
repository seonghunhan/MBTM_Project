package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserRes {
    //private String jwt;  //프롬투에서는 로그인할때 jwt발급해줬음
    private int userIdx;
}

package com.fastcampus.boardserver.domain.user.service;

import com.fastcampus.boardserver.domain.user.User;
import com.fastcampus.boardserver.domain.user.dto.*;

import java.util.Optional;

public interface UserService {

    // 회원가입
    void register(RegisterReq registerReq);

    // 로그인
    LoginRes login(LoginReq loginReq);

    // 회원 정보 수정
    Long editUserProfile(EditMemberReq editMemberReq);  // 영향을 받은 row 수를 반환

    // 패스워드 수정
    Long editPassword(PasswordReq passwordReq);

    // 회원 탈퇴
    Long statusToDeleteUser();

    // 회원 정보 조회
    UserDto getUserProfile();

    // 아이디 중복 검사
    Boolean isDuplicatedUserId(String userId);

    // 닉네임 중복 검사
    Boolean isDuplicatedNickname(String nickname);

    // 휴대폰 번호 중복 검사
    Boolean isDuplicatedPhoneNumber(String phoneNumber);

    // 회원 비밀번호 찾기
    String findUserPassword(String userId, String phoneNumber);

    // 휴대폰 번호로 회원 찾기
    Optional<User> findUserByPhoneNumber(String phoneNumber);


}

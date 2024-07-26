package com.fastcampus.boardserver.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BaseResponseStatus {

    /**
     * 1000 - 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000 - Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력 값을 확인해주세요."),
    CHECK_PASSWORD(false, 2004, "비밀번호가 일치하지 않습니다."),
    CHECK_USER(false, 2005, "유효하지 않은 계정입니다."),
    EMPTY_PARAMETER(false, 2007, "파라미터 값을 확인해주세요."),
    NOT_TO_DELETE(false, 2010, "삭제할 대상이 없습니다."),
    WRONG_ACCESS(false, 2011, "잘못된 접근입니다."),

    // Common 형식 관련: 2100 ~
//    INVALID_EMAIL(false, 2101, "올바른 이메일 형식을 입력해주세요."),
    INVALID_PASSWORD(false, 2102, "영문 대소문자, 숫자, 특수문자를 3가지 이상으로 조합하여 8자 이상 입력해주세요"),
    INVALID_PHONE(false, 2103, "올바른 전화번호 형식을 입력해주세요."),

    // [POST] null 관련
    POST_EMPTY(false, 2200, "필수 요소를 모두 확인해주세요"),
    //    POST_EMPTY_EMAIL(false, 2201, "이메일은 필수 정보입니다.")
    POST_EMPTY_PHONE(false, 2203, "전화번호는 필수 정보입니다."),
    POST_EMPTY_PASSWORD(false, 2204, "비밀번호는 필수 정보입니다."),
    SESSION_EMPTY(false, 2205, "세션이 만료되거나 세션이 없습니다."),

    USER_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_USER_ID(false, 2015, "아이디를 입력해주세요."),
    POST_USERS_INVALID_USER_ID(false, 2016, "아이디 형식을 확인해주세요."),
    POST_USERS_EXISTS_USER_ID(false, 2017, "중복된 아이디입니다."),
    POST_USERS_EXISTS_NICKNAME(false, 2018, "중복된 닉네임입니다."),
    POST_USERS_EXISTS_PHONE_NUMBER(false, 2019, "중복된 휴대폰 번호입니다."),

    POST_USERS_INVALID_NICKNAME(false, 2020, "잘못된 닉네임입니다."),
    POST_USERS_INVALID_PASSWORD(false, 2021, "잘못된 비밀번호 형식입니다."),

    VALID_INPUT_NULL(false, 2031, "입력되지 않은 값이 있습니다."),
    VALID_INPUT_BLANK(false, 2032, "입력되지 않은 값이 있습니다."),

    /**
     * 3000 - Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패했습니다."),
    RESPONSE_EMPTY(false, 3001, "조회할 정보가 없습니다."),
    DELETE_EMPTY(false, 3002, "삭제할 정보가 없습니다."),
    SESSION_CREATION_ERROR(false, 3003, "세션을 생성하는데 실패했습니다."),

    // [POST]
    FAILED_TO_LOGIN(false, 3011, "없는 아이디거나, 비밀번호가 틀렸습니다."),
    SEARCH_NOT_FOUND_POST(false, 3012, "조회된 포스트가 없습니다."),
    SESSION_NOT_FOUND(false, 3013, "세션에서 값을 불러오는데 실패했습니다."),

    /**
     * 4000 - DataBase, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스와의 연결에 실패했습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패했습니다."),

    ENCRYPTION_ERROR(false, 4011, "암호화에 실패했습니다."),

    SHA256_INVALID_SPEC(false, 4021, "SHA256 초기화에 오류가 있습니다."),
    SHA256_NO_SUCH_SPEC(false, 4022, "지정한 SHA256 스펙을 찾을 수 없습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;
}

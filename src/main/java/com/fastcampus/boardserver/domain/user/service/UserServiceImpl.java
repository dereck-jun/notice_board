package com.fastcampus.boardserver.domain.user.service;

import com.fastcampus.boardserver.constant.Role;
import com.fastcampus.boardserver.constant.Status;
import com.fastcampus.boardserver.domain.user.User;
import com.fastcampus.boardserver.domain.user.dto.*;
import com.fastcampus.boardserver.domain.user.repository.UserRepository;
import com.fastcampus.boardserver.exception.BaseException;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.fastcampus.boardserver.constant.Role.USER;
import static com.fastcampus.boardserver.constant.Status.*;
import static com.fastcampus.boardserver.exception.BaseResponseStatus.*;
import static com.fastcampus.boardserver.utils.SHA256.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final HttpSession session;

    @Override
    @Transactional
    public LoginRes register(RegisterReq registerReq) {

        // 비밀번호 암호화 메서드
        String encryptedPassword = encryptPassword(registerReq);

        // 중복 검사
        Optional<User> findUserId = repository.findUserByUserIdAndStatus(registerReq.getUserId(), ACTIVE);
        if (findUserId.isPresent()) {
            throw new BaseException(POST_USERS_EXISTS_USER_ID);
        }

        Optional<User> findUserNickname = repository.findUserByNicknameAndStatus(registerReq.getNickname(), ACTIVE);
        if (findUserId.isPresent()) {
            throw new BaseException(POST_USERS_EXISTS_NICKNAME);
        }

        Optional<User> findUserPhoneNumber = repository.findUserByPhoneNumberAndStatus(registerReq.getPhoneNumber(), ACTIVE);
        if (findUserId.isPresent()) {
            throw new BaseException(POST_USERS_EXISTS_PHONE_NUMBER);
        }

        User newUser = User.builder()
                .userId(registerReq.getUserId())
                .password(encryptedPassword)
                .nickname(registerReq.getNickname())
                .age(registerReq.getAge())
                .phoneNumber(registerReq.getPhoneNumber())
                .role(USER)
                .status(ACTIVE)
                .createTime(LocalDateTime.now())
                .build();

        log.info("new user: {}", newUser);
        repository.save(newUser);

        return new LoginRes(newUser.getUserId(), newUser.getPassword());
    }

    @Override
    public LoginRes login(LoginReq loginReq) {

        String userId = loginReq.getUserId();
        String password = loginReq.getPassword();   // encrypted password

        User findUser = repository.findUserByUserIdAndStatus(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(FAILED_TO_LOGIN));

        if (password.equals(findUser.getPassword())) {
            session.setAttribute("loginUser", findUser);
            session.setMaxInactiveInterval(60); // 세션 유효 시간 설정
            return new LoginRes(userId, password);
        } else {
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    @Override
    public Long editUserProfile(EditMemberReq editMemberReq) {
        User sessionUser = getSessionUser();    // 예외 처리 되어있음

        Boolean duplicatedNickname = isDuplicatedNickname(editMemberReq.getNickname());
        if (Boolean.TRUE.equals(duplicatedNickname)) {
            throw new BaseException(POST_USERS_EXISTS_NICKNAME);
        } else {
            return sessionUser.getId();
        }
    }

    @Override
    public Long editPassword(PasswordReq passwordReq) {     // id, password
        User sessionUser = getSessionUser();    // 예외 처리 되어있음
        String password = encrypt(passwordReq.getPassword());

        // repository에서 회원 찾은 뒤에 password 변경
        User editedUser = repository.findUserByUserIdAndStatus(sessionUser.getUserId(), ACTIVE)
                .orElseThrow(() -> new BaseException(CHECK_USER));
        editedUser.setPassword(password);

        // session 재발급
        session.removeAttribute("loginUser");
        session.setAttribute("loginUser", editedUser);

        User newSessionUser = (User) session.getAttribute("loginUser");

        return newSessionUser.getId();
    }

    @Override
    public Long statusToDeleteUser() {
        User sessionUser = getSessionUser();

        repository.findUserByUserIdAndStatus(sessionUser.getUserId(), ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_TO_DELETE)).setStatusToDelete();

        return sessionUser.getId();
    }

    @Override
    public UserDto getUserProfile() {
        String userId = session.getAttribute("loginUserId").toString();

        User findUser = repository.findUserByUserIdAndStatus(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(CHECK_USER));

        if (findUser.getUpdateTime() == null) {
            return UserDto.builder()
                    .age(findUser.getAge())
                    .nickname(findUser.getNickname())
                    .phoneNumber(findUser.getPhoneNumber())
                    .createTime(findUser.getCreateTime())
                    .build();
        } else {
            return UserDto.builder()
                    .age(findUser.getAge())
                    .nickname(findUser.getNickname())
                    .phoneNumber(findUser.getPhoneNumber())
                    .updateTime(findUser.getUpdateTime())
                    .build();
        }
    }

    @Override
    public Boolean isDuplicatedUserId(String userId) {
        return repository.existsByUserIdAndStatus(userId, ACTIVE);
    }

    @Override
    public Boolean isDuplicatedNickname(String nickname) {
        return repository.existsByNicknameAndStatus(nickname, ACTIVE);
    }

    @Override
    public Boolean isDuplicatedPhoneNumber(String phoneNumber) {
        return repository.existsByPhoneNumberAndStatus(phoneNumber, ACTIVE);
    }

    @Override
    public String findUserPassword(String userId, String phoneNumber) {
        User findUser = repository.findUserPassword(userId, phoneNumber, ACTIVE)
                .orElseThrow(() -> new BaseException(CHECK_USER));

        return findUser.getPassword();
    }

    @Override
    public Optional<User> findUserByPhoneNumber(String phoneNumber) {
        return Optional.of(repository.findUserByPhoneNumberAndStatus(phoneNumber, ACTIVE)
                .orElseThrow(() -> new BaseException(CHECK_USER)));
    }


    private String encryptPassword(RegisterReq registerReq) {
        String password;
        try {
            password = encrypt(registerReq.getPassword());
            log.info("encrypt password: {}", password);
        } catch (Exception e) {
            throw new BaseException(ENCRYPTION_ERROR);
        }

        return password;
    }

    private User getSessionUser() {
        User sessionUser = (User) session.getAttribute("loginUser");
        if (sessionUser == null) {
            throw new BaseException(SESSION_EMPTY);
        }
        return sessionUser;
    }
}

package com.fastcampus.boardserver.domain.user.service;

import com.fastcampus.boardserver.constant.Status;
import com.fastcampus.boardserver.domain.user.User;
import com.fastcampus.boardserver.domain.user.dto.*;
import com.fastcampus.boardserver.domain.user.repository.UserRepository;
import com.fastcampus.boardserver.exception.BaseException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;

import java.util.Optional;

import static com.fastcampus.boardserver.constant.Status.*;
import static com.fastcampus.boardserver.constant.Status.ACTIVE;
import static com.fastcampus.boardserver.exception.BaseResponseStatus.*;
import static com.fastcampus.boardserver.utils.SHA256.encrypt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService service;

    private MockHttpSession session;

    @AfterEach
    void clearSession() {
        if (session != null) {
            session.clearAttributes();
        }
    }

    @Test
    @DisplayName("01-1. 회원가입 테스트")
    @Order(1)
    void userRegisterTest() {

        // given
        RegisterReq newUser = RegisterReq.builder()
                .userId("junit12345")
                .password("tester123!")
                .nickname("해적")
                .phoneNumber("010-9999-9999")
                .build();
        log.info("newUser: {}", newUser);

        // when
        LoginRes loginRes = service.register(newUser);
        String userId = loginRes.getUserId();

        // then
        Optional<User> findUser = repository.findUserByUserIdAndStatus(newUser.getUserId(), ACTIVE);
        findUser.ifPresent(user -> assertEquals(userId, user.getUserId()));
    }

    @Test
    @DisplayName("01-2. 중복된 아이디 가입 시도")
    @Order(2)
    void userRegisterDuplicatedUserIdTest() throws BaseException {

        // given
        RegisterReq newUser = RegisterReq.builder()
                .userId("tester")
                .password("tester123!")
                .nickname("김진우")
                .phoneNumber("010-1234-0000")
                .build();
        log.info("newUser: {}", newUser);

        // when
        BaseException exception = assertThrows(BaseException.class, () -> service.register(newUser));
        assertEquals(POST_USERS_EXISTS_USER_ID.getCode(), exception.getStatus().getCode());
    }

    @Test
    @DisplayName("01-3. 중복된 닉네임 가입 시도")
    @Order(3)
    void userRegisterDuplicatedNicknameTest() throws BaseException {

        // given
        RegisterReq newUser = RegisterReq.builder()
                .userId("tester123")
                .password("tester123!")
                .nickname("나비")
                .phoneNumber("010-1111-1111")
                .build();
        log.info("newUser: {}", newUser);

        // when & then
        BaseException exception = assertThrows(BaseException.class, () -> service.register(newUser));
        log.info("exception: {}", exception.getClass());
        assertEquals(POST_USERS_EXISTS_NICKNAME.getCode(), exception.getStatus().getCode());
    }

    @Test
    @DisplayName("01-4. 중복된 휴대폰 번호 가입 시도")
    @Order(4)
    void userRegisterDuplicatedPhoneNumberTest() throws BaseException {

        // given
        RegisterReq newUser = RegisterReq.builder()
                .userId("qwerty")
                .password("tester123!")
                .nickname("쿼티")
                .phoneNumber("010-0000-0000")
                .build();
        log.info("newUser: {}", newUser);

        // when & then
        BaseException exception = assertThrows(BaseException.class, () -> service.register(newUser));
        log.info("exception: {}", exception.getClass());
        assertEquals(POST_USERS_EXISTS_PHONE_NUMBER.getCode(), exception.getStatus().getCode());
    }

    @Test
    @DisplayName("02-1. 로그인 테스트")
    @Order(5)
    void loginTest() {

        // given
        LoginReq loginReq = LoginReq.builder()
                .userId("tester")
                .password(encrypt("tester123!"))
                .build();
        log.info("login: {}", loginReq.toString());

        // when
        LoginRes loginRes = service.login(loginReq);
        log.info("loginRes: {}", loginRes.toString());

        // then
        assertNotNull(loginRes);
    }

    @Test
    @DisplayName("02-2. 로그인 실패(아이디 불일치) 테스트")
    @Order(6)
    void loginAndUserIdNotMatch() {

        // given
        LoginReq loginReq = LoginReq.builder()
                .userId("zkcxvlka")
                .password(encrypt("tester123!"))
                .build();
        log.info("login: {}", loginReq.toString());

        // when & then
        BaseException exception = assertThrows(BaseException.class, () -> service.login(loginReq));
        log.info("exception: {}", exception.getClass());
        assertEquals(FAILED_TO_LOGIN.getCode(), exception.getStatus().getCode());
    }

    @Test
    @DisplayName("02-3. 로그인 실패(패스워드 불일치) 테스트")
    @Order(7)
    void loginAndPasswordNotMatch() {

        // given
        LoginReq loginReq = LoginReq.builder()
                .userId("tester")
                .password(encrypt("qwerty123!"))
                .build();
        log.info("login: {}", loginReq.toString());

        // when & then
        BaseException exception = assertThrows(BaseException.class, () -> service.login(loginReq));
        log.info("exception: {}", exception.getClass());
        assertEquals(FAILED_TO_LOGIN.getCode(), exception.getStatus().getCode());
    }

    @Test
    @DisplayName("02-4. 로그인 성공 후 세션 생성 테스트")
    @Order(8)
    void loginAndCreateSessionTest() {

        // given
        LoginReq loginReq = LoginReq.builder()
                .userId("tester")
                .password(encrypt("tester123!"))
                .build();
        log.info("login: {}", loginReq.toString());

        // when
        LoginRes loginRes = service.login(loginReq);
        if (loginRes != null) {
            session = new MockHttpSession();
            session.setAttribute("loginUser", loginRes);
            log.info("session: {}", session.getAttribute("loginUser"));
            session.setMaxInactiveInterval(60);
        }

        assertNotNull(session);
        assertThat(session.getAttribute("loginUser")).isInstanceOf(LoginRes.class);
        assertThat(session.getMaxInactiveInterval()).isEqualTo(60);
    }

    @Test
    @DisplayName("02-5. 로그인 성공 후 빈 세션 생성 테스트")
    @Order(9)
    void createNullSessionTest() {

        // given
        LoginReq loginReq = LoginReq.builder()
                .userId("tester")
                .password(encrypt("tester123!"))
                .build();
        log.info("login: {}", loginReq.toString());

        // when
        LoginRes loginRes = service.login(loginReq);

        session = new MockHttpSession();
        session.setAttribute("loginUser", null);


        BaseException exception = assertThrows(BaseException.class, () -> getSessionUser());
        assertEquals(SESSION_EMPTY.getCode(), exception.getStatus().getCode());
    }

    @Test
    @DisplayName("03-1. 유저 정보 확인")
    @Order(10)
    void getUserProfile() {

        // given
        LoginRes loginRes = loginToTester();

        session = new MockHttpSession();
        session.setAttribute("loginUser", loginRes);

        UserDto userProfile = service.getUserProfile();
        log.info("userProfile: {}", userProfile.toString());

        // then
        assertNotNull(userProfile);
    }

    @Test
    @DisplayName("04-1 회원 정보 수정")
    @Order(11)
    void editUserProfile() {

        // given
        LoginRes loginRes = loginToTester();
        session = new MockHttpSession();
        session.setAttribute("loginUser", loginRes);

        EditMemberReq editUser = EditMemberReq.builder()
                .nickname("hihi")
                .age(11)
                .build();

        // when
        Long userId = service.editUserProfile(editUser);
        log.info("userId: {}", userId);

        //then
        assertThat(userId).isEqualTo(repository.findById(userId).get().getId());
    }

    @Test
    @DisplayName("04-2 회원 정보 수정 실패")
    @Order(12)
    void editUserProfileFailed() {

        // given
        LoginRes loginRes = loginToTester();
        session = new MockHttpSession();
        session.setAttribute("loginUser", loginRes);

        EditMemberReq editUser = EditMemberReq.builder()
                .nickname("나비")
                .age(20)
                .build();

        // when & then
        BaseException exception = assertThrows(BaseException.class, () -> service.editUserProfile(editUser));
        assertEquals(POST_USERS_EXISTS_NICKNAME.getCode(), exception.getStatus().getCode());
    }

    @Test
    @DisplayName("05-1 패스워드 수정")
    @Order(13)
    void editPassword() {

        // given
        LoginRes loginRes = loginToTester();
        session = new MockHttpSession();
        session.setAttribute("loginUser", loginRes);

        PasswordReq editPassword = PasswordReq.builder()
                .id(6L)
                .password("qwerty123!")
                .build();

        // when
        Long userId = service.editPassword(editPassword);

        // then
        assertThat(userId).isEqualTo(repository.findById(userId).get().getId());
    }

    @Test
    @DisplayName("06-1. 회원 탈퇴")
    @Order(14)
    void statusToDeleteUser() {

        // given
        LoginRes loginRes = loginToTester();
        session = new MockHttpSession();
        session.setAttribute("loginUser", loginRes);

        // when
        Long userId = service.statusToDeleteUser();
        User findUser = repository.findById(userId).get();

        // then
        assertThat(findUser.getStatus()).isEqualTo(DELETED);
        log.info("getStatusToFindUser: {}", findUser.getStatus());
    }

    @Test
    @DisplayName("06-2. 이미 탈퇴한 회원")
    @Order(15)
    void statusToDeleteUserFailed() {

        // given
        LoginRes loginRes = loginToTester();
        session = new MockHttpSession();
        session.setAttribute("loginUser", loginRes);


        // when & then
        service.statusToDeleteUser();
        BaseException exception = assertThrows(BaseException.class, () -> service.statusToDeleteUser());
        assertEquals(SESSION_EMPTY.getCode(), exception.getStatus().getCode());
    }

    private LoginRes loginToTester() {  // LOGIN TO ACTIVE USER
        LoginReq loginReq = LoginReq.builder()
                .userId("tester")
                .password(encrypt("tester123!"))
                .build();

        LoginRes loginRes = service.login(loginReq);
        return loginRes;
    }

    private User getSessionUser() {
        User sessionUser = (User) session.getAttribute("loginUser");
        if (sessionUser == null) {
            throw new BaseException(SESSION_EMPTY);
        }
        return sessionUser;
    }

}
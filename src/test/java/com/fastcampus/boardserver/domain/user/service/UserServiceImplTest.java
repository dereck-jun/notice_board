package com.fastcampus.boardserver.domain.user.service;

import com.fastcampus.boardserver.constant.Status;
import com.fastcampus.boardserver.domain.user.User;
import com.fastcampus.boardserver.domain.user.dto.LoginRes;
import com.fastcampus.boardserver.domain.user.dto.RegisterReq;
import com.fastcampus.boardserver.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Optional;

import static com.fastcampus.boardserver.constant.Status.ACTIVE;
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

    @Test
    @DisplayName("01. 회원가입 테스트")
    @Order(1)
    void userRegisterTest() {

        // given
        RegisterReq newUser = RegisterReq.builder()
                .userId("tester")
                .password("tester123!")
                .nickname("나비")
                .phoneNumber("010-0000-0000")
                .build();
        log.info("newUser: {}", newUser);

        // when
        LoginRes loginRes = service.register(newUser);
        String userId = loginRes.getUserId();

        // then
        Optional<User> findUser = repository.findUserByUserIdAndStatus("tester", ACTIVE);
        findUser.ifPresent(user -> assertEquals(userId, user.getUserId()));
    }
}
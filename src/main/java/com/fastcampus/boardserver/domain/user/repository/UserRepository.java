package com.fastcampus.boardserver.domain.user.repository;

import com.fastcampus.boardserver.constant.Status;
import com.fastcampus.boardserver.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUserIdAndStatus(String userId, Status status);

    Optional<User> findUserByNicknameAndStatus(String nickname, Status status);

    Optional<User> findUserByPhoneNumberAndStatus(String phoneNumber, Status status);

    @Query("select u from User u where u.userId = :userId and u.phoneNumber = :phoneNumber and u.status = :status")
    Optional<User> findUserPassword(@Param("userId") String userId, @Param("phoneNumber") String phoneNumber, @Param("status") Status status);

    Boolean existsByUserIdAndStatus(String userId, Status status);

    Boolean existsByNicknameAndStatus(String nickname, Status status);

    Boolean existsByPhoneNumberAndStatus(String phoneNumber, Status status);
}

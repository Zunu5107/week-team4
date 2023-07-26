package com.clone.team4.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clone.team4.domain.user.entity.AccountInfo;

public interface AccountInfoRepository extends JpaRepository<AccountInfo, Long>, QAccountInfoRepository {
    Optional<AccountInfo> findByNickname(String nickname);
}

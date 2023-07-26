package com.clone.team4.domain.user.repository;

import com.clone.team4.domain.user.entity.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountInfoRepository extends JpaRepository<AccountInfo, Long>, QAccountInfoRepository {
    Optional<AccountInfo> findByNickname(String nickname);
}

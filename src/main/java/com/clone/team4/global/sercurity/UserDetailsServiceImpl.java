package com.clone.team4.global.sercurity;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.clone.team4.domain.user.entity.AccountInfo;
import com.clone.team4.domain.user.entity.User;
import com.clone.team4.domain.user.repository.AccountInfoRepository;
import com.clone.team4.domain.user.repository.UserRepository;
import com.clone.team4.global.redis.RedisService;
import com.clone.team4.global.util.AESUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AESUtil aesUtil;
    private final RedisService redisService;
    private final JPAQueryFactory queryFactory;
    private final UserRepository userRepository;
    private final AccountInfoRepository accountInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + email));
        AccountInfo accountInfo = accountInfoRepository.findById(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + email));
        return new UserDetailsImpl(user, accountInfo);
    }

    public UserDetails loadUserByAccountInfo(String nickname) throws UsernameNotFoundException {
        AccountInfo accountInfo = accountInfoRepository.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + nickname));
        User user = userRepository.findById(accountInfo.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + nickname));
        return new UserDetailsImpl(user, accountInfo);
    }

    public String loadUsernameByRedis(String uuid){
        return redisService.getValues(uuid);
    }

    public void saveUsernameByRedis(String uuid, String username){
        redisService.setValues(uuid, username);
    }

    public void saveUsernameByRedisExpireDay(String uuid, String username, Long Day){
        redisService.setValuesByExpireDay(uuid, username, Day);
    }

    public String encryptAES(String uuid){
        return aesUtil.encrypt(uuid);
    }

    public String decryptAES(String encrypt){
        return aesUtil.decrypt(encrypt);
    }
}

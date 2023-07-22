package com.clone.team4.global.sercurity;


import com.clone.team4.domain.user.entity.AccountInfo;
import com.clone.team4.domain.user.entity.User;
import com.clone.team4.domain.user.entity.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final User user;
    private final AccountInfo accountInfo;
    private final UserRoleEnum role;

    public UserDetailsImpl(User user, AccountInfo accountInfo) {
        this.user = user;
        this.accountInfo = accountInfo;
        this.role = accountInfo.getRole();
    }

    public UserDetailsImpl(User user, UserRoleEnum role) {
        this.user = user;
        this.accountInfo = null;
        this.role = role;
    }

    public UserDetailsImpl(AccountInfo accountInfo) {
        this.user = null;
        this.accountInfo = accountInfo;
        this.role = accountInfo.getRole();
    }


    public User getUser() {
        return user;
    }

    public AccountInfo getAccountInfo() { return this.accountInfo; }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
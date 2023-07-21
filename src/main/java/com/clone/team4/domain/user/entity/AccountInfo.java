package com.clone.team4.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountInfo {
    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String nickname;

    private String introduce;

    private String profileImage;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public AccountInfo(Long id, User user, String nickname) {
        this.id = id;
        this.user = user;
        this.nickname = nickname;
        this.introduce = null;
        this.profileImage = null;
        this.role = UserRoleEnum.USER;
    }

    public void modifiedIntroduce(String introduce){
        this.introduce = introduce;
    }

    public void modifiedProfileImage(String profileImage){
        this.profileImage = profileImage;
    }
}

package com.clone.team4.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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

    @Column(nullable = false, unique = true)
    private String nickname;

    private String introduce;

    private String profileImage;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public AccountInfo(User user, String nickname) {
        this.user = user;
        this.nickname = nickname;
        this.introduce = null;
        this.profileImage = "default";
        this.role = UserRoleEnum.USER;
    }

    public void modifiedIntroduce(String introduce){
        this.introduce = introduce;
    }

    public void modifiedProfileImage(String profileImage){
        this.profileImage = profileImage;
    }
}

package com.clone.team4.domain.comment.entity;

import com.clone.team4.domain.comment.dto.CommentRequestDto;
import com.clone.team4.domain.post.entity.Post;
import com.clone.team4.domain.user.entity.AccountInfo;
import com.clone.team4.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="accountInfo_id")
    public AccountInfo accountInfo;

    public Comment(CommentRequestDto requestDto, Post post, AccountInfo accountInfo) {
        this.comment = requestDto.getComment();
        this.post = post;
        this.accountInfo = accountInfo;
    }

    public void update(CommentRequestDto requestDto){
        this.comment = requestDto.getComment();
        this.modifiedAt = LocalDateTime.now();
    }

//    private String nickname(AccountInfo accountInfo){
//        String nickname = accountInfo.getNickname();
//        return nickname;
//    };

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name= "user_id")
//    private AccountInfo accountInfo;


}

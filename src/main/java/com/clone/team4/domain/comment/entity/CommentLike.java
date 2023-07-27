package com.clone.team4.domain.comment.entity;

import com.clone.team4.domain.user.entity.AccountInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comment_like")
@NoArgsConstructor
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_info_id")
    public AccountInfo accountInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;


    public CommentLike(AccountInfo accountInfo, Comment comment) {
        this.accountInfo = accountInfo;
        this.comment = comment;
    }
}

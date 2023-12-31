package com.clone.team4.domain.post.entity;

import com.clone.team4.domain.user.entity.AccountInfo;
import com.clone.team4.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "like_count", nullable = false)
    @ColumnDefault(value = "0")
    private Long likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_info_id", nullable = false)
    private AccountInfo accountInfo;

    private int detailsCount;

    public Post(String category, Long likeCount, AccountInfo accountInfo) {
        this.category = category;
        this.likeCount = likeCount;
        this.accountInfo = accountInfo;
    }

    public Post(String category, AccountInfo accountInfo, int detailsCount){
        this.detailsCount = detailsCount;
        this.category = category;
        this.likeCount = 0L;
        this.accountInfo = accountInfo;
    }

    public void updatePost(String category, int detailsCount) {
        this.detailsCount = detailsCount;
        this.category = category;
        this.modifiedAt = LocalDateTime.now();
    }

    public void increaseLikeCount() {
        this.likeCount += 1;
    }

    public void decreaseLikeCount() {
        this.likeCount -= 1;
    }


//    @OneToMany(fetch = FetchType.LAZY)
//    private List<Comment> comments = new ArrayList<>();
}

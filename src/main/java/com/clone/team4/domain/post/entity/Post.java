package com.clone.team4.domain.post.entity;

import org.hibernate.annotations.ColumnDefault;
import com.clone.team4.domain.user.entity.AccountInfo;
import com.clone.team4.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "accountInfo_id", nullable = false)
    private AccountInfo accountInfo;

    public Post(String category, Long likeCount, AccountInfo accountInfo) {
        this.category = category;
        this.likeCount = likeCount;
        this.accountInfo = accountInfo;
    }

    public Post(String category, AccountInfo accountInfo){
        this.category = category;
        this.likeCount = 0L;
        this.accountInfo = accountInfo;
    }

    //    @OneToMany(fetch = FetchType.LAZY)
//    private List<Comment> comments = new ArrayList<>();
}

package com.clone.team4.domain.post.entity;

import java.util.ArrayList;
import java.util.List;
import com.clone.team4.domain.post.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_details")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDetails {

    @Id
    private Long id;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public PostDetails(Long id, String image, String content, Post post) {
        this.id = id;
        this.image = image;
        this.content = content;
        this.post = post;
    }

    public static List<PostDetails> createPostDetailsList(List<String> imageUrls, List<PostRequestDto> contentList, Post post, int maxImageCount){
        Long postId = post.getId();
        List<PostDetails> postDetails = new ArrayList<>();

        for (int i = 0; i < imageUrls.size(); i++) {
            Long postDetailsId = postId * maxImageCount + i;
            postDetails.add(new PostDetails(postDetailsId, imageUrls.get(i), contentList.get(i).getContent(), post));
        }
        return postDetails;
    }
}

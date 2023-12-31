package com.clone.team4.domain.post.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.clone.team4.domain.post.entity.QPostDetails.postDetails;

@Repository
@RequiredArgsConstructor
public class QPostDetailsRepositoryImpl implements QPostDetailsRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Tuple findBySelectImageAndContentById(Long id) {
        Tuple tuple = jpaQueryFactory.select(postDetails.image, postDetails.content).from(postDetails)
            .where(postDetails.id.eq(id)).fetchOne();
        return tuple;
    }

    @Override
    public List<String> getPostImages(Long postId) {
        return jpaQueryFactory
            .select(postDetails.image)
            .from(postDetails)
            .where(postDetails.post.id.eq(postId))
            .fetch();
    }
}

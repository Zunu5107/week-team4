package com.clone.team4.domain.like.repository;

import com.clone.team4.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long>, QLikeRepository {
}

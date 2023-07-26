package com.clone.team4.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clone.team4.domain.like.entity.Like;

public interface LikeRepository extends JpaRepository<Like, Long>, QLikeRepository {
}

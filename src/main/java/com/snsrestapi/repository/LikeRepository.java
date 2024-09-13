package com.snsrestapi.repository;


import com.snsrestapi.domain.entity.Like;
import com.snsrestapi.domain.entity.Post;
import com.snsrestapi.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);
    Long countByPost(Post post);
}

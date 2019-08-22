package com.security.everywhere.repository;

import com.security.everywhere.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByContentId(String contentid);

    List<Review> findTopByContentIdAndStarGreaterThanEqualAndLikecountGreaterThanOrderByLikecountDesc(String contentid, double star, int likecount);

    List<Review> findTopByContentIdAndStarLessThanAndLikecountGreaterThanOrderByLikecountDesc(String contentid, double star, int likecount);

    @Transactional
    @Modifying
    @Query("update Review r set r.likecount = r.likecount+1 where r.id = :id")
    void pluslikecount(@Param("id") long id);

}

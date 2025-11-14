package com.loopers.domain.like;

import com.loopers.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LikeDomainService {
    private final LikeRepository likeRepository;

    public void applyLike(Long userId, Product product, Like like) {

        // 최초 좋아요 시
        if (like == null) {
            Like createdLike = Like.create(userId, product.getId());
            likeRepository.save(createdLike);
            product.increaseLikeCount();
            return;
        }

        // 이미 좋아요 상태 인 경우
        if (like.isActive()) {
            return;
        }

        // 취소 상태 -> 좋아요 등록
        like.like();
        product.increaseLikeCount();
    }

    public void applyUnLike(Long userId, Product product, Like like) {

        // 등록된 좋아요가 없거나, 이미 좋아요 취소 상태인 경우
        if (like == null || !like.isActive()) {
            return;
        }

        // 좋아요 상태 -> 종아요 취소
        like.unlike();
        product.decreaseLikeCount();
    }
}

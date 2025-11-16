package com.loopers.application.like;

import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeDomainService;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikeFacade {
    private final LikeService likeService;
    private final ProductService productService;
    private final LikeDomainService likeDomainService;

    @Transactional
    public void addLike(Long userId, Long productId) {
        Product product = productService.getProduct(productId);
        Optional<Like> like = likeService.findLike(userId, productId);

        // 최초 좋아요 시
        if(like.isEmpty()) {
            likeDomainService.applyLike(userId, product, likeService.createLike(Like.create(userId, product.getId())), true);
        } else {
            likeDomainService.applyLike(userId, product, like.get(), false);
        }

    }

    @Transactional
    public void removeLike(Long userId, Long productId) {
        Product product = productService.getProduct(productId);
        Optional<Like> like = likeService.findLike(userId, productId);

        likeDomainService.applyUnLike(userId, product, like.orElse(null));
    }

    @Transactional(readOnly = true)
    public List<Product> getLikedProducts(Long userId) {
        List<Like> likes = likeService.findActiveLikesByUserId(userId);

        List<Long> productIds = likes.stream()
                .map(Like::getProductId)
                .toList();

        if (productIds.isEmpty()) {
            return List.of();
        }

        return productService.findAll(productIds);
    }
}

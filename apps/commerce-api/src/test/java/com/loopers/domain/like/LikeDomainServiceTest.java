package com.loopers.domain.like;

import com.loopers.domain.common.vo.Money;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.vo.Stock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeDomainServiceTest {
    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private  LikeDomainService likeDomainService;

    private Product productWithId(Long id) {
        Product product = Product.create(1L, "상품이름1", Money.of(1000), Stock.of(1));
        ReflectionTestUtils.setField(product, "id", id);
        return product;
    }

    @Nested
    @DisplayName("좋아요 반영 시")
    class ApplyLike {

        @Test
        @DisplayName("중복 등록이 아닌 경우, Like가 저장되고 상품의 likeCount가 1 증가한다.")
        void succeeds_whenFirstLike() {
            // arrange
            Product product = productWithId(1L);

            // act
            likeDomainService.applyLike(1L, product, null);

            // assert
            assertThat(product.getLikeCount()).isEqualTo(1);
            verify(likeRepository, times(1)).save(any(Like.class));
        }

        @DisplayName("중복 등록인 경우, Like와 상품의 likeCount는 변하지 않는다. (멱등성 유지)")
        @Test
        void doesNothing_whenDuplicateLike() {
            // arrange
            Product product = productWithId(1L);
            Like like = Like.create(1L, product.getId());

            // act
            likeDomainService.applyLike(1L, product, like);

            // assert
            assertThat(product.getLikeCount()).isEqualTo(0);
            verify(likeRepository, never()).save(any());
        }
    }
    @DisplayName("유저의 좋아요 취소 시,")
    @Nested
    class UnLike {
        @DisplayName("중복 취소가 아닌 경우, Like가 soft-deleted되고 상품의 likeCount가 1 감소한다.")
        @Test
        void succeeds_whenFirstCancel() {
            // arrange
            Product product = productWithId(1L);
            Like like = Like.create(1L, product.getId());
            product.increaseLikeCount();

            // act
            likeDomainService.applyUnLike(1L, product, like);

            // assert
            assertThat(product.getLikeCount()).isEqualTo(0);
            assertThat(like.isActive()).isFalse();
            verify(likeRepository, never()).save(any());
        }

        @DisplayName("중복 취소인 경우, Like와 상품의 likeCount는 변하지 않는다. (멱등성 유지)")
        @Test
        void doesNothing_whenDuplicateCancel() {
            // arrange
            Product product = productWithId(1L);
            Like like = Like.create(1L, product.getId());
            like.unlike();

            // act
            likeDomainService.applyUnLike(1L, product, like);

            // assert
            assertThat(product.getLikeCount()).isEqualTo(0);
            assertThat(like.isActive()).isFalse();
            verify(likeRepository, never()).save(any());

        }
    }
}

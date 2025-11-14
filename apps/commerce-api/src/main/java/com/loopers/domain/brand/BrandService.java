package com.loopers.domain.brand;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class BrandService {

    private final BrandRepository brandRepository;

    @Transactional(readOnly = true)
    public Brand getBrand(Long id) {
        return brandRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 브랜드입니다."));
    }
}

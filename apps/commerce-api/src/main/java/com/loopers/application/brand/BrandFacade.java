package com.loopers.application.brand;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BrandFacade {
    private static final Logger log = LoggerFactory.getLogger(BrandFacade.class); // Logger 필드 추가

    private final BrandService brandService;

    public BrandInfo getBrand(Long brandId) {
        Brand brand = brandService.getBrand(brandId);
        return BrandInfo.from(brand);
    }
}

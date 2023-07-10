package com.ftiland.travelrental.product.dto;

import com.ftiland.travelrental.category.dto.CategoryDto;
import com.ftiland.travelrental.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailDto {

    private String title;
    private String content;
    private Integer baseFee;
    private Integer feePerDay;
    private Integer overdueFee;
    private Integer minimumRentalPeriod;

    private Double rate;
    private Integer viewCount;

    private List<CategoryDto> categories;
    private List<String> images;

    private String username;
    private String address;

    public static ProductDetailDto from(Product product, List<CategoryDto> categories) {
        return ProductDetailDto.builder()
                .title(product.getTitle())
                .content(product.getContent())
                .baseFee(product.getBaseFee())
                .feePerDay(product.getFeePerDay())
                .overdueFee(product.getOverdueFee())
                .minimumRentalPeriod(product.getMinimumRentalPeriod())
                .rate((double) product.getTotalRateScore() / product.getTotalRateCount())
                .viewCount(product.getViewCount())
                .username(product.getMember().getDisplayName())
                .address(product.getAddress())
                .images(null)
                .categories(categories).build();
    }
}

package com.wheredidmymoneygo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryDetail {
    private String categoryName;
    private Double value;
    private Double percentageOfTotal;
}

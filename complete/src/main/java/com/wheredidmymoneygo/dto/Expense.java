package com.wheredidmymoneygo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class Expense {

    @NotNull
    private Double value;
    @NotBlank
    private String category;
    @NotBlank
    private String wayMoneySpent;
}

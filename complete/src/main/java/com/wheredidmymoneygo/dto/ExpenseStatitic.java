package com.wheredidmymoneygo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ExpenseStatitic {

    private Double total;
    private List<CategoryDetail> categoryDetails;
    private List<Expense> allExpenses;
}

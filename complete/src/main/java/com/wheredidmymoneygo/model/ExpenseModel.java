package com.wheredidmymoneygo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@Setter
@ToString
@Document(collection = "expense")
public class ExpenseModel {

    @Id
    public String id;

    @NotNull
    private Double value;
    private CategoryWrapper categoryWrapper;
    private WayMoneySpentEnum wayMoneySpent;
    private Instant timestamp;
}

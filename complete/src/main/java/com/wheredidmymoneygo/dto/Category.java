package com.wheredidmymoneygo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Document(collection = "category")
public class Category {

    @Id
    public String id;

    @NotNull
    private String name;
    @Min(1)
    private Integer goal;
}

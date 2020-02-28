package com.wheredidmymoneygo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class CategoryWrapper {

    @Id
    public String id;

    @NotNull
    private String name;
}

package com.example.grid.data.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class AdvanceSearchRequest extends DefaultPageRequest {
    @NotNull
    private String query;
}

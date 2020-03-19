package com.example.grid.data.payload;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdvanceSearchRequest extends DefaultPageRequest {
    private String query;
}

package com.example.grid.data.payload;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SearchByAnyWordRequest extends DefaultPageRequest {
    private String words;
}

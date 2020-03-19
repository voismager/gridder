package com.example.grid.data.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SearchByTagsRequest extends DefaultPageRequest {
    private List<String> tags;
}

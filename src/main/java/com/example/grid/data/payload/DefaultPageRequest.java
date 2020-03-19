package com.example.grid.data.payload;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DefaultPageRequest {
    private int page = 0;
    private int size = 10;
}

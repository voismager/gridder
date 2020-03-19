package com.example.grid.data.payload.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class Authenticated {
    private String accessToken;
}

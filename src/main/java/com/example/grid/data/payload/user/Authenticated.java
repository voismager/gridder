package com.example.grid.data.payload.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
@AllArgsConstructor
public class Authenticated {
    @NotNull
    private String accessToken;
}

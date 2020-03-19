package com.example.grid.controller;

import com.example.grid.data.payload.user.AuthUser;
import com.example.grid.data.payload.user.Authenticated;
import com.example.grid.data.payload.user.CreateUser;
import com.example.grid.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Authenticated create(@RequestBody CreateUser data) {
        return this.userService.create(data);
    }

    @RequestMapping(path = "/auth/signin", method = RequestMethod.POST)
    public Authenticated signin(@RequestBody AuthUser data) {
        return this.userService.authenticate(data.getUsername(), data.getPassword());
    }
}

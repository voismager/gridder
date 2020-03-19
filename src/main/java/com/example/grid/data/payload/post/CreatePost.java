package com.example.grid.data.payload.post;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter @Setter
public class CreatePost {
    private String content;
    private Collection<String> tags;
    private String topic;
    private boolean topicCreator;
}

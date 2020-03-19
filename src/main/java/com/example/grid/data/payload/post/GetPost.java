package com.example.grid.data.payload.post;

import com.example.grid.data.db.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Date;

@Getter @Setter
public class GetPost {
    private String id;
    private String content;
    private String author;
    private Long createdAt;
    private Long updatedAt;
    private Collection<String> tags;

    public GetPost(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.author = post.getAuthor();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.tags = post.getTags();
    }
}

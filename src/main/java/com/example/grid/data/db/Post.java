package com.example.grid.data.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Collection;
import java.util.Date;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

//test
@Document(indexName = "blog", type = "post")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Text)
    private String author;

    @Field(type = FieldType.Date)
    private Long createdAt;

    @Field(type = FieldType.Date)
    private Long updatedAt;

    @Field(type = Keyword)
    private Collection<String> tags;

    @Field(type = FieldType.Text)
    private String topicId;

    @Field(type = FieldType.Boolean)
    private Boolean topicCreator;
}

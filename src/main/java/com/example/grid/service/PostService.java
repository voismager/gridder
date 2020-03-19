package com.example.grid.service;

import com.example.grid.data.db.Post;
import com.example.grid.data.payload.post.CreatePost;
import com.example.grid.data.payload.post.GetPost;
import com.example.grid.repository.PostRepository;
import com.example.grid.repository.UserRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void create(CreatePost payload, String author) {
        if (!userRepository.existsById(author)) throw new EntityNotFoundException();

        Post post = new Post();
        post.setContent(payload.getContent());
        post.setAuthor(author);
        post.setTags(payload.getTags());

        if (payload.isTopicCreator()) {
            post.setTopicId(UUID.randomUUID().toString());
            post.setTopicCreator(true);
        } else {
            post.setTopicId(payload.getTopic());
            post.setTopicCreator(false);
        }

        Date now = new Date();
        post.setCreatedAt(now.getTime());
        post.setUpdatedAt(now.getTime());

        this.postRepository.save(post);
    }

    public Page<GetPost> advanceSearch(String query, Pageable pageable) {
        query = query.trim();

        if (query.isEmpty()) {
            return this.postRepository.findAll(pageable).map(GetPost::new);
        }
        if (query.startsWith("{") && query.endsWith("}")) {
            String[] tags = query.substring(1, query.length() - 1).split(" ");
            BoolQueryBuilder queryBuilder = boolQuery();
            for (String tag : tags) queryBuilder.must(termQuery("tags", tag));
            return this.postRepository.search(queryBuilder, pageable).map(GetPost::new);
        }
        if (query.startsWith("\"") && query.endsWith("\"")) {
            String phrase = query.substring(1, query.length() - 1);
            MatchPhraseQueryBuilder queryBuilder = matchPhraseQuery("content", phrase);
            return this.postRepository.search(queryBuilder, pageable).map(GetPost::new);
        }
        if (query.startsWith("(") && query.endsWith(")")) {
            String words = query.substring(1, query.length() - 1);
            MatchQueryBuilder queryBuilder = matchQuery("content", words).operator(Operator.AND);
            return this.postRepository.search(queryBuilder, pageable).map(GetPost::new);
        }
        if (query.startsWith("[") && query.endsWith("]")) {
            String words = query.substring(1, query.length() - 1);
            MatchQueryBuilder queryBuilder = matchQuery("content", words).operator(Operator.OR);
            return this.postRepository.search(queryBuilder, pageable).map(GetPost::new);
        }

        return Page.empty();
    }
}

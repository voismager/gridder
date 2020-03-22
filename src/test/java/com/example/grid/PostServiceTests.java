package com.example.grid;

import com.example.grid.data.payload.post.CreatePost;
import com.example.grid.data.payload.user.CreateUser;
import com.example.grid.service.PostService;
import com.example.grid.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class PostServiceTests {

    @Autowired
    private PostService postService;

    @BeforeAll
    static void init(@Autowired UserService userService, @Autowired PostService postService) {
        for (int i = 0; i < 3; i++) {
            CreateUser createUser = new CreateUser();
            createUser.setUsername("user" + i);
            createUser.setPassword("password" + i);
            userService.create(createUser);
        }

        Random random = new Random();
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer congue metus et ullamcorper feugiat. Sed id elementum odio. Sed nec posuere arcu. Suspendisse vel aliquet dolor. Sed fermentum ut libero quis lobortis. Sed placerat nisl at facilisis iaculis. Cras sed lacus malesuada, cursus justo eget, mollis ante. Donec euismod ante quis turpis imperdiet, et sodales odio gravida. Sed tristique sapien lorem, ut vehicula nunc elementum at. Sed augue magna, viverra non convallis consequat, auctor quis quam.\n" +
                "\n" +
                "Ut interdum cursus mauris eu pretium. Nullam ut odio at diam sagittis lacinia at sed urna. Nulla ut orci justo. Nam faucibus ullamcorper pharetra. Pellentesque et orci vitae nisl dapibus placerat. Quisque vitae velit rutrum, maximus dolor vel, euismod sem. Sed vulputate quam sit amet hendrerit placerat.\n" +
                "\n" +
                "Nunc velit nulla, ultrices sit amet tincidunt sit amet, semper a purus. Nulla a erat elementum, viverra lacus non, rhoncus mi. Mauris nec egestas magna. Nullam eget hendrerit libero. Phasellus sagittis libero ut pellentesque dictum. Donec ut dictum erat, sed fermentum risus. Nullam ultricies nibh metus, ac tincidunt metus dignissim eget. Maecenas est sapien, faucibus non tellus sit amet, varius auctor lacus. Donec pulvinar gravida elementum.\n" +
                "\n" +
                "Nulla turpis quam, feugiat non rutrum et, auctor sed dolor. Proin quis lacus metus. Nulla est dolor, convallis eget sem in, dapibus lobortis leo. Vivamus id eros nec orci mollis hendrerit. In tellus lorem, vehicula id enim in, interdum consequat nisi. Mauris justo eros, bibendum a mollis eu, ultricies vitae magna. Nullam mattis lacinia justo, tristique congue ante fringilla sit amet. In hac habitasse platea dictumst. Ut efficitur semper tortor a posuere. Curabitur pulvinar blandit auctor. Mauris cursus ligula nunc, quis condimentum arcu interdum quis. Quisque neque mi, auctor eget sagittis eu, laoreet ac magna. Sed efficitur bibendum ex. Aliquam et vestibulum urna. Nunc quis lectus ultrices, commodo nibh lacinia, facilisis quam.\n" +
                "\n" +
                "Vestibulum non leo augue. Morbi molestie laoreet aliquam. Morbi at risus pulvinar, rutrum arcu ac, consequat sapien. Cras blandit, erat nec varius auctor, ligula leo tristique nisi, eu rhoncus risus lorem eget libero. Quisque cursus lorem sit amet laoreet hendrerit. Nunc ac vehicula risus, in fermentum nunc. Phasellus efficitur vulputate sem, a tristique elit consectetur at. Praesent sed dui et mauris condimentum mollis in sit amet erat.";
        List<String> words = Arrays.asList(text.split(" "));

        int index = 0;
        int l = words.size() / 10;

        for (int i = 0; i < 10; i++) {
            List<String> sublist = words.subList(index, index + l);
            StringBuilder builder = new StringBuilder();
            for (String word : sublist) builder.append(word).append(' ');

            CreatePost createPost = new CreatePost();
            createPost.setContent(builder.toString());
            createPost.setTags(sublist);
            createPost.setTopicCreator(true);

            postService.create(createPost, "user" + random.nextInt(3));

            index += l;
        }
    }

    @Test
    void createMethodShouldNotThrowExceptionsOnValidRequest() {
        assertThatCode(() -> {
            CreatePost createPost = new CreatePost();
            createPost.setContent("Hello, test");
            createPost.setTags(Arrays.asList("hello", "test", "tag"));
            createPost.setTopicCreator(true);
            postService.create(createPost, "user0");
        }).doesNotThrowAnyException();
    }

    @Test
    void createMethodShouldThrowExceptionsOnInvalidRequest() {
        assertThrows(EntityNotFoundException.class, () -> {
            CreatePost createPost = new CreatePost();
            createPost.setContent("Hello, test");
            createPost.setTags(Arrays.asList("hello", "test", "tag"));
            createPost.setTopicCreator(true);
            postService.create(createPost, "us0r");
        });
    }

    @Test
    void testSearchByAllWordsRequest() {
        PageRequest req = PageRequest.of(0, 10);
        assertThat(postService.advanceSearch("(Lorem)", req)).isNotEmpty();
        assertThat(postService.advanceSearch("(Lorem ipsum)", req)).isNotEmpty();
        assertThat(postService.advanceSearch("(Lorem ipsum dolor)", req)).isNotEmpty();
        assertThat(postService.advanceSearch("(Lorem dolor ipsum)", req)).isNotEmpty();
        assertThat(postService.advanceSearch("(Lorem ipsum word)", req)).isEmpty();
        assertThat(postService.advanceSearch("()", req)).isEmpty();
    }

    @Test
    void testSearchByAnyWordRequest() {
        PageRequest req = PageRequest.of(0, 10);
        assertThat(postService.advanceSearch("[Lorem]", req)).isNotEmpty();
        assertThat(postService.advanceSearch("[Lorem ipsum]", req)).isNotEmpty();
        assertThat(postService.advanceSearch("[Lorem ipsum dolor]", req)).isNotEmpty();
        assertThat(postService.advanceSearch("[Lorem dolor ipsum]", req)).isNotEmpty();
        assertThat(postService.advanceSearch("[Lorem ipsum word]", req)).isNotEmpty();
        assertThat(postService.advanceSearch("[]", req)).isEmpty();
    }

    @Test
    void testSearchByPhraseRequest() {
        PageRequest req = PageRequest.of(0, 10);
        assertThat(postService.advanceSearch("\"Lorem\"", req)).isNotEmpty();
        assertThat(postService.advanceSearch("\"Lorem ipsum\"", req)).isNotEmpty();
        assertThat(postService.advanceSearch("\"dolor sit amet\"", req)).isNotEmpty();
        assertThat(postService.advanceSearch("\"Lorem ipsum dolor sit amet\"", req)).isNotEmpty();
        assertThat(postService.advanceSearch("\"dolor sit amet a\"", req)).isEmpty();
        assertThat(postService.advanceSearch("\"dolor sit amet c\"", req)).isEmpty();
    }

    @Test
    void testSearchByTagsRequest() {
        PageRequest req = PageRequest.of(0, 10);
        assertThat(postService.advanceSearch("{Lorem}", req)).isNotEmpty();
        assertThat(postService.advanceSearch("{Lorem ipsum}", req)).isNotEmpty();
        assertThat(postService.advanceSearch("{Lorem ipsum dolor}", req)).isNotEmpty();
        assertThat(postService.advanceSearch("{Lorem ipsum ipsum}", req)).isNotEmpty();
        assertThat(postService.advanceSearch("{Lorem word}", req)).isEmpty();
        assertThat(postService.advanceSearch("{}", req)).isEmpty();
    }
}

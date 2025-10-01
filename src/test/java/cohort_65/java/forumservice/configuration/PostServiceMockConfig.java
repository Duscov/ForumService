package cohort_65.java.forumservice.configuration;

import cohort_65.java.forumservice.post.service.PostService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class PostServiceMockConfig {
    @Bean
    public PostService postService() {
        return Mockito.mock(PostService.class);
    }
}

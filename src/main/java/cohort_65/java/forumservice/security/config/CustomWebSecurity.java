package cohort_65.java.forumservice.security.config;

import cohort_65.java.forumservice.post.dao.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("webSecurity")
@RequiredArgsConstructor
public class CustomWebSecurity {
    final PostRepository postRepository;

    public boolean isPostAuthor(String author, String postId) {
        return postRepository.findById(postId).map(p -> p.getAuthor().equals(author)).orElse(false);
    }

}
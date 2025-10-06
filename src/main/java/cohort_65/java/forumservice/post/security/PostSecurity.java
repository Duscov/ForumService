package cohort_65.java.forumservice.post.security;

import cohort_65.java.forumservice.post.dao.PostRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Component;

@Component("postSecurity")
@RequiredArgsConstructor
public class PostSecurity {
    private final PostRepository postRepository;

 //   public boolean isOwner(Authentication authentication, Long postId) {
   //     return postRepository.findById(String.valueOf(postId))
   //             .map(post -> post.getAuthor().equals(authentication.getName()))
     //           .orElse(false);
  //  }
}

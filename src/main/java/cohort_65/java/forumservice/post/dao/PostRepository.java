package cohort_65.java.forumservice.post.dao;

import cohort_65.java.forumservice.post.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public interface PostRepository extends MongoRepository<Post, String> {

    Iterable<Post> findAllByAuthorIgnoreCase(String author);

    Iterable<Post> findAllByTagsIgnoreCaseIn(Set<String> tags);

    Iterable<Post> findAllByDateCreatedBetween(LocalDate dateCreated, LocalDate dateCreated2);

    Iterable<Post> findAllByTitleIgnoreCase(String title);
}
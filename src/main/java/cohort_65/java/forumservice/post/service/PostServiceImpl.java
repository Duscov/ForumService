package cohort_65.java.forumservice.post.service;

import cohort_65.java.forumservice.post.dao.PostRepository;
import cohort_65.java.forumservice.post.dto.NewPostDto;
import cohort_65.java.forumservice.post.dto.PostDto;
import cohort_65.java.forumservice.post.model.Comment;
import cohort_65.java.forumservice.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    final PostRepository postRepository;
    final ModelMapper modelMapper;

    @Override
    public PostDto addNewPost(NewPostDto newPostDto, String author) {
        Post post = new Post(newPostDto.getTitle(),
                newPostDto.getContent(), author, newPostDto.getTags());
        post = postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto findPostById(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found: " + id));
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public void addLike(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found: " + id));
        post.addLike();
        postRepository.save(post);
    }

    @Override
    public List<PostDto> findPostsByAuthor(String author) {
        List<Post> posts = postRepository.findByAuthor(author);
        return posts.stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();
    }

    @Override
    public void addComment(String postId, String user, String message) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found: " + postId));
        post.addComment(new Comment(user, message));
        postRepository.save(post);
    }

    @Override
    public void deletePost(String id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("Post not found: " + id);
        }
        postRepository.deleteById(id);
    }

    @Override
    public List<PostDto> findPostsByTags(Set<String> tags) {
        List<Post> posts = postRepository.findByTagsIn(tags);
        return posts.stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();
    }

    @Override
    public List<PostDto> findPostsByPeriod(LocalDateTime from, LocalDateTime to) {
        List<Post> posts = postRepository.findByDateCreatedBetween(from, to);
        return posts.stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();
    }

    @Override
    public PostDto updatePost(String id, NewPostDto newPostDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found: " + id));
        post.setTitle(newPostDto.getTitle());
        post.setContent(newPostDto.getContent());
        post.getTags().clear();
        post.getTags().addAll(newPostDto.getTags());
        post = postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }
}
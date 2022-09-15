package com.shadab.service.impl;

import com.shadab.entity.Comment;
import com.shadab.entity.Post;
import com.shadab.exception.BlogAPIException;
import com.shadab.exception.ResourceNotFoundException;
import com.shadab.payload.CommentDto;
import com.shadab.repository.CommentRepository;
import com.shadab.repository.PostRepository;
import com.shadab.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {

        //convert to Entity
        Comment comment = mapToEntity(commentDto);

        //retrieve POST using postId
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("post","id",postId));

        //save to db
        comment.setPost(post);

        Comment newComment = commentRepository.save(comment);

        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDto> getCommentByPostId(long postId) {

        List<Comment> comments =commentRepository.findByPostId(postId);
        return comments.stream().map((comment)->mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {

        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("post","id",postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("comment","id", commentId));

        if(!Objects.equals(comment.getPost().getId(), post.getId())){
                throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to Post");
        }

        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, long commentId, CommentDto commentRequest) {

        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("post","id",postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("comment","id", commentId));

        if(!Objects.equals(comment.getPost().getId(), post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to Post");
        }

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {

        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("post","id",postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("comment","id", commentId));

        if(!Objects.equals(comment.getPost().getId(), post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to Post");
        }

        commentRepository.delete(comment);
    }

    //Map entity to dto
    private CommentDto mapToDTO(Comment comment){

        CommentDto commentDto = new CommentDto();

        commentDto.setId(comment.getId());
        commentDto.setBody(comment.getBody());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());

        return commentDto;

    }

    //Map dto to entity
    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = new Comment();

        comment.setId(commentDto.getId());
        comment.setBody(commentDto.getBody());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());

        return comment;
    }

}

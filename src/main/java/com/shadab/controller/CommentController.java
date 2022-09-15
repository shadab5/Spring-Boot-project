package com.shadab.controller;

import com.shadab.payload.CommentDto;
import com.shadab.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @PostMapping("posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(name = "postId") long postId,
                                                    @Valid @RequestBody CommentDto commentDto){
        return new ResponseEntity<>(commentService.createComment(postId,commentDto), HttpStatus.CREATED);
    }

    @GetMapping("posts/{postId}/comments")
    public List<CommentDto> getCommentByPostId(@PathVariable(name = "postId") long postId){
        return commentService.getCommentByPostId(postId);
    }

    @GetMapping("posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> getCommentByPostId(@PathVariable(name = "postId") Long postId,@PathVariable(name ="id") Long commentId){
        CommentDto commentDto = commentService.getCommentById(postId,commentId);
        return new ResponseEntity<>(commentDto,HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable (value ="postId") Long postId, @PathVariable(value ="id") Long commentId,@Valid @RequestBody CommentDto commentDto){

        CommentDto updatedComment =  commentService.updateComment(postId,commentId,commentDto);
        return new ResponseEntity<>(updatedComment,HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(value ="postId") Long postId,@PathVariable(value="id") Long commentId){
        commentService.deleteComment(postId,commentId);
        return new ResponseEntity<>("Comment deleted Successfully",HttpStatus.OK);
    }
}

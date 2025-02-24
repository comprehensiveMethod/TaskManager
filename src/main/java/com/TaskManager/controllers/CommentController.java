package com.TaskManager.controllers;

import com.TaskManager.dtos.CommentRequestDto;
import com.TaskManager.dtos.CommentResponseDto;
import com.TaskManager.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto commentRequestDto){
        try{
            return ResponseEntity.ok(commentService.createComment(commentRequestDto));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable Long id){
        try{
            return ResponseEntity.ok(commentService.getCommentById(id));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/ByAuthor/{email}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByAuthor(@PathVariable String email){
        try{
            return ResponseEntity.ok(commentService.getCommentsByAuthor(email));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/ByTask/{id}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByTask(@PathVariable Long id){
        try{
            return ResponseEntity.ok(commentService.getCommentsByTaskId(id));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }
}

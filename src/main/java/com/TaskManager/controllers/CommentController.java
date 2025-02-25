package com.TaskManager.controllers;

import com.TaskManager.dtos.CommentRequestDto;
import com.TaskManager.dtos.CommentResponseDto;
import com.TaskManager.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;


    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto commentRequestDto){
        Authentication authorization = SecurityContextHolder.getContext().getAuthentication();
        String requester_email = authorization.getPrincipal().toString();
        try{
            return ResponseEntity.ok(commentService.createComment(commentRequestDto,requester_email));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }catch (IllegalStateException ex){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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

    //TODO сделать фильтрацией
    @GetMapping("/byAuthor/{email}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByAuthor(@PathVariable String email){
        try{

            return ResponseEntity.ok(commentService.getCommentsByAuthor(email));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/byTask/{id}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByTask(@PathVariable Long id){
        try{
            return ResponseEntity.ok(commentService.getCommentsByTaskId(id));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }

}

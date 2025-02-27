package com.TaskManager.controllers;

import com.TaskManager.dtos.CommentRequestDto;
import com.TaskManager.dtos.CommentResponseDto;
import com.TaskManager.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comment API", description = "Управление комментариями")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "Создать комментарий", description = "Создает комментарий(создавать комментарии может только Исполнитель или Админ)")
    @ApiResponse(responseCode = "200", description = "Комментарий создан")
    @ApiResponse(responseCode = "404", description = "Задача не найдена",content = @Content())
    @ApiResponse(responseCode = "403", description = "Доступ запрещен" ,content = @Content())
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto commentRequestDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String requester_email = authentication.getPrincipal().toString();
        if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))){
            return ResponseEntity.ok(commentService.createComment(commentRequestDto,requester_email));
        }
        try{
            return ResponseEntity.ok(commentService.createComment(commentRequestDto,requester_email));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }catch (IllegalStateException ex){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Получить комментарий по id", description = "Выводит данные о комментарии по его id")
    @ApiResponse(responseCode = "200", description = "Комментарий создан")
    @ApiResponse(responseCode = "404", description = "Задача не найдена",content = @Content())
    @ApiResponse(responseCode = "403", description = "Доступ запрещен" ,content = @Content())
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable Long id){
        try{
            return ResponseEntity.ok(commentService.getCommentById(id));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Получить все комментарии", description = "Выводит данные о всех комментариях с пагинацией и фильтрацией по author(email) и taskId(id задачи)")
    @ApiResponse(responseCode = "200", description = "Комментарии получены")
    @ApiResponse(responseCode = "404", description = "Задача не найдена",content = @Content())
    @ApiResponse(responseCode = "403", description = "Доступ запрещен" ,content = @Content())
    @GetMapping
    public ResponseEntity<Page<CommentResponseDto>> getAllComments(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   @RequestParam(required = false) String author,
                                                                   @RequestParam(required = false) Long taskId){
        return ResponseEntity.ok(commentService.getAllTasks(PageRequest.of(page,size),author,taskId));
    }


}

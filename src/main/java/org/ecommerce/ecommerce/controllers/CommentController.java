package org.ecommerce.ecommerce.controllers;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ecommerce.ecommerce.dtos.CommentDTO;
import org.ecommerce.ecommerce.dtos.CommentImageDTO;
import org.ecommerce.ecommerce.models.Comment;
import org.ecommerce.ecommerce.models.CommentImage;
import org.ecommerce.ecommerce.models.Product;
import org.ecommerce.ecommerce.responses.CommentCountResponse;
import org.ecommerce.ecommerce.responses.CommentResponse;
import org.ecommerce.ecommerce.responses.create.ListCommentCountResponse;
import org.ecommerce.ecommerce.services.impl.CommentService;
import org.ecommerce.ecommerce.services.impl.ProductService;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    private final ProductController productController;

    @GetMapping("/count")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ListCommentCountResponse> countComment(
    ) {
        try {
            Map<Long, Integer> commentCountMap = commentService.countComment();
            List<CommentCountResponse> commentCountResponses = commentCountMap.entrySet().stream()
                    .map(entry -> CommentCountResponse.builder()
                            .productId(entry.getKey())
                            .comment(entry.getValue())
                            .build())
                    .toList();
            return ResponseEntity.ok(ListCommentCountResponse.builder()
                    .commentCountResponses(commentCountResponses)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("")
    @Transactional
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createComment(@Valid @RequestBody CommentDTO commentDto) {
        try {
            Comment comment = commentService.createComment(commentDto);
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }
    @PostMapping(value = "/uploadImages/{commentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> uploadImage(@PathVariable Long commentId, @ModelAttribute("files") List<MultipartFile> files) {
        try {
            Comment comment = commentService.getCommentById(commentId);
            if (comment == null) {
                return ResponseEntity.badRequest().body("Comment not found");
            }
            files = files == null ? new ArrayList<MultipartFile>() : files;
            List<CommentImage> commentImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file != null) {
                    if (file.getSize() <= 0) {
                        continue;
                    }
                    if (file.getSize() >= 1024 * 1024 * 10) {
                        return ResponseEntity.badRequest().body("Image size must be less than 10MB");
                    }
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        return ResponseEntity.badRequest().body("Only image files are allowed");
                    }
                    String uniqueFileName = productController.storeFile(file);
                    CommentImage commentImage = commentService.createCommentImage(commentId, CommentImageDTO.builder()
                            .image_url(uniqueFileName)
                            .commentId(commentId)
                            .build());
                    commentImages.add(commentImage);
                }
            }
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error while uploading image");
        }
    }
    @GetMapping("/viewImages/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName)
    {
        try {
            Path path = Paths.get("uploads/",imageName);
            UrlResource resource = new UrlResource(path.toUri());
            if(resource.exists()) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
            }else {
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e)
        {
            return ResponseEntity.notFound().build();
        }
    }

}

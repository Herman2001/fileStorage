package com.herman.fileStorage.controller;

import com.herman.fileStorage.dto.FileResource;
import com.herman.fileStorage.dto.FileResponseDto;
import com.herman.fileStorage.entity.FileEntity;
import com.herman.fileStorage.entity.Folder;
import com.herman.fileStorage.entity.User;
import com.herman.fileStorage.security.SecurityUtils;
import com.herman.fileStorage.service.FileService;
import com.herman.fileStorage.service.FolderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;
    private final FolderService folderService;

    public FileController(FileService fileService, FolderService folderService) {
        this.fileService = fileService;
        this.folderService = folderService;
    }

    /**
     * Upload a file to a folder owned by the authenticated user.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResource> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folderId") Long folderId
    ) throws IOException {
        User user = SecurityUtils.getAuthenticatedUser();

        Folder folder = folderService.getFolderByIdAndUser(folderId, user.getId());

        FileEntity savedFile = fileService.uploadFile(
                file.getOriginalFilename(),
                file.getBytes(),
                folder,
                user
        );

        FileResource resource = new FileResource(
                new FileResponseDto(
                        savedFile.getId(),
                        savedFile.getFilename(),
                        savedFile.getFolder().getId(),
                        savedFile.getFolder().getName()
                )
        );
        resource.add(linkTo(methodOn(FileController.class).downloadFile(savedFile.getId())).withRel("download"));
        resource.add(linkTo(methodOn(FileController.class).deleteFile(savedFile.getId())).withRel("delete"));

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    /**
     * Download a file owned by the authenticated user.
     */
    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) {
        User user = SecurityUtils.getAuthenticatedUser();
        FileEntity file = fileService.findByIdAndUserId(fileId, user.getId());

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\""
                )
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file.getContent());
    }

    /**
     * Delete a file owned by the authenticated user.
     */
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) {
        User user = SecurityUtils.getAuthenticatedUser();
        fileService.findByIdAndUserId(fileId, user.getId());
        fileService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }
}

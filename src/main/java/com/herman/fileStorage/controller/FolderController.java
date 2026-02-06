package com.herman.fileStorage.controller;

import com.herman.fileStorage.dto.FolderResponseDto;
import com.herman.fileStorage.entity.Folder;
import com.herman.fileStorage.entity.User;
import com.herman.fileStorage.security.SecurityUtils;
import com.herman.fileStorage.service.FolderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/folders")
public class FolderController {

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping
    public FolderResponseDto createFolder(@RequestParam String name) {
        User user = SecurityUtils.getAuthenticatedUser();
        Folder folder = folderService.createFolder(name, user);
        return new FolderResponseDto(folder.getId(), folder.getName());
    }

    @GetMapping
    public List<FolderResponseDto> getFolders() {
        User user = SecurityUtils.getAuthenticatedUser();
        return folderService.findAllByOwner(user)
                .stream()
                .map(folder -> new FolderResponseDto(
                        folder.getId(), folder.getName()
                ))
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFolder(@PathVariable long id) {
        User user = SecurityUtils.getAuthenticatedUser();

        Folder folder = folderService.getFolderByIdAndUser(id, user.getId());

        folderService.deleteFolder(folder.getId());
        return ResponseEntity.noContent().build();
    }
}

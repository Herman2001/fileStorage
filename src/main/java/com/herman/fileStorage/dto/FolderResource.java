package com.herman.fileStorage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@AllArgsConstructor
public class FolderResource extends RepresentationModel<FolderResource> {
    private final FolderResponseDto data;
}

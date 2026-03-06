package com.herman.fileStorage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@AllArgsConstructor
public class FileResource  extends RepresentationModel<FileResource> {
    private final FileResponseDto data;
}

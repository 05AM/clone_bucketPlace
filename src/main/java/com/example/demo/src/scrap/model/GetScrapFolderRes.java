package com.example.demo.src.scrap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetScrapFolderRes {
    private String folderName;
    private String content;
    private String type;
    private int totalScrapInFolder;
    private int totalProductInFolder;
    private int totalPhotoInFolder;
    private Object[] scraps;
}

package com.example.demo.src.scrap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Folder {
    private String scrapFolderName;
    private int totalScraps;
    private String recentImage;

    public Folder(String scrapFolderName) {
        this.scrapFolderName = scrapFolderName;
    }
}

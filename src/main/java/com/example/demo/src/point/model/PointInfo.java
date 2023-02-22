package com.example.demo.src.point.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointInfo {
    private String createAt;
    private String noticeTitle;
    private String content;
    private int point;
    private String expiredAt;

    public PointInfo(int point) {
        this.point = point;
    }
}

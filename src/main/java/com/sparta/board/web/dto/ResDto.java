package com.sparta.board.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ResDto<T> {

    private int code; // 1 - 성공 -1 - 실패
    private T data;
}

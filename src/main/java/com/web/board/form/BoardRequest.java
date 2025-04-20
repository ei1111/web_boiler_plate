package com.web.board.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardRequest {
    public Long boardId;

    @NotBlank(message = "제목은 필수 입니다.")
    public String title;

    @NotBlank(message = "내용은 필수 입니다.")
    public String content;
}

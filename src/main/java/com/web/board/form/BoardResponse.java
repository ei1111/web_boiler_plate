package com.web.board.form;

import com.web.board.domain.Board;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardResponse {
    public Long boardId;
    public String title;
    public String content;

    public static BoardResponse from(Board board) {
        return new BoardResponse(board.getBoardId(), board.getTitle(), board.getContent());
    }

    public static List<BoardResponse> boardList(List<Board> board) {
        return board.stream()
                .map(BoardResponse::from)
                .toList();
    }
}

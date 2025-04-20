package com.web.board.service;

import com.web.board.domain.Board;
import com.web.board.form.BoardRequest;
import com.web.board.form.BoardResponse;
import com.web.board.repository.BoardRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;


    public List<BoardResponse> findAll2(String searchText, Pageable pageable)  {
        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);
        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
        return BoardResponse.boardList(new ArrayList<>());
    }

    public Page<Board> findAll(String searchText, Pageable pageable) {
        return   boardRepository.findByTitleContainingOrContentContaining(searchText,searchText, pageable);
    }

    public Board findById(Long id) {
        return boardRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public void save(BoardRequest boardRequest) {
        boardRepository.save(Board.from(boardRequest));
    }

    @Transactional
    public void update(BoardRequest boardRequest) {
        Board board = findById(boardRequest.getBoardId());
        board.updateForm(boardRequest);
    }
}

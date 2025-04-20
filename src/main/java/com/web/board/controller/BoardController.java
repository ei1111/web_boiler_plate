package com.web.board.controller;

import com.web.board.domain.Board;
import com.web.board.form.BoardRequest;
import com.web.board.form.BoardResponse;
import com.web.board.service.BoardService;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    public String list(Model model,@PageableDefault(size = 2) Pageable pageable
            , @RequestParam(required = false, defaultValue = "") String searchText) {
        Page<Board> boards = boardService.findAll(searchText, pageable);
        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);
        return "board/list";
    }

    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Long boardId) {
        model.addAttribute("board",  findBoardResponseOrDefault(boardId));
        return "board/form";
    }

    private BoardResponse findBoardResponseOrDefault(Long boardId) {
      return Optional.ofNullable(boardId)
                .map(s -> boardService.findById(s))
                .map(BoardResponse::from)
                .orElseGet(BoardResponse::new);
    }

    @PostMapping("/form")
    public String save(@Valid @ModelAttribute("board") BoardRequest board , BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "board/form";
        }

        boardService.save(board);
        return "redirect:/board/list";
    }

    @PutMapping("/form")
    public String update(BoardRequest board) {
        boardService.update(board);
        return "redirect:/board/list";
    }
}

package com.web.board.controller;

import com.web.board.domain.Board;
import com.web.board.form.BoardRequest;
import com.web.board.form.BoardResponse;
import com.web.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
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
@Tag(name = "게시판 CRUD API")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    @Operation(summary = "게시판 작성 리스트 조회 API")
    public String list(Model model,@PageableDefault Pageable pageable, @RequestParam(required = false) String searchText) {
        Map<String, Object> result = boardService.findAll(searchText, pageable);

        for (String key : result.keySet()) {
            model.addAttribute(key, result.get(key));
        }

        return "board/list";
    }

    @GetMapping("/form")
    @Operation(summary = "게시판 글 조회 API")
    public String form(Model model, @RequestParam(required = false) @Parameter(example = "1") Long boardId) {
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
    @Operation(summary = "게시판 글 등록 API")
    public String save(@Valid @ModelAttribute("board") BoardRequest board , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "board/form";
        }

        boardService.save(board);
        return "redirect:/board/list";
    }

    @PutMapping("/form")
    @Operation(summary = "게시판 글 수정 API")
    public String update(BoardRequest board) {
        boardService.update(board);
        return "redirect:/board/list";
    }
}

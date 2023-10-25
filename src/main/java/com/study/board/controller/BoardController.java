package com.study.board.controller;

import com.study.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.study.board.dto.BoardDTO;

import java.io.IOException;
import java.util.List;


@Controller
@RequiredArgsConstructor //왜 붙이는거지?
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService; // 생성자 주입 방식
    @GetMapping("/save")
    public String saveForm(){
        return "save"; //save라는 이름의 뷰를 찾아서 렌더링
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        boardService.save(boardDTO);
        return "index";
    }

    @GetMapping("/")
    public String findAll(Model model) {
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다.
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault(page=1) Pageable pageable) {
        //기능 1: 해당 게시물 조회수 올리기
        boardService.updateHits(id);

        //기능 2 : 해당 게시물 상세보기
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);

        //목록을 눌렀을 때 페이지 번호를 유지하기 위해
        model.addAttribute("page",pageable.getPageNumber());
        return "detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDTO);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) {
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", board);
        return "detail";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/board/";
    }

    // 페이징 기능 : /board/paging?page=1
    @GetMapping("/paging")
    public String paging(@PageableDefault(page=1) Pageable pageable, Model model) {
        pageable.getPageNumber();
        Page<BoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3; // 한 블록에 보여줄 페이지 갯수

        int currentPage = pageable.getPageNumber();         // 현재 페이지 번호
        int startPage = ((currentPage - 1) / blockLimit) * blockLimit + 1;        // 시작 페이지 계산
        int endPage = startPage + blockLimit - 1; // 끝 페이지 계산
        int totalPage = boardList.getTotalPages();         // 총 페이지 수 (예: boardList.getTotalPages())
        if (endPage > totalPage) {
            endPage = totalPage;        // 끝 페이지가 총 페이지 수를 초과하는 경우 조정
        }

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "paging";
    }


//
}

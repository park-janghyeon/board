package com.study.board.controller;

import com.study.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.study.board.dto.BoardDTO;

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
    public String save(@ModelAttribute BoardDTO boardDTO){
        System.out.println("boardDTO: " + boardDTO); //dto에 담긴 데이터 확인
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
    public String findById(@PathVariable Long id, Model model) {
        //기능 1: 해당 게시물 조회수 올리기
        boardService.updateHits(id);

        //기능 2 : 해당 게시물 상세보기
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "detail";
    }
//
}

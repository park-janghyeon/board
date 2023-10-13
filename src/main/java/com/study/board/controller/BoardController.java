package com.study.board.controller;

import com.study.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.study.board.dto.BoardDTO;

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

}

package com.study.board.service;

import com.study.board.dto.BoardDTO;
import com.study.board.entity.BoardEntity;
import com.study.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// 이 service 패키지에서
// DTO를 Entity로 변환하거나 (이건 Entity 클래스에서 할 것이다)
// Entity를 DTO로 변환하는 작업을 한다 (이건 Controller에서 할 것이다)
// (컨트롤러는 dto, repository는 entity를 사용함)

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    public void save(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
        boardRepository.save(boardEntity);
    }
}
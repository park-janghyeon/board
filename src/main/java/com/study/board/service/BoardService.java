package com.study.board.service;

import com.study.board.dto.BoardDTO;
import com.study.board.entity.BoardEntity;
import com.study.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<BoardDTO> findAll() {
        //findAll 하면 repository에서 entity로 온다!
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        //entity를 dto로 변환
        List<BoardDTO> boardDTOList = new ArrayList<>();

        for(BoardEntity boardEntity : boardEntityList){
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            boardDTOList.add(boardDTO);
        }
        return boardDTOList;
    }


    @Transactional // 별도의 메소드를 작성헀으므로 Transactional을 붙여줘야한다
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        }   else {
            return null;
        }
    }



}
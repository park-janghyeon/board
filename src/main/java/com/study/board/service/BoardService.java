package com.study.board.service;

import com.study.board.dto.BoardDTO;
import com.study.board.entity.BoardEntity;
import com.study.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public BoardDTO update(BoardDTO boardDTO) {
        //똑같이 save를 쓴다 - insert와 update를 구분하는 법은 id 값의 유무
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId());
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1; //0부터 시작하므로 1빼줌
        int pageLimit = 10; //한 페이지에 보여줄 글 갯수

        //page가 entity이므로 dto로 바꿔줘야함
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        //Page 객체가 어떤 값들을 가지고 있는지 print 해보기
//        System.out.println("=====================================");
//        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); //현재 페이지 글 목록
//        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); //전체 글 갯수
//        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); //전체 페이지 수
//        System.out.println("boardEntities.getNumberOfElements() = " + boardEntities.getNumberOfElements()); //현재 페이지 글 갯수
//        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); //페이지당 글 갯수
//        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); //현재 페이지 번호
//        System.out.println("boardEntities.hasNext() = " + boardEntities.hasNext()); //다음 페이지 존재 여부
//        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); //이전 페이지 존재 여부
//        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); //현재 페이지가 첫 페이지인지 여부
//        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); //현재 페이지가 마지막 페이지인지 여부
//        System.out.println("=====================================");

        //목록 : id, writer, title, hits, createdTime
        //리스트 객체 dto로 변환하면 메서드 사용못함, page 객체를 담아갈 수 있는 방법이 없는가?
        //여기서 board는 entity 객체를 의미. map은 entity를 dto로 변환해줌
        Page<BoardDTO> boardDTOS = boardEntities.map(board ->
                new BoardDTO(
                        board.getId(),
                        board.getBoardWriter(),
                        board.getBoardTitle(),
                        board.getBoardHits(),
                        board.getCreatedTime()
                )
        );

        return boardDTOS;
    }
}
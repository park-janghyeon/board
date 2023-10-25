package com.study.board.service;

import com.study.board.dto.BoardDTO;
import com.study.board.entity.BoardEntity;
import com.study.board.entity.BoardFileEntity;
import com.study.board.repository.BoardFileRepository;
import com.study.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.coobird.thumbnailator.Thumbnails; //이미지 크기 조정

// 이 service 패키지에서
// DTO를 Entity로 변환하거나 (이건 Entity 클래스에서 할 것이다)
// Entity를 DTO로 변환하는 작업을 한다 (이건 Controller에서 할 것이다)
// (컨트롤러는 dto, repository는 entity를 사용함)

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    public void save(BoardDTO boardDTO) throws IOException {
        //파일 첨부 여부에따라 로직을 분리해야한다
        if (boardDTO.getBoardFile().isEmpty()){
            //첨부 파일 없음
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
            boardRepository.save(boardEntity);
        }
        else{
            //첨부 파일 있음.
            /*
            1) DTO에 담긴 파일을 꺼낸다
            2) 파일의 이름을 가져온다
            3) 서버 저장용으로 파일 이름을 수정한다 (중복 방지)
            4) 저장 경로를 지정한다
            5) 해당 경로에 파일을 저장한다
            6) board_table에 해당 데이터를 save 처리
            7) board_file_table에 해당 데이터를 save 처리
             */

            //부모데이터가 먼저 나와야 한다.
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getId(); //getID 하는 이유는? pk값이 아닌 엔티티값 전달
            BoardEntity board = boardRepository.findById(savedId).get();

            for(MultipartFile boardFile : boardDTO.getBoardFile()){
                String originalFileName = boardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
                String savePath = "C://SW_WORKSPACE//Java//board//src//main//resources//images/" + storedFileName;

                // 이미지 크기 조정
                Thumbnails.of(boardFile.getInputStream())
                        .size(500, 500) // 가로 최대 500, 세로 최대 500
                        .toFile(new File(savePath));

                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFileName, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }

        }

    }

    @Transactional
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

    @Transactional //toboarddto 호출,  boarddto가 fileentity 접근하고 있기때문에!
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
package com.study.board.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.study.board.entity.BoardEntity;
import com.study.board.entity.BoardFileEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


//DTO란 Data Transfer Object의 줄임말!
@Getter
@Setter
@ToString
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

    private List<MultipartFile> boardFile; //인터페이스, save.html -> Controller 파일 담는 용도
    private List<String> originalFileName; //원본 파일 이름
    private List<String> storedFileName; // 서버 저장용 파일 이름
    private int fileAttached; //파일 첨부 여부 (1은 첨부, 0은 첨부 안함) -> bool 타입은 왜 안쓸까? entity에서 손이 많이 가서..!

    public BoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }

    public static BoardDTO toBoardDTO(BoardEntity boardEntity){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        boardDTO.setBoardUpdatedTime(boardEntity.getUpdatedTime());

        //만약 첨부 파일이 없다면
        if (boardEntity.getFileAttached() == 0){
            boardDTO.setFileAttached(boardEntity.getFileAttached());
        }
        //만약 첨부 파일이 있다면
        else{
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
            boardDTO.setFileAttached(boardEntity.getFileAttached());

            for (BoardFileEntity boardFileEntity : boardEntity.getBoardFileEntityList()){
                originalFileNameList.add(boardFileEntity.getOriginalFileName()); // 원본 파일 이름
                storedFileNameList.add(boardFileEntity.getStoredFileName()); // 서버 저장용 파일 이름
            }
            boardDTO.setOriginalFileName(originalFileNameList);
            boardDTO.setStoredFileName(storedFileNameList);
        }
        return boardDTO;
    }


}

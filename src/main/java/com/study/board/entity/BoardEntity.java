package com.study.board.entity;
//entity는 DB의 테이블 역할을 하는 클래스이다

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import com.study.board.dto.BoardDTO;


@Entity
@Getter
@Setter
@Table(name = "board_table") //무슨 역할이지?
public class BoardEntity extends BaseEntity{
    @Id //primary key (PK) 필수다!
    @GeneratedValue(strategy = GenerationType.IDENTITY) //MYSQL 기준 auto_increment
    private Long id;

    @Column(length = 20, nullable = false) //크기 20, not null
    private String boardWriter;

    @Column // 크기 255, null 가능
    private String boardPass;

    @Column
    private String boardTitle;

    @Column(length = 500)
    private String boardContents;

    @Column
    private int boardHits;

    //DTO를 Entity로 변환하는 메소드
    public static BoardEntity toSaveEntity(BoardDTO boardDTO){
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        return boardEntity;
    }
}

package com.study.board.util;

import com.study.board.entity.BoardEntity;
import com.study.board.repository.BoardRepository;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Locale;

//@Component //만약 faker가 필요하지 않다면 component를 주석처리할 것 !
//이렇게 하면 해당 클래스가 스프링 빈으로 등록되지 않아서 CommandLineRunner로 실행되지 않습니다.
public class BoardDataFakerUtil implements CommandLineRunner {

    @Autowired
    private BoardRepository boardRepository;

    @Override
    public void run(String... args) throws Exception {
        int count = 5;
        insertFakeData(count); // 원하는 개수를 지정하여 호출할 수 있습니다.
    }

    public void insertFakeData(int count) {
        Faker faker = new Faker(new Locale("ko"));
        for (int i = 0; i < count; i++) {
            BoardEntity board = new BoardEntity();
            board.setBoardWriter(faker.name().fullName());
            board.setBoardPass(faker.internet().password());
            //최대 length 5로 설정
            board.setBoardTitle(faker.lorem().sentence().substring(0,5));
            board.setBoardContents(faker.lorem().paragraph());
            board.setBoardHits(0);

            boardRepository.save(board);
        }
    }
}

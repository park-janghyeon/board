### 개발환경 
1. IDE: IntelliJ IDEA Community
2. Spring Boot 2.6.13
3. JDK 11
4. mysql
5. Spring Data JPA
6. Thymeleaf

### 게시판 주요기능
1. 글쓰기(/board/save)
2. 글목록(/board/)
3. 글조회(/board/{id})
4. 글수정(/board/update/{id})
5. 글삭제(/board/delete/{id})
    - /board/paging?page=2
6. 페이징처리
7. 파일(이미지) 첨부하기
   - 단일 파일 첨부
   - 다중 파일 첨부
   - 파일 첨부와 관련해서 추가될 부분
      - save.html
      - BoardDTO
      - BoardService.save()
      - BoardEntity
      - BoardFileEntity, BoardFileRepository 추가
      - detail.html
   
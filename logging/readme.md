# Logging
- ExecptionLoggingInterceptor를 이용해, 예외 발생시 error로그를 남긴다.
- MenuDeniedLoggingInterceptor, 스프링시큐리티를 이용해, 메뉴 접근 권한이 없을 경우 error로그를 남긴다.
- MenuLoggingInterceptor를 이용해, 메뉴 접근시 access로그를 남긴다.
  - 메뉴 접근 권한이 없을 경우엔 리다이렉트 경로
  - 브라우저에 url를 입력할 경우, 브라우저의 프리로딩 기능에 의해 엔터 치기전 로그가 남는다.  
- logback으로 구현. logs 디렉토리에 access, error 로그파일이 생성된다.
- jpa를 이용해 application.properties에서 설정한 db에 access, error 로그가 저장된다.

# DB management
- 설정변경의 용이성을 위해, 외부 application.properties를 사용한다
- 웹서버의 구동 여부와 상관없이 batch 파일을 실행시키는 windows job scheduler를 이용해 주기적으로 db를 백업한다.
  - batch 스크립트의 변수들은 application.properties에서 설정한다.
  - jar 빌드할떄 application.properties 빼놓고 빌드.
  - java -jar 에서 --spring.config.location=file:c:/backup_test/application.properties 인수 추가함.batch 파일에서 application.properties 의 value들을 변수값으로 가져옴.
- 커스텀 백업은 batch 파일을 실행시키는 BackupController(\database\backup) 비동기식으로 구현 
  - 커스텀 백업의 로그는 db에 저장된다.
    - POST 메소드로 설정시 로그에서 requester가 익명으로 찍히는 문제.
- 복원은 sqlcommand를 구동시키는 RestoreController(\database\restore) 비동기식으로 구현
  - 복원의 로그는 db에 저장된다.
    - POST 메소드로 설정시 로그에서 requester가 익명으로 찍히는 문제.
- DumpedDBlistController(\database\list)는 application.properties에서 설정한 default 폴더에 있는 .sql 파일들의 리스트를 보여준다.
  - startDate, endDate를 parameter로 받아, 해당 기간의 .sql 파일들의 리스트를 보여준다.
  - parameter가 없을 경우, 오늘날짜~일주일전 기간의 .sql 파일들의 리스트를 보여준다. - 메뉴 접근시 기본값
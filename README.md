# GDGoC 4기 GrowThon 5팀,<br> SixPet팀의 백엔드 레퍼지토리입니다.<br>
<br>

## 브랜치 전략<br>
-main<br>
-develop<br>
&nbsp;&nbsp;-feat/기능명<br><br>

main : 배포 가능한 안정화 버전<br>
develop : 다음 배포를 위한 통합 브랜치<br>
feat/* : 기능 단위 작업용 브랜치<br><br>

## 구현 시작 전<br>
1. 이슈 생성<br>
2. 작업 브랜치 생성<br><br>

Git에서 원격 저장소(origin)와 브랜치를 다루는 기본 명령어<br><br>

git fetch origin : 원격 저장소(origin)에 있는 최신 변경 사항을 내 컴퓨터로 가져오기<br>
git checkout [브랜치명] : 해당 브랜치로 이동<br><br>

!! 기능 구현 전에는 항상 자신의 브랜치가 어디인지 확인하고 해주세요. !!<br><br>

## 이슈 생성 방법<br><br>

1. 이슈 제목 설정<br><br>

2. 이슈 템플릿 (Custom issue template)<br>
```
어떤 기능인가요?

추가하려는 기능에 대해 간결하게 설명해주세요

작업 상세 내용

-[ ] TODO
-[ ] TODO
-[ ] TODO
```
3. Assignee(작업자), Label(작업 타입) 추가<br><br>

## 코드 컨벤션<br>
도메인 기반의 패키지 구조<br>
-global<br>
&nbsp;&nbsp;-config<br>
&nbsp;&nbsp;-exception<br>
&nbsp;&nbsp;-JWT<br>
&nbsp;&nbsp;...<br>
-domain<br>
&nbsp;&nbsp;-controller<br>
&nbsp;&nbsp;-dto<br>
&nbsp;&nbsp;&nbsp;&nbsp;-request (요청 dto명: ~ReqDto)<br>
&nbsp;&nbsp;&nbsp;&nbsp;-response (응답 dto명: ~ResDto)<br>
&nbsp;&nbsp;-service<br>
&nbsp;&nbsp;-domain<br>
&nbsp;&nbsp;-repository<br><br>

## 네이밍 컨벤션<br>
| 항목                  | 스타일                  | 예시                                      |
| ------------------- | -------------------- | --------------------------------------- |
| 패키지명, 변수명, 메서드명 | lowerCamelCase       | addUser()<br>userName<br>findByEmail()  |
| 클래스명, 인터페이스명      | UpperCamelCase       | UserService<br>UserController           |
| 상수명 (static final)  | SCREAMING_SNAKE_CASE | DEFAULT_PAGE_SIZE<br>MAX_LOGIN_ATTEMPTS |

## API 엔드포인트 네이밍<br>
기능: /users, /posts<br>
파라미터: {user-id}<br><br>

## 이슈 및 커밋 컨벤션<br>
기능 추가 : feat<br>
버그 수정 : fix<br>
리팩토링 : refactor<br>
문서 작업 : docs<br>
패키지 매니저 파일 수정 : chore<br>
인프라(CI/CD): devops<br><br>

## 커밋 이름<br>
커밋타입[#관련 이슈번호]: 제목<br><br>
ex) feat[#23]: 로그인 기능 구현<br><br>

## PR 규칙<br>
#️⃣연관된 이슈<br>
ex) #이슈번호, #이슈번호<br><br>

📝작업 내용<br>
이번 PR에서 작업한 내용을 간략히 설명해주세요<br><br>

💬리뷰 요구사항(선택)<br>
리뷰어가 특별히 봐주었으면 하는 부분이 있다면 작성해주세요<br><br>

ex) 메서드 XXX의 이름을 더 잘 짓고 싶은데 혹시 좋은 명칭이 있을까요?

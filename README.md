# 🚀프로젝트 명 : Trip In Jeju

<img src="https://github.com/user-attachments/assets/1c871a74-31f9-498a-9bdf-0fda3adf97d4" style="border-radius: 10%;">

* 배포 Server URL : https://kkyul.site

## 🖥️ 프로젝트 설명

* 제주도를 여행하는 여행객을 위한 추천 장소 제공
* 다양한 여행객들의 리뷰를 통해 여행동선 최소화
* 여행 일정을 기록하고, 캘린더를 통한 여행 날짜 확인
* 해당 장소에 직접 방문한 사람들의 생생한 리뷰 확인

## 🧑‍🤝‍🧑 맴버구성
<table><thead>
  <tr>
    <th>유필선</th>
    <th>어근혁</th>
    <th>송현지</th>
  </tr></thead>
<tbody>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/fe07e6b3-d3d8-4428-8fbc-b77e7849ac12" alt="Image" width="200" height="200"></td>
    <td><img src="https://github.com/user-attachments/assets/c35b7954-7d27-4277-a572-07b875651c0b" alt="Image" width="200" height="200"></td>
    <td><img src="https://github.com/user-attachments/assets/9fd227fb-dbcf-49c9-bf32-86e72c9bfa91" alt="Image" width="200" height="200"></td>
  </tr>
  <tr>
    <td><a href="https://github.com/pilseon" target="_blank" rel="noopener noreferrer">@pilseon</a></td>
    <td><a href="https://github.com/EoGeunHyeok" target="_blank" rel="noopener noreferrer">@EoGeunHyeok</a></td>
    <td><a href="https://github.com/hyunji1205" target="_blank" rel="noopener noreferrer">@hyunji1205</a></td>
  </tr>
</tbody>
</table>

## 🕰️개발 기간

* 전체 개발 기간 : 2024-07-12 ~ 2024-09-03
* UI 구현 : 2024-07-12 ~ 2024-09-03
* 기능 구현 : 2024-07-12 ~ 2024-09-03

## ⚙ 개발 환경
* 운영체제 : Windows 10 -통합개발환경(IDE) : IntelliJ
* JDK 버전 : Project SDK 17
* 데이터 베이스 : MariaDB
* 빌드 툴 : Gradle
* 관리 툴 : GitHub
* 협업 툴 : Discord, Kakao Talk
* 디자인 : Figma
* 서비스 배포 환경 : Jenkins & N cloud

## 🔌 Dependencies
### Spring Boot Starters
* Spring Web
* Spring Websocket
* Spring Data JPA
* Spring Security
* Thymeleaf
* Mail
* Validation
* Oauth2-client
### Development Only
* Spring Boot DevTools
### Runtime Only
* MariaDB Driver
### Thymeleaf Extras
* Extras-Springsecurity6
* Lyout-Dialect
### CommonMark for Markdown
* Commonmark:0.22.0
### WebJars
* Sockjs Client
* Stomp Websocket
* Bower Bootstrap
* Bower Vue
* Bower Axios
### Google Gson
* Code Gson Gson
### JSON Web Token (JWT)
* Jsonwebtoken JWT
### Hibernate Validator
* Hibernate Validator
### Lombok
* Lombok


## 💻 기술 스택
### 백엔드
* Spring Boot
*  Spring Security
*  Spring Data JPA

### 프론트엔드
* HTML
* CSS
* JavaScript
* Bootstrap
* Thymeleaf
* JQuery
* Tailwind
* Font Awesome

### 데이터베이스 
* MariaDB
* MySQL Workbench

## 🔗 ER-DIAGRAM
<img src="https://github.com/user-attachments/assets/e358d3a0-69de-4ae2-aca2-310d46e0d796">

## 📝 FLOW-CHART
<img src="https://github.com/user-attachments/assets/a36a18c1-7ea2-40ac-9bb2-cd96c2d8d4dd">

## 🔗 ER - Diagram
```
├─.gradle
│  ├─8.8
│  │  ├─checksums
│  │  ├─dependencies-accessors
│  │  ├─executionHistory
│  │  ├─expanded
│  │  ├─fileChanges
│  │  ├─fileHashes
│  │  └─vcsMetadata
│  ├─buildOutputCleanup
│  └─vcs-1
├─.idea
├─gradle
│  └─wrapper
├─out
│  ├─production
│  │  ├─classes
│  │  │  └─com
│  │  │      └─example
│  │  │          └─Trip_In_Jeju
│  │  │              ├─Aitravel
│  │  │              │  ├─controller
│  │  │              │  ├─dto
│  │  │              │  ├─entity
│  │  │              │  ├─repository
│  │  │              │  └─service
│  │  │              ├─calendar
│  │  │              │  ├─controller
│  │  │              │  ├─entity
│  │  │              │  ├─repository
│  │  │              │  └─service
│  │  │              ├─config
│  │  │              ├─email
│  │  │              │  ├─entity
│  │  │              │  ├─repository
│  │  │              │  └─service
│  │  │              ├─event
│  │  │              │  ├─controller
│  │  │              │  ├─entity
│  │  │              │  ├─repository
│  │  │              │  └─service
│  │  │              ├─game
│  │  │              │  ├─controller
│  │  │              │  ├─entity
│  │  │              │  ├─repository
│  │  │              │  └─service
│  │  │              ├─global
│  │  │              │  └─security
│  │  │              ├─home
│  │  │              ├─inquiry
│  │  │              │  ├─controller
│  │  │              │  ├─entity
│  │  │              │  ├─repository
│  │  │              │  └─service
│  │  │              ├─jpa
│  │  │              ├─kategorie
│  │  │              │  ├─activity
│  │  │              │  │  ├─controller
│  │  │              │  │  ├─dto
│  │  │              │  │  ├─entity
│  │  │              │  │  ├─initData
│  │  │              │  │  ├─repository
│  │  │              │  │  └─service
│  │  │              │  ├─attractions
│  │  │              │  │  ├─controller
│  │  │              │  │  ├─dto
│  │  │              │  │  ├─entity
│  │  │              │  │  ├─initData
│  │  │              │  │  ├─repository
│  │  │              │  │  └─service
│  │  │              │  ├─dessert
│  │  │              │  │  ├─controller
│  │  │              │  │  ├─dto
│  │  │              │  │  ├─entity
│  │  │              │  │  ├─initData
│  │  │              │  │  ├─repository
│  │  │              │  │  └─service
│  │  │              │  ├─festivals
│  │  │              │  │  ├─controller
│  │  │              │  │  ├─dto
│  │  │              │  │  ├─entity
│  │  │              │  │  ├─initData
│  │  │              │  │  ├─repository
│  │  │              │  │  └─service
│  │  │              │  ├─food
│  │  │              │  │  ├─controller
│  │  │              │  │  ├─dto
│  │  │              │  │  ├─entity
│  │  │              │  │  ├─initData
│  │  │              │  │  ├─repository
│  │  │              │  │  └─service
│  │  │              │  ├─other
│  │  │              │  │  ├─controller
│  │  │              │  │  ├─dto
│  │  │              │  │  ├─entity
│  │  │              │  │  ├─initData
│  │  │              │  │  ├─repository
│  │  │              │  │  └─service
│  │  │              │  └─shopping
│  │  │              │      ├─controller
│  │  │              │      ├─dto
│  │  │              │      ├─entity
│  │  │              │      ├─initData
│  │  │              │      ├─repository
│  │  │              │      └─service
│  │  │              ├─like
│  │  │              │  ├─entity
│  │  │              │  └─repository
│  │  │              ├─location
│  │  │              │  ├─controller
│  │  │              │  ├─dto
│  │  │              │  ├─entity
│  │  │              │  ├─repository
│  │  │              │  └─service
│  │  │              ├─member
│  │  │              │  ├─controller
│  │  │              │  ├─dto
│  │  │              │  ├─entity
│  │  │              │  ├─initData
│  │  │              │  ├─repository
│  │  │              │  └─servcie
│  │  │              ├─rating
│  │  │              │  ├─controller
│  │  │              │  ├─entity
│  │  │              │  ├─repository
│  │  │              │  └─service
│  │  │              ├─response
│  │  │              │  ├─entity
│  │  │              │  └─repository
│  │  │              ├─scrap
│  │  │              ├─search
│  │  │              │  ├─controller
│  │  │              │  ├─dto
│  │  │              │  ├─entity
│  │  │              │  ├─ripository
│  │  │              │  └─service
│  │  │              ├─security
│  │  │              └─soical
│  │  │                  ├─detail
│  │  │                  ├─kakao
│  │  │                  └─naver
│  │  └─resources
│  │      ├─static
│  │      │  ├─admCreatejs
│  │      │  ├─calendarjs
│  │      │  ├─clickjs
│  │      │  ├─detailjs
│  │      │  ├─eventCreatejs
│  │      │  ├─gamejs
│  │      │  ├─imagefile
│  │      │  │  └─post
│  │      │  │      ├─activit
│  │      │  │      ├─attractions
│  │      │  │      ├─dessert
│  │      │  │      ├─festivals
│  │      │  │      ├─food
│  │      │  │      ├─orher
│  │      │  │      └─shopping
│  │      │  ├─images
│  │      │  │  ├─activity
│  │      │  │  ├─attractions
│  │      │  │  ├─dessert
│  │      │  │  ├─festivals
│  │      │  │  ├─food
│  │      │  │  ├─other
│  │      │  │  └─shopping
│  │      │  ├─mainjs
│  │      │  ├─mapjs
│  │      │  ├─memberPagejs
│  │      │  ├─myPagejs
│  │      │  ├─openAijs
│  │      │  ├─resource
│  │      │  │  └─css
│  │      │  ├─reviewjs
│  │      │  ├─searchjs
│  │      │  └─weekcalendarjs
│  │      └─templates
│  │          ├─activity
│  │          ├─adm
│  │          │  ├─content
│  │          │  ├─event
│  │          │  └─member
│  │          ├─AI
│  │          ├─attractions
│  │          ├─calendar
│  │          ├─dessert
│  │          ├─event
│  │          ├─festivals
│  │          ├─food
│  │          ├─game
│  │          ├─home
│  │          ├─layout
│  │          ├─map
│  │          ├─member
│  │          ├─other
│  │          ├─rating
│  │          ├─search
│  │          └─shopping
│  └─test
│      └─classes
│          ├─com
│          │  └─example
│          │      └─Trip_In_Jeju
│          │          └─kategorie
│          │              ├─dessert
│          │              ├─festivals
│          │              └─food
│          └─generated_tests
└─src
    ├─main
    │  ├─generated
    │  ├─java
    │  │  └─com
    │  │      └─example
    │  │          └─Trip_In_Jeju
    │  │              ├─Aitravel
    │  │              │  ├─controller
    │  │              │  ├─dto
    │  │              │  ├─entity
    │  │              │  ├─repository
    │  │              │  └─service
    │  │              ├─calendar
    │  │              │  ├─controller
    │  │              │  ├─entity
    │  │              │  ├─repository
    │  │              │  └─service
    │  │              ├─config
    │  │              ├─email
    │  │              │  ├─entity
    │  │              │  ├─repository
    │  │              │  └─service
    │  │              ├─event
    │  │              │  ├─controller
    │  │              │  ├─entity
    │  │              │  ├─repository
    │  │              │  └─service
    │  │              ├─game
    │  │              │  ├─controller
    │  │              │  ├─entity
    │  │              │  ├─repository
    │  │              │  └─service
    │  │              ├─global
    │  │              │  └─security
    │  │              ├─home
    │  │              │  └─service
    │  │              ├─inquiry
    │  │              │  ├─controller
    │  │              │  ├─entity
    │  │              │  ├─repository
    │  │              │  └─service
    │  │              ├─jpa
    │  │              ├─kategorie
    │  │              │  ├─activity
    │  │              │  │  ├─controller
    │  │              │  │  ├─dto
    │  │              │  │  ├─entity
    │  │              │  │  ├─initData
    │  │              │  │  ├─repository
    │  │              │  │  └─service
    │  │              │  ├─attractions
    │  │              │  │  ├─controller
    │  │              │  │  ├─dto
    │  │              │  │  ├─entity
    │  │              │  │  ├─initData
    │  │              │  │  ├─repository
    │  │              │  │  └─service
    │  │              │  ├─dessert
    │  │              │  │  ├─controller
    │  │              │  │  ├─dto
    │  │              │  │  ├─entity
    │  │              │  │  ├─initData
    │  │              │  │  ├─repository
    │  │              │  │  └─service
    │  │              │  ├─festivals
    │  │              │  │  ├─controller
    │  │              │  │  ├─dto
    │  │              │  │  ├─entity
    │  │              │  │  ├─initData
    │  │              │  │  ├─repository
    │  │              │  │  └─service
    │  │              │  ├─food
    │  │              │  │  ├─controller
    │  │              │  │  ├─dto
    │  │              │  │  ├─entity
    │  │              │  │  ├─initData
    │  │              │  │  ├─repository
    │  │              │  │  └─service
    │  │              │  ├─other
    │  │              │  │  ├─controller
    │  │              │  │  ├─dto
    │  │              │  │  ├─entity
    │  │              │  │  ├─initData
    │  │              │  │  ├─repository
    │  │              │  │  └─service
    │  │              │  └─shopping
    │  │              │      ├─controller
    │  │              │      ├─dto
    │  │              │      ├─entity
    │  │              │      ├─initData
    │  │              │      ├─repository
    │  │              │      └─service
    │  │              ├─like
    │  │              │  ├─entity
    │  │              │  └─repository
    │  │              ├─location
    │  │              │  ├─controller
    │  │              │  ├─dto
    │  │              │  ├─entity
    │  │              │  ├─repository
    │  │              │  └─service
    │  │              ├─member
    │  │              │  ├─controller
    │  │              │  ├─dto
    │  │              │  ├─entity
    │  │              │  ├─initData
    │  │              │  ├─repository
    │  │              │  └─servcie
    │  │              ├─rating
    │  │              │  ├─controller
    │  │              │  ├─entity
    │  │              │  ├─repository
    │  │              │  └─service
    │  │              ├─response
    │  │              │  ├─entity
    │  │              │  └─repository
    │  │              ├─scrap
    │  │              ├─search
    │  │              │  ├─controller
    │  │              │  ├─dto
    │  │              │  ├─entity
    │  │              │  ├─ripository
    │  │              │  └─service
    │  │              ├─security
    │  │              └─soical
    │  │                  ├─detail
    │  │                  ├─kakao
    │  │                  └─naver
    │  └─resources
    │      ├─static
    │      │  ├─admCreatejs
    │      │  ├─calendarjs
    │      │  ├─clickjs
    │      │  ├─detailjs
    │      │  ├─eventCreatejs
    │      │  ├─gamejs
    │      │  ├─imagefile
    │      │  │  └─post
    │      │  │      ├─activit
    │      │  │      ├─attractions
    │      │  │      ├─dessert
    │      │  │      ├─festivals
    │      │  │      ├─food
    │      │  │      ├─member
    │      │  │      ├─orher
    │      │  │      └─shopping
    │      │  ├─images
    │      │  │  ├─activity
    │      │  │  ├─attractions
    │      │  │  ├─dessert
    │      │  │  ├─festivals
    │      │  │  ├─food
    │      │  │  ├─other
    │      │  │  └─shopping
    │      │  ├─mainjs
    │      │  ├─mapjs
    │      │  ├─memberPagejs
    │      │  ├─myPagejs
    │      │  ├─openAijs
    │      │  ├─resource
    │      │  │  └─css
    │      │  ├─reviewjs
    │      │  ├─searchjs
    │      │  └─weekcalendarjs
    │      └─templates
    │          ├─activity
    │          ├─adm
    │          │  ├─content
    │          │  ├─event
    │          │  └─member
    │          ├─AI
    │          ├─attractions
    │          ├─calendar
    │          ├─dessert
    │          ├─event
    │          ├─festivals
    │          ├─food
    │          ├─game
    │          ├─home
    │          ├─layout
    │          ├─map
    │          ├─member
    │          ├─other
    │          ├─rating
    │          ├─search
    │          └─shopping
    └─test
        └─java
            └─com
                └─example
                    └─Trip_In_Jeju
                        └─kategorie
                            ├─dessert
                            ├─festivals
                            └─food
```

## 📌 페이지 기능 소개

### 메인화면
><img src="https://github.com/user-attachments/assets/5694c815-83db-446f-847f-7a98e23986bf">
>
>1. 홈페이지 접속 시 초기화면으로 화면의 기본 구조는 상단 메인로고 및 검색 , 중간 본문, 하단 메뉴바로 구분 되어 있습니다.
>    - 상단에는 메인 로고 및 메인 사진으로 구성되어있습니다.
>    - 중간 본문에는 회원 테마에 맞는 추천 게시글, 테마별 각 1개 추천 게시글, 이벤트로 구성되어있고 클릭을 통해 각 게시글로 이동이 가능합니다.
>    - 하단 메뉴바는 홈, 맴버페이지, 맵, 마이페이지로 구성되어있고, 가운데 화살표를 눌러 메뉴바를 열어 원하는 다른 페이지로 이동이 가능합니다.
>
><img src="https://github.com/user-attachments/assets/ab1f9a29-ff06-48fe-9451-e382e407b981">
>
>2. 로그인과 비로그인 시 중간 본문 및 메뉴바에 나타나는 메뉴가 상이합니다.
>   - 로그인이 되어있지 않은경우
>       - 중간 본문 : 회원 테마 추천글 x
>       - 하단 메뉴바 : 로그인
>   - 로그인이 되어있는 경우
>       - 중간 본문 : 회원 테마 추천글 o
>.        - 하단 메뉴바 : 미니게임, 로그아웃

### 회원가입
><img src="https://github.com/user-attachments/assets/4ae2ba61-9f47-4afd-9901-9a20cef650e8">
>
>1. 회원가입의 모든 항목에 대한 유효성 검사를 적용하여 입력하지 않으면 회원가입이 진행되지 않습니다.
>
>2. ID, 닉네임, 이메을은 중복확인을 필수로, 비밀번호는 비밀번호 확인절차를 거칩니다.
>
>3. 회원가입이 완료되면 로그인 화면으로 이동과 동시에 회원가입 시 입력한 이메일 주소로 환영 메일이 전송됩니다.

### 로그인
><img src="https://github.com/user-attachments/assets/45d020ab-0039-44ae-972a-a1f608f106a8">
>
>1. 회원가입을 통해 생성된 ID와 PW로 로그인을 수행합니다.
>
>
>2. 카카오, 네이버, 구글을 통한 소셜 로그인은 버튼을 눌러 각 플랫폼에 로그인하면 자동으로 계정 생성과 동시에 로그인을 수행합니다.
>
> 
>3. 로그인에 성공하면 메인화면으로 이동합니다.

### 아이디 찾기
><img src="https://github.com/user-attachments/assets/bec2995b-5717-4084-ab52-a46a764b15d8">
> 
>- 아이디 찾기 버튼을 통해 회원가입 시 입력한 이메일 주소를 입력하고 찾기 버튼을 누르면 해당 회원의 아이디를 보여줍니다.

### 비밀번호 찾기
><img src="https://github.com/user-attachments/assets/b3925412-0703-4008-b5e9-b1ae9ab1d416">
>
>- 비밀번호 찾기 버튼을 통해 회원가입 시 입력한 아이디와 이메일 주소를 입력해면 해당 이메일 주소로 인증 코드가 전송 됩니다.
>
><img src="https://github.com/user-attachments/assets/a3de3031-a9c8-4cc2-b05f-07d45b9ca9b4">
>
>- 전송받은 인증코드를 입력하고 함께 바꿀 비밀번호를 입력하면 바뀐 비밀번호를 사용할 수 있습니다.

### 마이페이지
><img src="https://github.com/user-attachments/assets/e8ca9dd5-23db-4cd1-991b-95adb01b7d29">
>
>1.로그인이 되어있는 사용자만 마이페이지에 접속 할 수 있습니다.
> 
>2.마이페이지 내 메뉴는 상단에 마이프로필, 우측에 프로필 수정, 아래 회원 탈퇴 로 구성되어있고 중간에는 회원 본인이 스크랩한 축제나 찜 목록을 확인 할 수 있습니다.

### 로그아웃
>- 하단 메뉴바에서 가운데 화살표 아이콘을 클릭 후 로그아웃 버튼을 클릭하면 로그아웃과 동시에 메인페이지로 이동합니다.

### 게시물-LIST
><img src="https://github.com/user-attachments/assets/c4c0730f-a543-484a-97e7-4892cfa5bfb8">
>
>
>1. 상단에 뒤로가기 버튼과 메인 페이지로 이동할 수 있는 버튼이 있습니다.
>
> 
>2. 중간 본문 위로는 메인 카테고리를 표시해 원하는 카테고리로 이동 할 수 있습니다.
>
>
>3. 아래로는 서브 카테고리를 표시해 원하는 서브테마별 게시글을 모아 볼 수 있습니다.
>
> 
>4. 중간에는 지도와 게시글을 표시해, 해당 게시글에 마우스를 올리면 지도에서 해당 게시글의 위치를 확인 할 수 있습니다.

### 게시물-DETAIL
><img src="https://github.com/user-attachments/assets/a2f385d4-8c20-4bef-89bb-a41ce31930f2">
>
>1. 상단에 뒤로가기 버튼과 메인 페이지로 이동할 수 있는 버튼이 있습니다.
>
>
>2. 중간에는 사진, 내용을 확인 할 수 있고, 스크랩버튼을 통한 스크랩, 좋아요 버튼을 통한 좋아요가 가능합니다.
>
> 
>3. 하단에서는 평균 점수 확인 , 리뷰 작성 및 확인이 가능합니다. 리뷰 작성은 해당위치 방문 확인을 통해 인증이 되어야 작성이 가능합니다.

### MAP
><img src="https://github.com/user-attachments/assets/98d0b677-2a31-408d-997a-5e8843838c59">
>
>1. 상단에 뒤로가기 버튼과 메인 페이지로 이동할 수 있는 버튼이 있습니다.
>
> 
>2. 중간 본문에는 지도가 있고 본인의 현위치 및 주변에 등록되어있는 테마별 위치가 표시됩니다.
>
>
>3. 본인의 현위치 와 테마별 위치가 30m 이내에서 20초 이상 머물러있게되면 리뷰작성이 가능합니다.

### MEMBER_PAGE
><img src="https://github.com/user-attachments/assets/6af1a70d-b562-42e3-b9b8-9bf4b0578e07">
><img src="https://github.com/user-attachments/assets/d32f298d-9098-45b5-8434-5c117b0b79eb">
>
>1. 상단에 뒤로가기 버튼과 메인 페이지로 이동할 수 있는 버튼이 있습니다.
>   - (본인과 타유저의 페이지의 뜨는 정보는 상이합니다.)
>
> 
>2. 본인의 memberPage에 접속을 하게되면 본인이 방문 인증을 성공한 게시글의 리뷰를 작성할 수 있게 표시가 됩니다. 본인이 작성한 리뷰 정보를 확인 가능합니다.
>
> 
>3. 타유저의 memberPage에 접속을 하게되면 해당 유저가 작성하였던 리뷰 목록을 확인 할 수 있습니다.

### EVENT_PAGE
><img src="https://github.com/user-attachments/assets/c917d2e4-0212-4d5a-ac5f-58bb7df5f098">
>
>1. 상단에 뒤로가기 버튼과 메인 페이지로 이동할 수 있는 버튼이 있습니다.
>   - (관리자와 유저의 페이지에 뜨는 정보는 상이합니다.)
>
> 
>2. 관리자는 상단에 이벤트 생성 버튼이 보이고 , 이벤트를 수정 삭제 가 가능합니다.
>
> 
>3. 일반 유저는 이벤트의 정보를 확인 할 수 있습니다.

### 문의하기
><img src="https://github.com/user-attachments/assets/38197882-721f-446c-bc2d-9a8790a17b5d">
>
>1. 상단에 뒤로가기 버튼과 메인 페이지로 이동할 수 있는 버튼이 있습니다.
>
> 
>2. 문의사항 목록을 확인 할 수 있고 제목 옆 답변상황을 통해 현 답변진행 상황을 확인 가능합니다.
>   - (관리자와 유저, 비로그인 상태에서의 페이지에 뜨는 정보는 상이합니다.)
>
> 
>3. 비로그인시 현재 올라와있는 문의 사항이 확인이 가능 합니다.
>
> 
>4. 일반 유저는 상단에 버튼을 통해 문의를 작성 할 수 있고 본인이 작성한 글에 대해서는 삭제가 가능합니다.
 관리자는 유저가 남긴 문의 사항에 답글을 남길 수 있고 문의사항 삭제가 가능합니다.

### 미니게임
><img src="https://github.com/user-attachments/assets/192e753d-dbf4-4b3d-8ae2-7aae61811901">
>
>1. 상단에 뒤로가기 버튼과 메인 페이지로 이동할 수 있는 버튼이 있습니다.
>
> 
>2. 게임 진행을 통해 오른쪽에서 랭킹을 확인 할 수 있습니다.

### 축제일정
><img src="https://github.com/user-attachments/assets/fd1169c2-984e-4ffc-b4d3-6546ded33b86">
>
>1. 상단에 뒤로가기 버튼과 메인 페이지로 이동할 수 있는 버튼이 있습니다.
>
> 
>2. 캘린더를 통해 현재 진행중인 축제를 확인 할 수 있습니다.
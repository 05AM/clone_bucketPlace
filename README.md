# 오늘의집 개발 진행상황

### 개발일지 링크

https://sebel.notion.site/9001f7deff124acd860c1e08d2b98114

## 2023-01-28

### 진행상황

-   기획 및 기능 리스트 업(완)
-   ERD 설계(진행중)

### 이슈

-   상품의 상세 페이지 내에 여러 상품들이 있는 경우
-   카테고리 분류 테이블 나누기
-   타임딜 구현 방법

<br>

## 2023-01-29

### 진행상황

-   ERD 설계(진행중)
-   EC2 서버 구축 및 도메인, 서브 도메인 연결, 리다이렉션 구현
-   RDS 서버 연결

### 이슈

-   할인 테이블을 상품 상세 테이블과 합치기
-   상품 상세 배너 이미지 데이터 중복 관련 고민

<br>

## 2023-01-30

### 진행상황

-   ERD 설계(완료)

    https://www.erdcloud.com/d/53XyeutBt4dXZFsMo

-   API 리스트 작성(진행중)

    https://docs.google.com/spreadsheets/d/1CSeoUAzOF3RgCG1nqe1Z94iSIg4KiZoV3-J6PJVsF-8/edit#gid=252227832

### 이슈

-   API 명명법에 대한 고민
    -   params가 많을 때 조건문 처리
    -   category로 분류하는 api의 명명을 by~로 분리해야할지 모든 조건문을 구현해야할지
-   클라이언트는 ID값을 어떻게 알고 요청을 하는지에 대한 궁금증

<br>

## 2023-01-31

### 진행상황

-   API 리스트 작성(완료)
-   Brand API 구현(진행중)

<br>

## 2023-02-01

### 진행상황

구현된 API

-   특정 브랜드 정보 가져오기
-   Exception Handler 추가
-   Exception Handler에 맞게 BaseResponse 수정
-   S3를 이용한 이미지 업로드, 삭제 구현
-   image resource 구해서 s3에 업로드

### 피드백 공유 후 변경사항

-   멀티 상품 판매 페이지 매핑
-   typeOfOAuth VARCHAR(10)로 변경
-   비밀번호 NULLABLE

### 이슈

-   datagrip datasource 연결시 전체 DB가 안 보이는 이슈가 있었는데, datasource-properties-schemas에서 보이는 DB를 변경할 수 있었다.

<br>

## 2023-02-02

### 진행상황

구현된 API

-   장바구니 생성, 수정, 삭제
-   특정 인덱스 장바구니 가져오기
-   유저의 장바구니 가져오기
-   유저 특정 장바구니 가져오기
-   장바구니 옵션 변경
-   상품 페이지 목록 가져오기
-   상품 페이지 인덱스로 가져오기
-   특정 브랜드 상품 페이지 가져오기

### 이슈

-   nullpointer 에러가 많이 나서 힘들었다 Optional 이라는 기능이 있다는 것을 알았고, try-catch로도 잡아낼 수 있었다.
    int의 경우 Integer로 선언하면 null 체크가 더 쉽다.

<br>

## 2023-02-03

### 진행상황

구현된 API

- 쿠폰 생성 삭제
- 유저 쿠폰 생성, 삭제
- 전체 쿠폰목록 가져오기
- 특정 유저 쿠폰목록 가져오기
- 쿠폰 적용 가능한 상품페이지 가져오기
- 장바구니 api 전반적인 수정

### 이슈

- 쿠폰 적용 상품 페이지 구현을 위해 맵핑 테이블을 추가했다.
- 장바구니의 특성상 입력과 삭제가 잦은데,<br>
장바구니를 가져올 때 너무 많은 테이블이 조인되어야 해서 <br>
고민 끝에 데이터 중복을 허용하여 
옵션과 상세옵션 테이블을 외래키가 아닌 문자열의 형태로 합쳐서 저장하기로 결정했다.<br>
또한 브랜드 이름 하나를 가져오기 위해 조인을 하는 것은 비효율적이라고 생각해 브랜드 이름도 컬럼으로 추가했다. 그에 맞게 테이블을 추가, 수정했다.

<br>

## 2023-02-04

### 진행상황

구현된 API

- 주문 페이지 불러오기
- 주문 페이지 배송 목록 불러오기
- 배송지 등록

### 이슈

- 주문 목록에 필요한 정보들을 불러오는 것을 몇 개의 쿼리로 나눠야할지가 가장 고민이 많이 됐다.
    지금 내 능력 안에서 최소화시켜 총 3번의 query로 불러오게 되었다.
    나중에 리팩토링을 하면 좋을 것 같다.
- 배열은 한꺼번에 초기화를 해야해서 배열의 크기가 정해져 있지 않은 매개변수 값을 넣는 것이 어려웠는데
    검색하다가 방법을 못 찾아서 그냥 리스트로 추가한 뒤에 toArray() 메소드로 배열로 만들었다. 나중에 더 멋있는 방법을 알아내야겠다.

<br>

## 2023-02-05

### 진행상황

구현된 API

- 주문 목록 생성
<<<<<<< HEAD
- 상태별로 주문 목록 가져오기

### 이슈

- 프론트에서 한 탭을 한꺼번에 불러오는 api에 대한 요청이 있었다. 하지만 유지보수와 독립성 유지를 위해 바람직한 방법이 아닌 것 같았고,
멘토님께서도 독립적인 api로 불러오는게 바람직해보인다고 하셨다.

<br>

## 2023-02-06

### 진행상황

구현된 API

- review 
  - 생성, 수정, 삭제
  - 브랜드 리뷰 가져오기
  - 유저 리뷰 가져오기
  - 상품 판매 페이지 리뷰 가져오기
- inquiry
  - 생성, 삭제
  - 상품 페이지 문의 내역 가져오기
  - 유저 문의 내역 가져오기
  - 특정 문의 가져오기
- cart, brand avgRate, cntReview 컬럼이 아닌 집계함수로 변경

### 이슈

- avgRate, cntReview 컬럼을 집계함수로 가져오는 방식으로 다시 짰는데, 리뷰가 없는 행은 가져오지 않았다.
그래서 left outer join으로 불러오는 방식을 채택했다.

<br>

## 2023-02-07

### 진행상황

구현된 API

- brand
  - 브랜드 목록 가져오기
  - 브랜드 생성
- product_page
  - 상품 판매 페이지 생성
- product
  - 상품 생성 
- review
  - 브랜드 리뷰 통계 가져오기 
  - 상품 판매 페이지 리뷰 통계 가져오기
- search
  - 검색 - 가구
  - 검색 - 소파
  - 검색어 저장
  - 검색어 top 10 불러오기

### 이슈

- queryForObject에서 데이터 값이 하나도 반환되지 않는 경우 EmptyResultDataAccessException가 발생했다. try-catch로 예외처리를 했지만 찾아보니 자바에서는 예외가 발생할 때 비용이
많이 발생해서 if를 사용해 처리하는 것이 효율적이라고 한다. 이후 다른 방법을 찾아 다시 짜봐야겠다.

br>

## 2023-02-08

### 진행상황

- api 부분적으로 수정
- api 명세서 작성(진행중)

### 피드백
- 베스트 상품의 경우 실무에서는 어떻게 처리하는지
  -> 기획에 따라 다르다

- 장바구니를 수정한다고 할때
  carts/{cartID} + PatchReq(userID, quantity)
  carts + PatchReq(userID, quantity)
    데이터 중복은 피하는 쪽으로

- spring jdbc - EmptyResultDataAccessException
    실무에서는 예외처리와 if를 비교해보고 비용에 따라서 결정

- 효율적인 코드를 짜기 위해서는?
  - 성능, 리팩토링, 클린코드
  - 쓰기는 읽기보다 비용이 더 많이 들지만 어느 기능을 사용자들이 더 많이 이용하냐에 따라서 결정


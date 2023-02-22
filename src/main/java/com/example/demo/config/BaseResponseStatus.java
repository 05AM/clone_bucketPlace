package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),

    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요"),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요"),
    POST_USERS_EXISTS_EMAIL(false,2017,"사용 중인 이메일입니다"),
    NOT_ACCEPT_OLDER_FOURTEEN(false, 2018, "'만 14세 이상입니다'에 동의해주세요"),
    NOT_ACCEPT_TOS(false, 2019, "'이용약관'에 동의해주세요"),
    NOT_ACCEPT_PERSONALINFO(false, 2020, "'개인정보수집 및 이용동의'에 동의해주세요"),
    POST_USERS_EMPTY_PASSWORD(false, 2021, "비밀번호를 입력해주세요"),
    POST_USERS_INVALID_PASSWORD(false, 2022, "비밀번호는 영문, 숫자를 포함하여 8자 이상이어야 합니다"),
    POST_USERS_EMPTY_PASSWORDCHECK(false, 2023, "확인을 위해 비밀번호를 한 번 더 입력해주세요"),
    NOT_SAME_PASSWORD(false, 2024, "비밀번호가 일치하지 않습니다"),
    POST_USERS_INVALID_NICKNAME(false, 2025, "별명을 2~15자 내로 입력해주세요"),
    GET_NOT_EXIST_TYPE(false, 2026, "type명을 다시 확인해주세요"),
    POST_EMAIL_EMPTY_EMAIL(false, 2027, "필수 입력 항목입니다."),

    // coupon
    POST_COUPON_EXISTS_COUPON(false,2030,"보유중인 쿠폰입니다."),
    DELETE_COUPON_USER_FAIL(false,2031,"유저 쿠폰 삭제 실패"),
    DELETE_COUPON_FAIL(false,2032,"쿠폰 삭제 실패"),
    MODIFY_COUPON_USER_FAIL(false,2033,"유저 쿠폰 삭제 실패"),

    // file
    DELETE_FILE_NOT_FOUND(false,2035,"해당 파일이 존재하지 않습니다."),

    // order
    POST_ORDER_EXISTS_ADDRESS_NAME(false,2040,"중복된 배송지명"),

    //scrap
    POST_SCRAP_EMPTY_FOLDERNAME(false, 2050, "스크랩북 이름을 입력해주세요"),

    // cart
    GET_ORDER_PAGE_CART_LIST_FAIL(false,2055,"비활성화된 장바구니 입니다. 장바구니 인덱스를 확인하세요."),
    MODIFY_CART_STATE_FAIL(false,2056,"장바구니 상태 변경 실패"),

    // review
    POST_REVIEW_FAIL(false,2061,"주문번호 중복 : 이미 작성된 리뷰가 존재합니다."),
    DELETE_REVIEW_FAIL(false,2063,"리뷰 삭제 실패 : 삭제됐거나 없는 리뷰입니다."),
    // product
    POST_PRODUCT_EXIST_PRODUCT_NAME(false,2066,"중복된 상품명 입니다."),


    //inquiry
    DELETE_INQUIRY_FAIL(false,2070,"문의 삭제 실패 : 삭제됐거나 없는 문의입니다."),

    // brand
    POST_BRAND_EXIST_REGIST_NUM (false,2080,"중복된 사업자 등록번호 입니다."),
    POST_BRAND_EXIST_BUSINESS_NAME (false,2081,"중복된 상호명 입니다."),

    // product_page
    POST_PRODUCT_PAGE_EXIST_TITLE (false,2090,"중복된 상품페이지명 입니다."),
    // search
    GET_SEARCH_RESULT_FAIL (false,2091,"검색 결과가 존재하지 않습니다."),
    GET_SEARCH_MIN_BIGGER (false,2092,"가격의 최솟값이 최댓값보다 큽니다."),
    GET_SEARCH_CATEGORY_DETAIL (false,2093,"상세 카테고리 값을 확인하세요."),


    //point
    POST_POINT_EMPTY_NOTICETITLE(false, 2085, "포인트 알림 제목을 입력해주세요"),
    POST_POINT_EMPTY_CONTENT(false, 2086, "포인트 알림 내용을 입력해주세요"),
    POST_POINT_EMPTY_POINT(false, 2087, "포인트 금액을 입력해주세요"),

    //photo
    POST_PHOTO_EMPTY_PHOTOLINK(false, 2090, "사진을 선택해 주세요."),
    POST_PHOTO_EMPTY_CATEGORYPHOTO(false, 2091, "잠깐! 공간을 선택해주세요"),
    POST_PHOTO_COMMENT_EMPTY_CONTENT(false, 2092, "댓글을 입력해주세요."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),
    INVALID_JWT(false, 3001, "유효하지 않은 JWT입니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"이메일이나 패스워드를 확인해주세요"),
    POST_USERS_EXISTS_NICKNAME(false, 3015, "사용 중인 별명입니다."),
    NOT_EXIST_EMAIL(false, 3016, "존재하지 않는 이메일 입니다"),
    DUPLICATED_NICKNAME(false, 3017, "은(는) 이미 존재합니다."),
    RESULT_NOT_EXISTS(false, 3020, "검색 결과가 존재하지 않습니다."),

    //mail
    INVALID_CERTIFIED_ID_CODE(false, 3018, "올바른 인증 코드가 아닙니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),


    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_PROFILE(false,4014,"프로필 수정 실패"),
    CREATE_FAIL_USER(false, 4015, "유저생성 실패"),
    MODIFY_FAIL_LEAVEUSER(false, 4016, "유저탈퇴 실패"),
    MODIFY_FAIL_NOTICE(false, 4017, "알림 설정 수정 실패"),
    DELETE_FAIL_FOLDER(false, 4018, "폴더 삭제 실패"),
    MODIFY_FAIL_FOLDER(false, 4019, "폴더 수정 실패"),
    ADD_FAIL_SCRAP(false, 4020, "폴더에 스크랩 추가 실패"),
    DELETE_FAIL_SCRAP(false, 4021, "폴더에서 스크랩 삭제 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    //like
    CREATE_FAIL_LIKE(false, 4020, "좋아요 실패"),
    CANCEL_FAIL_LIKE(false, 4021, "좋아요 취소 실패"),
    CREATE_FAIL_COMMENT_LIKE(false, 4022, "댓글 좋아요 실패"),
    CANCEL_FAIL_COMMENT_LIKE(false, 4023, "댓글 좋아요 취소 실패"),

    //follow
    CREATE_FAIL_FOLLOW(false, 4030, "팔로우 실패"),
    CANCEL_FAIL_FOLLOW(false, 4031, "팔로우 해제 실패"),

    // cart
    MODIFY_FAIL_CART(false,4050,"장바구니 수정 실패"),
    MODIFY_FAIL_CART_QUANTITY(false,4051,"장바구니 수량 수정 실패"),
    DELETE_FAIL_CART(false,4052,"장바구니 삭제 실패"),

    // review
    MODIFY_REVIEW_FAIL(false,2062,"리뷰 수정 실패 : 리뷰를 수정할 수 없습니다.");




    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

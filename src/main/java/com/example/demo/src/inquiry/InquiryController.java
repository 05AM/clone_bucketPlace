package com.example.demo.src.inquiry;

import com.example.demo.config.BaseResponse;
import com.example.demo.src.inquiry.model.DeleteInquiryReq;
import com.example.demo.src.inquiry.model.GetInquiryRes;
import com.example.demo.src.inquiry.model.PostInquiryReq;
import com.example.demo.src.inquiry.model.PostInquiryRes;
import com.example.demo.utils.JwtService;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app/inquiries")
public class InquiryController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final InquiryProvider inquiryProvider;
    @Autowired
    private final InquiryService inquiryService;
    @Autowired
    private final JwtService jwtService;


    public InquiryController(InquiryProvider inquiryProvider, InquiryService inquiryService, JwtService jwtService){
        this.inquiryProvider = inquiryProvider;
        this.inquiryService = inquiryService;
        this.jwtService = jwtService;
    }
    //문의 생성
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostInquiryRes> createInquiry(
            @RequestBody @Validated PostInquiryReq postInquiryReq
            ) {
        int userIdxByJwt = jwtService.getUserIdx();
        if(postInquiryReq.getUserID() != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }
        PostInquiryRes postInquiryRes = inquiryService.createInquiry(postInquiryReq);
        return new BaseResponse<>(postInquiryRes);
    }

    //특정 상품페이지 문의내역 가져오기
    @ResponseBody
    @GetMapping("/product-pages/{productPageID}")
    public BaseResponse<List<GetInquiryRes>> getProductPageInquiries(
            @PathVariable int productPageID
    ){
        List<GetInquiryRes> getProductPageInquiryRes = inquiryProvider.getProductPageInquiries(productPageID);

        return new BaseResponse<>(getProductPageInquiryRes);
    }

    //특정 유저 문의내역 가져오기
    @ResponseBody
    @GetMapping("/users/{userID}")
    public BaseResponse<List<GetInquiryRes>> getUserInquiries(
            @PathVariable int userID
    ){
        List<GetInquiryRes> getUserInquiryRes = inquiryProvider.getUserInquiries(userID);

        return new BaseResponse<>(getUserInquiryRes);
    }

    //특정 유저 특정 문의 가져오기
    @ResponseBody
    @GetMapping("/{inquiryID}")
    public BaseResponse<GetInquiryRes> getInquiry(
            @PathVariable int inquiryID
    ){
        GetInquiryRes getInquiryRes = inquiryProvider.getInquiry(inquiryID);

        return new BaseResponse<>(getInquiryRes);
    }

    //문의 삭제
    @ResponseBody
    @DeleteMapping("/{inquiryID}")
    public BaseResponse<String> deleteInquiry(
            @PathVariable int inquiryID,
            @RequestBody @Validated DeleteInquiryReq deleteInquiryReq
            ) {
        int userIdxByJwt = jwtService.getUserIdx();
        if(deleteInquiryReq.getUserID() != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }

        deleteInquiryReq.setInquiryID(inquiryID);

        inquiryProvider.deleteInquiry(deleteInquiryReq);

        return new BaseResponse<>("Delete Inquiry Success");
    }
}

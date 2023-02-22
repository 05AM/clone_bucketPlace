package com.example.demo.src.inquiry;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.inquiry.model.DeleteInquiryReq;
import com.example.demo.src.inquiry.model.GetInquiryRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InquiryProvider {
    private final InquiryDao inquiryDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public InquiryProvider(InquiryDao inquiryDao, JwtService jwtService) {
        this.inquiryDao = inquiryDao;
        this.jwtService = jwtService;
    }

    public List<GetInquiryRes> getProductPageInquiries(int productPageID) {
        return inquiryDao.getProductPageInquiries(productPageID);
    }

    public List<GetInquiryRes> getUserInquiries(int userID) {
        return inquiryDao.getUserInquiries(userID);
    }

    public GetInquiryRes getInquiry(int inquiryID) {
        return inquiryDao.getInquiry(inquiryID);
    }

    public void deleteInquiry(DeleteInquiryReq deleteInquiryReq) {
        int result = inquiryDao.deleteInquiry(deleteInquiryReq);

        if(result == 0) {
            throw new BaseException(BaseResponseStatus.DELETE_INQUIRY_FAIL);
        }
    }
}

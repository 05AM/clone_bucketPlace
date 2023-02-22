package com.example.demo.src.inquiry;

import com.example.demo.src.inquiry.model.PostInquiryReq;
import com.example.demo.src.inquiry.model.PostInquiryRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional()
public class InquiryService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final InquiryDao inquiryDao;
    private final InquiryProvider inquiryProvider;
    private final JwtService jwtService;


    @Autowired
    public InquiryService(InquiryDao inquiryDao, InquiryProvider inquiryProvider, JwtService jwtService) {
        this.inquiryDao = inquiryDao;
        this.inquiryProvider = inquiryProvider;
        this.jwtService = jwtService;

    }

    public PostInquiryRes createInquiry(PostInquiryReq postInquiryReq) {
        int inquryID = inquiryDao.createInquiry(postInquiryReq);

        return new PostInquiryRes(inquryID);
    }
}

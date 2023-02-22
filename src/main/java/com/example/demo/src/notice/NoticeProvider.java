package com.example.demo.src.notice;


import com.example.demo.config.BaseException;
import com.example.demo.src.notice.model.GetNoticeRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional(readOnly = true)
public class NoticeProvider {

    private final NoticeDao noticeDao;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public NoticeProvider(NoticeDao noticeDao) {
        this.noticeDao = noticeDao;
    }

    public List<GetNoticeRes> getUserNotice(int userID) throws BaseException {
        try {
            List<GetNoticeRes> getNoticeRes = noticeDao.getUserNotice(userID);
            return getNoticeRes;
        } catch (Exception exception) {
            logger.error("App - getUserNotice Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

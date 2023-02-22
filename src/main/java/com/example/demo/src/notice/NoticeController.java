package com.example.demo.src.notice;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.notice.model.GetNoticeRes;
import com.example.demo.src.user.model.GetUserNoticeRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/notices")
public class NoticeController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final NoticeProvider noticeProvider;

    public NoticeController(NoticeProvider noticeProvider){
        this.noticeProvider = noticeProvider;
    }

    /**
     * 유저 알림 조회 API (알림센터)
     * [GET] /app/notice/users/:userId
     */
    @ResponseBody
    @GetMapping("/users/{userID}")
    public BaseResponse<List<GetNoticeRes>> getUserProfile(@PathVariable("userID") int userID) {
        try{
            List<GetNoticeRes> getNoticeRes = noticeProvider.getUserNotice(userID);
            return new BaseResponse<>(getNoticeRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}

package com.example.demo.src.point;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.photo.model.PostPhotoCommentReq;
import com.example.demo.src.point.model.GetPointRes;
import com.example.demo.src.point.model.PostPointReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/points")
public class PointController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PointProvider pointProvider;
    @Autowired
    private final PointService pointService;
    @Autowired
    private final JwtService jwtService;

    public PointController(PointProvider pointProvider, PointService pointService, JwtService jwtService){
        this.pointProvider = pointProvider;
        this.pointService = pointService;
        this.jwtService = jwtService;
    }

    /**
     * 포인트 목록 조회 API
     * [GET] /app/points/users/:userId
     */
    @ResponseBody
    @GetMapping("/users/{userID}")
    public BaseResponse<GetPointRes> getUserPoints(@PathVariable("userID") int userID) {
        try{
            GetPointRes getPointRes = pointProvider.getUserPoints(userID);
            return new BaseResponse<>(getPointRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 포인트 생성 API
     * [POST] /app/points/users/:userId
     */
    @ResponseBody
    @PostMapping("/users/{userID}")
    public BaseResponse<String> createPoint(@PathVariable("userID") int userID, @RequestBody PostPointReq postPointReq) {
        try {
            if (postPointReq.getNoticeTitle() == null) {
                return new BaseResponse<>(POST_POINT_EMPTY_NOTICETITLE);
            }
            if (postPointReq.getContent() == null){
                return new BaseResponse<>(POST_POINT_EMPTY_CONTENT);
            }
            if(postPointReq.getPoint() == 0){
                return new BaseResponse<>(POST_POINT_EMPTY_POINT);
            }
            pointService.createPoint(userID, postPointReq);
            return new BaseResponse<>();
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}

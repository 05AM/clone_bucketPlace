package com.example.demo.src.email;

import com.example.demo.config.BaseResponse;
import com.example.demo.src.email.model.PostAuthNumberReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/emails")
public class EmailController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final EmailService emailService;
    @Autowired
    private final EmailProvider emailProvider;
    @Autowired
    private EmailServiceForSendMail emailServiceForSendMail;

    public EmailController(EmailService emailService, EmailProvider emailProvider, EmailServiceForSendMail emailServiceForSendMail){
        this.emailService = emailService;
        this.emailProvider = emailProvider;
        this.emailServiceForSendMail = emailServiceForSendMail;
    }

    @ResponseBody
    @PostMapping("/check-codes")
    public BaseResponse<String> mailCheck(@RequestBody PostAuthNumberReq postAuthNumberReq, @RequestParam("email") String email) {
        int certifiedIdCode = emailProvider.getCertifiedIdCodeByEmail(email);
        System.out.println("certifiedIdCode = " + certifiedIdCode);
        System.out.println("postAuthNumberReq = " + postAuthNumberReq.getAuthNumber());

        if(postAuthNumberReq.getAuthNumber() == certifiedIdCode)
            return new BaseResponse<>("확인완료");
        else
            return new BaseResponse<>(INVALID_CERTIFIED_ID_CODE);
    }

    @ResponseBody
    @PostMapping("/send-mails")
    public BaseResponse<String> sendCertifiedIdCode(@RequestParam("email") String email){
        int certifiedIdCode = emailProvider.getCertifiedIdCodeByEmail(email);

        emailServiceForSendMail.joinEmail(email, certifiedIdCode);
        return new BaseResponse<>("전송완료");
    }

    @ResponseBody
    @PostMapping("/update/codes/users/{userId}")
    public BaseResponse<String> updateCertifiedIdCode(@PathVariable("userId") int userId){
        emailService.updateCertifiedIdCode(userId);
        return new BaseResponse<>("인증코드 변경완료");
    }
}

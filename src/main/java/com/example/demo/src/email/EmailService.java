package com.example.demo.src.email;

import com.example.demo.config.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional(rollbackFor = Exception.class)
public class EmailService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EmailDao emailDao;

    @Autowired
    public EmailService(EmailDao emailDao){
        this.emailDao = emailDao;
    }

    public void updateCertifiedIdCode(int userId){
        try{
            Random r = new Random();
            int certifiedIdCode = r.nextInt(888888) + 111111;
            emailDao.updateCertifiedIdCode(userId, certifiedIdCode);
        } catch(Exception exception){
            logger.error("App - updateCertifiedIdCode Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

}

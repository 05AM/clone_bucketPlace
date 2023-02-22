package com.example.demo.src.email;


import com.example.demo.config.BaseException;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional(readOnly = true)
public class EmailProvider {

    private final EmailDao emailDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public EmailProvider(EmailDao emailDao, JwtService jwtService) {
        this.emailDao = emailDao;
        this.jwtService = jwtService;
    }

    public int getCertifiedIdCodeByEmail(String email) throws BaseException {
        try {
            int certifiedIdCode = emailDao.getCertifiedIdCodeByEmail(email);
            return certifiedIdCode;
        } catch (Exception exception) {
            logger.error("App - getCertifiedIdCodeByEmail Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

}

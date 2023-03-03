package com.example.demo.src.oauth;

import com.example.demo.config.BaseException;
import com.example.demo.src.oauth.model.PostOauthCreateUserReq;
import com.example.demo.src.oauth.model.PostOauthRes;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class OauthService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OauthDao oauthDao;
    private final OauthProvider oauthProvider;
    private final JwtService jwtService;

    @Value("${oauth.kakao.rest-api-key}")
    private String kakaoRestAPIKey;

    @Autowired
    public OauthService(OauthDao oauthDao, OauthProvider oauthProvider, JwtService jwtService){
        this.oauthDao = oauthDao;
        this.oauthProvider = oauthProvider;
        this.jwtService = jwtService;
    }

    public String getFaceBookAccessToken(String code){
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://graph.facebook.com/oauth/access_token";
        /*String reqURL = "https://graph.facebook.com/v2.15/oauth/access_token";*/

        try{
            URL url = new URL(reqURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("client_id=3295807997347315");
            sb.append("&redirect_uri=https://prod.bucketplace.shop/app/oauth/facebook/callback");
            sb.append("&client_secret=9b9d2ec5e8b2e07eaeb7909ee36fa36b");
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            System.out.println(sb.toString());
            int responseCode = connection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null){
                result += line;
            }
            System.out.println("responseBody = " + result);

            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(result);

            access_Token = (String) object.get("access_token");
            System.out.println("access_Token = " + access_Token);

            br.close();
            bw.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return access_Token;
    }

    public PostOauthRes getFaceBookUserInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<String, Object>();
        String reqUrl = "https://graph.facebook.com/v15.0/me";
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("fields=id%2Cname%2Cemail");
            bw.write(sb.toString());
            bw.flush();


            int responseCode = connection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            String result = "";

            while((line = br.readLine()) != null){
                result += line;
            }
            System.out.println("responseBody = " + result);

            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(result);
            /*JSONObject properties = (JSONObject) object.get("response");*/

            String nickName = (String) object.get("name");
            String email = (String) object.get("email");
            nickName = uniToKor(nickName);
            email = uniToKor(email);
            System.out.println("nickName = " + nickName);
            System.out.println("email = " + email);
            userInfo.put("nickName", nickName);
            userInfo.put("email", email);

            int checkEmail = oauthDao.checkUserByEmail(email);
            if(checkEmail == 1){
                PostOauthRes postOauthRes = oauthProvider.loginGoogle(email);
                return postOauthRes;
            }
            else{
                int userId = oauthDao.createUser(nickName, email);
                oauthDao.setTypeOfOAuth("facebook", userId);
                return new PostOauthRes(userId, "");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getGoogleAccessToken(String code){
        String access_Token = "";
        /*String reqURL = "https://www.googleapis.com/oauth2/v4/token";*/
        String reqURL = "https://oauth2.googleapis.com/token";

        try{
            URL url = new URL(reqURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("&client_id=405629753088-vg6t47mrs24jh0oq2feimvt04b4tumbi.apps.googleusercontent.com");
            sb.append("&redirect_uri=https://prod.bucketplace.shop/app/oauth/google/callback");
            sb.append("&grant_type=authorization_code");
            sb.append("&client_secret=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");
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            System.out.println(sb.toString());
            int responseCode = connection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null){
                result += line;
            }
            System.out.println("responseBody = " + result);

            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(result);

            access_Token = (String) object.get("access_token");
            System.out.println("access_token = " + access_Token);

            br.close();
            bw.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return access_Token;
    }

    public PostOauthRes getGoogleUserInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<String, Object>();
        String reqUrl = "https://www.googleapis.com/oauth2/v1/userinfo";
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = connection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            String result = "";

            while((line = br.readLine()) != null){
                result += line;
            }
            System.out.println("responseBody = " + result);

            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(result);
            String email = (String) object.get("email");
            String nickName = (String) object.get("name");

            System.out.println("email = " + email);
            System.out.println("name = " + nickName);
            userInfo.put("email", email);
            userInfo.put("name", nickName);

            int checkEmail = oauthDao.checkUserByEmail(email);
            if(checkEmail == 1){
                PostOauthRes postOauthRes = oauthProvider.loginGoogle(email);
                return postOauthRes;
            }
            else{
                int userId = oauthDao.createUser(nickName, email);
                oauthDao.setTypeOfOAuth("google", userId);
                return new PostOauthRes(userId, "");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getKakaoAccessToken(String code) {
        String access_Token="";
        String refresh_Token ="";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + kakaoRestAPIKey); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=http://localhost:9000/app/oauth/kakao/callback"); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    public PostOauthRes getKakaoUserInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<String, Object>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + accessToken); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            JsonElement properties = element.getAsJsonObject().get("properties");

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();

            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            String email = "";
            if (hasEmail) {
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }

            System.out.println("nickname : " + nickname);
            System.out.println("email : " + email);

            userInfo.put("email", email);
            userInfo.put("name", nickname);

            br.close();

            int checkEmail = oauthDao.checkUserByEmail(email);
            if(checkEmail == 1){
                PostOauthRes postOauthRes = oauthProvider.loginKakao(email);
                return postOauthRes;
            }
            else{
                int userId = oauthDao.createUser(nickname, email);
                oauthDao.setTypeOfOAuth("kakao", userId);
                return new PostOauthRes(userId, "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createSocialUser(PostOauthCreateUserReq postOauthCreateUserReq, int userID, String userNickname) throws BaseException {
        if(postOauthCreateUserReq.getUserNickname() != null){
            int result = oauthDao.changeNickName(postOauthCreateUserReq.getUserNickname(), userID);
            userNickname = postOauthCreateUserReq.getUserNickname();
        }
        try{
            int result = oauthDao.registerSocialUser(postOauthCreateUserReq, userID, userNickname);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostOauthRes createNormalUser(PostOauthCreateUserReq postOauthCreateUserReq) throws BaseException {
        try{
            String pwd = new SHA256().encrypt(postOauthCreateUserReq.getPassword());
            postOauthCreateUserReq.setPassword(pwd);
        }
        catch (Exception ignored){
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userID = oauthDao.registerNormalUser(postOauthCreateUserReq);
            String jwt = jwtService.createJwt(userID);
            return new PostOauthRes(userID, jwt);
        } catch (Exception exception){
            logger.error("App - createNormalUser Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String uniToKor(String uni){
        StringBuffer result = new StringBuffer();
        for(int i=0; i<uni.length(); i++){
            if(uni.charAt(i) == '\\' &&  uni.charAt(i+1) == 'u'){
                Character c = (char)Integer.parseInt(uni.substring(i+2, i+6), 16);
                result.append(c);
                i+=5;
            }else{
                result.append(uni.charAt(i));
            }
        }
        return result.toString();
    }
}

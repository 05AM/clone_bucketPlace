package com.example.demo.src.search;

import com.example.demo.src.productPage.model.GetProductPageRes;
import com.example.demo.src.search.model.GetFurnitureSofasReq;
import com.example.demo.src.search.model.GetFurnituresReq;
import com.example.demo.src.search.model.GetPopularSearchWordsRes;
import com.example.demo.src.search.model.GetUnifiedSearchRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchProvider {
    private final SearchDao searchDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SearchProvider(SearchDao searchDao, JwtService jwtService) {
        this.searchDao = searchDao;
        this.jwtService = jwtService;
    }

    public List<GetProductPageRes> getFurniturePPs(GetFurnituresReq getFurnituresReq) {
        return searchDao.getFurniturePPs(getFurnituresReq);
    }


    public List<GetProductPageRes> getFurnitureSofaPPs(GetFurnitureSofasReq getFurnitureSofasReq) {
        return searchDao.getFurnitureSofaPPs(getFurnitureSofasReq);

    }

    public List<GetPopularSearchWordsRes> getPopularSearchWords() {
        return searchDao.getPopularSearchWords();
    }

    public GetUnifiedSearchRes getUnifiedSearch(String searchWord, String[] hashTag) {
        return searchDao.getUnifiedSearch(searchWord, hashTag);
    }
}

package com.pansky.es.controller;

import com.pansky.es.util.EsBaseSearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Fo
 * @date 2023/6/30 16:27
 */
@RestController
public class EsBaseSearchController {
    
    @Autowired
    private EsBaseSearchUtil esBaseSearchUtil;

    public Boolean isIndexExist(String indexName) {
        boolean flag = false;
        try {
            flag = esBaseSearchUtil.isIndexExist(indexName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(flag);
        return flag;
    }

    
   public boolean createIndex(String indexName) {
       boolean goods = false ;
       try {
           goods = esBaseSearchUtil.createIndex(indexName);
       } catch (Exception e) {
           e.printStackTrace();
       }
        return goods;
   }


    public boolean deleteIndex(String indexName) {
        boolean flag = false ;
        try {
            flag = esBaseSearchUtil.deleteIndex(indexName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    
    void getMapping(String indexName) {
    }

    
    void addDocument() {
    }

    
    void batchImportGoodsData() {
    }

    
    void deleteDataById() {
    }

    
    void updateDocument() {
    }

    
    void updateDocByCond() {
    }

    
    void searchDataById() {
    }

}

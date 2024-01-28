package com.gist.cithub.backend.Repo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gist.cithub.backend.common.utils.PageUtils;
import com.gist.cithub.backend.Repo.entity.ListEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author xiangjiantu
 * @email 1223564154@qq.com
 * @date 2022-09-08 21:44:17
 */
public interface ListService extends IService<ListEntity> {

    PageUtils queryPage(Map<String, Object> params);


    String savaUploadFile(MultipartFile file);

    Page<ListEntity> searchByKeywords(Integer pagenum, Integer pagesize, String searchKeywords,String typeofPapers);

    Page<ListEntity> searchByAuthor (Integer pagenum, Integer pagesize, String searchKeywords,String typeofPapers);
    Page<ListEntity> searchByField (Integer pagenum, Integer pagesize, String searchKeywords,String typeofPapers);

    Page<ListEntity> searchByInstitutions (Integer pagenum, Integer pagesize, String searchKeywords,String typeofPapers);

    Page<ListEntity> searchByAbbr (Integer pagenum, Integer pagesize, String searchKeywords,String typeofPapers);

    Page<ListEntity> searchByBooktitle (Integer pagenum, Integer pagesize, String searchKeywords,String typeofPapers);

    Page<ListEntity> listAllPapers(Integer pagenum,Integer pagesize,String typerofPapers);
    Page<ListEntity> listBy(Integer pagenum,Integer pagesize,String typerofPapers,String listByKey,String listByValue );
    List<Map<String, Object>>  getAllTypeofPapers();

    Boolean writeToMysql(String filePath);

    ArrayList<ListEntity> saveUploadFileByBib(String JsonString);
}

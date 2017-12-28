package com.bonc.dw3.controller;

import com.bonc.dw3.entity.DownloadFile;
import com.bonc.dw3.service.DownloadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>Title: BONC -  DownloadController</p>
 * <p>Description:  </p>
 * <p>Copyright: Copyright BONC(c) 2013 - 2025 </p>
 * <p>Company: 北京东方国信科技股份有限公司 </p>
 *
 * @author zhaojie
 * @version 1.0.0
 */
@Controller
@Api(tags = "download", description ="下载")
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    DownloadService downloadService;

    @ApiOperation("生成excel")
    @PostMapping("/generateExcel")
    public void generateExcel(@ApiParam("请求参数")@RequestBody HashMap<String,String> paramMap){
        String moduleId = paramMap.get("moduleId");
        String date = paramMap.get("date");
        String fileId = "burning";
        try {
            downloadService.generateExcel(moduleId,date,fileId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("下载列表接口")
    @PostMapping("/downloadTable")
    public String downloadTable(@ApiParam("请求参数")@RequestBody HashMap<String,String> paramMap, Model model){
        List<DownloadFile> fileList = new ArrayList<>();
        fileList = downloadService.downloadTable(paramMap);
        model.addAttribute("dataList",fileList);
        return "downloadTable";
    }

    @ApiOperation("表格下载接口")
    @PostMapping("/allDataDownload")
    public String allDataDownload(@ApiParam("请求参数")@RequestBody HashMap<String,String> paramMap,Model model){
        String exportFilePath = new String();
        exportFilePath = downloadService.allDataDownload(paramMap);
        model.addAttribute("dataStr",exportFilePath);
        return "allDataDownload";
    }

    @ApiOperation("下载最大账期接口")
    @PostMapping("/downloadMaxDate")
    public String downloadMaxDate(@ApiParam("请求参数")@RequestBody HashMap<String,String> paramMap,Model model){
        String maxDate = new String();
        maxDate = downloadService.downloadMaxDate(paramMap);
        model.addAttribute("dataStr",maxDate);
        return "downloadMaxDate";
    }
}

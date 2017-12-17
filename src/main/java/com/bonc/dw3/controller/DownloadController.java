package com.bonc.dw3.controller;

import com.bonc.dw3.service.DownloadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

/**
 * <p>Title: BONC -  DownloadController</p>
 * <p>Description:  </p>
 * <p>Copyright: Copyright BONC(c) 2013 - 2025 </p>
 * <p>Company: 北京东方国信科技股份有限公司 </p>
 *
 * @author zhaojie
 * @version 1.0.0
 */
@RestController
@Api(tags = "download", description ="下载")
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    DownloadService downloadService;

    @ApiOperation("生成excel")
    @PostMapping("/generateExcel")
    public void generateExcel(@ApiParam("请求参数")@RequestBody HashMap<String,String> param){
        try {
            downloadService.generateExcel(param);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

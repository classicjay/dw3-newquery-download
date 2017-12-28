package com.bonc.dw3.mapper;

import com.bonc.dw3.entity.DownloadFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>Title: BONC -  DownloadFileMapper</p>
 * <p>Description:  </p>
 * <p>Copyright: Copyright BONC(c) 2013 - 2025 </p>
 * <p>Company: 北京东方国信科技股份有限公司 </p>
 *
 * @author zhaojie
 * @version 1.0.0
 */
@Mapper
public interface DownloadFileMapper {
    /**
     * 根据用户id获取全部文件
     * @return
     */
    public List<DownloadFile> getFileByUserId(@Param(value = "userId") String userId);

    /**
     * 根据文件id获取单个文件
     * @return
     */
    public DownloadFile getFileByFileId(@Param(value = "fileId") String fileId);

    /**
     * 新增文件
     * @param file
     */
    public void addFile(DownloadFile file);

    /**
     * 最大账期
     * @param dateType
     * @param markType
     * @return
     */
    public String getMaxDate(@Param(value = "dateType") String dateType,@Param(value = "markType") String markType);

    /**
     * 根据专题id获取专题名
     * @param subjectCode
     * @return
     */
    public String getSubjectNameByCode(@Param(value = "subjectCode") String subjectCode);
}

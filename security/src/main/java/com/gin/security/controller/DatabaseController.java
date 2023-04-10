package com.gin.security.controller;

import com.gin.common.annotation.MyRestController;
import com.gin.common.exception.file.FileDeleteException;
import com.gin.common.exception.file.FileNotExistsException;
import com.gin.common.vo.FileInfo;
import com.gin.common.vo.response.Res;
import com.gin.database.vo.response.ResPage;
import com.gin.databasebackup.controller.AbstractDatabaseController;
import com.gin.databasebackup.service.DatabaseBackupService;
import com.gin.operationlog.dto.param.OperationLogPageParam;
import com.gin.operationlog.vo.SubClassOption;
import com.gin.operationlog.vo.SystemOperationLogVo;
import com.gin.route.annotation.MenuEntry;
import com.gin.route.annotation.MenuItem;
import com.gin.security.Constant.Security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 数据库管理接口
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/10 15:58
 */
@MyRestController(AbstractDatabaseController.API_PREFIX)
@MenuItem(title = "数据库管理", description = "数据库镜像的查询、备份、还原、下载、上传、删除")
public class DatabaseController extends AbstractDatabaseController {
    public DatabaseController(DatabaseBackupService service) {
        super(service);
    }

    @Override
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public void getDownload(String filename, HttpServletResponse response, HttpServletRequest request) throws IOException {
        super.getDownload(filename, response, request);
    }

    @Override
    @MenuEntry
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<FileInfo>> getList(HttpServletRequest request) throws IOException {
        return super.getList(request);
    }

    /**
     * 列出该主实体类型(和主实体ID)下, 所有的副实体类型,及每个副实体类型下的操作类型
     * @param old     是否查询旧日志
     * @param mainId  主实体Id ， 是否由用户指定由接口决定
     * @param request 请求
     * @return 所有的副实体类型, 及每个副实体类型下的操作类型
     */
    @Override
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<SubClassOption>> getLogOptions(Boolean old, Long mainId, HttpServletRequest request) {
        return super.getLogOptions(old, mainId, request);
    }

    /**
     * 日志分页查询
     * @param old     是否查询旧日志
     * @param param   查询参数
     * @param request 请求
     * @return 日志
     */
    @Override
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public ResPage<SystemOperationLogVo> getLogPage(Boolean old, OperationLogPageParam param, HttpServletRequest request) {
        return super.getLogPage(old, param, request);
    }

    @Override
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<FileInfo> postBackup(Boolean gzip, HttpServletRequest request) throws IOException {
        return super.postBackup(gzip, request);
    }

    @Override
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<FileInfo> postDel(String filename, HttpServletRequest request) throws FileNotExistsException, FileDeleteException {
        return super.postDel(filename, request);
    }

    @Override
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<FileInfo> postRecover(String filename, HttpServletRequest request) throws IOException {
        return super.postRecover(filename, request);
    }

    @Override
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<FileInfo> postUpload(MultipartFile file, HttpServletRequest request) throws IOException {
        return super.postUpload(file, request);
    }
}

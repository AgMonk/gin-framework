package com.gin.security.controller;

import com.gin.common.constant.ApiPath;
import com.gin.common.exception.file.FileDeleteException;
import com.gin.common.exception.file.FileNotExistsException;
import com.gin.database.vo.response.ResPage;
import com.gin.databasebackup.controller.AbstractDatabaseController;
import com.gin.databasebackup.service.DatabaseBackupService;
import com.gin.operationlog.annotation.OpLog;
import com.gin.operationlog.dto.param.OperationLogPageParam;
import com.gin.operationlog.enums.OperationType;
import com.gin.operationlog.vo.SubClassOption;
import com.gin.operationlog.vo.SystemOperationLogVo;
import com.gin.route.annotation.MenuEntry;
import com.gin.route.annotation.MenuItem;
import com.gin.security.Constant.Security;
import com.gin.spring.annotation.MyRestController;
import com.gin.spring.vo.FileInfo;
import com.gin.spring.vo.response.Res;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.gin.redis.RedisConfig.REDIS_CACHE_MANAGER;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * 数据库管理接口
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/10 15:58
 */
@Tag(name = AbstractDatabaseController.GROUP_NAME)
@MyRestController(AbstractDatabaseController.API_PREFIX)
@CacheConfig(cacheManager = REDIS_CACHE_MANAGER, cacheNames = AbstractDatabaseController.CACHE_NAME)
@MenuItem(title = "数据库管理", description = "数据库镜像的查询、备份、还原、下载、上传、删除")
public class DatabaseController extends AbstractDatabaseController {
    public DatabaseController(DatabaseBackupService service) {
        super(service);
    }

    @Override
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    @GetMapping(ApiPath.DOWNLOAD)
    @Operation(summary = "下载镜像文件")
    @OpLog(mainClass = Database.class, mainId = "@databaseBackupService?.getBackupFile(#filename)?.lastModified() / 1000", type = OperationType.DOWNLOAD)
    public void getDownload(String filename, HttpServletResponse response, HttpServletRequest request) throws IOException {
        super.getDownload(filename, response, request);
    }

    @Override
    @MenuEntry
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    @GetMapping(ApiPath.LIST)
    @Operation(summary = "查询镜像列表")
    public Res<List<FileInfo>> getList(HttpServletRequest request) throws IOException {
        return super.getList(request);
    }

    /**
     * 列出该主实体类型(和主实体ID)下, 所有的副实体类型,及每个副实体类型下的操作类型
     *
     * @param old     是否查询旧日志
     * @param mainId  主实体Id ， 是否由用户指定由接口决定
     * @param request 请求
     * @return 所有的副实体类型, 及每个副实体类型下的操作类型
     */
    @Override
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    @GetMapping("/log/options")
    @Operation(summary = "日志选项", description = "列出该主实体类型(和主实体ID)下, 所有的副实体类型,及每个副实体类型下的操作类型")
    public Res<List<SubClassOption>> getLogOptions(Boolean old, Long mainId, HttpServletRequest request) {
        return super.getLogOptions(old, mainId, request);
    }

    /**
     * 日志分页查询
     *
     * @param old     是否查询旧日志
     * @param param   查询参数
     * @param request 请求
     * @return 日志
     */
    @Override
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    @GetMapping("/log/page")
    @Operation(summary = "日志分页查询")
    public ResPage<SystemOperationLogVo> getLogPage(Boolean old, OperationLogPageParam param, HttpServletRequest request) {
        return super.getLogPage(old, param, request);
    }

    @Override
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    @PostMapping(ApiPath.BACKUP)
    @Operation(summary = "执行备份", description = "返回备份好的文件信息")
    @OpLog(mainClass = Database.class, mainId = "#result?.data?.lastModified", type = OperationType.BACKUP)
    public Res<FileInfo> postBackup(Boolean gzip, HttpServletRequest request) throws IOException {
        return super.postBackup(gzip, request);
    }

    @Override
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    @PostMapping(ApiPath.DEL)
    @Operation(summary = "删除镜像文件", description = "返回被删除的文件信息")
    @OpLog(mainClass = Database.class, mainId = "#result?.data?.lastModified", type = OperationType.DEL)
    public Res<FileInfo> postDel(String filename, HttpServletRequest request) throws FileNotExistsException, FileDeleteException {
        return super.postDel(filename, request);
    }

    @Override
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    @PostMapping(ApiPath.RECOVER)
    @Operation(summary = "执行还原", description = "返回被还原的文件信息")
    @OpLog(mainClass = Database.class, mainId = "#result?.data?.lastModified", type = OperationType.RECOVER)
    public Res<FileInfo> postRecover(String filename, HttpServletRequest request) throws IOException {
        return super.postRecover(filename, request);
    }

    @Override
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    @PostMapping(value = ApiPath.UPLOAD, consumes = {MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "上传镜像文件", description = "文件后缀必须为 sql 或 gz;<br/>返回被上传的文件信息")
    @OpLog(mainClass = Database.class, mainId = "#result?.data?.lastModified", type = OperationType.UPLOAD)
    public Res<FileInfo> postUpload(MultipartFile file, HttpServletRequest request) throws IOException {
        return super.postUpload(file, request);
    }
}

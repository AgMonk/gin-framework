package old.databsebackup.controller;

import com.gin.springboot3template.databsebackup.service.DatabaseBackupService;
import com.gin.springboot3template.operationlog.annotation.OpLog;
import com.gin.springboot3template.operationlog.controller.OperationLogController;
import com.gin.springboot3template.operationlog.dto.param.OperationLogPageParam;
import com.gin.springboot3template.operationlog.enums.OperationType;
import com.gin.springboot3template.operationlog.vo.SubClassOption;
import com.gin.springboot3template.operationlog.vo.SystemOperationLogVo;
import com.gin.springboot3template.route.annotation.MenuEntry;
import com.gin.springboot3template.route.annotation.MenuItem;
import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.sys.enums.ServiceStatus;
import com.gin.springboot3template.sys.exception.file.FileDeleteException;
import com.gin.springboot3template.sys.exception.file.FileNotExistsException;
import com.gin.springboot3template.sys.vo.FileInfo;
import com.gin.springboot3template.sys.vo.response.Res;
import com.gin.springboot3template.sys.vo.response.ResPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * 数据库备份服务
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/1/12 14:47
 */

@MyRestController(DatabaseController.API_PREFIX)
@RequiredArgsConstructor
@Tag(name = DatabaseController.GROUP_NAME)
@Slf4j
@CacheConfig(cacheManager = "redisCacheManager")
@MenuItem(title = "数据库管理", description = "数据库镜像的查询、备份、还原、下载、上传、删除")
public class DatabaseController implements OperationLogController {
    /**
     * 接口路径前缀
     */
    public static final String API_PREFIX = "/database";
    public static final String CACHE_NAME = "database";
    public static final String GROUP_NAME = "数据库备份服务接口";
    private final DatabaseBackupService service;

    @GetMapping(Constant.Api.DOWNLOAD)
    @Operation(summary = "下载镜像文件")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    @OpLog(mainClass = Database.class, mainId = "@databaseBackupService?.getBackupFile(#filename)?.lastModified() / 1000", type = OperationType.DOWNLOAD)
    public void getDownload(
            @RequestParam @Parameter(description = "文件名") String filename,
            HttpServletResponse response,
            @SuppressWarnings("unused") HttpServletRequest request
    ) throws IOException {
        service.download(filename, response);
    }

    @GetMapping(Constant.Api.LIST)
    @Operation(summary = "查询镜像列表")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    @Cacheable(value = CACHE_NAME, key = "#root.methodName")
    @MenuEntry
    public Res<List<FileInfo>> getList(@SuppressWarnings("unused") HttpServletRequest request) throws IOException {
        return Res.of(service.list());
    }

    /**
     * 列出该主实体类型(和主实体ID)下, 所有的副实体类型,及每个副实体类型下的操作类型
     * @param old     是否查询旧日志
     * @param mainId  主实体Id ， 是否由用户指定由接口决定
     * @param request 请求
     * @return 所有的副实体类型, 及每个副实体类型下的操作类型
     */
    @Override
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<SubClassOption>> getLogOptions(Boolean old, Long mainId, HttpServletRequest request) {
        return OperationLogController.super.getLogOptions(old, mainId, request);
    }

    /**
     * 日志分页查询
     * @param old     是否查询旧日志
     * @param param   查询参数
     * @param request 请求
     * @return 日志
     */
    @Override
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public ResPage<SystemOperationLogVo> getLogPage(Boolean old, OperationLogPageParam param, HttpServletRequest request) {
        return OperationLogController.super.getLogPage(old, param, request);
    }

    @GetMapping(Constant.Api.STATUS)
    @Operation(summary = "查询服务状态")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<ServiceStatus> getStatus(@SuppressWarnings("unused") HttpServletRequest request) {
        return Res.of(service.getStatus(), service.getStatus().getZh());
    }

    /**
     * 主实体类型
     * @return 主实体类型
     */
    @Override
    public Class<?> mainClass() {
        return Database.class;
    }

    /**
     * 主实体ID
     * @return 主实体ID
     */
    @Override
    public Long mainId(Long mainId) {
        return null;
    }

    @PostMapping("backup")
    @Operation(summary = "执行备份", description = "返回备份好的文件信息")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    @OpLog(mainClass = Database.class, mainId = "#result?.data?.lastModified", type = OperationType.BACKUP)
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public Res<FileInfo> postBackup(
            @RequestParam(required = false, defaultValue = "true") @Parameter(description = "是否使用gzip压缩,默认true") Boolean gzip,
            @SuppressWarnings("unused") HttpServletRequest request
    ) throws IOException {
        return Res.of(service.backup(gzip), "备份成功");
    }

    @PostMapping(Constant.Api.DEL)
    @Operation(summary = "删除镜像文件", description = "返回被删除的文件信息")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    @OpLog(mainClass = Database.class, mainId = "#result?.data?.lastModified", type = OperationType.DEL)
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public Res<FileInfo> postDel(
            @RequestParam @Parameter(description = "文件名") String filename,
            @SuppressWarnings("unused") HttpServletRequest request
    ) throws FileNotExistsException, FileDeleteException {
        return Res.of(service.del(filename), "删除成功");
    }

    @PostMapping("recover")
    @Operation(summary = "执行还原", description = "返回被还原的文件信息")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    @OpLog(mainClass = Database.class, mainId = "#result?.data?.lastModified", type = OperationType.RECOVER)
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public Res<FileInfo> postRecover(
            @RequestParam @Parameter(description = "文件名") String filename,
            @SuppressWarnings("unused") HttpServletRequest request
    ) throws IOException {
        return Res.of(service.recover(filename), "还原成功");
    }

    @PostMapping(value = Constant.Api.UPLOAD, consumes = {MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "上传镜像文件", description = "文件后缀必须为 sql 或 gz;<br/>返回被上传的文件信息")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    @OpLog(mainClass = Database.class, mainId = "#result?.data?.lastModified", type = OperationType.UPLOAD)
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public Res<FileInfo> postUpload(
            MultipartFile file,
            @SuppressWarnings("unused") HttpServletRequest request
    ) throws IOException {
        return Res.of(service.upload(file), "上传成功");
    }
}
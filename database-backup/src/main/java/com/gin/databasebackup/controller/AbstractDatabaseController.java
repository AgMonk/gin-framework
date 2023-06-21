package com.gin.databasebackup.controller;

import com.gin.common.constant.ApiPath;
import com.gin.common.exception.file.FileDeleteException;
import com.gin.common.exception.file.FileNotExistsException;
import com.gin.spring.vo.FileInfo;
import com.gin.spring.vo.response.Res;
import com.gin.database.enums.ServiceStatus;
import com.gin.databasebackup.service.DatabaseBackupService;
import com.gin.operationlog.annotation.OpLog;
import com.gin.operationlog.controller.OperationLogController;
import com.gin.operationlog.enums.OperationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.gin.database.config.redis.RedisConfig.REDIS_CACHE_MANAGER;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * 数据库备份接口(抽象)
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/10 12:10
 */



@Tag(name = AbstractDatabaseController.GROUP_NAME)
@Slf4j
@CacheConfig(cacheManager = REDIS_CACHE_MANAGER, cacheNames = AbstractDatabaseController.CACHE_NAME)
@RequiredArgsConstructor
public abstract class AbstractDatabaseController
        implements OperationLogController {
    public static final String API_PREFIX = "/database";
    /**
     * 缓存名称
     */
    public static final String CACHE_NAME = "database";
    /**
     * 接口分组名称
     */
    public static final String GROUP_NAME = "数据库备份服务接口";
    /**
     * 本接口服务
     */
    private final DatabaseBackupService service;

    @GetMapping(ApiPath.STATUS)
    @Operation(summary = "查询服务状态")
    public final Res<ServiceStatus> getStatus(@SuppressWarnings("unused") HttpServletRequest request) {
        return Res.of(service.getStatus(), service.getStatus().getZh());
    }

    /**
     * 主实体类型
     * @return 主实体类型
     */
    @Override
    public final Class<?> mainClass() {
        return Database.class;
    }

    /**
     * 主实体ID
     * @param mainId 用户传入的主实体Id
     * @return 主实体ID
     */
    @Nullable
    @Override
    public final Long mainId(Long mainId) {
        return null;
    }

    @GetMapping(ApiPath.DOWNLOAD)
    @Operation(summary = "下载镜像文件")
    @OpLog(mainClass = Database.class, mainId = "@databaseBackupService?.getBackupFile(#filename)?.lastModified() / 1000", type = OperationType.DOWNLOAD)
    public void getDownload(
            @RequestParam @Parameter(description = "文件名") String filename,
            HttpServletResponse response,
            @SuppressWarnings("unused") HttpServletRequest request
    ) throws IOException {
        service.download(filename, response);
    }

    @GetMapping(ApiPath.LIST)
    @Operation(summary = "查询镜像列表")
    @Cacheable(key = "#root.methodName")
    public Res<List<FileInfo>> getList(@SuppressWarnings("unused") HttpServletRequest request) throws IOException {
        return Res.of(service.list());
    }

    @PostMapping(ApiPath.BACKUP)
    @Operation(summary = "执行备份", description = "返回备份好的文件信息")
    @OpLog(mainClass = Database.class, mainId = "#result?.data?.lastModified", type = OperationType.BACKUP)
    @CacheEvict(allEntries = true)
    public Res<FileInfo> postBackup(
            @RequestParam(required = false, defaultValue = "true") @Parameter(description = "是否使用gzip压缩,默认true") Boolean gzip,
            @SuppressWarnings("unused") HttpServletRequest request
    ) throws IOException {
        return Res.of(service.backup(gzip), "备份成功");
    }

    @PostMapping(ApiPath.DEL)
    @Operation(summary = "删除镜像文件", description = "返回被删除的文件信息")
    @OpLog(mainClass = Database.class, mainId = "#result?.data?.lastModified", type = OperationType.DEL)
    @CacheEvict(allEntries = true)
    public Res<FileInfo> postDel(
            @RequestParam @Parameter(description = "文件名") String filename,
            @SuppressWarnings("unused") HttpServletRequest request
    ) throws FileNotExistsException, FileDeleteException {
        return Res.of(service.del(filename), "删除成功");
    }

    @PostMapping(ApiPath.RECOVER)
    @Operation(summary = "执行还原", description = "返回被还原的文件信息")
    @OpLog(mainClass = Database.class, mainId = "#result?.data?.lastModified", type = OperationType.RECOVER)
    @CacheEvict(allEntries = true)
    public Res<FileInfo> postRecover(
            @RequestParam @Parameter(description = "文件名") String filename,
            @SuppressWarnings("unused") HttpServletRequest request
    ) throws IOException {
        return Res.of(service.recover(filename), "还原成功");
    }

    @PostMapping(value = ApiPath.UPLOAD, consumes = {MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "上传镜像文件", description = "文件后缀必须为 sql 或 gz;<br/>返回被上传的文件信息")
    @OpLog(mainClass = Database.class, mainId = "#result?.data?.lastModified", type = OperationType.UPLOAD)
    @CacheEvict(allEntries = true)
    public Res<FileInfo> postUpload(
            MultipartFile file,
            @SuppressWarnings("unused") HttpServletRequest request
    ) throws IOException {
        return Res.of(service.upload(file), "上传成功");
    }
}   

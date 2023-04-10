package service;

import config.DatabaseConConfig;
import properties.DatabaseProperties;
import com.gin.common.enums.OsType;
import com.gin.database.enums.ServiceStatus;
import com.gin.common.exception.BusinessException;
import com.gin.common.exception.file.DirCreateException;
import com.gin.common.exception.file.FileDeleteException;
import com.gin.common.exception.file.FileExistsException;
import com.gin.common.exception.file.FileNotExistsException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import com.gin.common.properties.FileProperties;
import com.gin.common.utils.FileUtils;
import com.gin.common.utils.IoUtils;
import com.gin.common.utils.ProcessUtils;
import com.gin.common.utils.TimeUtils;
import com.gin.common.vo.FileInfo;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库备份服务
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/1/11 16:09
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseBackupService {
    public static final List<String> EXT_LIST = new ArrayList<>(List.of("sql", "gz"));
    public static final String EXT_MESSAGE = "文件后缀必须为其中之一: " + String.join(",", EXT_LIST);
    /**
     * 备份命令
     */
    private static final String DUMP = "mysqldump";
    private static final String MYSQL = "mysql";
    private static final String PATH_BACKUP = "/database_backup";
    private static final String PATH_TEMP = "/mysql_client";
    private final FileProperties fileProperties;
    private final DatabaseProperties databaseProperties;
    private final DataSourceProperties dataSourceProperties;
    private final DatabaseConConfig databaseConConfig;
    @Getter
    ServiceStatus status = ServiceStatus.disable;
    private File dirBackup;
    private File dirTemp;

    /**
     * 自动备份
     */
    @Scheduled(cron = "0 10 4 * * ?")
    public void autoBackup() throws IOException {
        if (!databaseProperties.isAutoBackup()) {
            return;
        }
        log.info("自动备份数据库...");
        backup(true);
    }

    /**
     * 自动清理备份镜像
     */
    @Scheduled(cron = "0 20 4 * * ?")
    public void autoClear() throws IOException {
        final int max = databaseProperties.getMaxBackup();
        if (max <= 0) {
            return;
        }
        log.info("自动清理备份镜像...");
        final List<FileInfo> list = list();
        //需要删除的文件
        final List<FileInfo> target = list.subList(Math.min(max, list.size()), list.size());
        for (FileInfo info : target) {
            FileUtils.deleteFile(info.getFile());
        }
        log.info("备份镜像清理完毕...");
    }

    /**
     * 执行备份
     * @param gzip 是否使用gzip压缩
     * @return 备份好的文件
     */
    public FileInfo backup(boolean gzip) throws IOException {
        //当状态可用时 执行备份
        if (status != ServiceStatus.enable) {
            return null;
        }
        // 备份当前数据库
        final String database = databaseConConfig.getDatabase();
        // 当前时间
        final String datetime = TimeUtils.format(ZonedDateTime.now());
        // 文件名
        final String filename = (database + "_" + FileUtils.replaceInvalid(datetime, "_") + ".sql")
                .replace(" ", "_")
                + (gzip ? ".gz" : "");
        // 构建备份指令
        List<String> cmd = new ArrayList<>();
        cmd.add(DUMP);
        cmd.add("-u" + dataSourceProperties.getUsername());
        cmd.add("-p" + dataSourceProperties.getPassword());
        cmd.add("-h" + databaseConConfig.getHost());
        cmd.add(database);
        cmd.add("--column-statistics=0");
        cmd.add("--lock-tables");
        //加入压缩
        if (gzip) {
            cmd.add("|");
            cmd.add("gzip");
        }
        cmd.add(">");
        cmd.add(filename);

        log.info("开始备份: " + filename);
        applyCmd(cmd);
        log.info("备份完成: " + filename);

        return new FileInfo(getBackupFile(filename));
    }

    /**
     * 删除镜像
     * @param filename 文件名
     */
    public FileInfo del(String filename) throws FileNotExistsException, FileDeleteException {
        final File file = getBackupFile(filename);
        final FileInfo fileInfo = new FileInfo(file);
        FileUtils.deleteFile(file);
        return fileInfo;
    }

    /**
     * 下载镜像文件
     */
    public void download(String filename, HttpServletResponse response) throws IOException {
        //检查文件存在
        final File file = getBackupFile(filename);
        FileUtils.assertExists(file);
        final String cache = "no-cache";
//        final String cache = "max-age=" + Integer.MAX_VALUE;

        //设置响应的信息
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
        response.setHeader(HttpHeaders.PRAGMA, cache);
        response.setHeader(HttpHeaders.CACHE_CONTROL, cache);
        //设置浏览器接受类型为流
        response.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE + ";charset=UTF-8");

        try (FileInputStream fis = new FileInputStream(file); ServletOutputStream os = response.getOutputStream()) {
            IOUtils.copy(fis, os);
        }
    }

    @NotNull
    public File getBackupFile(String filename) {
        return new File(dirBackup.getPath() + "/" + filename);
    }

    @PostConstruct
    public void init() throws DirCreateException, FileExistsException {
        //初始化 创建文件夹
        dirTemp = new File(fileProperties.getHomePath() + PATH_TEMP);
        dirBackup = new File(fileProperties.getHomePath() + PATH_BACKUP);
        FileUtils.mkdir(dirTemp);
        FileUtils.mkdir(dirBackup);
    }

    /**
     * 列出备份镜像
     * @return 备份镜像
     */
    public List<FileInfo> list() throws IOException {
        return FileUtils.listFiles(dirBackup).stream().map(FileInfo::new)
                .sorted((o1, o2) -> Math.toIntExact(o2.getLastModified() - o1.getLastModified()))
                .toList();
    }

    /**
     * 检查mysql client是否安装完毕 , 如果未安装 执行安装
     */
    public void prepare() {
        if (OsType.find() != OsType.LINUX) {
            log.warn("当前系统非 Linux ,不执行数据库备份相关操作");
            return;
        }
        //如果状态为可用 不执行相关操作
        if (status == ServiceStatus.enable) {
            log.info("MysqlClient 可用");
            return;
        }
        //如果状态为可用 不执行相关操作
        if (status == ServiceStatus.preparing) {
            throw BusinessException.of(HttpStatus.SERVICE_UNAVAILABLE, "服务正在准备中");
        }

        //检查dump是否可用
        if (!checkDumpCmd()) {
            //命令不可用
            try {
                status = ServiceStatus.preparing;
                log.info("数据库备份服务状态:{}", status.getZh());
                //下载
                downloadClient();
                //安装
                installClient();
                //再次检查
                if (checkDumpCmd()) {
                    log.info("Mysql Client安装完毕 , 备份和还原功能可用");
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 指定一个镜像文件进行还原
     */
    public FileInfo recover(String filename) throws IOException {
        final File file = getBackupFile(filename);
        FileUtils.assertExists(file);
        //是否是压缩文件
        final boolean gzip = filename.endsWith(".gz");

        // 指令
        List<String> cmd = new ArrayList<>();
        if (gzip) {
            cmd.add("gunzip <");
            cmd.add(filename);
            cmd.add("|");
        }
        cmd.add(MYSQL);
        cmd.add("-u" + dataSourceProperties.getUsername());
        cmd.add("-p" + dataSourceProperties.getPassword());
        cmd.add("-h" + databaseConConfig.getHost());
        cmd.add(databaseConConfig.getDatabase());
        if (!gzip) {
            cmd.add("<");
            cmd.add(filename);
        }

        log.info("开始还原: " + filename);
        applyCmd(cmd);
        log.info("还原完成: " + filename);
        return new FileInfo(file);

    }

    /**
     * 上传镜像文件
     */
    public FileInfo upload(@NotNull MultipartFile multipartFile) throws IOException {
        final String filename = multipartFile.getOriginalFilename();
        if (ObjectUtils.isEmpty(filename)) {
            throw BusinessException.of(HttpStatus.BAD_REQUEST, "必须提供原文件名");
        }
        final String ext = FileUtils.getFileExtName(filename);
        if (!EXT_LIST.contains(ext)) {
            throw BusinessException.of(HttpStatus.BAD_REQUEST, EXT_MESSAGE);
        }

        //目标文件
        final File destFile = getBackupFile(filename);

        if (destFile.exists()) {
            throw BusinessException.of(HttpStatus.BAD_REQUEST, "该文件已存在");
        }
        // 保存文件
        multipartFile.transferTo(destFile);
        // 返回文件信息
        return new FileInfo(destFile);
    }

    /**
     * 执行指令
     * @param cmd 指令
     */
    private void applyCmd(List<String> cmd) throws IOException {
        log.debug("执行命令: " + String.join(" ", cmd));
        final Process process = new ProcessBuilder("sh", "-c", String.join(" ", cmd))
                .redirectErrorStream(true).directory(dirBackup).start();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        IoUtils.readLine(reader, log::info);
    }

    /**
     * 检查mysqldump命令是否可用
     * @return 是否可用
     */
    private boolean checkDumpCmd() {
        try {
            log.info("检查 mysqldump 命令是否可用");
            final ProcessBuilder pb = new ProcessBuilder(DUMP);
            pb.redirectErrorStream(true);
            final Process process = pb.start();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            IoUtils.readLine(reader, log::info);
            status = ServiceStatus.enable;
            log.info("数据库备份服务状态:{}", status.getZh());
            return true;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            status = ServiceStatus.disable;
            log.info("数据库备份服务状态:{}", status.getZh());
            return false;
        }
    }

    /**
     * 下载 mysql client 所需文件
     */
    private void downloadClient() throws IOException, InterruptedException {
        final List<String> files = FileUtils.listFiles(dirTemp).stream().map(File::getName).toList();
        for (String url : databaseProperties.getMysqlClient()) {
            final String filename = url.substring(url.lastIndexOf("/") + 1);
            if (!files.contains(filename)) {
                log.info("开始下载: " + url);
                final Process process = ProcessUtils.downloadWithCurl(url, dirTemp);
                if (process.waitFor() == 0) {
                    log.info("下载完毕: " + url);
                }
            } else {
                log.info("文件已存在,跳过: " + filename);
            }
        }
    }

    /**
     * 安装mysql client
     */
    private void installClient() throws IOException {
        final Process installProcess = new ProcessBuilder("rpm", "-ivh", "mysql-community-*").directory(dirTemp).start();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(installProcess.getInputStream()));
        IoUtils.readLine(reader, i -> log.info("正在安装: {}", i));
        log.info("安装完毕...");
    }
}

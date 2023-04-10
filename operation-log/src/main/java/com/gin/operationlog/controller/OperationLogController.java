package controller;

import dto.param.OperationLogPageParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.OpLogService;
import com.gin.common.utils.SpringContextUtils;
import vo.SubClassOption;
import vo.SystemOperationLogVo;
import com.gin.common.vo.response.Res;
import com.gin.database.vo.response.ResPage;

import java.util.List;

/**
 * 日志查询接口
 * 直接实现该接口即可,如果需要权限控制,重写方法添加注解
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/24 10:02
 */
public interface OperationLogController {
    /**
     * 主实体类型
     * @return 主实体类型
     */
    @NotNull
    Class<?> mainClass();

    /**
     * 主实体ID
     * @param mainId 用户传入的主实体Id
     * @return 主实体ID
     */
    @Nullable
    Long mainId(Long mainId);

    /**
     * 列出该主实体类型(和主实体ID)下, 所有的副实体类型,及每个副实体类型下的操作类型
     * @param old     是否查询旧日志
     * @param mainId  主实体Id ， 是否由用户指定由接口决定
     * @param request 请求
     * @return 所有的副实体类型, 及每个副实体类型下的操作类型
     */
    @GetMapping("/log/options")
    @Operation(summary = "日志选项", description = "列出该主实体类型(和主实体ID)下, 所有的副实体类型,及每个副实体类型下的操作类型")
    default Res<List<SubClassOption>> getLogOptions(
            @RequestParam(required = false, defaultValue = "false") @Parameter(description = "是否查询旧日志(默认false)") Boolean old,
            @RequestParam(required = false) @Parameter(description = "主实体ID") Long mainId,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        return Res.of(SpringContextUtils.getContext().getBean(OpLogService.class).options(mainClass(), mainId(mainId), old));
    }

    /**
     * 日志分页查询
     * @param old     是否查询旧日志
     * @param request 请求
     * @param param   查询参数
     * @return 日志
     */
    @GetMapping("/log/page")
    @Operation(summary = "日志分页查询")
    default ResPage<SystemOperationLogVo> getLogPage(
            @RequestParam(required = false, defaultValue = "false") @Parameter(description = "是否查询旧日志(默认false)") Boolean old,
            @ParameterObject OperationLogPageParam param,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        return SpringContextUtils.getContext().getBean(OpLogService.class).pageByParam(mainClass(), mainId(param.getMainId()), param, old);
    }
}

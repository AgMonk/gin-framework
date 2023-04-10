package bo;

import enums.OperationType;
import jakarta.servlet.http.HttpServletRequest;
import com.gin.common.utils.ParamArg;

import java.util.List;

/**
 * 操作日志上下文
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/20 12:39
 */
public record OperationLogContext(
        //被操作的实体的类型
        Class<?> entityClass,
        //被操作的实体ID
        Long entityId,
        //  方法参数和参数值
        List<ParamArg> paramArgs,
        //方法执行结果
        Object result,
        //执行方法之前计算的 Spring-EL 表达式 结果
        List<Object> preExp,
        //执行方法之前计算的 Spring-EL 表达式 结果
        List<Object> sufExp,
        //操作类型
        OperationType type,
        //请求
        HttpServletRequest request) {

}

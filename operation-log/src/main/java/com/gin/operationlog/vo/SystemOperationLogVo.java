package com.gin.operationlog.vo;

import com.gin.operationlog.entity.BaseOperationLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/23 13:58
 */
@Getter
@Setter
@Schema(description = "响应对象")
@NoArgsConstructor
@AllArgsConstructor
public class SystemOperationLogVo extends BaseOperationLog {
    @Schema(description = "操作人姓名")
    String userNickname;


    public SystemOperationLogVo(BaseOperationLog po) {
        BeanUtils.copyProperties(po, this);

    }

}
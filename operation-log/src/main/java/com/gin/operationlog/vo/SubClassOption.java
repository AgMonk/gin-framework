package com.gin.operationlog.vo;

import com.gin.common.vo.PageOption;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 副实体选项
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/23 16:32
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "副实体选项")
public class SubClassOption {
    @Schema(description = "出现次数")
    Integer count;
    @Schema(description = "副实体类型标签")
    String label;
    @Schema(description = "副实体类型值")
    String value;
    @Schema(description = "操作类型")
    List<PageOption> types;


}

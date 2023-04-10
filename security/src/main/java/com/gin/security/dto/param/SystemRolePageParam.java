package com.gin.security.dto.param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.database.dto.param.BasePageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author bx002
 */
@Getter
@Setter
@Schema(description = "角色分页查询参数")
public class SystemRolePageParam extends BasePageParam {
    @Schema(description = "关键字(名称,中文名称,描述,备注)")
    String key;
    @Schema(description = "排除的id(一般用于排除用户已持有的角色),多个id用逗号分隔")
    List<Long> excludedId;

    @Override
    public void handleQueryWrapper(QueryWrapper<?> queryWrapper) {
        queryWrapper.orderByDesc("id");
        if (!CollectionUtils.isEmpty(excludedId)) {
            queryWrapper.notIn("id", excludedId);
        }
        if (!ObjectUtils.isEmpty(key)) {
            queryWrapper
                    .eq("name", key).or()
                    .eq("name_zh", key).or()
                    .like("name", key).or()
                    .like("name_zh", key).or()
                    .like("description", key).or()
                    .like("remark", key).or()
            ;
        }
    }
}
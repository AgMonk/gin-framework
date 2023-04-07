package old.user.dto.param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.base.BasePageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;


/**
 * 权限
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/20 14:15
 */
@Getter
@Setter
@Schema(description = "权限分页查询参数")
public class SystemPermissionPageParam extends BasePageParam {
    @Schema(description = "关键字(路径,分组名称)")
    String key;
    @Schema(description = "排除的id(一般用于排除角色已持有的权限),多个id用逗号分隔")
    List<Long> excludedId;

    @Override
    public void handleQueryWrapper(QueryWrapper<?> queryWrapper) {
        queryWrapper.orderByDesc("id");

        if (!CollectionUtils.isEmpty(excludedId)) {
            queryWrapper.notIn("id", excludedId);
        }

        if (!ObjectUtils.isEmpty(key)) {
            queryWrapper
                    .eq("path", key).or()
                    .eq("group_name", key).or()
                    .like("path", key).or()
                    .like("group_name", key).or()
            ;
        }
    }
}
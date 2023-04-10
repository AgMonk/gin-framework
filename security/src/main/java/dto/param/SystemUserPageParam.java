package dto.param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import exception.BusinessException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import entity.SystemUserInfo;
import service.RelationUserRoleService;
import service.SystemUserInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import utils.SpringContextUtils;

import java.util.Collections;
import java.util.List;

/**
 * 用户分页查询参数
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/19 16:29
 */
@Getter
@Setter
@Schema(description = "用户分页查询参数")
public class SystemUserPageParam extends BasePageParam {
    @Schema(description = "昵称")
    String nickname;
    @Schema(description = "联系电话")
    String phone;
    @Schema(description = "持有的角色id")
    Long roleId;
    @Schema(description = "用户名")
    String username;


    @Override
    public void handleQueryWrapper(QueryWrapper<?> queryWrapper) {
        queryWrapper.orderByDesc("id");

        final RelationUserRoleService relationUserRoleService = SpringContextUtils.getContext().getBean(RelationUserRoleService.class);
        final SystemUserInfoService systemUserInfoService = SpringContextUtils.getContext().getBean(SystemUserInfoService.class);

        if (!ObjectUtils.isEmpty(username)) {
            queryWrapper.and(qw -> qw.eq("username", username).or().like("username", username));
        }

        if (roleId != null) {
            final List<Long> userId = relationUserRoleService.listUserIdByRoleId(Collections.singleton(roleId));
            if (userId.size() == 0) {
                throw BusinessException.of(HttpStatus.BAD_REQUEST, "未找到持有该角色的用户");
            }
            queryWrapper.in("id", userId);
        }

        if (!ObjectUtils.isEmpty(nickname)) {
            final QueryWrapper<SystemUserInfo> qw = new QueryWrapper<>();
            qw.eq("nickname", nickname).or().like("nickname", nickname).select("user_id");
            final List<Long> userId = systemUserInfoService.list(qw).stream().map(SystemUserInfo::getUserId).toList();
            if (userId.size() == 0) {
                throw BusinessException.of(HttpStatus.BAD_REQUEST, "未找到该昵称");
            }
            queryWrapper.in("id", userId);
        }

        if (!ObjectUtils.isEmpty(phone)) {
            final QueryWrapper<SystemUserInfo> qw = new QueryWrapper<>();
            qw.eq("phone", phone).or().like("phone", phone).select("user_id");
            final List<Long> userId = systemUserInfoService.list(qw).stream().map(SystemUserInfo::getUserId).toList();
            if (userId.size() == 0) {
                throw BusinessException.of(HttpStatus.BAD_REQUEST, "未找到该电话");
            }
            queryWrapper.in("id", userId);
        }
    }
}

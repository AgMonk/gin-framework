package vo;


import base.BaseVo;
import entity.SystemUserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * @author bx002
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户个人信息")
public class SystemUserInfoVo extends BaseVo {
    @Schema(description = "头像")
    String avatar;
    @Schema(description = "生日(UNIX秒)")
    Long birthday;
    @Schema(description = "昵称")
    String nickname;
    @Schema(description = "联系电话")
    String phone;
    @Schema(description = "用户id")
    Long userId;

    public SystemUserInfoVo(SystemUserInfo systemUserInfo) {
        BeanUtils.copyProperties(systemUserInfo, this);
    }
}
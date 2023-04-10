package dto.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import entity.SystemUserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import validation.Phone;

/**
 * @author bx002
 */
@Getter
@Setter
@Schema(description = "用户个人信息表单")
@Validated
public class SystemUserInfoForm {
    @Schema(description = "生日(UNIX秒)")
    Long birthday;
    @Schema(description = "昵称")
    @NotEmpty
    String nickname;
    @Schema(description = "联系电话")
    @Phone(nullable = true)
    String phone;

    public SystemUserInfo build(long userId) {
        final SystemUserInfo userInfo = new SystemUserInfo();
        BeanUtils.copyProperties(this, userInfo);
        userInfo.setUserId(userId);
        return userInfo;
    }
}
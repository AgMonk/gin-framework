package old.user.dto.form;

import com.gin.springboot3template.sys.validation.Phone;
import com.gin.springboot3template.user.entity.SystemUserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;

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
package com.gin.security.entity;

import com.gin.database.base.BaseAttach;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * 用户头像
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/26 16:51
 */
@Getter
@Setter
@TableName(value = SystemUserAvatar.TABLE_NAME, autoResultMap = true)
@Entity(name = SystemUserAvatar.TABLE_NAME)
@Schema(description = "用户头像")
public class SystemUserAvatar extends BaseAttach {
    public static final String TABLE_NAME = "t_system_entity_user_avatar";
    @Column(nullable = false, unique = true)
    @Comment("用户Id")
    @Schema(description = "用户Id")
    Long userId;
}
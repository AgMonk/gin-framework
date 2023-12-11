package com.gin.security.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gin.database.base.BasePo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * 系统用户绑定的手机
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/12/11 15:42
 **/
@Getter
@Setter
@TableName(value = SystemUserPhone.TABLE_NAME, autoResultMap = true)
@Entity(name = SystemUserPhone.TABLE_NAME)
@NoArgsConstructor
@Table
public class SystemUserPhone extends BasePo {
    protected static final String TABLE_NAME = "t_system_entity_user_phone";

    @Column(nullable = false, unique = true)
    @Comment("用户Id")
    Long userId;

    @Column(nullable = false, unique = true)
    @Comment("手机号")
    String phone;

}
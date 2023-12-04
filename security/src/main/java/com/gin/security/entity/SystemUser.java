package com.gin.security.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gin.database.base.BasePo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;


/**
 * 系统用户
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/6 14:30
 */
@Getter
@Setter
@TableName(value = SystemUser.TABLE_NAME, autoResultMap = true)
@Entity(name = SystemUser.TABLE_NAME)
public class SystemUser extends BasePo {
    protected static final String TABLE_NAME = "t_system_entity_user";
    @Column(length = 50, nullable = false, unique = true)
    @Comment("用户名")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    String username;
    @Column(nullable = false, length = 100)
    @Comment("密码")
    String password;
    @Column(nullable = false, columnDefinition = "int default 1")
    @Comment("账号未过期")
    Boolean accountNonExpired;
    @Column(nullable = false, columnDefinition = "int default 1")
    @Comment("账号未锁定")
    Boolean accountNonLocked;
    @Column(nullable = false, columnDefinition = "int default 1")
    @Comment("密码未过期")
    Boolean credentialsNonExpired;
    @Column(nullable = false, columnDefinition = "int default 1")
    @Comment("是否可用")
    Boolean enabled;


    @Column(length = 100, unique = true)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Comment("微信OpenId")
    String openId;
}

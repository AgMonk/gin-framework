package com.gin.security.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * RememberMe 功能数据库持久化 仅建表用
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/12/5 14:57
 **/
@Getter
@Setter
@TableName(value = PersistentLogins.TABLE_NAME, autoResultMap = true)
@Entity(name = PersistentLogins.TABLE_NAME)
public class PersistentLogins {
    public static final String TABLE_NAME = "persistent_logins";
    public static final int LENGTH = 64;

    @Column(length = LENGTH, nullable = false)
    String username;

    @Column(length = LENGTH, nullable = false)
    @Id
    String series;

    @Column(length = LENGTH, nullable = false)
    String token;

    @Column(nullable = false, columnDefinition = "timestamp")
    String last_used;
}

package com.gin.database.validation;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gin.spring.exception.BusinessException;
import com.gin.spring.validation.ValidatorUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import com.gin.spring.utils.SpringContextUtils;

import java.io.Serializable;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 16:24
 */
@Slf4j
public class EntityIdValidator implements ConstraintValidator<EntityId, Serializable> {
    private boolean nullable;
    private IService<?> service;

    private Class<?> clazz;

    @Override
    public void initialize(EntityId constraintAnnotation) {
        this.nullable = constraintAnnotation.nullable();
        this.clazz = constraintAnnotation.service();
        this.service = SpringContextUtils.getContext().getBean(constraintAnnotation.service());


    }

    @Override
    public boolean isValid(Serializable serializable, ConstraintValidatorContext constraintValidatorContext) {
        if (this.service == null) {
            throw BusinessException.of(HttpStatus.INTERNAL_SERVER_ERROR, "服务器错误", "未找到指定的service: " + clazz);
        }
        if (serializable == null || "".equals(serializable)) {
            if (nullable) {
                return true;
            }
            ValidatorUtils.changeMessage(constraintValidatorContext, "不允许为空");
            return false;
        }
        return service.getById(serializable) != null;
    }
}

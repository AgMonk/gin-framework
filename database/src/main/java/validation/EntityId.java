package validation;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * 校验实体类Id
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 16:22
 */

@Documented
@Target({PARAMETER, FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EntityIdValidator.class)
public @interface EntityId {

    String message() default "该ID不存在";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends IService<?>> service();

    boolean nullable() default false;
}

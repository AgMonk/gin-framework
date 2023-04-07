package validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;


/**
 * 电话校验器
 * @author bx002
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    public static final Pattern MOBILE_PATTERN = Pattern.compile("^1[3-9][0-9]{9}$");
    public static final Pattern TEL_PATTERN = Pattern.compile("^0\\d{2,3}-?\\d{7}$");
    public static final Pattern TEL_PATTERN_2 = Pattern.compile("^\\d{7}$");
    boolean nullable;

    @Override
    public void initialize(Phone phone) {
        this.nullable = phone.nullable();
    }

    @Override
    public boolean isValid(String obj, ConstraintValidatorContext context) {
        if (obj == null || "".equals(obj)) {
            if (nullable) {
                return true;
            }
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("电话不允许为空").addConstraintViolation();
            return false;
        }
        return MOBILE_PATTERN.matcher(obj).find() || TEL_PATTERN.matcher(obj).find() || TEL_PATTERN_2.matcher(obj).find();
    }
}

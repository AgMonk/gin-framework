package exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.FieldError;

import java.io.Serializable;

/**
 * 参数校验异常
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 15:41
 */
@Getter
@Setter
public class MyFieldError implements Serializable {
    String code;
    String field;
    String objectName;
    String defaultMessage;

    public MyFieldError(FieldError fieldError) {
        BeanUtils.copyProperties(fieldError,this);
    }

    public String getDescription(){
        return String.format("对象[%s]的字段[%s]校验失败:%s Msg:%s",objectName,field,code,defaultMessage);
    }
}

package exception;

import exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Collections;

/**
 * AuthorityEvaluator 重复注册异常
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 14:12
 */
public class AuthorityEvaluatorDuplicatedException extends BusinessException {
    public AuthorityEvaluatorDuplicatedException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "AuthorityEvaluator 重复注册", Collections.singletonList(message));
    }
}

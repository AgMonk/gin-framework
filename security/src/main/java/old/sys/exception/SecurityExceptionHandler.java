package old.sys.exception;

import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.sys.bo.ExpressionExceptionParser;
import com.gin.springboot3template.sys.vo.response.Res;
import com.gin.springboot3template.user.entity.SystemPermission;
import com.gin.springboot3template.user.service.SystemPermissionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 安全相关的 统一异常处理
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 11:16
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class SecurityExceptionHandler {
    private final SystemPermissionService systemPermissionService;

    @ExceptionHandler({CookieTheftException.class})
    public ResponseEntity<Res<Void>> exceptionHandler(CookieTheftException e) {
        return new ResponseEntity<>(Res.of(null, e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Res<String>> exceptionHandler(AccessDeniedException e, @SuppressWarnings("unused") HttpServletRequest request) {
        log.warn(e.getLocalizedMessage());
        final SystemPermission permission = systemPermissionService.getByPath(request.getRequestURI());
        final String message = permission == null ? null : new ExpressionExceptionParser(permission.getPreAuthorize(), request).explain();
        return new ResponseEntity<>(Res.of(message, Constant.Messages.ACCESS_DENIED), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<Res<String>> exceptionHandler(
            HttpMediaTypeNotSupportedException e,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        log.warn(e.getLocalizedMessage());
        return new ResponseEntity<>(Res.of("不允许的 Content-Type", e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

}

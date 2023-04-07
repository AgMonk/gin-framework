package old.sys.bo;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 尝试解释权限报错的原因
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/17 12:00
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpressionExceptionParser {
    String preAuthorize;
    @SuppressWarnings("unused")
    HttpServletRequest request;

    public String explain() {
        return this.preAuthorize = "需要: " + this.preAuthorize
                .replace("#request.requestURI", request.getRequestURI())
                .replace("hasAuthority", "持有权限")
                .replace("hasAnyAuthority", "持有任一权限")
                .replace("hasRole", "持有角色")
                .replace("hasAnyRole", "持有任一角色")
                .replace(" or ", " 或 ")
                .replace(" and ", " 且 ")
                ;
    }
}

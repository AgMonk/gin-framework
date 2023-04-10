package route.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import route.annotation.MenuPath;
import route.enums.MenuComponentType;

/**
 * menu组件
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/25 16:12
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class EleMenuComponent {
    @Schema(description = "排序序号")
    @JsonIgnore
    int order = 0;
    @Schema(description = "组件的title属性")
    String title;

    public EleMenuComponent(int order, String title) {
        this.order = order;
        this.title = title;
    }

    public EleMenuComponent(MenuPath menuPath) {
        this(menuPath.order(), menuPath.title());
    }

    /**
     * 该组件是否禁用
     * @return 该组件是否禁用
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    @Schema(description = "该组件是否禁用")
    abstract public boolean isDisabled();

    /**
     * 组件类型
     * @return 组件类型
     */
    abstract public MenuComponentType getType();
}

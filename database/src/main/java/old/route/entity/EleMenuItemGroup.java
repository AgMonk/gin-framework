package old.route.entity;

import com.gin.springboot3template.route.annotation.MenuPath;
import com.gin.springboot3template.route.base.EleMenuComponent;
import com.gin.springboot3template.route.base.HasChildren;
import com.gin.springboot3template.route.enums.MenuComponentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 路由导航组, 对应Element的MenuItemGroup组件
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/25 16:09
 */
@Schema(description = "MenuItemGroup组件")
@Getter
@Setter
@NoArgsConstructor
public class EleMenuItemGroup extends EleMenuComponent implements HasChildren<EleMenuComponent> {
    private final MenuComponentType type = MenuComponentType.menu_item_group;
    @Schema(description = "子SubMenu组件")
    List<EleMenuComponent> children;

    public EleMenuItemGroup(MenuPath menuPath) {
        super(menuPath);
    }

    /**
     * 该组件是否可用
     * @return 该组件是否可用
     */
    @Override
    public boolean isDisabled() {
        // children 为空直接禁用
        if (CollectionUtils.isEmpty(children)) {
            return false;
        }
        // children里有任意不禁用的组件 ，则不禁用
        return children.stream().anyMatch(i -> !i.isDisabled());
    }

    /**
     * 排序子组件
     */
    @Override
    public void sortChildren() {
        this.children.sort((o1, o2) -> o2.getOrder() - o1.getOrder());
    }
}

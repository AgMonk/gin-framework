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
import java.util.UUID;

/**
 * 路由导航项子菜单, 对应Element的SubMenu组件
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/25 16:01
 */
@Schema(description = "SubMenu组件")
@Getter
@Setter
@NoArgsConstructor
public class EleSubMenu extends EleMenuComponent implements HasChildren<EleMenuComponent> {
    private final MenuComponentType type = MenuComponentType.sub_menu;
    @Schema(description = "组件的index属性")
    String index;
    @Schema(description = "子组件,可能为SubMenu, SubMenu, MenuItemGroup")
    List<EleMenuComponent> children;

    public EleSubMenu(MenuPath menuPath) {
        super(menuPath);
        this.index = UUID.randomUUID().toString();
    }

    /**
     * 该组件是否禁用
     * @return 该组件是否禁用
     */
    @Override
    public boolean isDisabled() {
        // children 为空直接禁用
        if (CollectionUtils.isEmpty(children)) {
            return false;
        }
        // children里有任意不禁用的组件 ，则不禁用
        return children.stream().allMatch(EleMenuComponent::isDisabled);
    }

    /**
     * 排序子组件
     */
    @Override
    public void sortChildren() {
        // 降序排序
        this.children.sort((o1, o2) -> o2.getOrder() - o1.getOrder());
        // 对children中有children的对象也进行排序
        this.children.stream().filter(i -> i instanceof HasChildren<?>).forEach(i -> ((HasChildren<?>) i).sortChildren());
    }
}

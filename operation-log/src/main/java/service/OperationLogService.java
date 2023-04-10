package service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import entity.BaseOperationLog;
import jakarta.validation.constraints.NotNull;
import utils.reflect.ReflectUtils;
import vo.PageOption;
import vo.SubClassOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/23 15:01
 */
public interface OperationLogService<T extends BaseOperationLog> extends MyService<T> {
    /**
     * 列出该主实体类型(和主实体ID)下, 所有的副实体类型,及每个副实体类型下的操作类型
     * @param mainClass 主实体类型
     * @param mainId    主实体id
     * @return 副实体类型
     */
    default List<SubClassOption> options(@NotNull Class<?> mainClass, Long mainId) {
        final QueryWrapper<T> qw = new QueryWrapper<>();
        qw.eq("main_class", mainClass.getName());
        if (mainId != null) {
            qw.eq("main_id", mainId);
        }
        final ArrayList<SubClassOption> list = new ArrayList<>();
        final Map<? extends Class<?>, List<T>> map = countGroupBy(qw, "sub_class", "type").stream().collect(Collectors.groupingBy(i -> {
            final Class<?> subClass = i.getSubClass();
            return subClass == null ? mainClass : subClass;
        }));
        map.forEach((subClass, logs) -> {
            final SubClassOption option = new SubClassOption();
            list.add(option);
            option.setCount(logs.stream().mapToInt(BaseOperationLog::getCount).sum());
            if (mainClass.equals(subClass)) {
                option.setLabel("本对象");
                option.setValue(mainClass.getName());
            } else {
                option.setLabel(ReflectUtils.getAliasName(subClass));
                option.setValue(subClass.getName());
            }
            option.setTypes(PageOption.of(logs, i -> new PageOption(i.getCount(), i.getType().getName(), i.getType().name())));
        });

        return list;
    }

}

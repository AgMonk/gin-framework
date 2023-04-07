package utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 树形数据节点
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/23 15:19
 */
public interface TreeNode<T> {
    /**
     * 将一个列表数据整理成树形结构
     * @param list 数据
     * @return 数据
     */
    static <T extends TreeNode<T>> List<T> collate(List<T> list) {
        final ArrayList<T> data = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return data;
        }
        HashMap<Serializable, T> map = MapUtils.coll2Map(list, TreeNode::getNodeId);
        list.forEach(item -> {
            final Serializable parentNodeId = item.getParentNodeId();
            if (ObjectUtils.isEmpty(parentNodeId)) {
                // 父节点id为空 添加为根元素
                data.add(item);
            } else if (map.containsKey(parentNodeId)) {
                // 找到父节点,放入它的子节点列表
                final T parent = map.get(parentNodeId);
                List<T> children = parent.getChildrenNodeList();
                children = CollectionUtils.isEmpty(children) ? new ArrayList<>() : children;
                children.add(item);
                parent.setChildrenNodeList(children);
            }
        });
        return data;
    }

    /**
     * 本节点的唯一编号
     * @return 唯一编号
     */
    @JsonIgnore
    Serializable getNodeId();

    /**
     * 父节点的唯一编号
     * @return 唯一编号
     */
    @JsonIgnore
    Serializable getParentNodeId();

    /**
     * 子节点列表getter
     * @return 子节点列表getter
     */
    @JsonIgnore
    List<T> getChildrenNodeList();

    /**
     * 子节点列表setter
     * @param list 列表
     */
    void setChildrenNodeList(List<T> list);
}

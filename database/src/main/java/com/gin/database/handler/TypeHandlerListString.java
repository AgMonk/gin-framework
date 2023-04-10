package com.gin.database.handler;

import com.gin.common.exception.BusinessException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.http.HttpStatus;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 列表
 * @author bx002
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(List.class)
public class TypeHandlerListString extends BaseTypeHandler<List<String>> {

    public static final String DELIMITER = "\\|";

    @Override
    public List<String> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String data = resultSet.getString(s);
        return isEmpty(data) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(data.split(DELIMITER)));
    }

    @Override
    public List<String> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String data = resultSet.getString(i);
        return isEmpty(data) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(data.split(DELIMITER)));
    }

    @Override
    public List<String> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String data = callableStatement.getString(i);
        return isEmpty(data) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(data.split(DELIMITER)));
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<String> list, JdbcType jdbcType) throws SQLException {
        for (String item : list) {
            if (item.contains(DELIMITER)) {
                throw BusinessException.of(HttpStatus.BAD_REQUEST, "非法字符：" + DELIMITER);
            }
        }
        String s = String.join(DELIMITER, list);
        preparedStatement.setString(i, isEmpty(s) ? null : s);
    }

    private boolean isEmpty(String data) {
        return data == null || "".equals(data);
    }
}

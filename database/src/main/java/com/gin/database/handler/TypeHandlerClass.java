package com.gin.database.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 列表
 * @author bx002
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(Class.class)
public class TypeHandlerClass extends BaseTypeHandler<Class<?>> {


    @Override
    public Class<?> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String data = resultSet.getString(s);
        if (data == null) {
            return null;
        }
        try {
            return Class.forName(data);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Class<?> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String data = resultSet.getString(i);
        if (data == null) {
            return null;
        }
        try {
            return Class.forName(data);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Class<?> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String data = callableStatement.getString(i);
        if (data == null) {
            return null;
        }
        try {
            return Class.forName(data);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Class<?> clazz, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, clazz.getName());
    }

}

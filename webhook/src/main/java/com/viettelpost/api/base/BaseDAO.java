package com.viettelpost.api.base;

import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Transactional
public class BaseDAO {


    protected Connection getConnection(SessionFactory sf) {
        return ((SessionImpl) sf.getCurrentSession()).connection();
    }

    protected ResultSet getResultSet(SessionFactory em, String procedureNameNoParam, List<Object> params, int outType) throws Exception {
        CallableStatement statement = buildStatement(em, procedureNameNoParam, params, outType, 1);
        statement.setQueryTimeout(25000);
        int outIndex = 1;
        if (params != null) {
            outIndex = params.size() + 1;
        }
        statement.setFetchSize(10000);
        statement.execute();
        if (outType != 0) {
            ResultSet resultSet = (ResultSet) statement.getObject(outIndex);
            return resultSet;
        } else {
            return null;
        }
    }

//    protected CallableStatement createCallWithTwoResultset(SessionFactory em, String procedureNameNoParam, List<Object> params) throws Exception {
//        CallableStatement statement = buildStatement(em, procedureNameNoParam, params, OracleTypes.CURSOR, 2);
//        statement.setFetchSize(10000);
//        statement.execute();
//        return statement;
//    }


    private CallableStatement buildStatement(SessionFactory em, String procedureNameNoParam, List<Object> params, int outType, int count) throws Exception {
        Connection connection = getConnection(em);
        StringBuilder sql = new StringBuilder("{call ").append(procedureNameNoParam);
        sql.append("(");
        if (params != null) {
            int lenth = params.size() + count;
            if (outType == 0) {
                lenth--;
            }
            for (int i = 0; i < lenth; i++) {
                if (i == (lenth - 1)) {
                    sql.append("?");
                } else {
                    sql.append("?, ");

                }
            }
        } else {
            if (outType != 0) {
                sql.append("?");
                if (count > 1) {
                    sql.append(", ?");
                }
            }
        }
        sql.append(")}");
        CallableStatement statement = connection.prepareCall(sql.toString());
        int outIndex = 1;
        if (params != null) {
            outIndex = params.size() + 1;
            for (int i = 0; i < params.size(); i++) {
                int j = i + 1;
                Object b = params.get(i);
                if (b != null) {
                    if (b instanceof Date) {
                        statement.setDate(j, new java.sql.Date(((Date) b).getTime()));
                    } else {
                        if (b instanceof String) {
                            statement.setString(j, (String) b);
                        } else if (b instanceof Long) {
                            statement.setLong(j, (Long) b);
                        } else if (b instanceof Double) {
                            statement.setDouble(j, (Double) b);
                        } else {
                            statement.setInt(j, Integer.valueOf(b.toString()));
                        }
                    }
                } else {
                    statement.setObject(j, b);
                }
            }
        }
        if (outType != 0) {
            statement.registerOutParameter(outIndex, outType);
            if (count > 1) {
                statement.registerOutParameter(outIndex + 1, outType);
            }
        }
        return statement;
    }

    protected BigDecimal getBigDecimal(SessionFactory em, String procedureNameNoParam, List<Object> params, int outType) throws Exception {
        CallableStatement statement = buildStatement(em, procedureNameNoParam, params, outType, 1);
        int outIndex = 1;
        if (params != null) {
            outIndex = params.size() + 1;
        }
        statement.setFetchSize(10000);
        statement.execute();
        if (outType != 0) {
            BigDecimal result = (BigDecimal) statement.getObject(outIndex);
            return result;
        } else {
            return null;
        }
    }

//    protected List toJsonArray(ResultSet rs) throws Exception {
//        List jA = new ArrayList();
//        ResultSetMetaData metaData = rs.getMetaData();
//        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        int total = metaData.getColumnCount();
//
//        while (rs.next()) {
//            Map<String, Object> jO = new HashMap<>();
//            for (int i = 0; i < total; i++) {
//                buidObject(rs, metaData, jO, format, i);
//            }
//            jA.add(jO);
//        }
//        return jA;
//    }

//    protected Map<String, Object> toJsonObject(ResultSet rs) throws Exception {
//        ResultSetMetaData metaData = rs.getMetaData();
//        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        int total = metaData.getColumnCount();
//        if (rs.next()) {
//            Map<String, Object> jO = new HashMap<>();
//            for (int i = 0; i < total; i++) {
//                buidObject(rs, metaData, jO, format, i);
//            }
//            return jO;
//        }
//        return null;
//    }


//    private void buidObject(ResultSet rs, ResultSetMetaData metaData, Map<String, Object> jO, DateFormat format, int i) throws Exception {
//        String colName = metaData.getColumnName(i + 1);
//        if (metaData.getColumnType(i + 1) == OracleTypes.TIMESTAMP || metaData.getColumnType(i + 1) == OracleTypes.DATE) {
//            if (rs.getTimestamp(colName) == null) {
//                jO.put(colName, null);
//            } else {
//                jO.put(colName, format.format(rs.getTimestamp(colName)));
//            }
//        } else {
//            jO.put(colName, rs.getObject(colName));
//        }
//    }
}

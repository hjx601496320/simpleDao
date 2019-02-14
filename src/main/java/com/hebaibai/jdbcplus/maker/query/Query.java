package com.hebaibai.jdbcplus.maker.query;

import com.hebaibai.jdbcplus.maker.SqlMaker;

/**
 * 查询
 *
 * @author hjx
 */
public interface Query extends SqlMaker {

    /**
     * 排序
     *
     * @param orderBy
     * @param type:   ASC/DESC
     * @return
     */
    Query orderBy(String orderBy, String type);

    /**
     * limit
     *
     * @param line
     * @param num
     * @return
     */
    Query limit(int line, int num);

}

package com.hebaibai.jdbcplus.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author hjx
 */
@UtilityClass
public class StringUtils {

    public static final String SPACE = " ";

    public static final String BLANK = "";

    public static final String COMMA = ", ";

    /**
     * 将字段添加``，防止因为sql字段是关键字造成的操作失败
     *
     * @param column
     * @return
     */
    public static String sqlColumn(String column) {
        return "`" + column + "`";
    }

    /**
     * 重复字符串
     *
     * @param str
     * @param number
     * @return
     */
    public static String[] repeat(String str, int number) {
        Assert.notNull(str);
        String[] strings = new String[number];
        for (int i = 0; i < number; i++) {
            strings[i] = str;
        }
        return strings;
    }

    /**
     * 组合字符串
     *
     * @param strings
     * @return
     */
    public static String append(final Object... strings) {
        StringBuilder builder = new StringBuilder();
        for (Object s1 : strings) {
            if (s1 == null) {
                continue;
            }
            builder.append(s1.toString());
        }
        return builder.toString();
    }

    /**
     * 组合字符串
     *
     * @param collection
     * @param separator
     * @return
     */
    public static String join(Collection collection, String separator) {
        StringBuffer var2 = new StringBuffer();
        for (Iterator var3 = collection.iterator(); var3.hasNext(); var2.append((String) var3.next())) {
            if (var2.length() != 0) {
                var2.append(separator);
            }
        }
        return var2.toString();
    }

    /**
     * 字母小写
     *
     * @param str
     * @return
     */
    public String lowCase(String str, int index) {
        char[] ch = str.toCharArray();
        if (ch[index] >= 'A' && ch[index] <= 'Z') {
            ch[index] = (char) (ch[index] + 32);
        }
        return new String(ch);
    }
}

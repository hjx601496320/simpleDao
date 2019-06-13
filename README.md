# JdbcPlus

```$xslt
简单的数据库操作.
支持mysql.
依赖spring-jdbc.
```
## 配置:

### 配置JdbcPlus

```java
 JdbcPlus jdbcPlus = new JdbcPlus();
 JdbcPlus.setJdbcTemplate(jdbcTemplate);
```

### 建表语句

```sql
create table user
(
  id          int auto_increment
  comment '用户id'
    primary key,
  name        varchar(225) null
  comment '用户名',
  create_date datetime     null,
  status      int          null,
  age         int          null
  comment '年龄',
  mark        varchar(225) null,
  big         decimal      null
)
  comment '用户表';
```



### 在class上添加注解

```java
package com.hebaibai.jdbcplus.entity;

import lombok.Getter;
import lombok.Setter;

import com.hebaibai.jdbcplus.Column;
import com.hebaibai.jdbcplus.Id;
import com.hebaibai.jdbcplus.FK;
import com.hebaibai.jdbcplus.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户表
 *
 * @author hejiaxuan
 */
@Getter
@Setter
@Table("user")
public class User {

    /**
     * big
     */
    @Column("big")
    private BigDecimal big;

    /**
     * 用户名
     */
    @Column("name")
    private String name;

    /**
     * 用户id
     */
    @Id
    @Column("id")
    private int id;

    /**
     * 年龄
     */
    @Column("age")
    private int age;

    /**
     * mark
     */
    @Column("mark")
    private String mark;


    /**
     * create_date
     */
    @Column("create_date")
    private Date createDate;

    /**
     * status
     */
    @Column("status")
    private int status;

    @Override
    public String toString() {
        return "User{" +
                "big=" + big +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", age=" + age +
                ", mark='" + mark + '\'' +
                ", parentId=" + parentId +
                ", createDate=" + createDate +
                ", status=" + status +
                '}';
    }
}

```

## 基本操作

操作对象 **User.class**。

**User.class**需要添加注解**@Table("user")**，注解值为数据库表名称。

属性上添加**@Column("anInt")** 注解，表示为一个数据库字段，注解值为数据库字段名称。

表示**id**的属性上需要添加**@Id**注解，每个表只支持一个**id**。

### 插入数据

```java
//插入单条记录
jdbcPlus.insert(User user);
//插入多条数据(拼接sql形式)
jdbcPlus.insertBatch(User.class, list);
```

### 查询数据

#### 普通查询

```java
//查出所有的User
jdbcPlus.select(User.class)
//查出id为12的数据
jdbcPlus.selectById(User.class, "12");
//查出所有的user_name 等于 "123" 的数据,user_name 为数据库字段名称
jdbcPlus.selectBy(User.class, "user_name", "123");
//查出所有的name 等于 "123" 并且 age 等于"18" 的数据
//查询条件中的 参数可以是添加了 @Column 的属性名称 此处 user_name 等效与 name
jdbcPlus.selectBy( User.class, "name", "123", "age", "18" );
```

#### 执行sql查询

```java
//将查询结果绑定在Foo.class上
List<Foo> foos = jdbcPlus.selectBySql("select * from user limit 1, 2", Foo.class);
```



### 删除数据

```java
//根据id删除数据
Integer deleteById = jdbcPlus.deleteById(User.class, "123");
//删除属性age是19的数据
Integer deleteBy = jdbcPlus.deleteBy(User.class, "age", "19");
//删除age是19，user_name是hebaibai的数据
Integer deleteBy = jdbcPlus.deleteBy(
    User.class,
    "age", "19",
    "user_name", "hebaibai"
);
```

### 更新数据

```java
//根据id更新数据，不忽略属性中的null进行更新
User user = new User();
user.setMark("markUpdate");
user.setId(new Random().nextInt(100));
Integer integer = jdbcPlus.updateById(User.class, user);
//根据id更新数据，忽略属性中的null进行更新
User user = new User();
user.setMark("markUpdate");
user.setId(new Random().nextInt(100));
Integer integer = jdbcPlus.updateById(User.class, user, true);
```






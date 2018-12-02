package com.sherlocky.springboot2.mybatis.dao;

import com.sherlocky.springboot2.mybatis.model.UserModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>使用注解形式配置Mappe</p>
 * 注解的一些解释:
 * <li>@Select 是查询类的注解，所有的查询均使用这个</li>
 * <li>@Result 修饰返回的结果集，关联实体类属性和数据库字段一一对应，如果实体类* 属性和数据库属性名保持一致，就不需要这个属性来修饰。</li>
 * <li>@Insert 插入数据库使用，直接传入实体类会自动解析属性到对应的值</li>
 * <li>@Update 负责修改，也可以直接传入对象</li>
 * <li>@delete 负责删除</li>
 * <br>
 * <p>注意：启用注解配置，需要将 application 配置文件中的 【mybatis.mapper-locations】属性注释掉掉再启动项目，否则会报已存在的错。</p>
 *
 * @author: zhangcx
 * @date: 2018/12/2 16:11
 */
@Mapper
public interface UserDAO {
    // 如果实例对象中的属性名和数据表中字段名不一致，可以用 @Result注解进行说明映射关系
    @Select("SELECT * FROM t_user")
    @Results({
            @Result(property = "name", column = "name"),
            @Result(property = "phone",  column = "phone"),
            @Result(property = "password",column = "password")
    })
    List<UserModel> list();

    @Select("SELECT * FROM t_user WHERE id = #{id}")
    @Results({
            @Result(property = "name", column = "name"),
            @Result(property = "phone",  column = "phone"),
            @Result(property = "password",column = "password")
    })
    UserModel get(Long id);

    @Insert({"INSERT INTO t_user(name,phone,password}) VALUES(#{name}, #{phone}, #{password})"})
    Long create(UserModel user);

    @Update("UPDATE t_user SET name=#{name} WHERE id =#{id}")
    Long update(UserModel user);

    @Delete("DELETE FROM t_user WHERE id=#{id}")
    Long remove(Long id);
}

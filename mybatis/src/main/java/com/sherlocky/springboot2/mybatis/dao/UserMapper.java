package com.sherlocky.springboot2.mybatis.dao;

import com.sherlocky.springboot2.mybatis.model.UserModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: zhangcx
 * @date: 2018/12/2 14:09
 */
/**
 * 此处如果不使用 @Mapper 注解，则需要在启动类上添加 @MapperScan("com.sherlocky.springboot2.*.dao")
 */
@Mapper
public interface UserMapper {
    Long create(UserModel user);

    UserModel get(Long id);

    List<UserModel> list();

    Long update(UserModel user);

    Long remove(Long id);

    // TODO 目前插入、更新、删除 返回的都是影响结果行，能否返回对应数据或主键
}

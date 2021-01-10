package com.shiro.shiro.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
@Mapper
public interface BuildingDao {
    int add(Map<String, Object> params);

    List<Map<String, Object>> query();

    int update(Map<String, Object> params);

    int delete(@Param("id") String id);
}

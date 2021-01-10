package com.shiro.shiro.service.impl;

import com.shiro.shiro.dao.BuildingDao;
import com.shiro.shiro.service.BuildingService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
@Service
public class BuidlingServiceImpl implements BuildingService {
    @Autowired
    private BuildingDao buildingDao;

    @RequiresPermissions("building:add")
    @Transactional
    @Override
    public void add(Map<String, Object> params) {
        int num = buildingDao.add(params);
        if(num == 0)
            throw new RuntimeException("新增失败");
    }

    @RequiresPermissions("building:query")
    @Override
    public List<Map<String, Object>> query() {
        List<Map<String, Object>> list = buildingDao.query();
        return list;
    }

    @Transactional
    @RequiresPermissions("building:update")
    @Override
    public void update(Map<String, Object> params) {
        int num = buildingDao.update(params);
        if(num == 0)
            throw new RuntimeException("修改失败");
    }

    @Transactional
    @Override
    @RequiresPermissions("building:delete")
    public void delete(String id) {
        int num = buildingDao.delete(id);
        if(num == 0)
            throw new RuntimeException("要删除的楼宇不存在");
    }
}

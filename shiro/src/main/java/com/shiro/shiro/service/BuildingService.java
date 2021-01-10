package com.shiro.shiro.service;

import java.util.List;
import java.util.Map;

public interface BuildingService {
    void add(Map<String, Object> params);

    List<Map<String, Object>> query();

    void update(Map<String, Object> params);

    void delete(String id);
}

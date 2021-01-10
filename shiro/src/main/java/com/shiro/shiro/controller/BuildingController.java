package com.shiro.shiro.controller;

import com.shiro.shiro.returnData.ReturnData;
import com.shiro.shiro.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 *   此类是为了测试shiro框架授权用的
 */

@Controller
@ResponseBody
@RequestMapping("/building")
public class BuildingController {
    @Autowired
    private BuildingService buildingService;

    /**
     * 新增楼宇
     * @param params
     * @return
     */
    @RequestMapping("add")
    public ReturnData add(@RequestBody Map<String, Object> params) {
        buildingService.add(params);
        return new ReturnData("新增成功");
    }

    /**
     * 查询楼宇列表
     */
    @PostMapping("query")
    public ReturnData query() {
        List<Map<String, Object>> list = buildingService.query();
        return new ReturnData(list);
    }

    /**
     * 修改楼宇
     * @Params {"id":"", "buildingName":""}
     */
    @PostMapping("update")
    public ReturnData update(@RequestBody Map<String, Object> params) {
        buildingService.update(params);
        return new ReturnData("修改成功");
    }

    /**
     * 删除楼宇列表
     * @Params {"id":""}
     */
    @PostMapping("delete")
    public ReturnData delete (String id) {
        buildingService.delete(id);
        return new ReturnData("删除成功");
    }
}

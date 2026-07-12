package com.huashui.dormitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.api.client.user.UserClient;
import com.huashui.common.domain.dto.UserSimpleInfo;
import com.huashui.dormitory.domain.pojo.DormBuildingManager;
import com.huashui.dormitory.mapper.DormBuildingManagerMapper;
import com.huashui.dormitory.service.IDormBuildingManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 楼栋-宿管关联Service实现
 */
@Service
public class DormBuildingManagerServiceImpl extends ServiceImpl<DormBuildingManagerMapper, DormBuildingManager> implements IDormBuildingManagerService {


    @Autowired
    public UserClient userClient;

    //获取楼栋 map<buildingId  managerName>
    public Map<Long ,String> getManagerNameMap(){
        List<DormBuildingManager> buildingList = list();
        List<Long> userIds = buildingList.stream().map(DormBuildingManager::getUserId).toList();
        Map<Long, String> collect = userClient.getUserInfoList(userIds).stream().collect(Collectors.toMap(
                UserSimpleInfo::getId,
                UserSimpleInfo::getRealName
        ));

        Map<Long ,String> managerNameMap = new HashMap<>();
        for (DormBuildingManager dormBuildingManager : buildingList) {
            managerNameMap.put(dormBuildingManager.getBuildingId(),collect.get(dormBuildingManager.getUserId()));
        }
        return managerNameMap;

    }



}
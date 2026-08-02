package com.huashui.dormitory.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.enums.Status;
import com.huashui.common.exception.BusinessException;
import com.huashui.common.response.PageResult;
import com.huashui.dormitory.Enum.BedStatus;
import com.huashui.dormitory.Enum.RoomStatus;
import com.huashui.dormitory.Enum.RoomType;
import com.huashui.dormitory.domain.dto.RoomBatchCreateDTO;
import com.huashui.dormitory.domain.dto.RoomCreateDTO;
import com.huashui.dormitory.domain.dto.RoomPageDTO;
import com.huashui.dormitory.domain.dto.RoomUpdateDTO;
import com.huashui.dormitory.domain.pojo.DormBed;
import com.huashui.dormitory.domain.pojo.DormBuilding;
import com.huashui.dormitory.domain.pojo.DormRoom;
import com.huashui.dormitory.mapper.DormBedMapper;
import com.huashui.dormitory.mapper.DormRoomMapper;
import com.huashui.dormitory.service.DormRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DormRoomServiceImpl extends ServiceImpl<DormRoomMapper, DormRoom> implements DormRoomService {

    private final DormBedMapper bedMapper;



    //新增房间 自动生成床位
    @Override
    @Transactional
    public void create(RoomCreateDTO dto) {
        DormRoom room = BeanUtil.copyProperties(dto, DormRoom.class);
        room.setOccupiedBeds(0);
        room.setStatus(RoomStatus.EMPTY);
        save(room);
        // 自动生成空床位 A/B/C/D...
        char[] labels = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        for (int i = 0; i < dto.getTotalBeds() && i < labels.length; i++) {
            DormBed bed = new DormBed();
            bed.setRoomId(room.getId());
            bed.setBedNumber(String.valueOf(labels[i]));
            bed.setStatus(BedStatus.FREE); //默认空闲
            bedMapper.insert(bed);
        }
    }

    //更新房间信息
    @Override
    @Transactional
    public void update(Long id, RoomUpdateDTO dto) {
        DormRoom room = getById(id);
        if (room == null) throw new BusinessException("房间不存在");
        BeanUtil.copyProperties(dto, room, "id");
        updateById(room);
    }

    //删除房间
    @Override
    @Transactional
    public void deleteById(Long id) {
        DormRoom room = getById(id);
        if (room == null) throw new BusinessException("房间不存在");
        if (room.getOccupiedBeds() > 0) throw new BusinessException("房间有人入住，无法删除");
        bedMapper.delete(new LambdaQueryWrapper<DormBed>().eq(DormBed::getRoomId, id));
        removeById(id); // todo 把房间的状态改为封闭

    }

    //查询房间的详细信息
    @Override
    public DormRoom getDetail(Long id) {
        DormRoom room = getById(id);
        if (room == null) throw new BusinessException("房间不存在");
        return room;
    }

    @Override
    @Transactional
    public void batchCreate(RoomBatchCreateDTO dto) {
        for (int floor = dto.getStartFloor(); floor <= dto.getEndFloor(); floor++) {

            // todo 使用异步线程池 使用saveBeanch()批量保存
            for (int r = 1; r <= dto.getRoomsPerFloor(); r++) {
                String roomNo = floor + String.format("%02d", r);
                DormRoom room = new DormRoom();
                room.setBuildingId(dto.getBuildingId());
                room.setRoomNumber(roomNo);
                room.setFloorNumber(floor);
                room.setRoomType(RoomType.FOUR);
                room.setTotalBeds(4);
                room.setOccupiedBeds(0);
                room.setStatus(RoomStatus.EMPTY);
                save(room);

                for (char label : new char[]{'A', 'B', 'C', 'D'}) {
                    DormBed bed = new DormBed();
                    bed.setRoomId(room.getId());
                    bed.setBedNumber(String.valueOf(label));
                    bed.setStatus(BedStatus.FREE);
                    bedMapper.insert(bed);
                }
            }
        }
    }

    @Override
    public void updateBeds(Long roomId, String bedConfig) {
        // TODO: 解析床位配置 JSON，批量更新 dorm_bed
    }

    @Override
    public void exportData(Long buildingId, Integer floorNumber, String status) {
        // TODO: EasyExcel 导出房间数据
    }

    @Override
    public PageResult<DormRoom> getRoomPage(RoomPageDTO dto) {
        // todo 使用缓存
        return null;
    }
}
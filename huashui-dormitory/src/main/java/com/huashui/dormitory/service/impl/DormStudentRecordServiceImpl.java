package com.huashui.dormitory.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.enums.Status;
import com.huashui.common.exception.BusinessException;
import com.huashui.dormitory.Enum.BedStatus;
import com.huashui.dormitory.Enum.RoomStatus;
import com.huashui.dormitory.domain.dto.RecordAdjustDTO;
import com.huashui.dormitory.domain.dto.RecordAssignDTO;
import com.huashui.dormitory.domain.pojo.DormBed;
import com.huashui.dormitory.domain.pojo.DormRoom;
import com.huashui.dormitory.domain.pojo.DormStudentRecord;
import com.huashui.dormitory.mapper.DormBedMapper;
import com.huashui.dormitory.mapper.DormRoomMapper;
import com.huashui.dormitory.mapper.DormStudentRecordMapper;
import com.huashui.dormitory.service.DormStudentRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class DormStudentRecordServiceImpl
        extends ServiceImpl<DormStudentRecordMapper, DormStudentRecord>
        implements DormStudentRecordService {

    private final DormBedMapper bedMapper;
    private final DormRoomMapper roomMapper;

    @Override
    public Page<DormStudentRecord> page(Integer page, Integer size, Long buildingId, Long studentId) {
        LambdaQueryWrapper<DormStudentRecord> qw = new LambdaQueryWrapper<>();
        qw.eq(DormStudentRecord::getStatus, Status.ENABLED);
        if (buildingId != null) qw.eq(DormStudentRecord::getBuildingId, buildingId);
        if (studentId != null) qw.eq(DormStudentRecord::getStudentId, studentId);
        qw.orderByDesc(DormStudentRecord::getCheckInTime);
        return this.page(new Page<>(page, size), qw);
    }

    @Override
    @Transactional
    public void assign(RecordAssignDTO dto) {
        // 校验房间状态
        DormRoom room = roomMapper.selectById(dto.getRoomId());
        if (room == null) throw new BusinessException("房间不存在");
        if ("LOCKED".equals(room.getStatus())) throw new BusinessException("房间已封闭，无法入住");
        if ("FULL".equals(room.getStatus())) throw new BusinessException("房间已住满");

        // 自动分配或指定床位
        DormBed bed;
        if (dto.getBedId() != null) {
            bed = bedMapper.selectById(dto.getBedId());
            if (bed == null || bed.getStudentId() != null) throw new BusinessException("床位不可用");
        } else {
            bed = bedMapper.selectOne(new LambdaQueryWrapper<DormBed>()
                    .eq(DormBed::getRoomId, dto.getRoomId()).eq(DormBed::getStatus, 0).last("LIMIT 1"));
            if (bed == null) throw new BusinessException("无空闲床位");
        }

        // 更新床位
        bed.setStudentId(dto.getStudentId());
        bed.setStatus(BedStatus.FREE);
        bedMapper.updateById(bed);

        // 创建住宿记录
        DormStudentRecord record = new DormStudentRecord();
        record.setStudentId(dto.getStudentId());
        record.setBuildingId(room.getBuildingId());
        record.setRoomId(dto.getRoomId());
        record.setBedId(bed.getId());
        record.setCheckInTime(LocalDateTime.now());
        save(record);

        // 更新房间入住人数
        room.setOccupiedBeds(room.getOccupiedBeds() + 1);
        if (room.getOccupiedBeds() >= room.getTotalBeds()) room.setStatus(RoomStatus.FULL);
        else if (room.getOccupiedBeds() > 0) room.setStatus(RoomStatus.NORMAL);
        roomMapper.updateById(room);
    }

    @Override
    @Transactional
    public void adjust(RecordAdjustDTO dto) {
        // 先退原床位
        DormStudentRecord oldRecord = getByStudentId(dto.getStudentId());
        if (oldRecord == null) throw new BusinessException("该学生无住宿记录");

        DormBed oldBed = bedMapper.selectById(oldRecord.getBedId());
        if (oldBed != null) {
            oldBed.setStudentId(null);
            oldBed.setStatus(BedStatus.FREE);
            bedMapper.updateById(oldBed);
        }

        DormRoom oldRoom = roomMapper.selectById(oldRecord.getRoomId());
        if (oldRoom != null) {
            oldRoom.setOccupiedBeds(oldRoom.getOccupiedBeds() - 1);
            roomMapper.updateById(oldRoom);
        }


        oldRecord.setCheckOutTime(LocalDateTime.now());
        updateById(oldRecord);

        // 分配新床位
        RecordAssignDTO assignDTO = new RecordAssignDTO();
        assignDTO.setStudentId(dto.getStudentId());
        assignDTO.setRoomId(dto.getNewRoomId());
        assignDTO.setBedId(dto.getNewBedId());
        assign(assignDTO);
    }

    @Override
    @Transactional
    public void checkout(Long studentId) {
        DormStudentRecord record = getByStudentId(studentId);
        if (record == null) throw new BusinessException("该学生无住宿记录");

        DormBed bed = bedMapper.selectById(record.getBedId());
        if (bed != null) {
            bed.setStudentId(null);
            bed.setStatus(BedStatus.FREE);
            bedMapper.updateById(bed);
        }

        DormRoom room = roomMapper.selectById(record.getRoomId());
        if (room != null) {
            room.setOccupiedBeds(room.getOccupiedBeds() - 1);
            roomMapper.updateById(room);
        }


        record.setCheckOutTime(LocalDateTime.now());
        updateById(record);
    }

    @Override
    public DormStudentRecord getByStudentId(Long studentId) {
        return getOne(new LambdaQueryWrapper<DormStudentRecord>()
                .eq(DormStudentRecord::getStudentId, studentId)
                .eq(DormStudentRecord::getStatus, Status.ENABLED));
    }

    @Override
    public void importRecords(String fileUrl) {
        // TODO: EasyExcel 导入
    }

    @Override
    public void exportRecords(Long buildingId) {
        // TODO: EasyExcel 导出
    }
}
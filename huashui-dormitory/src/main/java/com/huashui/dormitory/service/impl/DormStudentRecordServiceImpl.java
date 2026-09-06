package com.huashui.dormitory.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.exception.BusinessException;
import com.huashui.api.client.user.UserClient;
import com.huashui.common.utils.SemesterUtil;
import com.huashui.dormitory.Enum.BedStatus;
import com.huashui.dormitory.Enum.DormRecordStatus;
import com.huashui.dormitory.Enum.RoomStatus;
import com.huashui.dormitory.domain.dto.DormRecordPageDTO;
import com.huashui.dormitory.domain.dto.RecordAdjustDTO;
import com.huashui.dormitory.domain.dto.RecordAssignDTO;
import com.huashui.dormitory.domain.pojo.DormBed;
import com.huashui.dormitory.domain.pojo.DormBuilding;
import com.huashui.dormitory.domain.pojo.DormRoom;
import com.huashui.dormitory.domain.pojo.DormStudentRecord;
import com.huashui.dormitory.domain.pojo.SysCampus;
import com.huashui.dormitory.domain.vo.DormStudentRecordPageVO;
import com.huashui.dormitory.mapper.DormBedMapper;
import com.huashui.dormitory.mapper.DormBuildingMapper;
import com.huashui.dormitory.mapper.DormRoomMapper;
import com.huashui.dormitory.mapper.DormStudentRecordMapper;
import com.huashui.dormitory.mapper.SysCampusMapper;
import com.huashui.dormitory.service.DormStudentRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DormStudentRecordServiceImpl
        extends ServiceImpl<DormStudentRecordMapper, DormStudentRecord>
        implements DormStudentRecordService {

    private final DormBedMapper bedMapper;
    private final DormRoomMapper roomMapper;
    private final DormBuildingMapper buildingMapper;
    private final SysCampusMapper campusMapper;
    private final UserClient userClient;

    @Override
    public Page<DormStudentRecordPageVO> page(DormRecordPageDTO dto) {
        LambdaQueryWrapper<DormStudentRecord> qw = new LambdaQueryWrapper<>();
        qw.eq(DormStudentRecord::getStatus, DormRecordStatus.LIVING);
        if (StrUtil.isNotBlank(dto.getSemester())) {
            LocalDateTime[] semesterRange = getSemesterRange(dto.getSemester());
            if (semesterRange == null) return emptyPage(dto);
            qw.between(DormStudentRecord::getCheckInTime, semesterRange[0], semesterRange[1]);
        }
        boolean hasBuildingCondition = false;
        if (StrUtil.isNotBlank(dto.getStudentName())) {
            List<Long> studentIds = userClient.listUserInfoByRealName(dto.getStudentName()).stream()
                    .map(item -> item.getId())
                    .filter(Objects::nonNull)
                    .toList();
            if (studentIds.isEmpty()) return emptyPage(dto);
            qw.in(DormStudentRecord::getStudentId, studentIds);
        }

        LambdaQueryWrapper<DormBuilding> buildingQw = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(dto.getBuildingName())) {
            buildingQw.like(DormBuilding::getBuildingName, dto.getBuildingName());
            hasBuildingCondition = true;
        }
        if (StrUtil.isNotBlank(dto.getCampusName())) {
            List<Long> campusIds = campusMapper.selectList(new LambdaQueryWrapper<SysCampus>()
                            .like(SysCampus::getCampusName, dto.getCampusName())).stream()
                    .map(SysCampus::getId)
                    .filter(Objects::nonNull)
                    .toList();
            if (campusIds.isEmpty()) return emptyPage(dto);
            buildingQw.in(DormBuilding::getCampusId, campusIds);
        }
        if (hasBuildingCondition) {
            List<Long> buildingIds = buildingMapper.selectList(buildingQw).stream()
                    .map(DormBuilding::getId)
                    .filter(Objects::nonNull)
                    .toList();
            if (buildingIds.isEmpty()) return emptyPage(dto);
            qw.in(DormStudentRecord::getBuildingId, buildingIds);
        }

        qw.orderByDesc(DormStudentRecord::getCheckInTime);
        Page<DormStudentRecord> recordPage = this.page(dto.toPage(), qw);
        return convertPage(recordPage);
    }

    private Page<DormStudentRecordPageVO> emptyPage(DormRecordPageDTO dto) {
        Page<DormStudentRecordPageVO> page = dto.toPage();
        page.setRecords(Collections.emptyList());
        page.setTotal(0);
        return page;
    }

    private Page<DormStudentRecordPageVO> convertPage(Page<DormStudentRecord> recordPage) {
        List<DormStudentRecord> records = recordPage.getRecords();
        Page<DormStudentRecordPageVO> result = new Page<>(recordPage.getCurrent(), recordPage.getSize(), recordPage.getTotal());
        if (records.isEmpty()) {
            result.setRecords(Collections.emptyList());
            return result;
        }

        List<Long> studentIds = records.stream().map(DormStudentRecord::getStudentId).filter(Objects::nonNull).distinct().toList();
        List<Long> buildingIds = records.stream().map(DormStudentRecord::getBuildingId).filter(Objects::nonNull).distinct().toList();
        List<Long> roomIds = records.stream().map(DormStudentRecord::getRoomId).filter(Objects::nonNull).distinct().toList();
        List<Long> bedIds = records.stream().map(DormStudentRecord::getBedId).filter(Objects::nonNull).distinct().toList();

        Map<Long, String> studentMap = userClient.getUserInfoList(studentIds).stream()
                .collect(Collectors.toMap(item -> item.getId(), item -> item.getRealName(), (a, b) -> a));
        Map<Long, DormBuilding> buildingMap = buildingIds.isEmpty() ? Collections.emptyMap()
                : buildingMapper.selectByIds(buildingIds).stream()
                .collect(Collectors.toMap(DormBuilding::getId, Function.identity(), (a, b) -> a));
        List<Long> campusIds = buildingMap.values().stream()
                .map(DormBuilding::getCampusId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        Map<Long, String> campusMap = campusIds.isEmpty() ? Collections.emptyMap()
                : campusMapper.selectByIds(campusIds).stream()
                .collect(Collectors.toMap(SysCampus::getId, SysCampus::getCampusName, (a, b) -> a));
        Map<Long, String> roomMap = roomIds.isEmpty() ? Collections.emptyMap()
                : roomMapper.selectByIds(roomIds).stream()
                .collect(Collectors.toMap(DormRoom::getId, DormRoom::getRoomNumber, (a, b) -> a));
        Map<Long, String> bedMap = bedIds.isEmpty() ? Collections.emptyMap()
                : bedMapper.selectByIds(bedIds).stream()
                .collect(Collectors.toMap(DormBed::getId, DormBed::getBedNumber, (a, b) -> a));

        List<DormStudentRecordPageVO> vos = records.stream().map(record -> {
            DormStudentRecordPageVO vo = new DormStudentRecordPageVO();
            vo.setStudentName(studentMap.get(record.getStudentId()));
            DormBuilding building = buildingMap.get(record.getBuildingId());
            vo.setBuildingName(building == null ? null : building.getBuildingName());
            vo.setCampusName(building == null ? null : campusMap.get(building.getCampusId()));
            vo.setRoomNumber(roomMap.get(record.getRoomId()));
            vo.setBedNumber(bedMap.get(record.getBedId()));
            vo.setSemester(record.getCheckInTime() == null ? null : SemesterUtil.getSemester(record.getCheckInTime().toLocalDate()));
            vo.setCheckInTime(record.getCheckInTime());
            vo.setCheckOutTime(record.getCheckOutTime());
            vo.setStatus(getStatusDesc(record.getStatus()));
            return vo;
        }).toList();
        result.setRecords(vos);
        return result;
    }

    private LocalDateTime[] getSemesterRange(String semester) {
        String[] parts = semester.split("_");
        if (parts.length != 2) return null;

        try {
            int year = Integer.parseInt(parts[0]);
            int term = Integer.parseInt(parts[1]);
            if (term == 1) {
                return new LocalDateTime[]{LocalDateTime.of(year, 2, 1, 0, 0), LocalDateTime.of(year, 7, 31, 23, 59, 59)};
            }
            if (term == 2) {
                return new LocalDateTime[]{LocalDateTime.of(year, 8, 1, 0, 0), LocalDateTime.of(year + 1, 1, 31, 23, 59, 59)};
            }
            return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String getStatusDesc(DormRecordStatus status) {
        if (status == null) return null;
        for (DormRecordStatus item : DormRecordStatus.values()) {
            if (item.getCode().equals(status.getCode())) return item.getDesc();
        }
        return null;
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
        DormBuilding building = buildingMapper.selectById(room.getBuildingId());
        record.setCampusId(building == null ? null : building.getCampusId());
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
                .eq(DormStudentRecord::getStatus, DormRecordStatus.LIVING));
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

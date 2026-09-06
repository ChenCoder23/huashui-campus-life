package com.huashui.dormitory.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.dormitory.domain.dto.DormRecordPageDTO;
import com.huashui.dormitory.domain.dto.RecordAdjustDTO;
import com.huashui.dormitory.domain.dto.RecordAssignDTO;
import com.huashui.dormitory.domain.pojo.DormStudentRecord;
import com.huashui.dormitory.domain.vo.DormStudentRecordPageVO;

public interface DormStudentRecordService extends IService<DormStudentRecord> {

    Page<DormStudentRecordPageVO> page(DormRecordPageDTO dto);

    void assign(RecordAssignDTO dto);

    void adjust(RecordAdjustDTO dto);

    void checkout(Long studentId);

    DormStudentRecord getByStudentId(Long studentId);

    void importRecords(String fileUrl);

    void exportRecords(Long buildingId);
}

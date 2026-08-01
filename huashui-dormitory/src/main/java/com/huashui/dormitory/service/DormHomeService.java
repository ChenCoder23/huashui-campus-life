package com.huashui.dormitory.service;

import com.huashui.dormitory.domain.vo.DormHomeVO;
import com.huashui.dormitory.domain.vo.RoommateVO;

import java.util.List;

public interface DormHomeService {

    DormHomeVO getMyDorm();

    List<RoommateVO> getMyRoommates();
}
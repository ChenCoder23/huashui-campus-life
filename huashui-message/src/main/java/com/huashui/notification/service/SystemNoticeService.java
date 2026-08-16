package com.huashui.notification.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.common.response.PageResult;
import com.huashui.notification.domain.dto.CreateNoticeDTO;
import com.huashui.notification.domain.dto.NoticePageQueryDTO;
import com.huashui.notification.domain.dto.NoticeScrollQueryDTO;
import com.huashui.notification.domain.dto.UpdateNoticeDTO;
import com.huashui.notification.domain.pojo.SystemNotice;
import com.huashui.notification.domain.vo.DictVO;
import com.huashui.notification.domain.vo.NoticeVO;
import com.huashui.notification.domain.vo.ScrollVO;

import java.util.List;

/**
 * 系统公告 Service
 */
public interface SystemNoticeService extends IService<SystemNotice> {

    ScrollVO<NoticeVO> scroll(NoticeScrollQueryDTO dto);

    List<NoticeVO> latest();

    PageResult<NoticeVO> getDraftPage(NoticePageQueryDTO dto);

    Long create(CreateNoticeDTO dto);

    List<DictVO> types();

    void updateNotice(Long id, UpdateNoticeDTO dto);

    void revoke(Long id);

    void deleteDraft(Long id);

    NoticeVO detail(Long id);
}

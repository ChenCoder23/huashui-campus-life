package com.huashui.auth.service;

import com.huashui.auth.domain.dto.AdminUserPageDTO;
import com.huashui.auth.domain.vo.AdminUserVO;
import com.huashui.common.response.PageResult;

public interface AdminUserService {

    PageResult<AdminUserVO> page(AdminUserPageDTO dto);
}

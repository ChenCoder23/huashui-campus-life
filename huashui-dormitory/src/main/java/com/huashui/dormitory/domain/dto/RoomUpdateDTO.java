package com.huashui.dormitory.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "房间编辑 DTO")
public class RoomUpdateDTO extends RoomCreateDTO {
    // 继承 RoomCreateDTO，id 通过 URL 路径传递
}
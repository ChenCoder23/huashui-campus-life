package com.huashui.utility.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.utility.domain.pojo.*;
public interface UtilityService {
    Page<WaterBalance> waterPage(Integer page, Integer size, Long buildingId);
    Page<ElectricityBalance> electricPage(Integer page, Integer size, Long buildingId);
    Page<PaymentOrder> paymentPage(Integer page, Integer size, Long roomId);
}
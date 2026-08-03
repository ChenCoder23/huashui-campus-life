package com.huashui.utility.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.utility.domain.pojo.PaymentOrder;
import com.huashui.utility.domain.pojo.WaterBalance;
import com.huashui.utility.domain.pojo.ElectricityBalance;
import com.huashui.utility.mapper.PaymentOrderMapper;
import com.huashui.utility.mapper.WaterBalanceMapper;
import com.huashui.utility.mapper.ElectricityBalanceMapper;
import com.huashui.utility.service.UtilityService;
import lombok.RequiredArgsConstructor; import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j @Service @RequiredArgsConstructor
public class UtilityServiceImpl implements UtilityService {
    private final WaterBalanceMapper waterMapper;
    private final ElectricityBalanceMapper electricMapper;
    private final PaymentOrderMapper paymentMapper;

    @Override public Page<WaterBalance> waterPage(Integer page, Integer size, Long buildingId) {
        LambdaQueryWrapper<WaterBalance> qw = new LambdaQueryWrapper<>();
        return waterMapper.selectPage(new Page<>(page, size), qw);
    }
    @Override public Page<ElectricityBalance> electricPage(Integer page, Integer size, Long buildingId) {
        return electricMapper.selectPage(new Page<>(page, size), new LambdaQueryWrapper<>());
    }
    @Override public Page<PaymentOrder> paymentPage(Integer page, Integer size, Long roomId) {
        LambdaQueryWrapper<PaymentOrder> qw = new LambdaQueryWrapper<>();
        if (roomId != null) qw.eq(PaymentOrder::getRoomId, roomId);
        qw.orderByDesc(PaymentOrder::getCreateTime);
        return paymentMapper.selectPage(new Page<>(page, size), qw);
    }
}
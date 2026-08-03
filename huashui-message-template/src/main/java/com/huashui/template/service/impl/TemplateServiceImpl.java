package com.huashui.template.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.template.domain.pojo.MessageTemplate;
import com.huashui.template.mapper.MessageTemplateMapper;
import com.huashui.template.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j @Service
public class TemplateServiceImpl extends ServiceImpl<MessageTemplateMapper, MessageTemplate> implements TemplateService {}
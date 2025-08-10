package com.ragukvn.data.manager;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
// @EnableSpringDataWebSupport makes valid and necessary fields exposed in response pagination
public class AppConfig {
}

package com.allane.bp.smstest.api.configuration;

import com.allane.bp.common.security.config.BaseAuthConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class AuthConfiguration extends BaseAuthConfiguration {
}

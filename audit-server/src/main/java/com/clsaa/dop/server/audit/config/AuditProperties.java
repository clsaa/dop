package com.clsaa.dop.server.audit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "audit")
@Validated
public class AuditProperties {
    @NotEmpty
    @Valid
    private Set<String> supportedServices;
}

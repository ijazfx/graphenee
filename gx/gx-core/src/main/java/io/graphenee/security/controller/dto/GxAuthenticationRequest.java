package io.graphenee.security.controller.dto;

import lombok.Data;

@Data
public class GxAuthenticationRequest {
    private String accessKey;
    private String secret;
}

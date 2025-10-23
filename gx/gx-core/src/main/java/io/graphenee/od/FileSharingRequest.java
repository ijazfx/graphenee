package io.graphenee.od;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileSharingRequest {

    private String expireDate;
    private Integer permissions;
    private String name;
    private Integer shareType;
    private String password;
    private String path;
}

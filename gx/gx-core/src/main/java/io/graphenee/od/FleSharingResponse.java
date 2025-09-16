package io.graphenee.od;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FleSharingResponse {

    private String id;
    @JsonProperty("share_type")
    private String shareType;
    @JsonProperty("uuid_owner")
    private String uuidOwner;
    private Integer permissions;
    private Long stime;
    private String token;
    private String name;
    private String url;
}

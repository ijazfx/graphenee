package io.graphenee.aws.messaging;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class Payload<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String traceId;
    private T dto;
    private final Timestamp creationTime;

    public Payload() {
        this(null);
    }

    public Payload(T dto) {
        traceId = UUID.randomUUID().toString();
        creationTime = Timestamp.from(Instant.now());
        this.dto = dto;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public T getDto() {
        return dto;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setDto(T dto) {
        this.dto = dto;
    }
}

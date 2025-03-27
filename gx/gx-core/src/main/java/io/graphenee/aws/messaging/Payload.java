package io.graphenee.aws.messaging;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Data
public class Payload<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String traceId;
    private final Timestamp creationTime;
    private T dto;

    public Payload() {
        this(null);
    }

    public Payload(T dto) {
        this.traceId = UUID.randomUUID().toString();
        this.creationTime = Timestamp.from(Instant.now());
        this.dto = dto;
    }
    @Override
    public String toString() {
        return "Payload{" +
                "traceId='" + traceId + '\'' +
                ", creationTime=" + creationTime +
                ", dto=" + dto +
                '}';
    }
}


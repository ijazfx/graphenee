package io.graphenee.common;

import java.time.LocalDateTime;

public interface GxAuditable {

    LocalDateTime getDateCreated();

    void setDateCreated(LocalDateTime dateCreated);

    LocalDateTime getDateModified();

    void setDateModified(LocalDateTime dateModified);

    String getCreatedBy();

    void setCreatedBy(String createdBy);

    String getModifiedBy();

    void setModifiedBy(String modifiedBy);

}

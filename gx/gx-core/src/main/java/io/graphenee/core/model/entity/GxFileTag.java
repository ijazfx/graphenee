package io.graphenee.core.model.entity;

import io.graphenee.core.model.GxMappedSuperclass;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "gx_file_tag")
public class GxFileTag {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer oid;

    @Transient
    private UUID uuid = UUID.randomUUID();

    String tag;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "gx_file_tag_document_join", joinColumns = @JoinColumn(name = "oid_tag", referencedColumnName = "oid"), inverseJoinColumns = @JoinColumn(name = "oid_document", referencedColumnName = "oid"))
    Set<GxDocument> documents = new HashSet<>();

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "gx_file_tag_folder_join", joinColumns = @JoinColumn(name = "oid_tag", referencedColumnName = "oid"), inverseJoinColumns = @JoinColumn(name = "oid_folder", referencedColumnName = "oid"))
    Set<GxFolder> folders = new HashSet<>();

    @Override
    public String toString() {
        return tag;
    }
}

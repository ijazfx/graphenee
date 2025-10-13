package io.graphenee.core.model.entity;

import java.util.HashSet;
import java.util.Set;

import io.graphenee.core.model.GxMappedSuperclass;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gx_file_tag")
public class GxFileTag extends GxMappedSuperclass {
    
    String tag;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "gx_file_tag_document_join", joinColumns = @JoinColumn(name = "oid_tag", referencedColumnName = "oid"), inverseJoinColumns = @JoinColumn(name = "oid_document", referencedColumnName = "oid"))
    Set<GxDocument> documents = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "gx_file_tag_folder_join", joinColumns = @JoinColumn(name = "oid_tag", referencedColumnName = "oid"), inverseJoinColumns = @JoinColumn(name = "oid_folder", referencedColumnName = "oid"))
    Set<GxFolder> folders = new HashSet<>();

    @Override
    public String toString() {
        return tag;
    }
}

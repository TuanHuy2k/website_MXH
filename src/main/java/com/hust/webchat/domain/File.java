package com.hust.webchat.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "file")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class File extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "path_file")
    private String pathFile;

    @Column(name = "file_name_hash")
    private String fileNameHash;

    @Column(name = "status", columnDefinition = "integer default 1")
    private Integer status;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "public_url")
    private String publicUrl;

    @Column(name = "type")
    private String type;
}

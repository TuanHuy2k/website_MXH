package com.hust.webchat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "posting_file")
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class PostingFile extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "posting_id")
    private Long postingId;

    @JoinColumn(name = "file_id")
    private Long fileId;
}

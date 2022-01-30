package com.hust.webchat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posting")
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class Posting extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private Integer status;

    @Column(name = "content")
    private String content;

    @Column(name = "hash_tag")
    private String hashTag;

    @Column(name = "type")
    private Integer type;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "work_place_policy")
    private String workPlacePolicy;

    @Column(name = "job_location")
    private String jobLocation;

    @Column(name = "company")
    private String company;

    @Column(name = "job_type")
    private Integer jobType;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "description")
    private String description;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "posting", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

}

package com.hust.webchat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "emotion")
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class Emotion extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "type")
    private String type;

    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "posting_id")
    private Long postingId;
}

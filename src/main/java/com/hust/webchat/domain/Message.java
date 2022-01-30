package com.hust.webchat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "message")
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class Message extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_group_code")
    private String messageGroupCode;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "receiver_id")
    private Long receiverId;

    @Column(name = "message_content")
    private String messageContent;

    @Column(name = "sending_time")
    private Long sendingTime;

    @Column(name = "type")
    private String type;

    @Column(name = "name_message")
    private String nameMessage;
}

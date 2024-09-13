package com.snsrestapi.domain.entity;

import com.snsrestapi.enumerate.AlamType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Alarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long targetId;

    private Long fromId;

    private String text;

    @Enumerated(EnumType.STRING)
    private AlamType alamType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static Alarm createdAlarm(Long targetId, Long fromId, AlamType alamType,String text, User user) {
        return Alarm.builder()
                .targetId(targetId)
                .fromId(fromId)
                .alamType(alamType)
                .text(text)
                .user(user)
                .build();
    }
}

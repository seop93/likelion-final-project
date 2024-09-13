package com.snsrestapi.domain.dto.response.arlam;

import com.snsrestapi.domain.entity.Alarm;
import com.snsrestapi.enumerate.AlamType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlarmResponse {
    private Long id;
    private AlamType alamType;
    private Long fromUserId;
    private Long targetId;
    private String text;
    private LocalDateTime createdAt;

    public static Page<AlarmResponse> of(Page<Alarm> alarms) {
        return alarms.map(alarm -> AlarmResponse.builder()
                .id(alarm.getId())
                .alamType(alarm.getAlamType())
                .fromUserId(alarm.getFromId())
                .targetId(alarm.getTargetId())
                .text(alarm.getText())
                .createdAt(alarm.getCreatedAt())
                .build());
    }
}

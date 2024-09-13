package com.snsrestapi.service;

import com.snsrestapi.domain.dto.response.arlam.AlarmResponse;
import com.snsrestapi.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

    public Page<AlarmResponse> findAll(Pageable pageable) {
        return AlarmResponse.of(alarmRepository.findAll(pageable));
    }
}

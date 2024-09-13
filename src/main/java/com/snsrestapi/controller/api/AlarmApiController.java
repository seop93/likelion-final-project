package com.snsrestapi.controller.api;

import com.snsrestapi.domain.dto.response.Response;
import com.snsrestapi.domain.dto.response.arlam.AlarmResponse;
import com.snsrestapi.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarms")
public class AlarmApiController {

    private final AlarmService alarmService;

    @GetMapping
    public Response<Page<AlarmResponse>> findAllAlarms(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(alarmService.findAll(pageable));
    }
}

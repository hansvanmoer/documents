package org.documents.documents.mapper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.config.settings.ApiSettings;
import org.documents.documents.mapper.HandlerMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.List;

@AllArgsConstructor
@Component
public class HandlerMapperImpl implements HandlerMapper {

    private static final String PAGE_SIZE_PARAM_NAME = "page-size";
    private static final String PAGE_PARAM_NAME = "page";

    private final ApiSettings apiSettings;

    @Override
    public Pageable mapPageable(ServerRequest request) {
        return Pageable.ofSize(request.queryParam(PAGE_SIZE_PARAM_NAME)
                .map(Integer::parseInt)
                .orElseGet(apiSettings::getDefaultPageSize)
        ).withPage(request.queryParam(PAGE_PARAM_NAME)
                .map(Integer::parseInt)
                .orElse(0)
        );
    }
}

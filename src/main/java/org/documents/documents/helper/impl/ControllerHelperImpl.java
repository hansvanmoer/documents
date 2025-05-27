package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.config.settings.ApiSettings;
import org.documents.documents.helper.ControllerHelper;
import org.documents.documents.model.api.PaginationRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ControllerHelperImpl implements ControllerHelper {
    private final ApiSettings apiSettings;

    @Override
    public Pageable getPageable(PaginationRequest paginationRequest) {
        return Pageable.ofSize(paginationRequest.pageSize()).withPage(paginationRequest.page());
    }

    @Override
    public Pageable getPageable(int pageSize, int page) {
        return Pageable.ofSize(pageSize).withPage(page);
    }

    public Pageable getDefaultPageable() {
        return Pageable.ofSize(apiSettings.getDefaultPageSize()).withPage(0);
    }
}

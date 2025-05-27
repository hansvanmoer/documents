package org.documents.documents.helper;

import org.documents.documents.model.api.PaginationRequest;
import org.springframework.data.domain.Pageable;

public interface ControllerHelper {
    Pageable getPageable(PaginationRequest paginationRequest);

    Pageable getPageable(int pageSize, int page);

    Pageable getDefaultPageable();
}

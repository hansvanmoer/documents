package org.documents.documents.mapper;

import org.springframework.data.domain.Pageable;
import org.springframework.web.reactive.function.server.ServerRequest;

public interface HandlerMapper {

    Pageable mapPageable(ServerRequest request);
}

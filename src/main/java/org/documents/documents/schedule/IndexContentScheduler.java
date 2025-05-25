package org.documents.documents.schedule;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.documents.documents.service.SearchService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@AllArgsConstructor
@Configuration
@Slf4j
public class IndexContentScheduler {

    private final SearchService searchService;

    @Scheduled(initialDelayString = "#{searchSettings.contentIndexDelayInMs}", fixedDelayString = "#{searchSettings.contentIndexPeriodInMs}")
    public void indexContent() {
        log.debug("running index content scheduler");
        // Note that we need to ensure that reactor producers terminate within this thread
        // otherwise the scheduling delay will not work
        searchService.indexDocuments().blockLast();
    }
}

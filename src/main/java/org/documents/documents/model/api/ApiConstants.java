package org.documents.documents.model.api;

/**
 * All REST API constants
 */
public interface ApiConstants {
    /**
     * The API base path
     */
    String API_BASE_PATH = "/api";

    /**
     * The content path
     */
    String CONTENT_PATH = API_BASE_PATH + "/content";

    /**
     * Document path
     */
    String DOCUMENTS_PATH = API_BASE_PATH + "/documents";

    /**
     * Search document path
     */
    String SEARCH_DOCUMENTS_PATH = API_BASE_PATH + "/documents/search";
}

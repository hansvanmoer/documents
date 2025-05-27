package org.documents.documents.model.api;

/**
 * All REST API constants
 */
public interface ApiConstants {

    /**
     * The document UUID
     */
    String DOCUMENT_UUID_PATH_VARIABLE = "document-uuid";

    /**
     * The API base path
     */
    String API_BASE_PATH = "/api";

    /**
     * The content path
     */
    String CONTENT_PATH = API_BASE_PATH + "/content";

    /**
     * Documents path
     */
    String DOCUMENTS_PATH = API_BASE_PATH + "/documents";

    /**
     * Document by uuid path
     */
    String DOCUMENT_BY_UUID_PATH = API_BASE_PATH + "/document-by-uuid/{" + DOCUMENT_UUID_PATH_VARIABLE + "}";

    /**
     * The renditions by document UUID
     */
    String DOCUMENT_BY_UUID_RENDITIONS_PATH = DOCUMENT_BY_UUID_PATH + "/renditions";

    /**
     * Search document path
     */
    String SEARCH_DOCUMENTS_PATH = API_BASE_PATH + "/documents/search";
}

package org.documents.documents.model.api;

/**
 * All REST API constants
 */
public interface ApiConstants {

    /**
     * Mime type parameter
     */
    String MIME_TYPE_PARAMETER = "mime-type";

    /**
     * The content UUID
     */
    String CONTENT_UUID_PATH_VARIABLE = "content-uuid";

    /**
     * The document UUID
     */
    String DOCUMENT_UUID_PATH_VARIABLE = "document-uuid";

    /**
     * The rendition UUID
     */
    String RENDITION_UUID_PATH_VARIABLE = "rendition-uuid";

    /**
     * The API base path
     */
    String API_BASE_PATH = "/api";

    /**
     * The content path
     */
    String CONTENT_PATH = API_BASE_PATH + "/content";

    /**
     * The content by UUID path
     */
    String CONTENT_BY_UUID_PATH = API_BASE_PATH + "/content-by-uuid/{" + CONTENT_UUID_PATH_VARIABLE + "}";

    /**
     * The download content by UUID path
     */
    String CONTENT_BY_UUID_DOWNLOAD_PATH = API_BASE_PATH + "/content-by-uuid/{" + CONTENT_UUID_PATH_VARIABLE + "}/download";


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
     * The download by document UUID
     */
    String DOCUMENT_BY_UUID_DOWNLOAD_PATH = DOCUMENT_BY_UUID_PATH + "/download";

    /**
     * Search document path
     */
    String SEARCH_DOCUMENTS_PATH = API_BASE_PATH + "/documents/search";

    /**
     * Renditions
     */
    String RENDITIONS_PATH = API_BASE_PATH + "/renditions";

    /**
     * Rendition by UUID path
     */
    String RENDITION_BY_UUID_PATH = API_BASE_PATH + "/rendition-by-uuid/{" + RENDITION_UUID_PATH_VARIABLE + "}";

    /**
     * The download rendition by UUID path
     */
    String RENDITION_BY_UUID_DOWNLOAD_PATH = API_BASE_PATH + "/rendition-by-uuid/{" + RENDITION_UUID_PATH_VARIABLE + "}/download";

}

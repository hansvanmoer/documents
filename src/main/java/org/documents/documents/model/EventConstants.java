package org.documents.documents.model;

public interface EventConstants {

    String PREFIX = "event";

    String CREATED_SUFFIX = "created";

    String UPDATED_SUFFIX = "updated";

    String DELETED_SUFFIX = "deleted";

    String CONTENT_CREATED_ROUTING_KEY = PREFIX + ".content." + CREATED_SUFFIX;

    String CONTENT_CREATED_QUEUE_NAME = PREFIX + "-content-" + CREATED_SUFFIX;

    String RENDITION_CREATED_ROUTING_KEY = PREFIX + ".rendition." + CREATED_SUFFIX;

    String RENDITION_CREATED_QUEUE_NAME = PREFIX + "-rendition-" + CREATED_SUFFIX;

    String RENDITION_DELETED_ROUTING_KEY = PREFIX + ".rendition." + DELETED_SUFFIX;

    String RENDITION_DELETED_QUEUE_NAME = PREFIX + "-rendition-" + DELETED_SUFFIX;

    String DOCUMENT_CREATED_ROUTING_KEY = PREFIX + ".rendition." + CREATED_SUFFIX;

    String DOCUMENT_CREATED_QUEUE_NAME = PREFIX + "-rendition-" + CREATED_SUFFIX;

    String DOCUMENT_UPDATED_ROUTING_KEY = PREFIX + ".rendition." + UPDATED_SUFFIX;

    String DOCUMENT_UPDATED_QUEUE_NAME = PREFIX + "-rendition-" + UPDATED_SUFFIX;

    String DOCUMENT_DELETED_ROUTING_KEY = PREFIX + ".document." + DELETED_SUFFIX;

    String DOCUMENT_DELETED_QUEUE_NAME = PREFIX + "-document-" + DELETED_SUFFIX;
}

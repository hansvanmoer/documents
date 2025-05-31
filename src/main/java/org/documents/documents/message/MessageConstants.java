package org.documents.documents.message;

public interface MessageConstants {

    String PREFIX = "event";

    String CREATED_SUFFIX = "created";

    String UPDATED_SUFFIX = "updated";

    String DELETED_SUFFIX = "deleted";

    String EXCHANGE_SUFFIX = "-exchange";

    String QUEUE_SUFFIX = "-queue";

    String FANOUT_EXCHANGE_NAME = "fanout" + EXCHANGE_SUFFIX;

    String TRANSFORM_EXCHANGE_NAME = "transform" + EXCHANGE_SUFFIX;

    String EVENT_EXCHANGE_NAME = "event" + EXCHANGE_SUFFIX;

    String PREVIEW_EXCHANGE_NAME = "preview" + EXCHANGE_SUFFIX;

    String CONTENT_CREATED_ROUTING_KEY = PREFIX + ".content." + CREATED_SUFFIX;

    String CONTENT_CREATED_QUEUE_NAME = PREFIX + "-content-" + CREATED_SUFFIX + QUEUE_SUFFIX;

    String RENDITION_CREATED_ROUTING_KEY = PREFIX + ".rendition." + CREATED_SUFFIX;

    String RENDITION_CREATED_QUEUE_NAME = PREFIX + "-rendition-" + CREATED_SUFFIX + QUEUE_SUFFIX;

    String RENDITION_DELETED_ROUTING_KEY = PREFIX + ".rendition." + DELETED_SUFFIX;

    String RENDITION_DELETED_QUEUE_NAME = PREFIX + "-rendition-" + DELETED_SUFFIX + QUEUE_SUFFIX;

    String DOCUMENT_CREATED_ROUTING_KEY = PREFIX + ".rendition." + CREATED_SUFFIX;

    String DOCUMENT_CREATED_QUEUE_NAME = PREFIX + "-rendition-" + CREATED_SUFFIX + QUEUE_SUFFIX;

    String DOCUMENT_UPDATED_ROUTING_KEY = PREFIX + ".rendition." + UPDATED_SUFFIX;

    String DOCUMENT_UPDATED_QUEUE_NAME = PREFIX + "-rendition-" + UPDATED_SUFFIX + QUEUE_SUFFIX;

    String DOCUMENT_DELETED_ROUTING_KEY = PREFIX + ".document." + DELETED_SUFFIX;

    String DOCUMENT_DELETED_QUEUE_NAME = PREFIX + "-document-" + DELETED_SUFFIX + QUEUE_SUFFIX;

    String PREVIEW_QUEUE_NAME = "preview";

    String TRANSFORM_REQUESTED_ROUTING_KEY = "transform.requested";

    String TRANSFORM_REQUESTED_QUEUE_NAME = "transform-requested" + QUEUE_SUFFIX;

    String TRANSFORM_SUCCEEDED_ROUTING_KEY = "transform.succeeded";

    String TRANSFORM_SUCCEEDED_QUEUE_NAME = "transform-succeeded" + QUEUE_SUFFIX;

    String TRANSFORM_FAILED_ROUTING_KEY = "transform.failed";

    String TRANSFORM_FAILED_QUEUE_NAME = "transform-failed" + QUEUE_SUFFIX;
}

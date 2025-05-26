CREATE DATABASE documents;

\c documents

CREATE TABLE content (
     id BIGSERIAL PRIMARY KEY,
     uuid CHARACTER(36) UNIQUE NOT NULL,
     created TIMESTAMP NOT NULL,
     mime_type VARCHAR(128) NOT NULL
                    );

CREATE TABLE rendition (
    id BIGSERIAL PRIMARY KEY,
    uuid CHARACTER(36) UNIQUE NOT NULL,
    created TIMESTAMP NOT NULL,
    mime_type VARCHAR(128) NOT NULL,
    content_id BIGINT NOT NULL REFERENCES content (id)
                        );

ALTER TABLE rendition ADD CONSTRAINT c_rendition_mime_type_content_id_unique UNIQUE (mime_type, content_id);

CREATE TABLE document (
    id BIGSERIAL PRIMARY KEY,
    uuid CHARACTER(36) UNIQUE NOT NULL,
    created TIMESTAMP NOT NULL,
    content_id BIGINT NOT NULL REFERENCES content (id),
    content_index_status VARCHAR(32) NOT NULL,
    title VARCHAR(256) NOT NULL
                      );
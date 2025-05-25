CREATE DATABASE documents;

\c documents

CREATE TABLE content (
     id BIGSERIAL PRIMARY KEY,
     uuid CHARACTER(36) UNIQUE NOT NULL,
     created TIMESTAMP NOT NULL,
     path VARCHAR(256) UNIQUE NOT NULL,
     mime_type VARCHAR(128) NOT NULL
                    );

CREATE TABLE rendition (
    id BIGSERIAL PRIMARY KEY,
    original_id BIGINT NOT NULL REFERENCES content (id),
    rendition_id BIGINT NOT NULL REFERENCES content (id)
                        );

CREATE TABLE document (
    id BIGSERIAL PRIMARY KEY,
    uuid CHARACTER(36) UNIQUE NOT NULL,
    created TIMESTAMP NOT NULL,
    content_id BIGINT NOT NULL REFERENCES content (id)
                      );
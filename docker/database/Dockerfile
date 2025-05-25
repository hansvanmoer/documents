FROM postgres:17

COPY 001-setup-documents-db.sql /docker-entrypoint-initdb.d
COPY 002-setup-documents-users.sql /docker-entrypoint-initdb.d
COPY 003-setup-documents-test-data.sql /docker-entrypoint-initdb.d
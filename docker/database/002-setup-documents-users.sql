CREATE USER documents WITH PASSWORD 'documents';

\c documents

GRANT CONNECT ON DATABASE documents to documents;
GRANT ALL PRIVILEGES ON content_id_seq TO documents;
GRANT SELECT, INSERT, DELETE ON TABLE content TO documents;
GRANT ALL PRIVILEGES ON rendition_id_seq TO documents;
GRANT SELECT, INSERT, DELETE ON TABLE rendition TO documents;
GRANT ALL PRIVILEGES ON document_id_seq TO documents;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE document TO documents;
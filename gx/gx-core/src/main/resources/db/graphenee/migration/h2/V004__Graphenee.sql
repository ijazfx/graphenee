CREATE SEQUENCE gx_saved_query_seq;

CREATE TABLE gx_saved_query (
    oid INT DEFAULT NEXTVAL('gx_saved_query_seq') PRIMARY KEY,
    query_name VARCHAR(50) NOT NULL,
    query_bean_json TEXT NOT NULL,
    query_bean_class_name VARCHAR(200) NOT NULL,
    additional_info VARCHAR(1000),
    target_user VARCHAR(200)
);

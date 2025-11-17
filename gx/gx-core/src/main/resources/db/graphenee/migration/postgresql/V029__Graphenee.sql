alter table gx_document add column share_key uuid;

UPDATE gx_document SET share_key = gen_random_uuid() WHERE share_key IS NULL;
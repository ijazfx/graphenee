ALTER TABLE gx_document ADD COLUMN share_key UUID;

UPDATE gx_document SET share_key = RANDOM_UUID() WHERE share_key IS NULL;

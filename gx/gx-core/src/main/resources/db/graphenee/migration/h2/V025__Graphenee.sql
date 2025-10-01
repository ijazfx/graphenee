CREATE TABLE gx_term_translation (
	oid INT AUTO_INCREMENT PRIMARY KEY,
	oid_term INT NOT NULL,
	oid_supported_locale INT NOT NULL,
	term_singular VARCHAR(1000) NOT NULL,
    term_plural VARCHAR(1000),
    FOREIGN KEY (oid_term) REFERENCES gx_term(oid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (oid_supported_locale) REFERENCES gx_supported_locale(oid) ON DELETE RESTRICT ON UPDATE CASCADE
);

INSERT INTO gx_term_translation (oid_term, oid_supported_locale, term_singular, term_plural) SELECT oid, oid_supported_locale, term_singular, term_plural FROM gx_term;

ALTER TABLE gx_term DROP COLUMN oid_supported_locale;
ALTER TABLE gx_term DROP COLUMN term_singular;
ALTER TABLE gx_term DROP COLUMN term_plural;
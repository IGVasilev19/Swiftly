ALTER TABLE IF EXISTS blackouts
    DROP COLUMN IF EXISTS vehicle_id;

ALTER TABLE IF EXISTS blackouts
    ADD COLUMN listing_id INTEGER NOT NULL;

ALTER TABLE IF EXISTS blackouts
    ADD CONSTRAINT fk_blackouts_listing
    FOREIGN KEY (listing_id) REFERENCES listings(id)
    ON DELETE CASCADE;
ALTER TABLE IF EXISTS bookings
    DROP COLUMN IF EXISTS vehicle_id,
    DROP COLUMN IF EXISTS profile_id;

ALTER TABLE IF EXISTS bookings
    ADD COLUMN listing_id INTEGER NOT NULL,
    ADD COLUMN renter_id INTEGER NOT NULL;

ALTER TABLE IF EXISTS bookings
    ADD CONSTRAINT fk_bookings_listing
    FOREIGN KEY (listing_id) REFERENCES listings(id)
    ON DELETE CASCADE;

ALTER TABLE IF EXISTS bookings
    ADD CONSTRAINT fk_bookings_renter
    FOREIGN KEY (renter_id) REFERENCES users(id)
    ON DELETE CASCADE;
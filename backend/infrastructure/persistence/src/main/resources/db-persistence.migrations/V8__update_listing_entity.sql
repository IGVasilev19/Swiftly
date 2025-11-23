ALTER TABLE IF EXISTS listings
    DROP COLUMN IF EXISTS vehicle_id;

ALTER TABLE IF EXISTS listings
    ADD COLUMN vehicle_id INTEGER NOT NULL;

ALTER TABLE IF EXISTS listings
    ADD CONSTRAINT fk_listings_vehicle
        FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
            ON DELETE CASCADE;

ALTER TABLE IF EXISTS listings
    ADD CONSTRAINT listings_vehicle_unique UNIQUE (vehicle_id);

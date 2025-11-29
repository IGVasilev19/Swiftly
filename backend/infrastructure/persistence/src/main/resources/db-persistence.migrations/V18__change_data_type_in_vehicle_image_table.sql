DROP TABLE IF EXISTS public.vehicle_images;

CREATE TABLE IF NOT EXISTS public.vehicle_images (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    data OID NOT NULL,
    mime_type VARCHAR(255) NOT NULL,
    file_name VARCHAR(255),
    vehicle_id INTEGER NOT NULL,
    uploaded_at TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_vehicle
    FOREIGN KEY (vehicle_id)
    REFERENCES vehicles(id)
    ON DELETE CASCADE
    );

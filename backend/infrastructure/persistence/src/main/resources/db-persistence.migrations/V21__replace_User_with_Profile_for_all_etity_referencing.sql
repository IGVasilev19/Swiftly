ALTER TABLE IF EXISTS public.vehicles
DROP CONSTRAINT fk_vehicle_owner;

ALTER TABLE IF EXISTS public.bookings
DROP CONSTRAINT fk_bookings_renter;

ALTER TABLE IF EXISTS public.bookings
    ADD CONSTRAINT fk_bookings_renter_profile
        FOREIGN KEY (renter_id)
            REFERENCES profiles(id)
            ON DELETE RESTRICT;

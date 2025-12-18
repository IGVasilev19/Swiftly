ALTER TABLE IF EXISTS public.listings
    DROP COLUMN IF EXISTS end_availability,
    DROP COLUMN IF EXISTS start_availability,
    DROP COLUMN IF EXISTS latitude,
    DROP COLUMN IF EXISTS longitude,
    DROP COLUMN IF EXISTS pick_up_address,
    DROP COLUMN IF EXISTS time_zone;
ALTER TABLE IF EXISTS public.bookings
ALTER COLUMN start_at TYPE DATE
        USING start_at::date,
ALTER COLUMN end_at TYPE DATE
        USING end_at::date;
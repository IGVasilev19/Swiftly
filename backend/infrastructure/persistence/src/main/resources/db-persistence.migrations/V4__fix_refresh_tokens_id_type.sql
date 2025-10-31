-- Fix id column type from INTEGER to BIGINT
ALTER TABLE public.refresh_tokens ALTER COLUMN id TYPE BIGINT;

-- Fix user_id column type from BIGINT to INTEGER
ALTER TABLE public.refresh_tokens ALTER COLUMN user_id TYPE INTEGER;

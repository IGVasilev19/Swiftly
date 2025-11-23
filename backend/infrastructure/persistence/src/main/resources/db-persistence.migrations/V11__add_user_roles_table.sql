CREATE TABLE IF NOT EXISTS public.user_roles (
                            user_id INTEGER NOT NULL,
                            role VARCHAR(50) NOT NULL,
                            CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id)
);
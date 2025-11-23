CREATE TABLE IF NOT EXISTS public.vehicle_features (
                                  vehicle_id INTEGER NOT NULL,
                                  feature VARCHAR(100) NOT NULL,
                                  CONSTRAINT fk_vehicle_features_vehicle FOREIGN KEY (vehicle_id)
                                      REFERENCES vehicles(id)
                                      ON DELETE CASCADE
);
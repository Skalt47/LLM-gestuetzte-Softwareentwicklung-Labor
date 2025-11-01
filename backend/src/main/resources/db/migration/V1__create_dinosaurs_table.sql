CREATE TABLE
    IF NOT EXISTS dinosaurs (
        id BIGSERIAL PRIMARY KEY,
        species TEXT NOT NULL,
        group_code VARCHAR(4) NOT NULL,
        lifespan_years INT,
        length_m DOUBLE PRECISION,
        speed_kmh INT,
        intelligence INT,
        attack INT,
        defense INT,
        created_at TIMESTAMPTZ NOT NULL DEFAULT NOW ()
    );

CREATE UNIQUE INDEX IF NOT EXISTS uq_dinosaurs_species ON dinosaurs (species);

CREATE UNIQUE INDEX IF NOT EXISTS uq_dinosaurs_group_code ON dinosaurs (group_code);
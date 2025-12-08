-- Map each dinosaur (by id order) to the numbered images you already have
WITH ordered AS (
  SELECT id, ROW_NUMBER() OVER (ORDER BY id) AS rn
  FROM dinosaurs
)
UPDATE dinosaurs d
SET image_url = '/images/dino_' || o.rn || '.png'
FROM ordered o
WHERE d.id = o.id;

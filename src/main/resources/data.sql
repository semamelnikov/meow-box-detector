INSERT INTO device(id, name)
SELECT 1, 'device1'
WHERE NOT EXISTS (
  SELECT id FROM device WHERE id = 1
);

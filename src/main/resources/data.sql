INSERT INTO device(name)
SELECT 'device1'
WHERE NOT EXISTS (
  SELECT id FROM device WHERE id = 1
);

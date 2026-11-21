-- Datos ficticios para pruebas. Ejecutar después de schema.sql en tablas vacías.
BEGIN;

INSERT INTO public.cliente (dni, nombre, apellido, telefono, email) VALUES
    ('90000001', 'Lucía', 'Paz', '0000000001', 'lucia.paz@example.com'),
    ('90000002', 'Martín', 'Herrera', '0000000002', NULL),
    ('90000003', 'Sofía', 'Vega', '0000000003', 'sofia.vega@example.com');

INSERT INTO public.mecanico (legajo, nombre, apellido, especialidad, valor_hora) VALUES
    (101, 'Raúl', 'Soria', 'MOTOR', 12000.00),
    (102, 'Elena', 'Acosta', 'ELECTRICIDAD', 13500.00),
    (103, 'Bruno', 'Medina', 'FRENOS', 11500.00);

INSERT INTO public.repuesto (codigo, descripcion, precio_unitario, stock) VALUES
    ('REP001', 'Filtro de aceite', 15000.00, 12),
    ('REP002', 'Aceite de motor de 4 litros', 52000.00, 8),
    ('REP003', 'Filtro de aire', 18500.00, 10),
    ('REP004', 'Juego de pastillas de freno', 42000.00, 6),
    ('REP005', 'Bujía de encendido', 9800.50, 20),
    ('REP006', 'Correa de accesorios', 28500.00, 4),
    ('REP007', 'Batería de 12 V', 115000.00, 3),
    ('REP008', 'Lámpara de faro H7', 12400.00, 9),
    ('REP009', 'Filtro de combustible', 23500.00, 1),
    ('REP010', 'Disco de freno para moto', 38000.00, 0);

INSERT INTO public.vehiculo
    (patente, cliente_dni, marca, modelo, anio, tipo, cantidad_puertas, combustible, cilindrada)
VALUES
    ('AB101CD', '90000001', 'Fiat', 'Cronos', 2021, 'AUTO', 4, 'NAFTA', NULL),
    ('AE202EF', '90000002', 'Toyota', 'Hilux', 2022, 'AUTO', 4, 'DIESEL', NULL),
    ('AC303GH', '90000003', 'Renault', 'Logan', 2019, 'AUTO', 4, 'GNC', NULL),
    ('A411BCD', '90000001', 'Yamaha', 'FZ25', 2023, 'MOTO', NULL, NULL, 250),
    ('A512CEF', '90000003', 'Kawasaki', 'Versys 300', 2020, 'MOTO', NULL, NULL, 296);

COMMIT;

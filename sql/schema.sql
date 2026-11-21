DROP TABLE IF EXISTS public.detalle_repuesto;
DROP TABLE IF EXISTS public.orden_trabajo;
DROP TABLE IF EXISTS public.vehiculo;
DROP TABLE IF EXISTS public.repuesto;
DROP TABLE IF EXISTS public.mecanico;
DROP TABLE IF EXISTS public.cliente;

CREATE TABLE public.cliente (
    dni character varying PRIMARY KEY,
    nombre character varying NOT NULL,
    apellido character varying NOT NULL,
    telefono character varying NOT NULL,
    email character varying
);

CREATE TABLE public.mecanico (
    legajo integer PRIMARY KEY,
    nombre character varying NOT NULL,
    apellido character varying NOT NULL,
    especialidad character varying NOT NULL,
    valor_hora numeric(12,2) NOT NULL,
    CONSTRAINT ck_mecanico_especialidad
        CHECK (especialidad IN ('MOTOR', 'ELECTRICIDAD', 'FRENOS', 'GENERAL')),
    CONSTRAINT ck_mecanico_valor_hora CHECK (valor_hora >= 0)
);

CREATE TABLE public.repuesto (
    codigo character varying PRIMARY KEY,
    descripcion character varying NOT NULL,
    precio_unitario numeric(12,2) NOT NULL,
    stock integer NOT NULL,
    CONSTRAINT ck_repuesto_precio CHECK (precio_unitario >= 0),
    CONSTRAINT ck_repuesto_stock CHECK (stock >= 0)
);

CREATE TABLE public.vehiculo (
    patente character varying PRIMARY KEY,
    cliente_dni character varying NOT NULL,
    marca character varying NOT NULL,
    modelo character varying NOT NULL,
    anio integer NOT NULL,
    tipo character varying NOT NULL,
    cantidad_puertas integer,
    combustible character varying,
    cilindrada integer,
    CONSTRAINT fk_vehiculo_cliente FOREIGN KEY (cliente_dni)
        REFERENCES public.cliente (dni) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT ck_vehiculo_tipo_campos CHECK (
        (
            tipo = 'AUTO'
            AND cantidad_puertas IS NOT NULL
            AND cantidad_puertas > 0
            AND combustible IS NOT NULL
            AND combustible IN ('NAFTA', 'DIESEL', 'GNC')
            AND cilindrada IS NULL
        )
        OR
        (
            tipo = 'MOTO'
            AND cilindrada IS NOT NULL
            AND cilindrada > 0
            AND cantidad_puertas IS NULL
            AND combustible IS NULL
        )
    )
);

CREATE TABLE public.orden_trabajo (
    numero integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    vehiculo_patente character varying NOT NULL,
    mecanico_legajo integer NOT NULL,
    fecha_ingreso date NOT NULL,
    descripcion_problema character varying NOT NULL,
    horas_mano_obra numeric(8,2) NOT NULL,
    estado character varying NOT NULL DEFAULT 'PENDIENTE',
    CONSTRAINT fk_orden_vehiculo FOREIGN KEY (vehiculo_patente)
        REFERENCES public.vehiculo (patente),
    CONSTRAINT fk_orden_mecanico FOREIGN KEY (mecanico_legajo)
        REFERENCES public.mecanico (legajo),
    CONSTRAINT ck_orden_horas CHECK (horas_mano_obra >= 0),
    CONSTRAINT ck_orden_estado
        CHECK (estado IN ('PENDIENTE', 'EN_REPARACION', 'FINALIZADA', 'ENTREGADA'))
);

CREATE TABLE public.detalle_repuesto (
    orden_numero integer NOT NULL,
    repuesto_codigo character varying NOT NULL,
    cantidad integer NOT NULL,
    precio_unitario_aplicado numeric(12,2) NOT NULL,
    CONSTRAINT pk_detalle_repuesto PRIMARY KEY (orden_numero, repuesto_codigo),
    CONSTRAINT fk_detalle_orden FOREIGN KEY (orden_numero)
        REFERENCES public.orden_trabajo (numero),
    CONSTRAINT fk_detalle_repuesto FOREIGN KEY (repuesto_codigo)
        REFERENCES public.repuesto (codigo),
    CONSTRAINT ck_detalle_cantidad CHECK (cantidad > 0),
    CONSTRAINT ck_detalle_precio CHECK (precio_unitario_aplicado >= 0)
);

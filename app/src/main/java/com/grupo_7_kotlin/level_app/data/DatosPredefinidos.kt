package com.grupo_7_kotlin.level_app.data

import com.grupo_7_kotlin.level_app.data.model.Producto

fun obtenerProductosPredefinidos(): List<Producto> {
    return listOf(
        // JUEGOS DE MESA
        Producto(
            "JM001",
            "Catan",
            "Juegos de Mesa",
            29990.0,
            "Clásico de estrategia y comercio de recursos.",
            "catan_url"
        ),
        Producto(
            "JM002",
            "Carcassonne",
            "Juegos de Mesa",
            24990.0,
            "Juego de colocación de fichas para construir paisajes medievales.",
            "carcassonne_url"
        ),
        // ACCESORIOS
        Producto(
            "AC001",
            "Controlador Xbox Series X",
            "Accesorios",
            59990.0,
            "Mando inalámbrico de última generación, alta precisión.",
            "xbox_control_url"
        ),
        Producto(
            "AC002",
            "Auriculares HyperX Cloud II",
            "Accesorios",
            75990.0,
            "Sonido envolvente 7.1 y micrófono desmontable.",
            "hyperx_url"
        ),
        // CONSOLAS
        Producto(
            "CO001",
            "PlayStation 5",
            "Consolas",
            549990.0,
            "Consola de Sony con gráficos impresionantes y carga ultrarrápida.",
            "ps5_url"
        ),
        // COMPUTADORES GAMERS
        Producto(
            "CG001",
            "PC Gamer ASUS ROG Strix",
            "Computadores Gamers",
            1899990.0,
            "Potente equipo diseñado para los gamers más exigentes.",
            "asus_rog_url"
        ),
        // SILLAS GAMERS
        Producto(
            "SG001",
            "Silla Gamer Secretlab Titan",
            "Sillas Gamers",
            399990.0,
            "Máximo confort, soporte ergonómico para largas sesiones.",
            "secretlab_url"
        ),
        // MOUSE
        Producto(
            "MO001",
            "Mouse Gamer Logitech G502 HERO",
            "Mouse",
            49990.0,
            "Sensor de alta precisión y peso ajustable.",
            "g502_url"
        ),
        // MOUSEPAD
        Producto(
            "MP001",
            "Mousepad Razer Goliathus Extended Chroma",
            "Mousepad",
            29990.0,
            "Superficie suave y amplia con iluminación RGB.",
            "razer_mousepad_url"
        ),
        // POLERAS PERSONALIZADAS
        Producto(
            "PE001",
            "Polera Gamer Personalizada 'Level-Up'",
            "Poleras Personalizadas",
            15990.0,
            "Personaliza con tu gamer tag.",
            "polera_url"
        ),
        // SERVICIO TÉCNICO
        Producto(
            "ST001",
            "Servicio Técnico - Mantenimiento",
            "Servicio Técnico",
            35000.0,
            "Limpieza interna y optimización de software.",
            "servicio_url"
        )


    )


}
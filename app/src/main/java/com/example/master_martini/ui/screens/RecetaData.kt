package com.example.master_martini.ui.screens

data class VideoMartini(
    val titulo: String,
    val descripcion: String,
    val duracion: String,
    val nivel: String,
    val url: String,
    val videoUrl: String,
    val categoria: String,
    val rendimiento: String,
    val ingredientes: List<String>,
    val pasos: List<String>
) {
    val youtubeId: String
        get() = url.substringAfter("v=").substringBefore("&")

    val thumbnailUrl: String
        get() = "https://img.youtube.com/vi/$youtubeId/hqdefault.jpg"
}

val videosMartini = listOf(
    VideoMartini(
        titulo = "Pudin de Chía | ORASI COCO",
        descripcion = "Preparación saludable y vegetal con bebida ORASÍ COCO.",
        duracion = "5:20",
        nivel = "Técnico",
        url = "https://www.youtube.com/watch?v=8n7kAGbS5uE",
        videoUrl = "https://master-martini-videos-2026.s3.us-east-1.amazonaws.com/Pudin+de+Ch%C3%ADa+_+ORASI+COCO.mp4",
        categoria = "Chocolatería",
        rendimiento = "6 porciones (150g c/u)",
        ingredientes = listOf(
            "500 ml de bebida vegetal Orasí Coco",
            "80 g de semillas de chía hidratadas",
            "40 g de jarabe de agave o miel",
            "100 g de cobertura Ariba Fondente 54% fundida",
            "50 g de coco laminado tostado para decorar"
        ),
        pasos = listOf(
            "Mezclar la bebida Orasí Coco fría con el endulzante hasta integrar por completo.",
            "Incorporar las semillas de chía en forma de lluvia batiendo constantemente para evitar grumos.",
            "Reposar en refrigeración a 4°C durante al menos 2 horas hasta lograr textura gelificada.",
            "Disponer una base de ganache de Ariba Fondente en el vaso, verter el pudin y terminar con coco tostado."
        )
    ),
    VideoMartini(
        titulo = "Mousse chocolate blanco | ARIBA",
        descripcion = "Textura aireada y brillo profesional con Ariba Blanco.",
        duracion = "4:15",
        nivel = "Técnico",
        url = "https://www.youtube.com/watch?v=-8KFtkMyntY",
        videoUrl = "https://master-martini-videos-2026.s3.us-east-1.amazonaws.com/Mousse+chocolate+blanco+_+ARIBA.mp4",
        categoria = "Chocolatería",
        rendimiento = "10 vasos individuales",
        ingredientes = listOf(
            "300 g de chocolate Ariba Blanco en discos",
            "150 ml de leche entera",
            "400 ml de crema vegetal Decor Up semimontada",
            "8 g de gelatina sin sabor hidratada en 40 ml de agua",
            "1 vaina de vainilla o pasta aromatizante"
        ),
        pasos = listOf(
            "Fundir los discos de Ariba Blanco a baño maría o microondas en intervalos de 30 segundos sin superar los 45°C.",
            "Calentar la leche a 60°C, disolver la gelatina hidratada y verter en tres partes sobre el chocolate formando una emulsión brillante.",
            "Cuando la base alcance 32°C, incorporar la crema Decor Up semimontada con movimientos envolventes.",
            "Dosificar en moldes o vasos y abatir temperatura por 3 horas antes de glasear."
        )
    ),
    VideoMartini(
        titulo = "Torta Chocolate Avellana | CARAVELLA DECO",
        descripcion = "Bizcocho húmedo con relleno y acabado espejo Caravella.",
        duracion = "8:30",
        nivel = "Técnico",
        url = "https://www.youtube.com/watch?v=ZwOkabO177M",
        videoUrl = "https://master-martini-videos-2026.s3.us-east-1.amazonaws.com/Torta+Chocolate+Avellana+_+CARAVELLA+DECO.mp4",
        categoria = "Chocolatería",
        rendimiento = "1 torta (22 cm diámetro)",
        ingredientes = listOf(
            "3 discos de bizcocho de cacao oscuro",
            "350 g de crema Caravella Nocciola (avellana)",
            "250 g de Caravella Deco Fondente para baño",
            "150 ml de almíbar ligero al ron o café",
            "80 g de avellanas europeas tostadas y troceadas"
        ),
        pasos = listOf(
            "Humedecer ligeramente el primer disco de bizcocho con almíbar.",
            "Aplicar una capa uniforme de Caravella Nocciola con manga pastelera y espolvorear avellanas troceadas.",
            "Repetir el armado, alisar los bordes con espátula y llevar a frío durante 40 minutos.",
            "Calentar Caravella Deco a 38°C y bañar la torta sobre rejilla para lograr un acabado espejo sin grietas."
        )
    ),
    VideoMartini(
        titulo = "Tarta de coco | ORASÍ COCO",
        descripcion = "Base sablé crocante con crema suave de coco vegetal.",
        duracion = "6:45",
        nivel = "Técnico",
        url = "https://www.youtube.com/watch?v=vmC2JpTvrLM",
        videoUrl = "https://master-martini-videos-2026.s3.us-east-1.amazonaws.com/Tarta+de+coco+_+ORAS%C3%8D+COCO.mp4",
        categoria = "Pastelería",
        rendimiento = "8 porciones",
        ingredientes = listOf(
            "250 g de masa sablé horneada en aro de 20 cm",
            "400 ml de bebida Orasí Coco",
            "90 g de azúcar granulada",
            "45 g de almidón de maíz",
            "100 g de coco rallado fino"
        ),
        pasos = listOf(
            "Disolver el almidón de maíz en 100 ml de Orasí Coco frío.",
            "Llevar el resto de la bebida con el azúcar y el coco rallado a hervor suave.",
            "Agregar la mezcla de almidón y cocinar batiendo constantemente durante 2 minutos hasta espesar.",
            "Rellenar la tartaleta cocida, cubrir a piel con film y refrigerar 3 horas."
        )
    ),
    VideoMartini(
        titulo = "¿Cómo utilizar crema pastelera? | DELICE",
        descripcion = "Estandarización en frío y horneabilidad con línea Delice.",
        duracion = "3:50",
        nivel = "Técnico",
        url = "https://www.youtube.com/watch?v=1U1twaw6bP0",
        videoUrl = "https://master-martini-videos-2026.s3.us-east-1.amazonaws.com/%C2%BFC%C3%B3mo+utilizar+crema+pastelera%2C+merengue+o+crema+chantilly+EN+POLVO_+_+DELICE.mp4",
        categoria = "Pastelería",
        rendimiento = "1.35 kg de crema lista",
        ingredientes = listOf(
            "350 g de premezcla Crema Pastelera Delice",
            "1000 ml de agua potable fría (10°C - 15°C)",
            "Opcional: 100 g de crema Decor Up montada para diplomática"
        ),
        pasos = listOf(
            "Verter el agua fría en el bol de la batidora e incorporar el polvo Delice.",
            "Batir con globo a velocidad media-alta durante 3 a 4 minutos hasta obtener consistencia lisa y brillante.",
            "Dejar reposar 5 minutos antes de manguear; resiste ciclos de horneado y congelación sin sinéresis."
        )
    ),
    VideoMartini(
        titulo = "Queque navideño | VITA BAKE",
        descripcion = "Miga húmeda, volumen estable y conservación prolongada.",
        duracion = "10:00",
        nivel = "Técnico",
        url = "https://www.youtube.com/watch?v=xoTHn4_-Fhs",
        videoUrl = "https://master-martini-videos-2026.s3.us-east-1.amazonaws.com/Queque+navide%C3%B1o+_+VITA+BAKE.mp4",
        categoria = "Panadería",
        rendimiento = "3 moldes de 500 g",
        ingredientes = listOf(
            "1000 g de premezcla Vita Bake Queque",
            "350 g de huevo entero líquido",
            "300 g de aceite vegetal neutro",
            "200 ml de agua",
            "250 g de frutos secos macerados y fruta confitada"
        ),
        pasos = listOf(
            "Colocar Vita Bake, huevos, aceite y agua en el bol con paleta.",
            "Mezclar 1 minuto en velocidad baja y luego 4 minutos en velocidad media hasta homogeneizar.",
            "Incorporar los frutos macerados previamente enharinados para evitar que decanten.",
            "Hornear a 170°C durante 40 a 45 minutos."
        )
    ),
    VideoMartini(
        titulo = "Helado Frutos del Bosque | GELATO",
        descripcion = "Balance de sólidos y frescura frutal con Martini Gelato.",
        duracion = "7:20",
        nivel = "Técnico",
        url = "https://www.youtube.com/watch?v=R2N_oOG0xI8",
        videoUrl = "https://master-martini-videos-2026.s3.us-east-1.amazonaws.com/Helado+Artesanal+Frutos+del+Bosque+_+GELATO+MARTINI.mp4",
        categoria = "Gelatería",
        rendimiento = "4.5 litros de gelato",
        ingredientes = listOf(
            "2500 ml de agua purificada",
            "1000 g de Base Fruta Martini Gelato",
            "450 g de pulpa de frutos del bosque",
            "150 g de variegato Frutos del Bosque para vetear"
        ),
        pasos = listOf(
            "Mixear el agua, la Base Fruta y la pulpa de frutos del bosque con turbomixer durante 2 minutos.",
            "Dejar madurar la mezcla en frío por 20 minutos para hidratar estabilizantes.",
            "Mantecar en fabricadora de helados hasta alcanzar extracción cremosa a -9°C.",
            "Extraer en cubeta vetando capas con el variegato."
        )
    ),
    VideoMartini(
        titulo = "Helado de Avellana | BASE BLANCA",
        descripcion = "Cremosidad italiana intensa con Pasta Avellana Martini.",
        duracion = "6:10",
        nivel = "Técnico",
        url = "https://www.youtube.com/watch?v=KhhA9pAJZxA",
        videoUrl = "https://master-martini-videos-2026.s3.us-east-1.amazonaws.com/Helado+Artesanal+de+Avellana+_+BASE+BLANCA+MARTINI+GELATO.mp4",
        categoria = "Gelatería",
        rendimiento = "4.5 litros de gelato",
        ingredientes = listOf(
            "2500 ml de leche entera fresca",
            "1000 g de Base Blanca Martini Gelato",
            "280 g de Pasta Avellana Pura Martini Gelato",
            "100 g de granillo de avellana caramelizada"
        ),
        pasos = listOf(
            "Integrar la Base Blanca en la leche con turbomixer hasta disolver completamente.",
            "Añadir la Pasta Avellana Pura y emulsionar por 2 minutos.",
            "Procesar en mantecadora y decorar la cubeta con granillo de avellana."
        )
    ),
    VideoMartini(
        titulo = "Café helado | BASE VAINILLA",
        descripcion = "Perfil aromático balanceado para vitrina de gelatería.",
        duracion = "4:30",
        nivel = "Técnico",
        url = "https://www.youtube.com/watch?v=mGTlgfD_x5Y",
        videoUrl = "https://master-martini-videos-2026.s3.us-east-1.amazonaws.com/Caf%C3%A9+helado+_+BASE+VAINILLA+PARA+HELADOS+_+MARTINI+GELATO.mp4",
        categoria = "Gelatería",
        rendimiento = "4.5 litros de gelato",
        ingredientes = listOf(
            "2400 ml de leche entera",
            "1000 g de Base Vainilla Martini Gelato",
            "120 g de Pasta Café Espresso Martini Gelato",
            "Cacao en polvo para espolvorear"
        ),
        pasos = listOf(
            "Mixear la leche junto con la Base Vainilla y la Pasta Café Espresso.",
            "Mantecar hasta lograr estructura firme y sedosa.",
            "Decorar en cubeta con ondas de espátula y un toque fino de cacao."
        )
    )
)
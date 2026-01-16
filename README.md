# SiliconPowerTV — Prueba técnica Android (Kotlin)

App Android para consultar series de TV populares usando la API de TMDB.
Este repositorio es la entrega de la **versión 2.0** (incluye lo de V1 + mejoras V2): listado
paginado, detalle, modo offline con Room, multiidioma, orientación responsive y dark mode.

## Qué incluye (V2)

- **Listado de series populares** con paginación (Paging 3).
- **Detalle de una serie** al pulsar en un ítem.
- **Modo offline (Room)**:
    - El listado va cacheando resultados mientras se navega.
    - El detalle se guarda al consultarlo, para poder verlo después sin conexión.
- **Multiidioma**:
    - Textos de UI en **ES/EN**.
    - La información se pide a TMDB usando el `language` del dispositivo (no tiene un botón
      explícito para cambiar el idioma, se establecerá el que tengamos en el dispositivo).
- **UI** con **Jetpack Compose** y navegación con `NavHost`.
- **Diseño V2**:
    - Funciona en **vertical y horizontal**.
    - Soporte de temas, con **paleta clara y paleta oscura** claramente distintas.

## Stack / decisiones técnicas

- **Kotlin**
- **MVVM** (ViewModel + Repository)
- **Hilt** para inyección de dependencias
- **Retrofit + Moshi** (API)
- **Room** (caché offline)
- **Paging 3**
- **Coroutines / Flow**
- **Coil** (imágenes)

En Room se usa una migración sencilla y, para mantenerlo simple en el contexto de una prueba,
hay `fallbackToDestructiveMigration` como salida si cambia el esquema.

## Funcionalidad extra:

En la pantalla de detalle mostramos **recomendaciones** (“Te puede gustar”) basadas en la serie que
se está viendo, usando el endpoint de recomendaciones de TMDB.

**Criterio / lógica:** recomendaciones contextuales a partir del `tvId` actual.  
**Por qué esta solución:** aporta personalización sin inventarnos un sistema complejo y es fácil de
implementar.  
**Evolución futura:** guardar historial local (series abiertas, géneros consultados, puntuaciones) y
reordenar/recomendar mezclando señales propias + recomendaciones de TMDB. Incluso podríamos preparar
un módulo de perfil para una integración futura con ML. Por falta de tiempo y conocimiento
actualmente no se ha integrado esa funcionalidad ni arquitectura específica requerida.

## API Key (TMDB)

La API key se carga desde `local.properties` (no se sube a GitHub).  
Añade esto:
`TMDB_API_KEY=INTRODUCIR_KEY_AQUI`
 
## Notas

- **Tiempo invertido:** aproximadamente **12–14 horas**.
- **Uso de IA:** **sí**, he usado **ChatGPT** como apoyo durante el desarrollo, principalmente para:
  - entender conceptos y requisitos de la prueba,
  - aprender/confirmar el uso de librerías y frameworks requeridos (p. ej. Compose, Paging 3, Hilt),
  - refrescar conocimientos de **Kotlin** (mi experiencia principal es en **Java**, lo que me ha ayudado a asimilar Kotlin y la arquitectura rápidamente).

La implementación final, las decisiones técnicas y la integración de la solución se han realizado por mí.


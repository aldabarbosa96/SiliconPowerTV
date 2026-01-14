# SiliconPowerTV — Prueba técnica Kotlin

App Android para consultar series de TV populares usando la API de TMDB.

Este repositorio es mi entrega para la prueba técnica (V1 + V2).  
Actualmente: **V1 implementada** (listado paginado + detalle).

## Versión 1.0 (funcionalidades básicas)

### Qué incluye
- **Listado de series populares** con paginación (Paging 3).
- **Detalle de una serie** al pulsar en un ítem.
- Arquitectura **MVVM** (ViewModel + repositorio + capa remota).
- UI con **Jetpack Compose** y navegación con **NavHost**.
- Restricciones de diseño V1:
    - **Solo orientación vertical (portrait)**.
    - **Sin modo oscuro** (tema forzado a claro).

### Qué NO incluye (a propósito)
- No he metido todavía Hilt en V1 para no añadir complejidad innecesaria antes de cerrar la base
  funcional.  
- En V2, con Room/offline, tiene más sentido incorporarlo para inyectar DB/DAO/Repo/Network de forma
  limpia.

## Stack técnico
- Kotlin
- Jetpack Compose (Material 3)
- Navigation Compose
- Retrofit + Moshi
- Coroutines / Flow
- Paging 3


## API Key (TMDB)
La API key se carga desde `local.properties` (no se sube a GitHub).
Añade esta línea en tu `local.properties`:

**TMDB_API_KEY=INTRODUCIR_KEY_AQUI**
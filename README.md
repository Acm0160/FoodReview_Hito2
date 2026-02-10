# FoodReview - Hito 1

Aplicación Android (Java) para crear, consultar, editar y borrar reseñas de restaurantes funcionando 100% offline.

## Funcionalidades (Hito 1)
- Login con validación visual (email y contraseña).
- Listado de reseñas con RecyclerView y tarjetas personalizadas.
- Alta/Edición de reseñas con formulario.
- Persistencia local con SQLite (CRUD completo).
- Estado vacío en listado.
- Operaciones de BBDD en segundo plano (Repository + Executor).

## Pantallas
- LoginActivity
- MyReviewsActivity
- AddEditReviewActivity

## Datos (SQLite)
- FoodReviewDbHelper (creación de tabla)
- ReviewDao (CRUD)
- ReviewRepository (hilos y callbacks)

## Cómo ejecutar
1. Abrir el proyecto en Android Studio.
2. File → Sync Project with Gradle Files.
3. Run en emulador o dispositivo.

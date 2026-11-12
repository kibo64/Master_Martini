# Master_Martini

# Integrantes:
<br>
Camilo Covarrubias.
<br>
Nicolás Chauca.
<br>
Gabriel Yañez.

---

# Contexto caso
Master Martini Chile SPA es una comercializadora de insumos para alimentos que busca
fortalecer la relación con clientes y prospectos mediante contenidos de valor vinculados
a sus productos. El desafío considera cursos, recetas y contenidos técnicos dirigidos a
emprendedores y profesionales de panaderías, pastelerías, chocolaterías, casinos,
restaurantes y otros negocios gastronómicos. Entre los contenidos mencionados se
encuentran recetas simples, videos técnicos y cursos de especialidad en chocolatería,
pastelería de vitrina, heladería, bollería, masas de hoja, galletas, empanadas y productos
de horneo.

---

## Tecnologías y Arquitectura
El proyecto está desarrollado en **Kotlin** utilizando **Jetpack Compose** para la interfaz de usuario, siguiendo el patrón de arquitectura **MVVM (Model-View-ViewModel)** para garantizar la mantenibilidad y organización del código.

La estructura del proyecto se divide principalmente en:
- **Screens y Composables:** Componentes de interfaz de usuario desacoplados y reutilizables.

- **ViewModel:** Gestión del estado de la UI y lógica de validación de negocio.

- **Model / Repository:** Manejo de datos y conexión con la base de datos local.


---

# Funcionalidades Implementadas

### 1. Interfaz Visual y Usabilidad 
- Distribución adaptativa de elementos gastronómicos.

- Implementación de paleta de colores y logotipos corporativos autorizados de Master Martini.

- Tipografías y contrastes optimizados para usuarios mayores de 50 años o con dificultades visuales.


### 2. Formularios Validados y Desacoplados
- **Formulario de Registro/Perfil de Cliente:** Captura de datos ficticios (Nombre, Correo, Teléfono, Dirección, RUT de empresa y Encuesta de intereses).

- **Validación en tiempo real:** Lógica de validación centralizada en el ViewModel (desacoplada de la UI) con íconos de advertencia y mensajes informativos claros por cada campo.


### 3. Persistencia de Datos Local
- Integración de **Room (SQLite)** para el almacenamiento local de perfiles de usuario, cursos inscritos y recetas favoritas de forma persistente.


### 4. Animaciones y Transiciones
- Transiciones fluidas entre pantallas de navegación.

- Animaciones funcionales de retroalimentación (loaders de carga multimedia, efectos visuales al presionar botones).


---

## Instalacion y Ejecución
Para ejecutar este proyecto de manera local en Android Studio:

### clonar:
  1. Clona este repositorio: `git clone https://github.com/kibo64/Master_Martini.git`
  2. Abre el proyecto en **Android Studio**.
  3. Espera a que Gradle sincronice todas las dependencias.
  4. Ejecuta la aplicación en un emulador (API 24+) o dispositivo físico. 
  - *(Nota: Si experimentas problemas de compilación, realiza un `Clean Project` seguido de `Rebuild Project`).*

### zip:
  1. Descomprima el archivo `.zip` entregado.
  2. Abra el directorio raíz del proyecto en **Android Studio**.
  3. Permita que Gradle sincronice las dependencias (Jetpack Compose, Room, Navigation).
  4. Ejecute la aplicación preferentemente en un emulador con **API 24 o superior** o en un dispositivo físico.
  - *(Nota de contingencia: Si el proyecto presenta problemas de ejecución inicial en su entorno, se recomienda realizar un `Build > Clean Project` y luego `Rebuild Project`. Toda la arquitectura de datos está disponible para revisión directa en el código fuente).*

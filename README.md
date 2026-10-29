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

# Funcionalidades Implementadas

### 1. Interfaz Visual y Usabilidad (Material Design 3)
* Distribución adaptativa de elementos gastronómicos.
* Implementación de paleta de colores y logotipos corporativos autorizados de Master Martini.
* Tipografías y contrastes optimizados para usuarios mayores de 50 años o con dificultades visuales.

### 2. Formularios Validados y Desacoplados
* **Formulario de Registro/Perfil de Cliente:** Captura de datos ficticios (Nombre, Correo, Teléfono, Dirección, RUT de empresa y Encuesta de intereses).
* **Validación en tiempo real:** Lógica de validación centralizada en el ViewModel (desacoplada de la UI) con íconos de advertencia y mensajes informativos claros por cada campo.

### 3. Persistencia de Datos Local
* Integración de **Room (SQLite)** para el almacenamiento local de perfiles de usuario, cursos inscritos y recetas favoritas de forma persistente.

### 4. Animaciones y Transiciones
* Transiciones fluidas entre pantallas de navegación.
* Animaciones funcionales de retroalimentación (loaders de carga multimedia, efectos visuales al presionar botones).


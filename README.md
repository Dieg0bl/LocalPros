# LocalPros

**LocalPros** es una aplicación móvil nativa para Android que conecta profesionales de servicios locales con clientes que necesitan sus servicios. La plataforma facilita la contratación de servicios domésticos y de oficios de manera geolocalizada.

## 🏗️ Tecnologías Principales

- **Android Nativo** (Kotlin)
- **Jetpack Compose** - UI moderna y declarativa
- **Firebase** - Backend completo (Auth, Firestore, Analytics, Crashlytics)
- **Google Maps API** - Servicios de geolocalización
- **Hilt/Dagger** - Inyección de dependencias
- **Material Design 3** - Componentes de UI

## 🎯 Funcionalidades Clave

### Para Clientes (Particulares)
- Publicación de ofertas de trabajo/servicios
- Búsqueda geolocalizada de profesionales
- Gestión de presupuestos y calidad de materiales
- Sistema de evaluación y reseñas

### Para Profesionales
- Perfil profesional con indicadores de desempeño
- Gestión de disponibilidad temporal y geográfica
- Aplicación a ofertas de trabajo
- Sistema de reputación y puntuaciones

## 🏢 Categorías de Servicios

La aplicación cubre más de 20 categorías profesionales:

- **Construcción**: Albañil, Electricista, Fontanero, Carpintero, Pintor
- **Mantenimiento**: Jardinero, Mecánico, Soldador, Cerrajero
- **Técnicos**: Climatización, Refrigeración, Electrodomésticos, Informática
- **Especializados**: Energías Renovables, Seguridad, Telecomunicaciones
- **Otros**: Limpieza, Reformas Integrales, y más

## 🏛️ Arquitectura

- **Patrón MVVM** con ViewModels
- **Clean Architecture** con capas separadas
- **Repository Pattern** para gestión de datos
- **Inyección de Dependencias** con Hilt

## 🚀 Configuración del Proyecto

### Prerrequisitos
- Android Studio Hedgehog | 2023.1.1+
- JDK 17+
- SDK de Android (API 23-34)
- Cuenta de Google Cloud Platform (para Maps API)
- Proyecto de Firebase

### Instalación
1. Clona el repositorio
2. Configura tu archivo `google-services.json` de Firebase
3. Añade tu clave de Google Maps API en `build.gradle.kts`
4. Sincroniza el proyecto con Gradle

## 📱 Capturas de Pantalla

*(Añadir capturas de las pantallas principales)*

## 🎨 Diseño

La aplicación implementa Material Design 3 con un enfoque moderno y accesible, optimizada para la experiencia del usuario en dispositivos móviles.

## 🤝 Contribución

Las contribuciones son bienvenidas. Por favor, sigue las convenciones de código existentes y asegúrate de que las pruebas pasen antes de enviar un PR.

## 📄 Licencia

Este proyecto está bajo licencia MIT. Ver el archivo `LICENSE` para más detalles.

---

**LocalPros** - Conectando profesionales locales con clientes de manera eficiente y confiable.
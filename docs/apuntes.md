# Apuntes de hybrid-lab

Repaso teórico de lo que aprendemos en este repo. Cada lección nueva añade o amplía una sección.

## Índice

1. [Qué es brownfield](#1-qué-es-brownfield)
2. [El monorepo](#2-el-monorepo)
3. [iOS básico: SwiftUI, UIKit y ciclo de vida](#3-ios-básico-swiftui-uikit-y-ciclo-de-vida)
4. [Android básico: Activity, Compose y ciclo de vida](#4-android-básico-activity-compose-y-ciclo-de-vida)
5. [Piezas comunes de React Native en una app nativa](#5-piezas-comunes-de-react-native-en-una-app-nativa)
6. [React Native dentro de iOS](#6-react-native-dentro-de-ios)
7. [React Native dentro de Android](#7-react-native-dentro-de-android)
8. [Problemas que encontramos y su causa](#8-problemas-que-encontramos-y-su-causa)
9. [Glosario](#9-glosario)
10. [Lo que falta](#10-lo-que-falta)

---

## 1. Qué es brownfield

Hay dos formas de usar React Native:

- **Greenfield**: la app nace en React Native. El proyecto nativo es solo un envoltorio que genera la plantilla.
- **Brownfield**: la app ya existe y es nativa (Swift, Kotlin). React Native entra después, en algunas pantallas o en trozos de pantalla.

En brownfield la app nativa manda. Ella decide cuándo arranca React Native, qué pantalla de React se muestra y qué datos recibe. React Native es un invitado.

Lo que hay que resolver a mano en brownfield:

- Instalar React Native en un proyecto nativo que no lo esperaba (CocoaPods en iOS, Gradle en Android).
- Arrancar el motor de React Native una sola vez y reutilizarlo.
- Mostrar un componente de React dentro de una pantalla nativa.
- Pasar datos en los dos sentidos.

## 2. El monorepo

Un monorepo es un solo repositorio con varios proyectos dentro. Usamos **Yarn 4 workspaces**: Yarn instala las dependencias de todos los paquetes desde la raíz.

```
hybrid-lab/
├── apps/
│   ├── ios-host/        app nativa iOS (Xcode)
│   └── android-host/    app nativa Android (Android Studio)
└── packages/
    └── rn-app/          código React Native que las apps consumen
```

Regla de reparto: en `apps/` va lo que se instala en un móvil. En `packages/` va lo que otros consumen.

Ideas importantes:

- **Un monorepo no elimina los conflictos de versiones** entre lo nativo y lo híbrido. Solo los pone a la vista en un mismo sitio.
- `nodeLinker: node-modules` en `.yarnrc.yml` hace que Yarn cree una carpeta `node_modules` normal. Las herramientas nativas de React Native (CocoaPods, Gradle) buscan los archivos ahí y no entienden otros formatos.
- Las dependencias se instalan una vez en la raíz. Por eso las rutas de los proyectos nativos apuntan a `../../node_modules/...`.
- No usamos Nx ni herramientas parecidas, porque esconden la parte nativa y aquí queremos verla.

## 3. iOS básico: SwiftUI, UIKit y ciclo de vida

### SwiftUI y UIKit

iOS tiene dos formas de dibujar pantallas:

| | UIKit | SwiftUI |
|---|---|---|
| Estilo | Imperativo: creas vistas y las cambias tú | Declarativo: describes la vista según el estado |
| Pieza principal | `UIViewController` (una pantalla) y `UIView` | `View` (un `struct` con `body`) |
| Parecido en React | Manipular el DOM a mano | Componentes de React |

SwiftUI se parece mucho a React:

- `struct ContentView: View` con `var body` es un componente con su `return` de JSX.
- `@State var count = 0` es `useState(0)`. Al cambiar el valor, SwiftUI vuelve a calcular `body`.
- `NavigationStack` y `NavigationLink` son la navegación por pila, como un stack navigator.

Las dos formas conviven. Para usar una pieza de UIKit dentro de SwiftUI hay dos adaptadores:

- `UIViewControllerRepresentable`: mete una pantalla UIKit (`UIViewController`) en SwiftUI.
- `UIViewRepresentable`: mete una vista UIKit (`UIView`) en SwiftUI.

Los dos tienen dos funciones:

- `make…`: se llama una vez para crear la pieza. Es como montar el componente.
- `update…`: se llama cada vez que cambian los datos que recibe de SwiftUI. Es como un nuevo render con props nuevas.

### Ciclo de vida

**De la app**, con `scenePhase` en SwiftUI:

- `active`: la app está en primer plano y responde.
- `inactive`: visible pero sin responder (por ejemplo, al abrir el centro de control).
- `background`: la app ya no se ve.

**De una pantalla UIKit** (`UIViewController`), en este orden:

1. `init`: se crea el objeto.
2. `loadView`: crea su vista principal. Aquí ponemos la vista de React Native.
3. `viewDidLoad`: la vista existe. Se llama una sola vez.
4. `viewWillAppear` / `viewDidAppear`: antes y después de mostrarse. Se repite cada vez que vuelve a verse.
5. `viewWillDisappear` / `viewDidDisappear`: antes y después de ocultarse.
6. `deinit`: el objeto se destruye y libera memoria. Es como el desmontaje definitivo.

Detalle de Swift: dentro de un `UIViewController`, `print(...)` llama a un método de la clase y no a la función global. Por eso escribimos `Swift.print(...)`.

## 4. Android básico: Activity, Compose y ciclo de vida

### Activity e Intent

- Una **Activity** es una pantalla. Cada una se declara en `AndroidManifest.xml`.
- Un **Intent** es un mensaje para abrir otra Activity. Puede llevar datos extra (`putExtra`), parecido a los parámetros de una ruta.
- La Activity que se abre lee esos datos con `intent.getStringExtra(...)`.

### Compose

Jetpack Compose es el equivalente de SwiftUI en Android. También se parece a React:

- Una función con `@Composable` es un componente.
- `remember { mutableStateOf(0) }` es `useState(0)`.
- `DisposableEffect(key) { onDispose { ... } }` es un `useEffect` con función de limpieza.
- `AndroidView` mete una vista Android clásica (`View`) dentro de Compose. Es el equivalente de `UIViewRepresentable`.
- `Modifier` agrupa tamaño, márgenes y estilo, parecido a la prop `style`.

### `remember` y `rememberSaveable`

- `remember` guarda un valor mientras la función siga en pantalla. Se pierde si la Activity se destruye.
- `rememberSaveable` además guarda el valor en un `Bundle` que Android conserva cuando destruye y vuelve a crear la Activity. Sobrevive a una rotación.

### Ciclo de vida de una Activity

1. `onCreate`: se crea. Aquí se monta la interfaz.
2. `onStart`: se hace visible.
3. `onResume`: está en primer plano y responde.
4. `onPause`: pierde el primer plano.
5. `onStop`: deja de verse.
6. `onDestroy`: se destruye.

**Por defecto Android destruye y vuelve a crear la Activity al girar la pantalla.** Por eso se pierde el estado de `remember`. Con `android:configChanges` en el manifiesto, la Activity se queda viva y solo recibe un aviso del cambio.

### Kotlin y los valores nulos

Kotlin distingue entre tipos que pueden ser `null` (`ReactHost?`) y tipos que no (`ReactHost`). Es como `strictNullChecks` de TypeScript, pero siempre activo.

- `valor?.metodo()`: llama solo si no es `null`. Igual que `?.` en JS.
- `valor!!`: afirmas que no es `null`. Si lo es, la app falla en ese punto.
- `valor ?: otro`: valor por defecto si es `null`. Igual que `??` en JS.

## 5. Piezas comunes de React Native en una app nativa

Estas ideas valen para iOS y Android.

### El motor se crea una vez

El motor de React Native (Hermes, el hilo de JS y los módulos) tarda en arrancar. Se crea una sola vez para toda la app y todas las pantallas de React lo comparten.

- iOS: `RCTReactNativeFactory`, guardado en nuestro `ReactNativeRuntime.shared`.
- Android: `ReactHost`, guardado en `MainApplication`.

### `AppRegistry` y `moduleName`

En `packages/rn-app/index.js` registramos componentes con un nombre:

```js
AppRegistry.registerComponent('HelloScreen', () => HelloScreen);
AppRegistry.registerComponent('InlineCard', () => InlineCard);
```

El lado nativo pide un componente por ese nombre (`moduleName`). El nombre es el único contrato entre los dos lados.

### Surface

Una **surface** es un árbol de React montado en una vista nativa. Cada vez que el nativo muestra un componente de React, crea una surface nueva:

- Cada surface tiene su propio `rootTag` (un identificador).
- Si la surface se destruye, React desmonta el componente y su estado se pierde.
- Varias surfaces pueden vivir a la vez sobre el mismo motor.

### Props iniciales y props actualizadas

- **Props iniciales**: el nativo las pasa al crear la surface. En iOS es `initialProperties`; en Android, un `Bundle` (launch options). Llegan al componente como props normales.
- **Props actualizadas**: si el nativo cambia las props de una surface viva, React hace un nuevo render sin desmontar. El estado interno (por ejemplo, los likes) se conserva.

Las props solo admiten datos simples: textos, números, booleanos, listas y diccionarios.

### Metro y el bundle

- En **Debug** el JS se descarga de Metro (`yarn workspace @hybrid-lab/rn-app start`). Por eso hay que tener Metro arrancado.
- En **Release** el JS va dentro de la app en un archivo (`main.jsbundle` en iOS).

## 6. React Native dentro de iOS

### Instalación

- React Native se instala con **CocoaPods**, el gestor de dependencias nativas de iOS.
- El `Podfile` llama a `use_react_native!` con la ruta de `packages/rn-app` y a `use_native_modules!` para el autolinking (detectar librerías nativas desde `package.json`).
- `bundle exec pod install` crea `HostApp.xcworkspace`. Desde ese momento se abre el workspace y no el `.xcodeproj`.
- `.xcode.env.local` dice a Xcode dónde está `node`, porque Xcode no ve el `node` de nvm.

### Piezas de código

- `ReactNativeRuntime`: guarda el delegado y la factoría. El delegado decide de dónde sale el JS (Metro o `main.jsbundle`).
- `ReactViewController`: pantalla UIKit cuya vista es una surface de React. En `deinit` se destruye la surface y React desmonta el componente.
- `ReactScreen`: adaptador `UIViewControllerRepresentable` para usar esa pantalla desde SwiftUI. Lo usamos para **navegar de nativo a una pantalla de React**.
- `ReactView`: adaptador `UIViewRepresentable` para meter un **trozo de React dentro de una pantalla SwiftUI** (`InlineCard`).

### Cómo cambian las props en iOS

`ReactView.updateUIView` se ejecuta cada vez que cambia el estado de SwiftUI. Ahí asignamos `rootView.appProperties = properties`. La surface recibe las props nuevas y React vuelve a renderizar sin desmontar.

Resultado comprobado:

- Cada vez que abres `HelloScreen` aparece un `rootTag` nuevo. Al volver atrás, el contador se pierde.
- En `InlineCard`, el título cambia con el contador nativo y los likes se mantienen.

## 7. React Native dentro de Android

### Instalación

- React Native se instala con el **plugin de Gradle** de React Native, que sale de `node_modules/@react-native/gradle-plugin`.
- `settings.gradle.kts` activa el autolinking con el comando del CLI ejecutado desde `packages/rn-app`.
- `app/build.gradle.kts` configura el bloque `react { ... }` con las rutas al monorepo y añade `react-android` y `hermes-android`.
- `gradle.properties` activa la nueva arquitectura y Hermes.
- `local.properties` dice a Gradle dónde está `node`, porque Android Studio no ve el `node` de nvm.

### Piezas de código

- `MainApplication`: la clase `Application` es lo primero que crea Android, antes de cualquier Activity. Implementa `ReactApplication` y crea el `ReactHost` con `by lazy` (se crea la primera vez que alguien lo pide). `loadReactNative(this)` carga las librerías nativas.
- `HelloReactActivity`: hereda de `ReactActivity`, que ya sabe mostrar una pantalla de React. `getMainComponentName` dice qué componente. `getLaunchOptions` convierte el extra del Intent en props iniciales. Así **navegamos de Compose a una pantalla de React**.
- `ReactView` (Compose, en curso): crea una surface con `reactHost.createSurface(...)`, la arranca con `start()`, la muestra con `AndroidView` y la para con `stop()` en `onDispose`. Para actualizar props llama a `updateInitProps`.

Resultado comprobado:

- `HelloScreen` se abre con el valor que manda Compose.
- El contador de React sobrevive a la rotación gracias a `configChanges`.
- Al cerrar y abrir de nuevo, el contador vuelve a cero (surface nueva).

## 8. Problemas que encontramos y su causa

| Problema | Causa | Solución |
|---|---|---|
| CocoaPods no reconoce el proyecto de Xcode | El formato del proyecto era más nuevo que la librería `xcodeproj` | Bajar el formato del proyecto a Xcode 16.0 |
| `pod` no existe | CocoaPods estaba instalado para otra versión de Ruby | Usar Bundler: `bundle exec pod install` |
| Xcode o Android Studio no encuentran `node` | nvm solo existe en la terminal | `.xcode.env.local` y `node.binary` en `local.properties` |
| Error de enlazado `___gxx_personality_v0` | Faltaba la librería estándar de C++ | `-lc++` en Other Linker Flags |
| Los scripts de React Native fallan en Xcode | User Script Sandboxing bloquea el acceso a archivos | Desactivar User Script Sandboxing |
| Gradle rechaza los repositorios del proyecto | `FAIL_ON_PROJECT_REPOS` | Cambiar a `PREFER_SETTINGS` |
| `java.util.Properties` no se encuentra en el script de Gradle | `java` es también un accesor de Gradle y tapa el paquete | `import java.util.Properties` al principio |
| `createSurface` no compila | `reactHost` puede ser `null` en la interfaz | Afirmar que no es `null` con `!!` |

## 9. Glosario

- **Brownfield**: integrar React Native en una app nativa que ya existe.
- **Surface**: un árbol de React montado en una vista nativa.
- **`moduleName`**: el nombre con que se registró el componente en `AppRegistry`.
- **`rootTag`**: identificador de cada surface.
- **Autolinking**: detección automática de librerías nativas de React Native a partir de `package.json`.
- **Hermes**: el motor de JavaScript de React Native.
- **Nueva arquitectura**: la arquitectura actual de React Native (Fabric para la interfaz, Turbo Modules para módulos nativos, JSI para hablar con C++ sin puente).
- **Metro**: el servidor que empaqueta y sirve el JS en desarrollo.
- **CocoaPods / Gradle**: gestores de dependencias y compilación de iOS y Android.
- **Representable**: adaptador de SwiftUI para usar piezas de UIKit.

## 10. Lo que falta

- [ ] Terminar `InlineCard` dentro de Compose.
- [ ] Desde React Native, pedir datos al nativo (Turbo Module).
- [ ] Desde el nativo, emitir eventos a React Native.
- [ ] Repetir el módulo con Nitro Modules y con Expo Modules, y compararlos.
- [ ] Compilar en Release con el bundle dentro de la app.

**<h1>Desarrollo de las Preguntas requerimiento App Lista de Tareas</h1>**

**<h3>:blue_book: 1. Investigar y explicar brevemente qué son los patrones de diseño y por qué usar MVVM en Android:</h3>**

<p>Los patrones de diseño son soluciones generales a problemas recurrentes en el desarrollo de software. No son recetas de código, sino formas de organizar responsabilidades para que el sistema sea más fácil de entender, mantener y extender.</p>
<p>En aplicaciones móviles esto es especialmente importante, porque las apps cambian rápido, crecen en funcionalidades y dependen del ciclo de vida de la interfaz.</p>
<p>En Android, uno de los patrones más usados es MVVM (Model–View–ViewModel), porque:</p>
<ul>
    <li>Separa responsabilidades:</li>
        <li>Model: datos y reglas de negocio (por ejemplo, la entidad Task y el TaskRepository que maneja las tareas).</li>
        <li>View: lo que ve y toca el usuario (Activity/Fragment o, en este caso, pantallas con Jetpack Compose).</li>
        <li>ViewModel: actúa como puente, expone datos observables y funciones para que la vista interactúe con la lógica sin conocer los detalles internos.</li>
    <li>Respeta el ciclo de vida de Android:</li>
        <li>Los ViewModel sobreviven a cambios de configuración (rotación de pantalla, cambio de tema), evitando perder el estado y reduciendo código “pegado” en la Activity.</li>
    <li>Facilita pruebas y mantenimiento:</li>
        <li>La lógica de negocio se concentra en el ViewModel y en el repositorio. Esto permite probar esa lógica sin necesidad de levantar la UI y también cambiar la vista (por ejemplo, pasar de XML a Compose) sin reescribir todo.</li>
    <li>Encaja con Android Jetpack:</li>
        <li>Componentes como ViewModel, LiveData, Room, Navigation, etc., fueron diseñados para trabajar en conjunto bajo este tipo de patrón, lo que reduce código manual y errores de ciclo de vida.</li>
    <li>En esta app de lista de tareas, MVVM se refleja así:</li>
        <li>Task como modelo, TaskRepository / InMemoryTaskRepository en la capa de datos, TaskListViewModel como capa de lógica, y una pantalla Compose que solo observa cambios y llama funciones del ViewModel.</li>
</ul>

**<h3>:blue_book: 3. Demostrar el funcionamiento de observadores y explicar cómo mejora la escalabilidad:</h3>**

<p>En la implementación, el TaskListViewModel expone la lista de tareas como:</p>
<p>val tasks: LiveData<List<Task>></p>
<p>La UI no manipula directamente la lista interna del repositorio. En su lugar, se suscribe como observador de ese LiveData. En Compose, la pantalla TaskListScreen hace algo como:</p>
<ul>
    <li>val tasks by viewModel.tasks.observeAsState(emptyList())</li>
    <li>El flujo de trabajo es:</li>
</ul>

1.	El usuario interactúa con la UI (agregar, marcar como completada, eliminar).
2.	La pantalla llama a una función del ViewModel (addTask, toggleTaskCompleted, deleteTask).
3.	El ViewModel actualiza los datos a través del repositorio y cambia el valor del MutableLiveData interno.
4.	LiveData notifica automáticamente a sus observadores activos.
5.	Compose detecta el nuevo estado y recompone la UI mostrando la lista actualizada.

<p>Esto mejora la escalabilidad porque:</p>
<ul>
    <li>La vista no sabe de dónde vienen los datos (memoria, base de datos local, API remota). Solo “reacciona” a los cambios en el LiveData. Si más adelante cambiamos la implementación del repositorio, la UI casi no se toca.</li>
    <li>Permite tener múltiples observadores del mismo estado sin duplicar lógica. Por ejemplo, otra pantalla o componente podría observar las mismas tareas.</li>
    <li>Reduce el acoplamiento: la UI se limita a presentar el estado y delegar acciones. La lógica de cuándo y cómo actualizar los datos queda centralizada en el ViewModel.</li>
</ul>

<p>Limitación reconocida:</p>
<ul>
    <li>LiveData es suficiente para este escenario y cumple la pauta, pero para flujos más complejos (estados de carga, errores, streams continuos) una alternativa más moderna como StateFlow/Flow ofrece mayor flexibilidad. </li>
    <li>Aun así, para esta app y el objetivo de demostrar observadores, LiveData es una elección correcta y coherente.</li>
</ul>

**<h3>:blue_book: 4. Preparar la estructura del proyecto pensando en la posibilidad de agregar nuevas funciones (por ejemplo, base de datos o consumo de API):</h3>**

<p>La estructura del proyecto se organizó en paquetes separados por responsabilidad:</p>

```Android
📁 om.example.listadetareas
├─ 📁 data
│   ├─ 📁 model          → entidades de dominio (por ejemplo, Task)
│   └─ 📁 repository     → contrato y lógica de acceso a datos
└─ 📁 ui
    └─ 🟦 tasklist       → ViewModel y pantalla(s) de la lista de tareas
```
<ul>
    <li>En data.model está Task, que representa la entidad principal de la aplicación.</li>
    <li>En data.repository se define el contrato TaskRepository y una implementación inicial InMemoryTaskRepository, que guarda las tareas en memoria.</li>
    <li>En ui.tasklist están TaskListViewModel, que contiene la lógica de negocio (agregar, completar, eliminar), y TaskListScreen, que muestra la lista usando Jetpack Compose y observa los cambios vía LiveData.</li>
</ul>

<p>Esta separación deja el proyecto preparado para agregar nuevas funciones sin romper lo existente:</p>
<ul>
    <li>Para agregar base de datos local, se puede crear una nueva implementación de TaskRepository usando Room (por ejemplo, RoomTaskRepository) y conectarla al ViewModel, manteniendo intacta la UI.</li>
    <li>Para consumir una API, se puede crear un RemoteTaskRepository que use Retrofit/Ktor, y luego combinarlo con una base local o caché si se necesita.</li>
    <li>Para notificaciones o tareas programadas, se pueden agregar casos de uso que reaccionen a eventos del ViewModel (por ejemplo, al crear una tarea con fecha límite, programar un trabajo con WorkManager).</li>
</ul>

<p>Limitación actual (y posible mejora futura):</p>
<ul>
    <li>El TaskListViewModel instancia directamente InMemoryTaskRepository().</li>
    <li>Eso es suficiente para un primer módulo demostrativo, pero acopla la lógica a una implementación concreta.</li>
    <li>Una mejora natural en una etapa posterior sería introducir inyección de dependencias (por ejemplo, con Hilt) o un ViewModelProvider.</li>
    <li>Factory para inyectar el TaskRepository.</li>
    <li>De esa forma, cambiar de repositorio en memoria a base de datos o API no requeriría modificar el ViewModel.</li>
</ul>

**<h3>:book: Conclusión:</h3>**

<p>La aplicación implementa una lista de tareas sencilla, pero construida con MVVM, ViewModel y LiveData, respetando la separación de responsabilidades:</p>
<ul>
    <li>El patrón MVVM organiza el código en modelo, vista y ViewModel, facilitando mantenimiento y pruebas.</li>
    <li>Los observadores (LiveData + Compose) desacoplan la UI de la fuente de datos y permiten que la pantalla se actualice automáticamente ante cambios de estado.</li>
    <li>La estructura de paquetes por capas (data y ui) y el uso de un repositorio detrás de una interfaz dejan el proyecto listo para crecer hacia base de datos, APIs y notificaciones sin reescribir todo.</li>
    <li></li>
</ul>
<p>Al mismo tiempo, se reconocen mejoras futuras (inyección de dependencias, posible uso de Flow en vez de LiveData) que permitirían llevar esta base a una arquitectura aún más robusta si el proyecto se vuelve más complejo.</p>
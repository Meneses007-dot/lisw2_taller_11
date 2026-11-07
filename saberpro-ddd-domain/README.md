# Banco de Preguntas Saber PRO — Taller 11: Domain-Driven Design (DDD)

**Universidad del Cauca**  
**Facultad de Ingeniería Electrónica y Telecomunicaciones**  
**Departamento de Sistemas — Laboratorio de Ingeniería de Software II (2026.2)**  
**Docente:** Ing. Paola Andrea Bedoya Toro  
**Integrantes:** Santiago Rodríguez — Juan Manuel Meneses Daza  

---

## 1. Propósito y Objetivos del Taller

Este proyecto implementa la solución completa del **Taller 11: Domain-Driven Design (DDD) — Modelado Estratégico y Táctico en el Banco de Preguntas Saber PRO**.

Los objetivos primordiales alcanzados son:
1. **Modelado Estratégico de DDD:** Descomposición formal del sistema en cuatro **Contextos Delimitados (Bounded Contexts)** autónomos, definiendo el **Lenguaje Ubicuo** y el **Mapa de Contextos (Context Map)**.
2. **Modelado Táctico de DDD:** Implementación rigurosa de los bloques de construcción tácticos:
   - **Raíz de Agregado (Aggregate Root):** `Pregunta`.
   - **Objetos de Valor (Value Objects):** `Enunciado`, `Justificacion`, `OpcionRespuesta`.
   - **Entidades (Entities):** `Revisor`, `Revision`.
   - **Enumeraciones de Dominio:** `EstadoPregunta`, `Dictamen`.
   - **Invariantes de Negocio:** Reglas inviolables protegidas por el agregado.
   - **Contratos de Repositorio Puros:** `PreguntaRepository` sin abstracciones de persistencia pesada.
3. **Aislamiento Absoluto del Núcleo del Dominio:** El módulo de dominio está programado en **Java 17+ puro**, libre al 100% de cualquier dependencia o anotación de frameworks técnicos externos (**sin Spring Boot, sin Jakarta Persistence / JPA, sin Hibernate, sin Lombok**). Las únicas dependencias del proyecto se encuentran en alcance exclusivo de pruebas (`test scope`) con **JUnit 5**.

---

## 2. Arquitectura y Estructura del Dominio

Siguiendo las directrices del taller, la estructura del proyecto se organiza bajo el paquete raíz `co.edu.unicauca.saberpro.domain`:

```text
saberpro-ddd-domain/
├── pom.xml                                   # Configuración Maven Java 17+, JUnit 5 (scope test)
├── README.md                                 # Documentación técnica integral
└── src/
    ├── main/java/co/edu/unicauca/saberpro/domain/
    │   ├── model/                            # Clases de Dominio Puras (Java 17+)
    │   │   ├── Pregunta.java                 # AGGREGATE ROOT (Gobierna invariantes y transiciones)
    │   │   ├── Enunciado.java                # VALUE OBJECT (Inmutable, valida texto no nulo >= 5 car.)
    │   │   ├── Justificacion.java            # VALUE OBJECT (Inmutable, >= 15 car., sin palabras prohibidas)
    │   │   ├── OpcionRespuesta.java          # VALUE OBJECT (Inmutable, texto + indicador de clave correcta)
    │   │   ├── EstadoPregunta.java           # ENUM (BORRADOR, PENDIENTE_REVISION, EN_REVISION, APROBADA, RECHAZADA)
    │   │   ├── Revisor.java                  # ENTITY (Docente evaluador con identidad persistente única)
    │   │   ├── Revision.java                 # ENTITY (Evaluación pedagógica con ID, dictamen y observaciones)
    │   │   └── Dictamen.java                 # ENUM (APROBADO, RECHAZADO, REQUIERE_AJUSTES)
    │   ├── exception/                        # Excepciones de Negocio
    │   │   ├── DomainException.java          # Excepción base del dominio (RuntimeException)
    │   │   └── ReglaNegocioException.java    # Violación de invariantes de negocio
    │   └── repository/                       # Contratos de Repositorio Puros
    │       └── PreguntaRepository.java       # Interfaz pura de persistencia para el agregado Pregunta
    └── test/java/co/edu/unicauca/saberpro/domain/
        └── model/
            ├── PreguntaTest.java             # Pruebas de invariantes del agregado (JUnit 5 anidadas)
            ├── JustificacionTest.java        # Pruebas de validación y de igualdad por valor
            ├── EnunciadoTest.java            # Pruebas de nulidad, longitud mínima e inmutabilidad
            ├── OpcionRespuestaTest.java      # Pruebas de opciones, clave y distractor
            └── RevisionTest.java             # Pruebas de identidad de entidades y observaciones obligatorias
```

---

## 3. Modelado Estratégico (Bounded Contexts)

El Banco de Preguntas Saber PRO se descompone en 4 Contextos Delimitados (Bounded Contexts) con responsabilidades y conceptos bien definidos:

| Bounded Context | Responsabilidad Principal | Conceptos del Lenguaje Ubicuo |
| :--- | :--- | :--- |
| **Contexto de Autoría** | Gestión de reactivos DCE (Diseño Centrado en Evidencias), enunciados, distractores, clave y justificaciones pedagógicas formuladas por los docentes autores. | `Pregunta`, `Enunciado`, `Clave`, `Distractor`, `Justificacion`, `Autor` |
| **Contexto de Validación** | Verificación de reglas de calidad estructural, pedagógica y de formato antes de permitir el avance del reactivo. | `ReglaEstructural`, `FiltroExpresion`, `ValidezOpciones`, `Invariante` |
| **Contexto de Ciclo de Vida** | Control estricto de las fases y transiciones válidas de las preguntas durante el flujo editorial. | `EstadoPregunta`, `Transicion`, `Borrador`, `PendienteRevision`, `EnRevision` |
| **Contexto de Revisión de Pares** | Asignación de evaluadores expertos, dictamen técnico y registro de observaciones pedagógicas constructivas. | `Revisor`, `Asignacion`, `Evaluacion`, `Revision`, `Dictamen` |

### Relaciones en el Mapa de Contextos (Context Map)
- **Autoría -> Ciclo de Vida:** El contexto de Autoría se comunica con el Ciclo de Vida para solicitar el cambio de estado de `BORRADOR` a `PENDIENTE_REVISION` una vez se satisfacen las invariantes.
- **Autoría -> Validación:** Validación actúa como un filtro protector que aplica las reglas estructurales y los filtros de expresiones no deseadas.
- **Revisión de Pares -> Ciclo de Vida:** El veredicto del revisor (`APROBADO`, `RECHAZADO`, `REQUIERE_AJUSTES`) determina la transición hacia `APROBADA` o el retorno a `BORRADOR` para subsanación.

---

## 4. Modelado Táctico (Invariantes y Encapsulamiento)

### 4.1 Objetos de Valor (Value Objects)
- **`Justificacion`**: Inmutable. Garantiza que el texto explicativo no sea nulo, cuente con al menos **15 caracteres** y rechaza expresiones vacías o coloquiales no pedagógicas (`"porque sí"`, `"es obvio"`, `"obvio"`, `"es la correcta"`, `"sin justificar"`, `"por descarte"`). Implementa igualdad estructural (`equals` y `hashCode`).
- **`Enunciado`**: Inmutable. Valida que el texto no sea nulo ni vacío y contenga al menos 5 caracteres.
- **`OpcionRespuesta`**: Inmutable. Modela cada alternativa con su texto y el flag booleano `esCorrecta`.

### 4.2 Entidades (Entities)
- **`Revisor`**: Entidad identificada por su ID único de docente. Modela al par evaluador calificado.
- **`Revision`**: Entidad identificada por su ID de evaluación. Exige que todo dictamen de rechazo o ajuste contenga observaciones pedagógicas explícitas (mínimo 10 caracteres).

### 4.3 Raíz del Agregado (Aggregate Root): `Pregunta`
Garantiza las siguientes **invariantes de negocio inviolables**:
1. **Modificación restringida al estado Borrador:** Las opciones, enunciados y justificaciones únicamente pueden agregarse o modificarse si la pregunta está en `EstadoPregunta.BORRADOR`. En cualquier otro estado se lanza `ReglaNegocioException`.
2. **Límite de opciones:** Una pregunta de selección múltiple no puede contener más de 4 opciones. Intentar agregar una quinta opción dispara `ReglaNegocioException("Una pregunta no puede tener más de 4 opciones.")`.
3. **Invariantes para el envío a revisión (`enviarARevision()`):**
   - Debe poseer exactamente 4 opciones de respuesta (`opciones.size() == 4`).
   - Debe poseer exactamente 1 opción correcta marcada (`correctas == 1`).
   - Debe contar con un `Enunciado` válido asignado.
   - Debe contar con una `Justificacion` pedagógica válida asignada.
4. **Encapsulamiento de colecciones:** `getOpciones()` y `getRevisiones()` retornan colecciones inmodificables (`Collections.unmodifiableList(...)`), impidiendo alteraciones externas por fuera de la raíz del agregado.
5. **Transición controlada del ciclo de vida:** Al registrar un dictamen aprobado pasa a `APROBADA`. Al registrar un rechazo o ajuste regresa a `BORRADOR`, habilitando de nuevo al autor para corregir la pregunta.

---

## 5. Pruebas Unitarias (JUnit 5)

La suite de pruebas contiene **48 casos de prueba**, todos exitosos, sin ninguna falla ni error:

- **`PreguntaTest` (17 pruebas):**
  - Validación del ejercicio del PDF: `noDebePermitirEnviarARevisionSinCuatroOpciones()`.
  - Validación de exactamente 4 opciones y mensajes exactos de excepción.
  - Validación de unicidad de clave: rechazo con 0 claves correctas o más de 1 clave correcta.
  - Prohibición de agregar más de 4 opciones.
  - Inmutabilidad y protección de encapsulamiento (`UnsupportedOperationException` al modificar la lista externa).
  - Bloqueo de modificaciones fuera del estado `BORRADOR`.
  - Transiciones de ciclo de vida completas: envío, inicio de revisión, aprobación y rechazo.
- **`JustificacionTest` (12 pruebas):**
  - Nulidad, textos vacíos, textos con espacios blancos.
  - Longitud menor a 15 caracteres.
  - Detección de palabras prohibidas con pruebas parametrizadas (`@ParameterizedTest`).
  - Igualdad por valor de Objetos de Valor.
- **`EnunciadoTest` (6 pruebas):**
  - Nulidad, textos en blanco, longitud mínima.
  - Igualdad estructural.
- **`OpcionRespuestaTest` (6 pruebas):**
  - Opciones correctas (clave) y distractores.
  - Igualdad estructural y consistencia de atributos.
- **`RevisionTest` (7 pruebas):**
  - Semántica de entidad: igualdad por identificador único.
  - Exigencia de observaciones pedagógicas en dictámenes de rechazo.

---

## 6. Instrucciones de Compilación y Ejecución

### Compilación y ejecución con Apache Maven
```bash
# Situarse en la carpeta del proyecto
cd saberpro-ddd-domain

# Compilar y ejecutar la suite completa de pruebas JUnit 5
mvn clean test
```

El proyecto usa el _layout_ estándar de Maven (`src/main/java` y `src/test/java`), por lo que la compilación y la ejecución de pruebas son reproducibles desde cero sin configuración adicional.

---

## 7. Verificación de Cero Dependencias Externas

Para corroborar que el dominio es 100% puro y no posee contaminación de frameworks, se puede ejecutar:

```powershell
Get-ChildItem -Path "src\main\java" -Recurse -Filter "*.java" | Select-String "org.springframework", "jakarta.persistence", "javax.persistence", "org.hibernate", "lombok"
```

*Resultado esperado:* **0 coincidencias**.

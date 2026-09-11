package modelo.Juego5; // Paquete de lógica del juego



/**
 * CLASE: FabricaExamenes
 * -------------------------------------------------------------
 * Aplica el PATRÓN FACTORY: una clase responsable de CREAR objetos complejos.
 * Aquí se crean todos los exámenes con sus preguntas reales de programación.
 *
 * Esto desacopla la creación de objetos de su uso:
 * la vista no necesita saber cómo se construye un Examen, solo lo pide a la Fábrica.
 *
 * Cada examen tiene 5 preguntas de opción múltiple sobre el tema del día.
 * Las preguntas están inspiradas en el estilo del Maestro Sergio Mora.
 *
 * Aplica: Encapsulamiento, Patrones de diseño (Factory)
 */
public class FabricaExamenes {

    // ─── CONSTRUCTOR PRIVADO ──────────────────────────────────────────────────────
    // La clase solo tiene métodos estáticos, no necesita instanciarse
    private FabricaExamenes() {}

    // ─── MÉTODO FACTORY: crear examen del día indicado ────────────────────────────

    /**
     * Crea y retorna el examen correspondiente al día indicado.
     * @param dia número del día (1 al 5)
     * @return Examen completo con sus preguntas listas
     * @throws IllegalArgumentException si el día no es válido (manejo de excepciones)
     */
    public static Examen crearExamen(int dia) {
        // Selecciona el examen según el día usando switch
        switch (dia) {
            case 1: return crearExamenPOO();          // Día 1: Programación Orientada a Objetos
            case 2: return crearExamenArrayList();     // Día 2: ArrayList y colecciones
            case 3: return crearExamenPilasyColas();   // Día 3: Pilas y Colas (Stack y Queue)
            case 4: return crearExamenCSharp();        // Día 4: Lenguaje C#
            case 5: return crearExamenPython();        // Día 5: Lenguaje Python
            default:
                // Manejo de excepciones: día inválido
                throw new IllegalArgumentException("Día de examen no válido: " + dia +
                    ". Solo se aceptan días del 1 al 5.");
        }
    }

    // ─── EXAMEN DÍA 1: PROGRAMACIÓN ORIENTADA A OBJETOS ─────────────────────────

    /**
     * Crea el examen de POO del día 1.
     * Preguntas sobre pilares de POO: herencia, encapsulamiento, polimorfismo, abstracción.
     * @return Examen con 5 preguntas de POO
     */
    private static Examen crearExamenPOO() {
        // Crea el objeto Examen con nombre, descripción y tema
        Examen examen = new Examen(
            "Examen Día 1 - POO",
            "Examen sobre los fundamentos de Programación Orientada a Objetos",
            "Programación Orientada a Objetos"
        );

        // ── PREGUNTA 1: Herencia ──────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Qué pilar de la POO permite que una clase reutilice atributos y métodos de otra?",
            new String[]{
                "Encapsulamiento",   // Opción A (índice 0)
                "Polimorfismo",      // Opción B (índice 1)
                "Herencia",          // Opción C (índice 2) ← CORRECTA
                "Abstracción"        // Opción D (índice 3)
            },
            2, // Índice 2 = opción C = "Herencia" es la correcta
            "La HERENCIA permite crear clases hijas que extienden (extends) una clase padre, " +
            "heredando sus atributos y métodos. Ejemplo: class Perro extends Animal."
        ));

        // ── PREGUNTA 2: Encapsulamiento ───────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Cuál modificador de acceso hace que un atributo SOLO sea visible dentro de su propia clase?",
            new String[]{
                "public",    // A: visible en todas partes
                "protected", // B: visible en la clase y sus hijas
                "default",   // C: visible solo en el paquete
                "private"    // D: solo dentro de la clase ← CORRECTA
            },
            3, // Índice 3 = opción D = "private"
            "'private' restringe el acceso al atributo solo dentro de la clase que lo declara. " +
            "Es la base del encapsulamiento: ocultar los datos internos."
        ));

        // ── PREGUNTA 3: Polimorfismo ──────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "Si la clase Animal tiene un método hablar(), y las clases Perro y Gato lo sobreescriben, ¿qué concepto se aplica?",
            new String[]{
                "Abstracción",    // A
                "Polimorfismo",   // B ← CORRECTA
                "Herencia",       // C
                "Encapsulamiento" // D
            },
            1, // Índice 1 = opción B = "Polimorfismo"
            "POLIMORFISMO: el mismo método hablar() se comporta diferente en Perro (ladra) " +
            "y en Gato (maulla). 'Muchas formas del mismo método'."
        ));

        // ── PREGUNTA 4: Clase abstracta ───────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Qué característica tiene una clase abstracta en Java?",
            new String[]{
                "Puede instanciarse con new",                // A
                "No puede tener métodos",                    // B
                "No puede instanciarse directamente",        // C ← CORRECTA
                "Solo puede tener métodos estáticos"         // D
            },
            2, // Índice 2 = opción C
            "Una clase ABSTRACTA (abstract class) NO puede instanciarse con 'new'. " +
            "Solo sirve como molde para sus clases hijas. Puede tener métodos abstractos."
        ));

        // ── PREGUNTA 5: Interface ─────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Cuál es la diferencia entre una interface y una clase abstracta en Java?",
            new String[]{
                "No hay diferencia",                                          // A
                "Una interface puede tener atributos con estado (variables)", // B
                "Una clase puede implementar múltiples interfaces, pero solo heredar una clase", // C ← CORRECTA
                "Las interfaces pueden instanciarse"                          // D
            },
            2, // Índice 2 = opción C
            "En Java, una clase solo puede EXTENDER (heredar) UNA clase (abstracta o no), " +
            "pero puede IMPLEMENTAR múltiples interfaces. Esto soluciona la herencia múltiple."
        ));

        return examen; // Retorna el examen completamente construido
    }

    // ─── EXAMEN DÍA 2: ARRAYLIST ─────────────────────────────────────────────────

    /**
     * Crea el examen de ArrayList del día 2.
     * @return Examen con 5 preguntas sobre ArrayList y colecciones
     */
    private static Examen crearExamenArrayList() {
        Examen examen = new Examen(
            "Examen Día 2 - ArrayList",
            "Examen sobre ArrayList y colecciones en Java",
            "ArrayList"
        );

        // ── PREGUNTA 1 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Qué paquete debes importar para usar ArrayList en Java?",
            new String[]{
                "java.util.List",        // A
                "java.util.ArrayList",   // B ← CORRECTA
                "java.collections.List", // C
                "java.array.ArrayList"   // D
            },
            1,
            "Se importa con 'import java.util.ArrayList;'. El paquete java.util contiene " +
            "las principales estructuras de datos de Java."
        ));

        // ── PREGUNTA 2 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Cuál método agrega un elemento al FINAL de un ArrayList?",
            new String[]{
                "insert(elemento)",  // A
                "push(elemento)",    // B
                "add(elemento)",     // C ← CORRECTA
                "append(elemento)"   // D
            },
            2,
            "El método add() agrega al final del ArrayList. También existe add(índice, elemento) " +
            "para insertar en una posición específica."
        ));

        // ── PREGUNTA 3 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Cómo obtienes el elemento en la posición 3 de un ArrayList llamado 'lista'?",
            new String[]{
                "lista[3]",          // A - sintaxis de array, no funciona en ArrayList
                "lista.get(3)",      // B ← CORRECTA
                "lista.element(3)",  // C
                "lista.fetch(3)"     // D
            },
            1,
            "En ArrayList se usa get(índice), NO los corchetes []. " +
            "Los corchetes son para arrays primitivos (int[], String[])."
        ));

        // ── PREGUNTA 4 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Cuál es la diferencia principal entre un array (int[]) y un ArrayList<Integer>?",
            new String[]{
                "No hay diferencia",                                          // A
                "El ArrayList tiene tamaño fijo, el array crece dinámicamente", // B - al revés
                "El array tiene tamaño fijo; el ArrayList crece dinámicamente", // C ← CORRECTA
                "ArrayList solo funciona con números"                          // D
            },
            2,
            "Un array tiene tamaño FIJO definido al crearlo. Un ArrayList crece y encoge " +
            "dinámicamente según se agregan/eliminan elementos."
        ));

        // ── PREGUNTA 5 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Qué hace este código? ArrayList<String> lista = new ArrayList<>();  lista.remove(0);",
            new String[]{
                "Elimina el elemento con valor '0'",          // A
                "Elimina el ÚLTIMO elemento de la lista",     // B
                "Elimina el elemento en la posición 0 (primero)", // C ← CORRECTA
                "Lanza un error de compilación"               // D
            },
            2,
            "remove(int índice) elimina el elemento en esa posición. remove(Object obj) " +
            "elimina por valor. Como 0 es un int, elimina el elemento en posición 0."
        ));

        return examen; // Retorna el examen construido
    }

    // ─── EXAMEN DÍA 3: PILAS Y COLAS ─────────────────────────────────────────────

    /**
     * Crea el examen de Pilas y Colas del día 3.
     * @return Examen con 5 preguntas sobre Stack y Queue
     */
    private static Examen crearExamenPilasyColas() {
        Examen examen = new Examen(
            "Examen Día 3 - Pilas y Colas",
            "Examen sobre estructuras de datos Stack (Pila) y Queue (Cola)",
            "Pilas y Colas"
        );

        // ── PREGUNTA 1 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Cuál es el principio de funcionamiento de una PILA (Stack)?",
            new String[]{
                "FIFO: el primero en entrar es el primero en salir",   // A
                "LIFO: el último en entrar es el primero en salir",    // B ← CORRECTA
                "Aleatorio: sin orden definido",                        // C
                "FILO: el primero en entrar es el último en salir"     // D
            },
            1,
            "LIFO = Last In, First Out. Como una pila de platos: el último que pones " +
            "encima es el primero que tomas. En Java: Stack<T> o Deque<T>."
        ));

        // ── PREGUNTA 2 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Cuál es el principio de funcionamiento de una COLA (Queue)?",
            new String[]{
                "LIFO: el último en entrar es el primero en salir",   // A
                "HIFO: el más pesado primero",                         // B
                "FIFO: el primero en entrar es el primero en salir",  // C ← CORRECTA
                "Ninguno de los anteriores"                            // D
            },
            2,
            "FIFO = First In, First Out. Como una fila del banco: el primero en llegar " +
            "es el primero en ser atendido. En Java: Queue<T>, LinkedList<T>."
        ));

        // ── PREGUNTA 3 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Qué método agrega un elemento a una Pila (Stack) en Java?",
            new String[]{
                "push(elemento)",    // A ← CORRECTA
                "add(elemento)",     // B - es de List/Queue
                "enqueue(elemento)", // C - concepto de cola, no Java estándar
                "insert(elemento)"   // D
            },
            0,
            "push() agrega al tope de la pila. Su opuesto es pop(), que extrae del tope. " +
            "peek() ve el tope sin extraerlo."
        ));

        // ── PREGUNTA 4 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "Si una Pila contiene [1, 2, 3] (3 es el tope), ¿qué devuelve pop()?",
            new String[]{
                "1",   // A - es el primero que entró
                "2",   // B - es el del medio
                "3",   // C ← CORRECTA - es el tope (último en entrar)
                "Error, la pila está vacía" // D
            },
            2,
            "pop() extrae y devuelve el elemento del TOPE de la pila. Como 3 está en el tope " +
            "(fue el último en entrar con push), pop() devuelve 3."
        ));

        // ── PREGUNTA 5 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Para qué sirve el método peek() tanto en Pila como en Cola?",
            new String[]{
                "Elimina el primer elemento",                          // A
                "Agrega un elemento al final",                         // B
                "Consulta el elemento frontal SIN extraerlo",          // C ← CORRECTA
                "Vacía completamente la estructura"                    // D
            },
            2,
            "peek() permite VER el siguiente elemento a salir (tope en Pila, frente en Cola) " +
            "sin eliminarlo. Es como 'espiar' sin modificar la estructura."
        ));

        return examen; // Retorna el examen construido
    }

    // ─── EXAMEN DÍA 4: C# ────────────────────────────────────────────────────────

    /**
     * Crea el examen de C# del día 4.
     * @return Examen con 5 preguntas sobre C#
     */
    private static Examen crearExamenCSharp() {
        Examen examen = new Examen(
            "Examen Día 4 - C#",
            "Examen sobre el lenguaje de programación C# (C Sharp)",
            "C#"
        );

        // ── PREGUNTA 1 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Qué empresa desarrolló el lenguaje de programación C#?",
            new String[]{
                "Google",     // A
                "Apple",      // B
                "Oracle",     // C
                "Microsoft"   // D ← CORRECTA
            },
            3,
            "C# fue desarrollado por Microsoft en el año 2000, diseñado por Anders Hejlsberg. " +
            "Es parte de la plataforma .NET y es similar a Java en muchos aspectos."
        ));

        // ── PREGUNTA 2 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Cómo se declara una propiedad automática en C#?",
            new String[]{
                "public int Edad { get; set; }",        // A ← CORRECTA
                "private int Edad = get, set;",          // B
                "public int Edad => getset;",            // C
                "int Edad { public get; public set; }"  // D
            },
            0,
            "Las propiedades automáticas en C# usan la sintaxis { get; set; }. " +
            "Es más conciso que Java donde necesitas getEdad() y setEdad() por separado."
        ));

        // ── PREGUNTA 3 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "En C#, ¿qué keyword reemplaza al 'extends' de Java para la herencia?",
            new String[]{
                "inherits",   // A
                "extends",    // B - es de Java, no C#
                ":",          // C ← CORRECTA - en C# se usa ':' para herencia e interfaces
                "base"        // D - base es para llamar al constructor padre, no para heredar
            },
            2,
            "En C#: 'class Perro : Animal' equivale al 'class Perro extends Animal' de Java. " +
            "El ':' también se usa para implementar interfaces en C#."
        ));

        // ── PREGUNTA 4 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Qué es LINQ en C#?",
            new String[]{
                "Un framework para crear interfaces gráficas",                // A
                "Un lenguaje de consulta integrado para manipular colecciones y datos", // B ← CORRECTA
                "Un sistema de tipos dinámicos",                               // C
                "Una librería para conectarse a internet"                      // D
            },
            1,
            "LINQ (Language Integrated Query) permite hacer consultas a colecciones, " +
            "bases de datos y XML con sintaxis similar a SQL directamente en C#. " +
            "Ejemplo: var adultos = personas.Where(p => p.Edad >= 18).ToList();"
        ));

        // ── PREGUNTA 5 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Cuál es la diferencia entre 'struct' y 'class' en C#?",
            new String[]{
                "No hay diferencia, son sinónimos",                               // A
                "struct es tipo por valor (stack); class es tipo por referencia (heap)", // B ← CORRECTA
                "class es tipo por valor; struct es por referencia",              // C - al revés
                "struct no puede tener métodos"                                   // D
            },
            1,
            "En C#, 'struct' es un tipo por VALOR (se copia al asignarse, vive en el stack). " +
            "'class' es por REFERENCIA (las variables apuntan al mismo objeto en el heap). " +
            "En Java no existe struct; todo objeto es por referencia."
        ));

        return examen; // Retorna el examen construido
    }

    // ─── EXAMEN DÍA 5: PYTHON ────────────────────────────────────────────────────

    /**
     * Crea el examen de Python del día 5.
     * @return Examen con 5 preguntas sobre Python
     */
    private static Examen crearExamenPython() {
        Examen examen = new Examen(
            "Examen Día 5 - Python",
            "Examen sobre el lenguaje de programación Python",
            "Python"
        );

        // ── PREGUNTA 1 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Cómo se define una función en Python?",
            new String[]{
                "function miFuncion():",     // A - JavaScript
                "void miFuncion() {}",       // B - Java/C#
                "def miFuncion():",          // C ← CORRECTA
                "func miFuncion():"          // D - Go/Swift
            },
            2,
            "En Python las funciones se definen con 'def'. No necesitan llaves {}, " +
            "usan indentación (espacios/tabs) para delimitar el bloque de código."
        ));

        // ── PREGUNTA 2 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Qué hace esta comprensión de lista en Python?  cuadrados = [x**2 for x in range(5)]",
            new String[]{
                "Crea la lista [0, 1, 2, 3, 4]",         // A - es range(5), no los cuadrados
                "Crea la lista [0, 1, 4, 9, 16]",         // B ← CORRECTA (0², 1², 2², 3², 4²)
                "Crea la lista [1, 4, 9, 16, 25]",        // C - sería range(1,6)
                "Genera un error de sintaxis"              // D
            },
            1,
            "range(5) genera [0,1,2,3,4]. Luego x**2 eleva cada número al cuadrado: " +
            "0²=0, 1²=1, 2²=4, 3²=9, 4²=16. Resultado: [0, 1, 4, 9, 16]."
        ));

        // ── PREGUNTA 3 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "En Python, ¿cuál es la diferencia entre una lista y una tupla?",
            new String[]{
                "No hay diferencia",                                          // A
                "Las listas son inmutables; las tuplas son mutables",        // B - al revés
                "Las listas son mutables; las tuplas son inmutables",        // C ← CORRECTA
                "Las tuplas solo pueden contener números"                    // D
            },
            2,
            "Lista [1,2,3]: MUTABLE, puedes agregar, eliminar y modificar elementos. " +
            "Tupla (1,2,3): INMUTABLE, una vez creada no puede modificarse. " +
            "Las tuplas son más rápidas y se usan para datos que no deben cambiar."
        ));

        // ── PREGUNTA 4 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Qué imprime este código Python?  print('Hola' * 3)",
            new String[]{
                "Error: no se puede multiplicar texto",  // A
                "HolaHolaHola",                          // B ← CORRECTA
                "Hola3",                                 // C
                "Hola Hola Hola"                         // D - con espacios, sería diferente
            },
            1,
            "En Python, multiplicar un string por un número lo repite esa cantidad de veces. " +
            "'Hola' * 3 = 'HolaHolaHola'. También funciona con listas: [0] * 5 = [0,0,0,0,0]."
        ));

        // ── PREGUNTA 5 ────────────────────────────────────────────────────────────
        examen.agregarPregunta(new Pregunta(
            "¿Qué es un decorador (@) en Python?",
            new String[]{
                "Un comentario especial",                                           // A
                "Una función que modifica/envuelve el comportamiento de otra función", // B ← CORRECTA
                "Un operador matemático",                                            // C
                "Una forma de declarar variables constantes"                        // D
            },
            1,
            "Un decorador es una función que recibe otra función y extiende su comportamiento " +
            "sin modificarla directamente. Ejemplo: @staticmethod, @property, @login_required. " +
            "Es un patrón de diseño 'Decorator' implementado directamente en el lenguaje."
        ));

        return examen; // Retorna el examen construido
    }
}

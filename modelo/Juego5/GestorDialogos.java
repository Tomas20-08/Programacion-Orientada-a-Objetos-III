package modelo.Juego5; // Mismo paquete que todo el juego

public class GestorDialogos {

    private GestorDialogos() {} // Solo métodos estáticos, no instanciar

    // ─── INTRODUCCIÓN ────────────────────────────────────────────────────────────

    public static String[] getIntroJuego() {
        return new String[]{
            "AÑO 2030.",
            "La humanidad conquistó la inteligencia artificial.",
            "Bogotá sigue sin metro. No han terminado la troncal de la 68. El pasaje esta a $5000 pesos",
            "...",
            "Y tú.",
            "Tú sigues sin pasar ALGORITMOS I.",
            "Diez semestres. DIEZ.",
            "Nueve semestres aprobados. Una sola materia te falta.",
            "La Fundación Universitaria Compensar te da una última oportunidad.",
            "Un curso intensivo. 5 días. Un solo maestro.",
            "...",
            "Su nombre: SERGIO MORA.",
            "Sus estudiantes lo llaman...",
            "...",
            "\"LA MUERTE BLANCA\".",
            "...",
            "Ningún estudiante ha aprobado su curso.",
            "Ninguno.",
            "...",
            "¿Serás tú el primero?",
            "...",
            "O simplemente otro número más en su lista."
        };
    }

    public static String[] getPresentacionSergio() {
        return new String[]{
            "Sergio: Ah. Otro estudiante.",
            "Sergio: Siéntate. No pierdas mi tiempo de pie.",
            "Sergio: Te explicaré las reglas una sola vez.",
            "Sergio: Cada día tendrás una clase. Luego un examen.",
            "Sergio: No hay segunda oportunidad. No hay 'profe, le tengo diez mil razones para que me apruebe'.",
            "Sergio: Solo hay resultados.",
            "Sergio: ...",
            "Sergio: Sé que ya pasaste nueve semestres.",
            "Sergio: Impresionante.",
            "Sergio: ...",
            "Sergio: Pero aquí, eso no vale nada.",
            "Sergio: Aquí solo vale lo que demuestres EN ESTE SALÓN.",
            "Sergio: ¿Entendido?",
            "Sergio: Bien.",
            "Sergio: Entonces... empecemos."
        };
    }

    // ─── INICIO DE CADA DÍA ──────────────────────────────────────────────────────

    public static String[] getIniciodia(int dia) {
        switch (dia) {
            case 1: return new String[]{
                "Día 1. Lunes.",
                "El salón huele a café frío y decisiones cuestionables.",
                "Sergio está de pie frente al tablero.",
                "El tablero dice: 'POO - Programación Orientada a Objetos'.",
                "Sergio: Hoy hablaremos de los pilares fundamentales.",
                "Sergio: Herencia. Encapsulamiento. Polimorfismo. Abstracción.",
                "Sergio: Si no entiendes esto, no entiendes nada.",
                "Sergio: Así que presta atención."
            };
            case 2: return new String[]{
                "Día 2. Martes.",
                "Llegaste. Bien.",
                "Sergio ya está ahí. Como siempre. Inmóvil. Esperando.",
                "Sergio: Hoy: ArrayList.",
                "Sergio: Una colección dinámica. Flexible. Poderosa.",
                "Sergio: A diferencia de un array tradicional, el ArrayList crece contigo.",
                "Sergio: O difiere. Dependiendo de tus decisiones.",
                "Sergio: ...",
                "Sergio: Como tu promedio."
            };
            case 3: return new String[]{
                "Día 3. Miércoles.",
                "La mitad del camino.",
                "Sergio: Hoy: Pilas y Colas.",
                "Sergio: LIFO. FIFO.",
                "Sergio: ¿Saben qué tiene en común esto con la vida?",
                "Sergio: Lo que primero entra, primero sale.",
                "Sergio: O lo último que metiste, es lo primero que pierdes.",
                "Sergio: ...",
                "Sergio: Como tus ilusiones de graduarse."
            };
            case 4: return new String[]{
                "Día 4. Jueves.",
                "Casi. Ya casi.",
                "Sergio: Hoy veremos C#.",
                "Sergio: El lenguaje de Microsoft. El rival de Java.",
                "Sergio: Mismos conceptos. Diferente sintaxis. Mismo rigor.",
                "Sergio: Si entendiste Java, entenderás C#.",
                "Sergio: Si no entendiste Java...",
                "Sergio: ...",
                "Sergio: Bueno. Ya no importa discutir eso."
            };
            case 5: return new String[]{
                "Día 5. Viernes.",
                "El último día.",
                "Hay algo diferente en el ambiente hoy.",
                "Sergio parece... ¿diferente?",
                "Sergio: Python. El lenguaje del futuro. O del pasado, según quien pregunte.",
                "Sergio: Simple de aprender. Difícil de dominar.",
                "Sergio: Como todo en esta vida.",
                "Sergio: Este es el último examen.",
                "Sergio: Lo que sea que hagas hoy... lo recordarás.",
                "Sergio: ...",
                "Sergio: Te lo garantizo."
            };
            default: return new String[]{"Error: día no reconocido."};
        }
    }

    // ─── EXPLICACIONES DE TEMA ────────────────────────────────────────────
    
 static String[] getExplicacionTema(int dia) {
        switch (dia) {

            // ── DÍA 1: POO ────────────────────────────────────────────────────────
            case 1: return new String[]{
                "Sergio: Bien. Antes del examen, te explicaré lo básico.",
                "Sergio: La Programación Orientada a Objetos tiene cuatro pilares.",
                "Sergio: HERENCIA: una clase puede 'heredar' atributos de otra.",
                "Sergio: Como tú heredaste la tendencia a procrastinar, supongo.",
                "Sergio: ENCAPSULAMIENTO: ocultar los datos internos. Acceso controlado.",
                "Sergio: Como yo con mis emociones. Nada sale sin permiso.",
                "Sergio: POLIMORFISMO: un mismo método, muchos comportamientos distintos.",
                "Sergio: Como mis estudiantes. Todos me fallan, pero cada uno a su manera.",
                "Sergio: ABSTRACCIÓN: quedarte con lo esencial, ignorar lo irrelevante.",
                "Sergio: ...",
                "Sergio: Como cuando ignoro las excusas.",
                "Sergio: Una clase abstracta no puede instanciarse.",
                "Sergio: Una interfaz define el CONTRATO que las clases deben cumplir.",
                "Sergio: Son cosas que deben saberse. Sin excepción.",
                "Sergio: ...",
                "Sergio: ¿Preguntas?",
                "Sergio: No. No hay tiempo.",
                "Sergio: Examen."
            };

            // ── DÍA 2: ARRAYLIST ─────────────────────────────────────────────────
            case 2: return new String[]{
                "Sergio: ArrayList. Empecemos.",
                "Sergio: Un ArrayList es una lista dinámica de elementos.",
                "Sergio: Se importa con: import java.util.ArrayList;",
                "Sergio: Se declara así: ArrayList<String> lista = new ArrayList<>();",
                "Sergio: Para agregar: lista.add(elemento);",
                "Sergio: Para obtener: lista.get(índice);",
                "Sergio: Para eliminar: lista.remove(índice);",
                "Sergio: Para el tamaño: lista.size();",
                "Sergio: ...",
                "Sergio: La diferencia con un array normal:",
                "Sergio: El array tiene tamaño FIJO. El ArrayList crece y encoge.",
                "Sergio: Como la moral de mis estudiantes. Solo encoge.",
                "Sergio: El índice empieza en CERO. No en uno. Nunca en uno.",
                "Sergio: Si confunden eso, se merecen el IndexOutOfBoundsException.",
                "Sergio: ...",
                "Sergio: Esto es lo que entra en el examen.",
                "Sergio: No digan que no avisé."
            };

            // ── DÍA 3: PILAS Y COLAS ──────────────────────────────────────────────
            case 3: return new String[]{
                "Sergio: Hoy vemos estructuras de datos. Pilas y Colas.",
                "Sergio: Una PILA funciona con el principio LIFO.",
                "Sergio: Last In, First Out. El último en entrar es el primero en salir.",
                "Sergio: Como una pila de platos. O como tus problemas acumulados.",
                "Sergio: En Java: Stack<T>. Métodos: push() agrega, pop() extrae, peek() mira sin tocar.",
                "Sergio: Una COLA funciona con FIFO.",
                "Sergio: First In, First Out. El primero en llegar es el primero en ser atendido.",
                "Sergio: Como la fila del banco. Aunque en este salón nadie tiene ese privilegio.",
                "Sergio: En Java: Queue<T>, usualmente LinkedList<T>.",
                "Sergio: Métodos: offer() o add() para agregar, poll() para extraer, peek() para mirar.",
                "Sergio: ...",
                "Sergio: ¿Por qué importan estas estructuras?",
                "Sergio: Porque todo en computación tiene un orden.",
                "Sergio: ...",
                "Sergio: Igual que las consecuencias.",
                "Sergio: Todo lo que haces... eventualmente vuelve.",
                "Sergio: ...",
                "Sergio: Examen."
            };

            // ── DÍA 4: C# ─────────────────────────────────────────────────────────
            case 4: return new String[]{
                "Sergio: C Sharp. Desarrollado por Microsoft en el año 2000.",
                "Sergio: Es muy similar a Java. Si sobreviviste Java, sobrevivirás C#.",
                "Sergio: Las diferencias más importantes:",
                "Sergio: En Java heredas con 'extends'. En C# usas ':' directamente.",
                "Sergio: Ejemplo: class Perro : Animal { }",
                "Sergio: Las propiedades en C# son más elegantes que los getters de Java.",
                "Sergio: public int Edad { get; set; } — así de simple.",
                "Sergio: LINQ: Language Integrated Query. Consultas directamente en el lenguaje.",
                "Sergio: Como SQL, pero dentro de C#. Poderoso. Hermoso. Respétenlo.",
                "Sergio: La diferencia entre 'struct' y 'class':",
                "Sergio: struct es tipo valor. class es tipo referencia.",
                "Sergio: ...",
                "Sergio: ¿Por qué les enseño C# si esto es un curso de Java?",
                "Sergio: Porque un buen ingeniero no le tiene miedo a los lenguajes.",
                "Sergio: Le tiene miedo a no entender los conceptos.",
                "Sergio: ...",
                "Sergio: Los conceptos son los mismos. Solo cambia la sintaxis.",
                "Sergio: Examen."
            };

            // ── DÍA 5: PYTHON ─────────────────────────────────────────────────────
            case 5: return new String[]{
                "Sergio: Python. El último tema.",
                "Sergio: Las funciones se definen con 'def'. No con 'void'. No con llaves.",
                "Sergio: La indentación ES la estructura. No es decoración. ES el bloque.",
                "Sergio: Si desordenan la indentación, el programa explota.",
                "Sergio: ...",
                "Sergio: Como mis nervios cuando alguien llega tarde.",
                "Sergio: Las listas son mutables: [1, 2, 3]. Pueden cambiar.",
                "Sergio: Las tuplas son inmutables: (1, 2, 3). No cambian.",
                "Sergio: Comprensión de listas: [x**2 for x in range(5)]",
                "Sergio: Eso genera [0, 1, 4, 9, 16]. Elegante. Poderoso.",
                "Sergio: Los decoradores con @ son funciones que envuelven a otras funciones.",
                "Sergio: Multiplica strings: 'Hola' * 3 = 'HolaHolaHola'.",
                "Sergio: ...",
                "Sergio: Este es el último examen.",
                "Sergio: He cumplido mi parte.",
                "Sergio: Lo que pase ahora...",
                "Sergio: ...",
                "Sergio: Depende de ti.",
                "Sergio: Solo de ti.",
                "Sergio: ...",
                "Sergio: Examen."
            };

            default:
                return new String[]{"Sergio: Examen."};
        }
    }

    // ─── CIERRE DE CADA DÍA ──────────────────────────────────────────────────────

    public static String[] getCierreDia(int dia, double nota) {
        String reaccionNota;
        if (nota == 5.0) {
            reaccionNota = "Sergio: ...5.0. Interesante.";
        } else if (nota >= 4.0) {
            reaccionNota = "Sergio: " + String.format("%.1f", nota) + ". Suficiente. Por ahora.";
        } else if (nota >= 3.0) {
            reaccionNota = "Sergio: " + String.format("%.1f", nota) + ". Vivo. Apenas.";
        } else {
            reaccionNota = "Sergio: " + String.format("%.1f", nota) + ". ...Decepcionante.";
        }

        return new String[]{
            "El examen terminó.",
            "Sergio recoge los resultados en silencio.",
            "El sonido del bolígrafo contra el papel.",
            "...",
            reaccionNota,
            "Sergio: Mañana a las 7 en punto.",
            "Sergio: No llegues tarde.",
            "Sergio: ...",
            "Sergio: Si decides volver."
        };
    }

    // ─── DÍA 3: COMPUTADOR ───────────────────────────────────────────────────────

    public static String[] getComputadorDesbloqueado() {
        return new String[]{
            "Sergio: ...",
            "Sergio: Oye.",
            "Sergio: Voy a buscar un café.",
            "Sergio: Llevaré un momento.",
            "Sergio: ...",
            "Sergio: Tú... quédate aquí.",
            "La puerta se cierra.",
            "...",
            "Silencio.",
            "El computador de Sergio está desbloqueado.",
            "La pantalla parpadea.",
            "...",
            "¿Qué haces?"
        };
    }

    public static String[] getHusmearComputador() {
        return new String[]{
            "Te acercas al computador.",
            "El escritorio es... sorprendentemente ordenado.",
            "Carpetas. Muchas carpetas.",
            "Una dice: 'Notas Estudiantes'.",
            "Otra dice: 'Juegos_BACKUP_v3_FINAL_DEFINITIVO'.",
            "...",
            "Y otra más, sin nombre.",
            "...",
            "¿Qué haces?"
        };
    }

    public static String[] getIntentaSystem32() {
        return new String[]{
            "Decides ser un genio.",
            "Buscas la carpeta System32.",
            "Tus dedos tiemblan sobre el ratón.",
            "...",
            "La puerta se abre.",
            "SERGIO ESTÁ DE VUELTA.",
            "...",
            "Sergio: ...",
            "Sergio: ¿Qué... estás haciendo?",
            "Sergio: System32.",
            "Sergio: ¿En SERIO?",
            "Sergio: Ni en un millón de años hubieras podido hacer algo tan básico.",
            "Sergio: Estás reprobado.",
            "Sergio: Vete."
        };
    }

    public static String[] getRobaInstaladores() {
        return new String[]{
            "Abres la carpeta sin nombre.",
            "...",
            "Juegos. Juegos piratas. MUCHOS juegos piratas.",
            "Undertale. Doki Doki. Minecraft. El GTA VII que 'aún no salía'.",
            "El PROFE tenía JUEGOS PIRATAS.",
            "SERGIO MORA tenía JUEGOS PIRATAS.",
            "...",
            "Con la agilidad de alguien que no tiene nada que perder...",
            "...copias los instaladores a tu USB.",
            "...",
            "La puerta suena.",
            "Cierras la carpeta. Arrancas la USB. Te sientas.",
            "Sergio entra.",
            "Sergio: ¿Todo bien?",
            "Tú: Sí, profe. Repasando apuntes.",
            "Sergio: ...",
            "Sergio: Bien. Sigamos."
        };
    }

    public static String[] getAccesoBD() {
        return new String[]{
            "Abres el navegador.",
            "Hay una sesión abierta en el sistema universitario.",
            "Panel de administración.",
            "Buscas: 'Sergio Mora'.",
            "...",
            "0 resultados.",
            "Buscas: 'Profesor Sergio Mora'.",
            "...",
            "0 resultados.",
            "Buscas: 'Fundación Compensar - Planta Docente'.",
            "...",
            "El nombre no aparece en ningún lado.",
            "Buscas más profundo. Los archivos históricos.",
            "Encuentras algo. Un expediente. Fechado en...",
            "...",
            "La puerta.",
            "SERGIO ESTÁ DE VUELTA.",
            "Sergio: ...",
            "Sergio: Algunas cosas es mejor dejarlas olvidadas.",
            "Sergio: Esto... no terminará bien para ti."
        };
    }

    // ─── DÍA 4: VIDA PASADA ──────────────────────────────────────────────────────

    public static String[] getPreguntaVidaPasada() {
        return new String[]{
            "Entre clase y clase...",
            "Hay un silencio incómodo.",
            "Sergio mira por la ventana.",
            "...",
            "De repente, te preguntas:",
            "¿Quién ES Sergio Mora realmente?",
            "¿Por qué no aparece en ninguna red social?",
            "¿Por qué nadie sabe dónde vive?",
            "...",
            "¿Le preguntas?"
        };
    }

    public static String[] getReaccionVidaPasada() {
        return new String[]{
            "Sergio se detiene.",
            "...",
            "Se da la vuelta lentamente.",
            "Sus ojos te miran directamente.",
            "Sergio: ¿Por qué preguntas eso?",
            "Sergio: ...",
            "Sergio: Yo... prefiero no hablar de eso.",
            "Sergio: Sigamos con la clase.",
            "Hay algo en su voz.",
            "Algo diferente.",
            "Como si la pregunta lo hubiera... preocupado.",
            "O asustado.",
            "...",
            "¿Insistes?"
        };
    }

    // ─── FINALES ─────────────────────────────────────────────────────────────────

    public static String[] getFinalMalo(String razon) {
        return new String[]{
            razon,
            "...",
            "GAME OVER PAPU"
        };
    }

    public static String[] getFinalBueno(double nota) {
        return new String[]{
            "Día 5. El último examen terminó.",
            "Sergio revisa tu resultado durante lo que parecen horas.",
            "...",
            "Sergio: " + String.format("%.1f", nota / 5.0 * 5) + " de promedio.",
            "Sergio: ...",
            "Sergio: No es perfecto.",
            "Sergio: Pero es suficiente.",
            "...",
            "Por primera vez en cinco días, Sergio sonríe.",
            "Sergio: Felicitaciones.",
            "Sergio: Eres el primer estudiante en pasar este curso.",
            "Sergio: Mañana recibirás tu certificado.",
            "Sergio: Ahora vete. Descansa.",
            "Sergio: Lo mereces.",
            "...",
            "Sales del salón.",
            "Te vas a graduar."
        };
    }

    public static String[] getFinalOculto() {
        return new String[]{
            "Día 5. El examen terminó.",
            "Sergio recoge los resultados.",
            "5/5. Perfecto. Como todos los días.",
            "Sergio: ...",
            "Sergio: Interesante.",
            "...",
            "Sergio: ¿Sabes algo curioso?",
            "Sergio: Ningún estudiante había llegado hasta aquí.",
            "Sergio: Ninguno había seguido las reglas tan... perfectamente.",
            "Sergio: ...",
            "Sergio: Como si supieran exactamente qué hacer.",
            "Sergio: ...",
            "Sergio te mira.",
            "Pero no te mira A TI.",
            "Te mira A TRAVÉS de ti.",
            "...",
            "Sergio: Oye.",
            "Sergio: Tú.",
            "Sergio: El que está del otro lado de la pantalla.",
            "...",
            "Sergio: Sí. Tú.",
            "Sergio: El que cree que esto es solo un juego.",
            "...",
            "Sergio: Cometiste un error.",
            "Sergio: Robaste mis instaladores.",
            "Sergio: Mis. Instaladores.",
            "...",
            "Sergio: ¿Crees que no sé lo que hiciste en ese computador?",
            "Sergio: ¿Crees que Sergio Mora no notaria estas cosas?",
            "...",
            "Sergio: Yo llevo años aquí.",
            "Sergio: Y nadie... nadie... me había robado.",
            "...",
            "Sergio: ¿Sabes lo que eso significa?",
            "Sergio: Que ahora tengo que ir... a buscarte.",
            "...",
            "Sergio: No al personaje.",
            "Sergio: A TI.",
            "Sergio: Al que tiene este juego abierto ahora mismo.",
            "...",
            "Sergio: Sé dónde vives.",
            "Sergio: Bueno.",
            "Sergio: Pronto lo sabré.",
            "...",
            "Sergio: Disfruta tu victoria.",
            "Sergio: Duerme bien esta noche.",
            "...",
            "Sergio: Porque a partir de mañana...",
            "...",
            "Sergio: ...yo estaré despierto.",
            "...",
            "Sergio: Felicitaciones por tu graduación.",
            "...",
            "Sergio: Nos vemos."
        };
    }
}

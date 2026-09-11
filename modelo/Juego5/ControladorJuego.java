package modelo.Juego5;

public class ControladorJuego {

    private static ControladorJuego instancia = null;

    private final VentanaPrincipal ventana;
    private final EstadoJuego      estado;
    private final GestorMusica     musica;

    private ControladorJuego(VentanaPrincipal ventana) {
        this.ventana = ventana;
        this.estado  = EstadoJuego.getInstance();
        this.musica  = GestorMusica.getInstance();
    }

    public static ControladorJuego getInstance(VentanaPrincipal ventana) {
        if (instancia == null) {
            instancia = new ControladorJuego(ventana);
        }
        return instancia;
    }

    public void iniciarJuego() {
        instancia = null;
        instancia = new ControladorJuego(ventana);
        instancia.arrancar();
    }

    private void arrancar() {
        estado.reiniciar();
        musica.reproducir(GestorMusica.PISTA_AMBIENTE);
        ventana.mostrarNarrativa();

        ventana.getPanelNarrativa().iniciarDialogos(
            GestorDialogos.getIntroJuego(),
            "Fondo2",
            null,
            () -> mostrarPresentacionSergio()
        );
    }

    private void mostrarPresentacionSergio() {
        ventana.getPanelNarrativa().iniciarDialogos(
            GestorDialogos.getPresentacionSergio(),
            "Fondo1",
            "Sprite1",
            () -> iniciarEleccionDia(1)
        );
    }

    private void iniciarEleccionDia(int dia) {
        String pregunta = "Día " + dia + " — Son las 7:00 AM.\n" +
                          "El salón del Maestro Sergio está al fondo del pasillo.\n" +
                          "¿Qué haces?";

        String[] opciones = {
            "Ingresar a la clase del Maestro Sergio.",
            "Darse de baja del programa intensivo."
        };

        Runnable[] acciones = {
            () -> iniciarDia(dia),
            () -> activarFinalMaloPorDesercion()
        };

        ventana.getPanelNarrativa().cambiarSprite("Sprite2");
        ventana.getPanelNarrativa().mostrarOpciones(pregunta, opciones, acciones);
    }

    private void activarFinalMaloPorDesercion() {
        estado.setFinalMaloActivado(true);
        musica.reproducir(GestorMusica.PISTA_FINAL_MALO);

        ventana.getPanelNarrativa().iniciarDialogos(
            new String[]{
                "Te das la vuelta, sin mirar atras.",
                "El pasillo parece más largo que antes.",
                "Afuera, el aire se siente pesado",
                "...",
                "Te diste de baja.",
                "Otro número más en la lista de Sergio."
            },
            "Fondo2", null,
            () -> ventana.mostrarFinal(0, "Elegiste darte de baja.\nNunca sabrás si podrías haberlo logrado.")
        );
    }

    private void iniciarDia(int dia) {
        ventana.getPanelNarrativa().iniciarDialogos(
            GestorDialogos.getIniciodia(dia),
            "Fondo1",
            "Sprite3",
            () -> mostrarExplicacionTema(dia)
        );
    }

    private void mostrarExplicacionTema(int dia) {
        ventana.getPanelNarrativa().iniciarDialogos(
            GestorDialogos.getExplicacionTema(dia),
            "Fondo1",
            "Sprite3",
            () -> iniciarExamenDia(dia)
        );
    }

    private void iniciarExamenDia(int dia) {
        try {
            Examen examen = FabricaExamenes.crearExamen(dia);
            estado.registrarExamen(examen);

            musica.reproducir(GestorMusica.PISTA_EXAMEN);
            ventana.mostrarExamen();

            ventana.getPanelExamen().iniciarExamen(examen, (nota) -> {
                procesarResultadoDia(dia, nota);
            });

        } catch (IllegalArgumentException e) {
            System.err.println("[ControladorJuego] Error examen día " + dia + ": " + e.getMessage());
        }
    }

    private void procesarResultadoDia(int dia, double nota) {
        estado.registrarNotaDia(nota);

        musica.reproducir(GestorMusica.PISTA_AMBIENTE);
        ventana.mostrarNarrativa();

        ventana.getPanelNarrativa().iniciarDialogos(
            GestorDialogos.getCierreDia(dia, nota),
            "Fondo1",
            "Sprite7",
            () -> {
                if (dia < 5) {
                    estado.avanzarAlSiguienteDia();
                }
                manejarPostDia(dia, nota);
            }
        );
    }

    private void manejarPostDia(int dia, double nota) {
        switch (dia) {
            case 1:
                iniciarEleccionDia(2);
                break;
            case 2:
                iniciarEleccionDia(3);
                break;
            case 3:
                // Evalúa los 3 días completos para desbloquear la computadora
                double nd1 = estado.getNotaDia(1);
                double nd2 = estado.getNotaDia(2);
                double nd3 = estado.getNotaDia(3);
                
                if (nd1 == 5.0 && nd2 == 5.0 && nd3 == 5.0) {
                    mostrarSecuenciaComputador();
                } else {
                    iniciarEleccionDia(4);
                }
                break;
            case 4:
                mostrarSecuenciaVidaPasada();
                break;
            case 5:
                activarFinalCorrespondiente();
                break;
            default:
                System.err.println("[ControladorJuego] Día no reconocido: " + dia);
        }
    }

    private void mostrarSecuenciaComputador() {
        ventana.getPanelNarrativa().iniciarDialogos(
            GestorDialogos.getComputadorDesbloqueado(),
            "Fondo1", "Sprite6",
            () -> ofrecerOpcionComputador()
        );
    }

    private void ofrecerOpcionComputador() {
        ventana.getPanelNarrativa().iniciarDialogos(
            GestorDialogos.getHusmearComputador(),
            "Fondo1", null,
            () -> {
                String pregunta = "El computador de Sergio está desbloqueado.\n¿Qué haces?";
                String[] opciones = {
                    "No husmear. Volver a mi silla.",
                    "Curiosear el computador..."
                };
                Runnable[] acciones = {
                    () -> iniciarEleccionDia(4),
                    () -> ofrecerOpcionesComputadorInterno()
                };
                ventana.getPanelNarrativa().mostrarOpciones(pregunta, opciones, acciones);
            }
        );
    }

    private void ofrecerOpcionesComputadorInterno() {
        String pregunta = "Abres el escritorio del computador.\n¿Qué haces?";
        String[] opciones = {
            "Eliminar la carpeta System32.",
            "Copiar los instaladores de juegos pirata del profe.",
            "Ingresar a la base de datos de la Universidad."
        };
        Runnable[] acciones = {
            () -> activarRutaSystem32(),
            () -> activarRutaInstaladores(),
            () -> activarRutaBaseDatos()
        };
        ventana.getPanelNarrativa().mostrarOpciones(pregunta, opciones, acciones);
    }

    private void activarRutaSystem32() {
        ventana.getPanelNarrativa().iniciarDialogos(
            GestorDialogos.getIntentaSystem32(),
            "Fondo1", "Sprite5",
            () -> {
                estado.setFinalMaloActivado(true);
                musica.reproducir(GestorMusica.PISTA_FINAL_MALO);
                ventana.mostrarFinal(0, "Sergio: \"Ni en un millón de años hubieras podido hacer algo tan básico.\"\n\"Estás reprobado.\"");
            }
        );
    }

    private void activarRutaInstaladores() {
        estado.setRoboInstaladores(true);
        estado.setAccedioComputador(true);
        estado.setOpcionComputador(2);

        ventana.getPanelNarrativa().iniciarDialogos(
            GestorDialogos.getRobaInstaladores(),
            "Fondo1", "Sprite1",
            () -> iniciarEleccionDia(4)
        );
    }

    private void activarRutaBaseDatos() {
        ventana.getPanelNarrativa().iniciarDialogos(
            GestorDialogos.getAccesoBD(),
            "Fondo1", "Sprite5",
            () -> {
                estado.setFinalMaloActivado(true);
                musica.reproducir(GestorMusica.PISTA_FINAL_MALO);
                ventana.mostrarFinal(0, "Sergio: \"Algunas cosas es mejor dejarlas olvidadas.\"\n\"Esto no terminará bien para ti.\"");
            }
        );
    }

    private void mostrarSecuenciaVidaPasada() {
        ventana.getPanelNarrativa().iniciarDialogos(
            GestorDialogos.getPreguntaVidaPasada(),
            "Fondo1", "Sprite4",
            () -> {
                String pregunta = "De repente surge la duda...\n¿Le preguntas a Sergio sobre su pasado?";
                String[] opciones = {
                    "No. Mejor no meterme en eso.",
                    "Sí, le pregunto sobre su pasado."
                };
                Runnable[] acciones = {
                    () -> iniciarEleccionDia(5),
                    () -> mostrarInsistenciaVida()
                };
                ventana.getPanelNarrativa().mostrarOpciones(pregunta, opciones, acciones);
            }
        );
    }

    private void mostrarInsistenciaVida() {
        estado.incrementarInsistencia();

        ventana.getPanelNarrativa().iniciarDialogos(
            GestorDialogos.getReaccionVidaPasada(),
            "Fondo1", "Sprite5",
            () -> {
                if (estado.getVecesInsistio() < 3) {
                    String p = "Sergio está incómodo. ¿Insistes?";
                    String[] ops = { "No insistir más.", "Insistir de nuevo." };
                    Runnable[] acs = {
                        () -> iniciarEleccionDia(5),
                        () -> mostrarInsistenciaVida()
                    };
                    ventana.getPanelNarrativa().mostrarOpciones(p, ops, acs);
                } else {
                    iniciarEleccionDia(5);
                }
            }
        );
    }

    private void activarFinalCorrespondiente() {
        int tipoFinal = estado.determinarFinal();

        switch (tipoFinal) {
            case 0:
                double nf0 = estado.getNotaDia(5);
                String razonMalo = "Obtuviste " + String.format("%.1f", nf0) + "/5 en el examen final.\nNo fue suficiente.";
                musica.reproducir(GestorMusica.PISTA_FINAL_MALO);
                ventana.getPanelNarrativa().iniciarDialogos(
                    GestorDialogos.getFinalMalo(razonMalo),
                    "Fondo1", "Sprite3",
                    () -> ventana.mostrarFinal(0, razonMalo)
                );
                break;
            case 1:
                // ── CORRECCIÓN: calcular el promedio real (notaAcumulada / 5)
                // Antes se pasaba notaAcumulada directamente (ej: 23.0 en lugar de 4.6)
                double acum    = estado.getNotaAcumulada();
                double promedio = acum / 5.0;
                String resumen = "Promedio final: " + String.format("%.1f", promedio) + " / 5.0";
                musica.reproducir(GestorMusica.PISTA_FINAL_BUENO);
                // GestorDialogos.getFinalBueno() también recibe el promedio, no la suma
                ventana.getPanelNarrativa().iniciarDialogos(
                    GestorDialogos.getFinalBueno(promedio),
                    "Fondo1", "Sprite1",
                    () -> ventana.mostrarFinal(1, resumen)
                );
                break;
            case 2:
                musica.reproducir(GestorMusica.PISTA_FINAL_OCULTO);
                ventana.mostrarFinal(2, null);
                break;
            default:
                System.err.println("[ControladorJuego] Tipo de final desconocido: "
                        + "Bueno, necesito fingir que estoy haciendo algo productivo con mi vida XD,"
                        + "Muy buenas a todos guapisimos aqui Vegetta 777"
                        + "En un nuevo directo de karmaland"
                        + "Oh ciedoz, es el martillo, es real "
                        + "La vi, la quise, pero al final, el deseo no era mutuo"
                        + "jamas olvidare las noches de desvelo"
                        + "cuando los unico que nos preocupaba era vernos de nuevo"
                        + "cuando la ansiedad era por querer amar al otro"
                        + "y no por las tensiones entre nostros"
                        + "poco a poco se fue alejando"
                        + "pero de mis " + tipoFinal);
        }
    }
}
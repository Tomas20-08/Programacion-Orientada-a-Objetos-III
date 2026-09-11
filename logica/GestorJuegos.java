package logica;

import modelo.Juego;
import modelo.Juego1.Piano;
import modelo.Juego2.Memoria;
import modelo.Juego3.Buscaminas;
import modelo.Juego4.Snake;
import modelo.Juego5.NovelaGrafica;
import vista.VentanaPrincipal;

import java.util.ArrayList;

// LÓGICA: administra la lista de juegos usando polimorfismo
public class GestorJuegos {

    private ArrayList<Juego> juegos;
    private VentanaPrincipal menuPrincipal; // referencia al menú para poder volver

    public GestorJuegos(VentanaPrincipal menuPrincipal) {
        this.menuPrincipal = menuPrincipal;
        juegos = new ArrayList<>();
        cargarJuegos();
    }

    private void cargarJuegos() {
        juegos.add(new Piano());
        juegos.add(new Memoria());
        juegos.add(new Buscaminas());
        juegos.add(new Snake());
        juegos.add(new NovelaGrafica());
    }

    public ArrayList<Juego> getJuegos() { return juegos; }

    public void iniciarJuego(int indice) {
        try {
            if (indice < 0 || indice >= juegos.size())
                throw new IndexOutOfBoundsException("Opción inválida.");

            menuPrincipal.setVisible(false);          // oculta el menú
            juegos.get(indice).iniciar(menuPrincipal); // abre el juego pasándole el menú
        } catch (IndexOutOfBoundsException e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "Error: " + e.getMessage(), "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getInstrucciones(int indice) {
        try {
            if (indice < 0 || indice >= juegos.size())
                throw new IndexOutOfBoundsException("Opción inválida.");
            return juegos.get(indice).getInstrucciones();
        } catch (IndexOutOfBoundsException e) {
            return "Error: " + e.getMessage();
        }
    }
}

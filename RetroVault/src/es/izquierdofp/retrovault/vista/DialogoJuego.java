package es.izquierdofp.retrovault.vista;

import es.izquierdofp.retrovault.modelo.Juego;
import java.awt.*;
import javax.swing.*;

public class DialogoJuego extends JDialog {

    // Los valores se guardan tal cual en la base de datos, por eso no cambian.
    private static final String[] CONSOLAS = {
        "NES", "SNES", "Mega Drive", "Game Boy",
        "PlayStation", "Nintendo 64", "Game Gear", "Arcade"};
    private static final String[] GENEROS = {
        "Acción", "Aventura", "RPG", "Plataformas", "Puzzle",
        "Deportes", "Lucha", "Disparos", "Estrategia", "Simulación"};
    private static final String[] ESTADOS = {
        "Pendiente", "Jugando", "Completado", "Abandonado"};
    // La posición del array coincide con la puntuación numérica (0 a 5).
    private static final String[] NOTAS = {
        "Sin nota", "★", "★★", "★★★", "★★★★", "★★★★★"};

    private JTextField campoTitulo;
    private JComboBox<String> selPlataforma;
    private JComboBox<String> selGenero;
    private JSpinner selAnio;
    private JComboBox<String> selNota;
    private JComboBox<String> selProgreso;

    private final Juego juego;
    private boolean confirmado = false;

    public DialogoJuego(Frame padre, Juego juegoExistente) {
        super(padre, true);
        boolean esEdicion = juegoExistente != null;
        this.juego = esEdicion ? juegoExistente : new Juego();

        construirInterfaz();
        setTitle(esEdicion ? "Modificar juego" : "Nuevo juego");
        if (esEdicion) {
            volcarJuegoEnCampos();
        }
        setLocationRelativeTo(padre);
    }

    // ═════════════ Construcción de la interfaz ═════════════

    private void construirInterfaz() {
        campoTitulo = new JTextField(25);
        selPlataforma = new JComboBox<>(CONSOLAS);
        selGenero = new JComboBox<>(GENEROS);
        selAnio = new JSpinner(new SpinnerNumberModel(1990, 1970, 2010, 1));
        selNota = new JComboBox<>(NOTAS);
        selProgreso = new JComboBox<>(ESTADOS);

        // Formulario con GridBagLayout: etiquetas a la izquierda, campos a la derecha.
        JPanel zonaCampos = new JPanel(new GridBagLayout());
        zonaCampos.setBorder(BorderFactory.createEmptyBorder(14, 16, 8, 16));
        String[] rotulos = {"Título:", "Plataforma:", "Género:", "Año:", "Nota:", "Progreso:"};
        JComponent[] controles = {campoTitulo, selPlataforma, selGenero,
                                  selAnio, selNota, selProgreso};
        for (int fila = 0; fila < rotulos.length; fila++) {
            colocarFila(zonaCampos, fila, rotulos[fila], controles[fila]);
        }

        add(zonaCampos, BorderLayout.CENTER);
        add(crearBotonera(), BorderLayout.SOUTH);
        pack();
    }

    /** Añade una fila "etiqueta + control" en la posición indicada del panel. */
    private void colocarFila(JPanel panel, int fila, String rotulo, JComponent control) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = fila;
        c.insets = new Insets(4, 4, 4, 4);

        c.gridx = 0;
        c.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel(rotulo), c);

        c.gridx = 1;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        panel.add(control, c);
    }

    private JPanel crearBotonera() {
        JButton aceptar = new JButton("Aceptar");
        JButton descartar = new JButton("Descartar");
        aceptar.addActionListener(e -> aceptarCambios());
        descartar.addActionListener(e -> dispose());

        JPanel botonera = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botonera.add(descartar);
        botonera.add(aceptar);
        return botonera;
    }

    // ═════════════ Lógica del formulario ═════════════

    /** Valida el título y, si es correcto, copia los campos al juego y cierra. */
    private void aceptarCambios() {
        String titulo = campoTitulo.getText().trim();
        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Escribe un título antes de continuar.", "Dato obligatorio",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        copiarCamposAlJuego(titulo);
        confirmado = true;
        dispose();
    }

    private void copiarCamposAlJuego(String titulo) {
        juego.setTitulo(titulo);
        juego.setConsola((String) selPlataforma.getSelectedItem());
        juego.setGenero((String) selGenero.getSelectedItem());
        juego.setAnio((Integer) selAnio.getValue());
        juego.setPuntuacion(selNota.getSelectedIndex());
        juego.setEstado((String) selProgreso.getSelectedItem());
    }

    private void volcarJuegoEnCampos() {
        campoTitulo.setText(juego.getTitulo());
        selPlataforma.setSelectedItem(juego.getConsola());
        selGenero.setSelectedItem(juego.getGenero());
        selAnio.setValue(juego.getAnio());
        selNota.setSelectedIndex(juego.getPuntuacion());
        selProgreso.setSelectedItem(juego.getEstado());
    }

    // ═════════════ Acceso al resultado ═════════════

    public boolean isGuardado() { return confirmado; }
    public Juego getJuego()     { return juego; }
}

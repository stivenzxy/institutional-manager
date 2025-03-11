package vista;

import controlador.ControladorCursosEstudiantes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.IntStream;
import modelo.institucion.Curso;
import modelo.entidades.Estudiante;

public class GestionCursosEstudiantesGUI extends JPanel implements PropertyChangeListener {
    private final JTextField txtAnio, txtSemestre;
    private final JComboBox<Curso> cmbCurso;
    private final JComboBox<Estudiante> cmbEstudiante;
    private final JButton btnGuardar, btnActualizar, btnEliminar, btnCargar;
    private final JTable tablaInscripciones;
    private final DefaultTableModel modeloTabla;

    private final ControladorCursosEstudiantes controlador;

    public GestionCursosEstudiantesGUI() {
        setLayout(new BorderLayout());
        controlador = new ControladorCursosEstudiantes();

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTituloFormulario = new JLabel("Formulario de Inscripción", SwingConstants.CENTER);
        lblTituloFormulario.setFont(new Font("Arial", Font.BOLD, 16));
        panelFormulario.add(lblTituloFormulario, gbc);

        gbc.gridwidth = 1;
        agregarCampo(panelFormulario, gbc, "Curso:", cmbCurso = new JComboBox<>(), 1);
        agregarCampo(panelFormulario, gbc, "Estudiante:", cmbEstudiante = new JComboBox<>(), 2);
        agregarCampo(panelFormulario, gbc, "Año:", txtAnio = new JTextField(10), 3);
        agregarCampo(panelFormulario, gbc, "Semestre:", txtSemestre = new JTextField(10), 4);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 5, 5));
        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnCargar = new JButton("Cargar listado");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCargar);
        panelFormulario.add(panelBotones, gbc);

        add(panelFormulario, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout());
        JSeparator separador = new JSeparator();
        panelCentral.add(separador, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblTituloTabla = new JLabel("Listado de Inscripciones", SwingConstants.CENTER);
        lblTituloTabla.setFont(new Font("Arial", Font.BOLD, 14));
        panelTabla.add(lblTituloTabla, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                    new Object[]{"ID", "Estudiante", "Semestre", "Curso", "Año"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };

        tablaInscripciones = new JTable(modeloTabla);
        panelTabla.add(new JScrollPane(tablaInscripciones), BorderLayout.CENTER);
        panelCentral.add(panelTabla, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        inicializarEventos();
        controlador.agregarListener(this);
        controlador.cargarCursos();
        controlador.cargarEstudiantes();
        eventoBtnCargar();
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta, JComponent componente, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        panel.add(componente, gbc);
    }

    private void inicializarEventos() {
        btnGuardar.addActionListener(event -> eventoBtnGuardar());
        btnActualizar.addActionListener(event -> eventoBtnActualizar());
        btnEliminar.addActionListener(event -> eventoBtnEliminar());
        btnCargar.addActionListener(event -> eventoBtnCargar());
        btnActualizar.addActionListener(event -> controlador.cargarEstudiantes());

        eventoSeleccionarRegistro();
        mostrarMensajes();
    }

    private void eventoSeleccionarRegistro() {
        tablaInscripciones.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) seleccionarInscripcion();
        });
    }

    public void mostrarMensajes() {
        controlador.agregarListener(event -> {
            if ("mensaje".equals(event.getPropertyName())) {
                JOptionPane.showMessageDialog(this, event.getNewValue().toString());
            }
        });
    }

    private void eventoBtnGuardar() {
        try {
            Map<String, Object> datos = obtenerDatosFormulario();
            controlador.inscribirEstudianteACurso(datos);
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage());
        }
    }

    private void eventoBtnActualizar() {
        int filaSeleccionada = tablaInscripciones.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una inscripción para actualizar.");
            return;
        }

        try {
            Map<String, Object> datos = obtenerDatosFormulario();
            double id = Double.parseDouble(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            datos.put("id", id);

            controlador.actualizarInscripcion(datos);
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "Error al procesar los datos.");
        }
    }

    private void eventoBtnEliminar() {
        int filaSeleccionada = tablaInscripciones.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una inscripción para eliminar.");
            return;
        }

        try {
            double inscripcionId = Double.parseDouble(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            controlador.eliminarInscripcion(inscripcionId);
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "Error al procesar los datos.");
        }
    }

    private void eventoBtnCargar() { controlador.cargarInscripciones(); }

    public Map<String, Object> obtenerDatosFormulario() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("estudiante", cmbEstudiante.getSelectedItem());
        datos.put("curso", cmbCurso.getSelectedItem());
        datos.put("anio", txtAnio.getText());
        datos.put("semestre", txtSemestre.getText());

        return datos;
    }

    private void actualizarTabla(List<Object[]> datos) {
        modeloTabla.setRowCount(0);
        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
        modeloTabla.fireTableDataChanged();
    }

    public void seleccionarInscripcion() {
        int fila = tablaInscripciones.getSelectedRow();
        if (fila == -1) return;

        limpiarCampos();

        JTextField[] campos = { txtAnio, txtSemestre };
        int[] indices = { 4, 2 };

        IntStream.range(0, campos.length)
                .forEach(i -> campos[i].setText(modeloTabla.getValueAt(fila, indices[i]).toString()));

        cmbEstudiante.setSelectedItem(
                IntStream.range(0, cmbEstudiante.getItemCount())
                        .mapToObj(cmbEstudiante::getItemAt)
                        .filter(e -> e.getNombreCompleto().equals(modeloTabla.getValueAt(fila, 1).toString()))
                        .findFirst().orElse(null)
        );

        cmbCurso.setSelectedItem(
                IntStream.range(0, cmbCurso.getItemCount())
                        .mapToObj(cmbCurso::getItemAt)
                        .filter(c -> c.getNombre().equals(modeloTabla.getValueAt(fila, 3).toString()))
                        .findFirst().orElse(null)
        );
    }

    private void limpiarCampos() {
        txtAnio.setText("");
        txtSemestre.setText("");
        cmbEstudiante.setSelectedIndex(-1);
        cmbCurso.setSelectedIndex(-1);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propiedad = evt.getPropertyName();
        Object nuevoValor = evt.getNewValue();

        if ("datosInscripciones".equals(propiedad)) {
            if (nuevoValor instanceof List<?> datosGenericos) {
                List<Object[]> datos = datosGenericos.stream()
                        .filter(obj -> obj instanceof Object[])
                        .map(obj -> (Object[]) obj)
                        .toList();
                actualizarTabla(datos);
            }
        } else if ("cursosCargados".equals(propiedad)) {
            if (nuevoValor instanceof List<?> listaGenerica) {
                cmbCurso.removeAllItems();
                listaGenerica.forEach(curso -> {
                    if (curso instanceof Curso) {
                        cmbCurso.addItem((Curso) curso);
                    }
                });
            }
        } else if ("estudiantesCargados".equals(propiedad)) {
            if (nuevoValor instanceof List<?> listaGenerica) {
                cmbEstudiante.removeAllItems();
                listaGenerica.forEach(estudiante -> {
                    if (estudiante instanceof Estudiante) {
                        cmbEstudiante.addItem((Estudiante) estudiante);
                    }
                });
            }
        }
    }

    public void actualizarListaEstudiantes() {
        controlador.cargarEstudiantes();
    }
}
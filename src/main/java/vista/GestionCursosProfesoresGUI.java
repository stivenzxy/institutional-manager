package vista;

import controlador.ControladorCursosProfesores;
import modelo.institucion.Curso;
import modelo.entidades.Profesor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class GestionCursosProfesoresGUI extends JPanel implements PropertyChangeListener {
    private final JComboBox<Profesor> cmbProfesor;
    private final JComboBox<Curso> cmbCurso;
    private final JTextField txtAnio, txtSemestre;
    private final JButton btnAsignar, btnEliminar ,btnCargar;
    private final JTable tablaAsignaciones;
    private final DefaultTableModel modeloTabla;

    private final ControladorCursosProfesores controlador;

    public GestionCursosProfesoresGUI() {
        setLayout(new BorderLayout());
        controlador = new ControladorCursosProfesores();

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTituloFormulario = new JLabel("Asignación de Cursos a Profesores", SwingConstants.CENTER);
        lblTituloFormulario.setFont(new Font("Arial", Font.BOLD, 16));
        panelFormulario.add(lblTituloFormulario, gbc);

        gbc.gridwidth = 1;
        agregarCampo(panelFormulario, gbc, "Profesor:", cmbProfesor = new JComboBox<Profesor>(), 1);
        agregarCampo(panelFormulario, gbc, "Curso:", cmbCurso = new JComboBox<Curso>(), 2);
        agregarCampo(panelFormulario, gbc, "Año:", txtAnio = new JTextField(10), 3);
        agregarCampo(panelFormulario, gbc, "Semestre:", txtSemestre = new JTextField(10), 4);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 5, 5));
        btnAsignar = new JButton("Asignar");
        btnEliminar = new JButton("Eliminar");
        btnCargar = new JButton("Cargar Listado");
        panelBotones.add(btnAsignar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCargar);
        panelFormulario.add(panelBotones, gbc);

        add(panelFormulario, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout());
        JSeparator separador = new JSeparator();
        panelCentral.add(separador, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblTituloTabla = new JLabel("Listado de Asignaciones", SwingConstants.CENTER);
        lblTituloTabla.setFont(new Font("Arial", Font.BOLD, 14));
        panelTabla.add(lblTituloTabla, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"Profesor", "Curso", "Año", "Semestre"}, 0);

        tablaAsignaciones = new JTable(modeloTabla);
        panelTabla.add(new JScrollPane(tablaAsignaciones), BorderLayout.CENTER);
        panelCentral.add(panelTabla, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        inicializarEventos();
        controlador.agregarListener(this);
        controlador.cargarProfesores();
        controlador.cargarCursos();
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
        btnAsignar.addActionListener(event->eventoBtnGuardar());
        btnEliminar.addActionListener(event->eventoBtnEliminar());
        btnCargar.addActionListener(event->eventoBtnCargar());

        eventoSeleccionarRegistro();
        mostrarMensajes();
    }

    private void eventoSeleccionarRegistro() {
        tablaAsignaciones.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) seleccionarAsignacion();
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
            controlador.inscribirCursoProfesor(datos);
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage());
        }
    }

    private void eventoBtnEliminar() {
        int filaSeleccionada = tablaAsignaciones.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una asignación para eliminar.");
            return;
        }

        try {
            String profesorNombre = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
            String cursoNombre = modeloTabla.getValueAt(filaSeleccionada, 1).toString();

            controlador.eliminarAsignacion(profesorNombre, cursoNombre);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Error al procesar los datos.");
        }
    }

    private void eventoBtnCargar() { controlador.cargarCursoProfesores(); }

    public Map<String, Object> obtenerDatosFormulario() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("profesor", cmbProfesor.getSelectedItem());
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

    public void seleccionarAsignacion() {
        int fila = tablaAsignaciones.getSelectedRow();
        if (fila == -1) return;

        limpiarCampos();

        JTextField[] campos = { txtAnio, txtSemestre };
        int[] indices = { 2, 3 };

        IntStream.range(0, campos.length)
                .forEach(i -> campos[i].setText(modeloTabla.getValueAt(fila, indices[i]).toString()));

        cmbProfesor.setSelectedItem(
                IntStream.range(0, cmbProfesor.getItemCount())
                        .mapToObj(cmbProfesor::getItemAt)
                        .filter(p -> p.getNombreCompleto().equals(modeloTabla.getValueAt(fila, 0).toString()))
                        .findFirst().orElse(null)
        );

        cmbCurso.setSelectedItem(
                IntStream.range(0, cmbCurso.getItemCount())
                        .mapToObj(cmbCurso::getItemAt)
                        .filter(c -> c.getNombre().equals(modeloTabla.getValueAt(fila, 1).toString()))
                        .findFirst().orElse(null)
        );
    }

    private void limpiarCampos() {
        txtAnio.setText("");
        txtSemestre.setText("");
        cmbProfesor.setSelectedIndex(-1);
        cmbCurso.setSelectedIndex(-1);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propiedad = evt.getPropertyName();
        Object nuevoValor = evt.getNewValue();

        if ("datosCursoProfesores".equals(propiedad)) {
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
        } else if ("profesoresCargados".equals(propiedad)) {
            if (nuevoValor instanceof List<?> listaGenerica) {
                cmbProfesor.removeAllItems();
                listaGenerica.forEach(profesor -> {
                    if (profesor instanceof Profesor) {
                        cmbProfesor.addItem((Profesor) profesor);
                    }
                });
            }
        }
    }

    public void actualizarListaProfesores() {
        controlador.cargarProfesores();
    }
}
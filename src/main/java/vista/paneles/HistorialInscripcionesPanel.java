package vista.paneles;

import controlador.ControladorCursosEstudiantes;
import fabricas.ControladorFactory;
import interfaces.Observador;
import modelo.entidades.Estudiante;
import modelo.institucion.Inscripcion;
import vista.ventanas.EstudianteDetalle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistorialInscripcionesPanel extends JPanel implements Observador {
    private DefaultTableModel modeloTabla;
    private final ControladorCursosEstudiantes controlador;

    private static HistorialInscripcionesPanel instancia;
    private Estudiante estudiante;

    private HistorialInscripcionesPanel() {
        controlador = ControladorFactory.CrearControladorCursosEstudiantes();

        inicializarComponentes();
        EstudianteDetalle.getInstancia().adicionarObservador(this);
        FormularioInscripcionCurso.getInstancia().adicionarObservador(this);
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        modeloTabla = new DefaultTableModel();
        modeloTabla.setColumnIdentifiers(new String[]{"ID", "Curso", "Estudiante", "Año", "Periodo"});

        JTable tablaCursos = new JTable(modeloTabla);
        tablaCursos.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(tablaCursos);
        add(scrollPane, BorderLayout.CENTER);
    }

    public static HistorialInscripcionesPanel getInstancia() {
        if (instancia == null) {
            instancia = new HistorialInscripcionesPanel();
        }
        return instancia;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    @Override
    public void actualizar() {
        cargarCursos();
    }

    private void cargarCursos() {
        modeloTabla.setRowCount(0);

        if (estudiante == null) {
            modeloTabla.addRow(new Object[]{"No se", "ha seleccionado", "un", "estudiante", "válido"});
            return;
        }

        List<Inscripcion> inscripciones = controlador.obtenerInscripcionesPorEstudiante(estudiante);

        if (inscripciones == null || inscripciones.isEmpty()) {
            modeloTabla.addRow(new Object[]{"El", "estudiante", "no tiene", "cursos", "asignados"});
        } else {
            for (Inscripcion inscripcion : inscripciones) {
                modeloTabla.addRow(new Object[]{
                        inscripcion.getID(),
                        inscripcion.getCurso().getNombre(),
                        inscripcion.getEstudiante().toString(),
                        inscripcion.getAnio(),
                        inscripcion.getPeriodo()
                });
            }
        }
    }
}
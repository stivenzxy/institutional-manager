package vista.paneles;

import controlador.ControladorCursosEstudiantes;
import interfaces.Observador;
import modelo.institucion.Inscripcion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistorialInscripcionesPanel extends JPanel implements Observador {
    private DefaultTableModel modeloTabla;
    private final ControladorCursosEstudiantes controlador;

    public HistorialInscripcionesPanel() {
        controlador = new ControladorCursosEstudiantes();

        setLayout(new BorderLayout());
        inicializarComponentes();
        cargarCursos();
        FormularioInscripcionCurso.getInstancia().adicionarObservador(this);
    }

    private void inicializarComponentes() {
        modeloTabla = new DefaultTableModel();
        modeloTabla.setColumnIdentifiers(new String[]{"ID", "Curso", "Estudiante", "Año", "Periodo"});

        JTable tablaCursos = new JTable(modeloTabla);
        tablaCursos.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(tablaCursos);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void actualizar() {
        cargarCursos();
    }

    private void cargarCursos() {
        modeloTabla.setRowCount(0);
        List<Inscripcion> inscripciones = controlador.cargarInscripciones();

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
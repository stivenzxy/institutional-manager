package vista.paneles;

import controlador.ControladorCursos;
import fabricas.ControladorFactory;
import interfaces.Observador;
import modelo.institucion.Curso;
import vista.ventanas.FormularioCurso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VistaCursosPanel extends JPanel implements Observador {
    private DefaultTableModel modeloTabla;
    private final ControladorCursos controlador;

    public VistaCursosPanel() {
        controlador = ControladorFactory.CrearControladorCursos();

        setLayout(new BorderLayout());
        inicializarComponentes();
        cargarCursos();
        FormularioCurso.getInstancia().adicionarObservador(this);
    }

    private void inicializarComponentes() {
        modeloTabla = new DefaultTableModel();
        modeloTabla.setColumnIdentifiers(new String[]{"Código", "Nombre", "Activo", "Programa"});

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
        List<Curso> cursos = controlador.cargarCursos();

        for (Curso curso : cursos) {
            modeloTabla.addRow(new Object[]{
                    curso.getCodigo(),
                    curso.getNombre(),
                    curso.isActivo() ? "Sí" : "No",
                    curso.getPrograma().getNombre()
            });
        }
    }
}
package vista.paneles;

import controlador.ControladorProfesores;
import fabricas.ControladorFactory;
import interfaces.Observador;
import modelo.entidades.Profesor;
import vista.ventanas.FormularioProfesor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VistaProfesoresPanel extends JPanel implements Observador {
    private DefaultTableModel modeloTabla;
    private final ControladorProfesores controlador;

    public VistaProfesoresPanel() {
        controlador = ControladorFactory.CrearControladorProfesores();

        setLayout(new BorderLayout());
        inicializarComponentes();
        cargarProfesores();
        FormularioProfesor.getInstancia().adicionarObservador(this);
    }

    private void inicializarComponentes() {
        modeloTabla = new DefaultTableModel();
        modeloTabla.setColumnIdentifiers(new String[]{"ID", "Nombre Completo", "Tipo de Contrato"});

        JTable tablaProfesores = new JTable(modeloTabla);
        tablaProfesores.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(tablaProfesores);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void actualizar() {
        cargarProfesores();
    }

    private void cargarProfesores() {
        modeloTabla.setRowCount(0);
        List<Profesor> profesores = controlador.cargarProfesores();

        for (Profesor profesor : profesores) {
            modeloTabla.addRow(new Object[]{
                    profesor.getID(),
                    profesor.getNombres() + " " + profesor.getApellidos(),
                    profesor.getTipoContrato()
            });
        }
    }
}
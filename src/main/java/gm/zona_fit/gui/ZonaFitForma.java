package gm.zona_fit.gui;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicio.ClienteServicio;
import gm.zona_fit.servicio.IClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class ZonaFitForma extends JFrame {
    private JPanel panelPrincipal;
    private JTable clientesTabla;
    private JTextField nombreTexto;
    private JTextField apellidoTexto;
    private JTextField membresiaTexto;
    private JButton guardarButton;
    private JButton eliminarButton;
    private JButton limpiarButton;
    private Integer idCliente;
    IClienteServicio clienteServicio;
    DefaultTableModel modeloTabla;

    @Autowired
    public ZonaFitForma(ClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;
        iniciarForma();
        guardarButton.addActionListener(e -> guardarCliente());
        clientesTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarClienteSeleccionado();
            }
        });
        eliminarButton.addActionListener(e -> eliminarCliente());
        limpiarButton.addActionListener(e -> limpiarFormulario());
    }

    private void eliminarCliente() {
        if (this.idCliente == null) {
            mostrarMensaje("Debe seleccionar un cliente");
            return;
        }
        this.clienteServicio.eliminarCliente(this.idCliente);
        mostrarMensaje("Cliente eliminado correctamente");
        limpiarFormulario();
        listarClientes();
    }

    private void cargarClienteSeleccionado() {
        var renglon = clientesTabla.getSelectedRow();
        if (renglon != -1) {
            var id = clientesTabla.getModel().getValueAt(renglon, 0).toString();
            this.idCliente = Integer.parseInt(id);
            var nombre = clientesTabla.getModel().getValueAt(renglon, 1).toString();
            this.nombreTexto.setText(nombre);
            var apellido = clientesTabla.getModel().getValueAt(renglon, 2).toString();
            this.apellidoTexto.setText(apellido);
            var membresia = clientesTabla.getModel().getValueAt(renglon, 3).toString();
            this.membresiaTexto.setText(membresia);


        }
    }

    private void guardarCliente() {
        if (nombreTexto.getText().equals("")) {
            mostrarMensaje("El nombre es requerido");
            nombreTexto.requestFocusInWindow();
            return;
        }
        if (membresiaTexto.getText().equals("")) {
            mostrarMensaje("La membresia es requerida");
            membresiaTexto.requestFocusInWindow();
            return;
        }
        var nombre = nombreTexto.getText();
        var apellido = apellidoTexto.getText();
        var membresia = Integer.parseInt(membresiaTexto.getText());

        var cliente = new Cliente();
        cliente.setId(this.idCliente);
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setMembresia(membresia);

        this.clienteServicio.guardarCliente(cliente);
        if (this.idCliente != null) {
            mostrarMensaje("Cliente actualizado correctamente");
        } else {
            mostrarMensaje("Cliente guardado correctamente");
        }
        limpiarFormulario();
        listarClientes();
    }

    private void limpiarFormulario() {
        nombreTexto.setText("");
        apellidoTexto.setText("");
        membresiaTexto.setText("");
        this.idCliente = null;
        this.clientesTabla.getSelectionModel().clearSelection();
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void iniciarForma() {

        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        this.modeloTabla = new DefaultTableModel(0, 4) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String[] columnas = {"ID", "Nombre", "Apellido", "Membresia"};
        this.modeloTabla.setColumnIdentifiers(columnas);
        this.clientesTabla = new JTable(modeloTabla);
        listarClientes();
    }

    private void listarClientes() {
        this.modeloTabla.setRowCount(0);
        var clientes = this.clienteServicio.listarClientes();
        clientes.forEach(cliente -> {
            Object[] renglonCliente = {
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getMembresia()
            };
            this.modeloTabla.addRow(renglonCliente);
        });
    }
}

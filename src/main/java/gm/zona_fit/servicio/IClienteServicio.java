package gm.zona_fit.servicio;

import gm.zona_fit.modelo.Cliente;

import java.util.List;

public interface IClienteServicio {
    public List<Cliente> listarClientes();
    public Cliente encontrarClientePorId(Integer id);
    public void guardarCliente(Cliente cliente);
    public void eliminarCliente(Integer id);
}

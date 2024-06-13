/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package bdcompras;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * REST Web Service
 *
 * @author eetxa
 */
@Path("clientes")
public class servRESTClientes {

    @Context
    private UriInfo context;

    public servRESTClientes() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        EntityManagerFactory emf = null;
        Response response;
        Response.Status status;
        HashMap<String, String> mensaje = new HashMap();
        List<Clientes> lista;
        try {
            emf = Persistence.createEntityManagerFactory("ApiRestCliComprasJPAPU");
            ClientesJpaController dao = new ClientesJpaController(emf);
            lista = dao.findClientesEntities();
            if (lista == null) {
                status = Response.Status.NO_CONTENT;
                response = Response.status(status).build();
            } else {
                status = Response.Status.OK;
                response = Response.status(status).entity(lista).build();
            }
        } catch (Exception ex) {
            status = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la peticion");
            response = Response.status(status).entity(mensaje).build();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
        return response;
    }

    @PUT
    @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public Response put(Clientes cli) { //datos de envio con el id email y telefono  // solo update email y tlf
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap();
        Response response;
        Response.Status status;
        Clientes parcial;
        try {
            emf = Persistence.createEntityManagerFactory("ApiRestCliComprasJPAPU");
            ClientesJpaController dao = new ClientesJpaController(emf);
            Clientes clifound = dao.findClientes(cli.getIdcli());
            if (clifound == null) {
                status = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "Cliente no existe con ID:" + cli.getIdcli());
                response = Response.status(status).entity(mensaje).build();
            } else {
                parcial = new Clientes(cli.getEmail(), cli.getTelefono());
                if (parcial.getEmail() != null) {
                    clifound.setEmail(parcial.getEmail());
                }
                if (parcial.getTelefono() != null) {
                    clifound.setTelefono(parcial.getTelefono());
                }

                dao.edit(clifound);
                status = Response.Status.OK;
                mensaje.put("mensaje", "Cliente id " + cli.getIdcli() + " actualizado");
                response = Response.status(status).entity(mensaje).build();
            }
        } catch (Exception ex) {
            status = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la peticion");
            response = Response.status(status).entity(mensaje).build();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
        return response;
    }
}

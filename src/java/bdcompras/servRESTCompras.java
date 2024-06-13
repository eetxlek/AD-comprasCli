/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package bdcompras;

import bdcamiones.Cargas;
import bdcamiones.CargasJpaController;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * REST Web Service
 *
 * @author eetxa
 */
@Path("compras")
public class servRESTCompras {

    @Context
    private UriInfo context;

    public servRESTCompras() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        EntityManagerFactory emf = null;
        Response response;
        Response.Status status;
        HashMap<String, String> mensaje = new HashMap();
        List<Compras> lista;
        try {
            emf = Persistence.createEntityManagerFactory("ApiRestCliComprasJPAPU");
            ComprasJpaController dao = new ComprasJpaController(emf);
            lista = dao.findComprasEntities();
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

    @GET
    @Path("/totales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTotales() {
        EntityManagerFactory emf = null;
        Response response;
        Response.Status status;
        HashMap<String, String> mensaje = new HashMap();
        String resultado = "{}";
        try {
            emf = Persistence.createEntityManagerFactory("ApiRestCliComprasJPAPU");
            ComprasJpaController dao = new ComprasJpaController(emf);
            EntityManager em = dao.getEntityManager();
            // CONSULTA USANDO JPQL  como en ORM nombrecliente y sumaCompras de tabla compras ordenado por nombreCliente
            Query query = em.createQuery("SELECT comp.fkcli.nombre, SUM(comp.importe) FROM Compras comp GROUP BY comp.fkcli.nombre"); // empleado mayor de cada dep. // JPQL del persistent
            List<Object[]> lista = query.getResultList();

            if ((lista != null) && (!lista.isEmpty())) {
                //CREA lista Json con objetos json con dep y maxedad  // CODIGO PARTIENDO DE LISTA
                JsonArrayBuilder jsonArrayB = Json.createArrayBuilder();
                for (Object[] obj : lista) {
                    JsonObjectBuilder jsonOB = Json.createObjectBuilder();
                    jsonOB.add("total", (BigDecimal) obj[1]);
                    jsonOB.add("nomcliente", (String) obj[0]);
                    jsonArrayB.add(jsonOB);
                }
                JsonArray arrayJson = jsonArrayB.build();
                resultado = arrayJson.toString();

                //OTRA OPCION ES resultado= new JsonBindingBuilder().build().toJson(lista); // se crea pero sin los nombre de los campos: dep y maxedad
                //OTRA OPCION resultado= new ObjectMapper(new JsonFactory()).writeValueAsString(lista); // con mapeador de objetos de Jackson
                status = Response.Status.OK;
                response = Response.status(status).entity(resultado).build();

            } else {
                status = Response.Status.NO_CONTENT;
                response = Response.status(status).build();
            }
        } catch (Exception ex) {
            status = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la peticion" + ex.getMessage());
            response = Response.status(status).entity(mensaje).build();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
        return response;
    }
}

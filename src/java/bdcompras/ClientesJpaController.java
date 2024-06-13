/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bdcompras;

import bdcompras.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author eetxa
 */
public class ClientesJpaController implements Serializable {

    public ClientesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clientes clientes) {
        if (clientes.getComprasCollection() == null) {
            clientes.setComprasCollection(new ArrayList<Compras>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Compras> attachedComprasCollection = new ArrayList<Compras>();
            for (Compras comprasCollectionComprasToAttach : clientes.getComprasCollection()) {
                comprasCollectionComprasToAttach = em.getReference(comprasCollectionComprasToAttach.getClass(), comprasCollectionComprasToAttach.getIdcom());
                attachedComprasCollection.add(comprasCollectionComprasToAttach);
            }
            clientes.setComprasCollection(attachedComprasCollection);
            em.persist(clientes);
            for (Compras comprasCollectionCompras : clientes.getComprasCollection()) {
                Clientes oldFkcliOfComprasCollectionCompras = comprasCollectionCompras.getFkcli();
                comprasCollectionCompras.setFkcli(clientes);
                comprasCollectionCompras = em.merge(comprasCollectionCompras);
                if (oldFkcliOfComprasCollectionCompras != null) {
                    oldFkcliOfComprasCollectionCompras.getComprasCollection().remove(comprasCollectionCompras);
                    oldFkcliOfComprasCollectionCompras = em.merge(oldFkcliOfComprasCollectionCompras);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clientes clientes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clientes persistentClientes = em.find(Clientes.class, clientes.getIdcli());
            Collection<Compras> comprasCollectionOld = persistentClientes.getComprasCollection();
            Collection<Compras> comprasCollectionNew = clientes.getComprasCollection();
            Collection<Compras> attachedComprasCollectionNew = new ArrayList<Compras>();
            for (Compras comprasCollectionNewComprasToAttach : comprasCollectionNew) {
                comprasCollectionNewComprasToAttach = em.getReference(comprasCollectionNewComprasToAttach.getClass(), comprasCollectionNewComprasToAttach.getIdcom());
                attachedComprasCollectionNew.add(comprasCollectionNewComprasToAttach);
            }
            comprasCollectionNew = attachedComprasCollectionNew;
            clientes.setComprasCollection(comprasCollectionNew);
            clientes = em.merge(clientes);
            for (Compras comprasCollectionOldCompras : comprasCollectionOld) {
                if (!comprasCollectionNew.contains(comprasCollectionOldCompras)) {
                    comprasCollectionOldCompras.setFkcli(null);
                    comprasCollectionOldCompras = em.merge(comprasCollectionOldCompras);
                }
            }
            for (Compras comprasCollectionNewCompras : comprasCollectionNew) {
                if (!comprasCollectionOld.contains(comprasCollectionNewCompras)) {
                    Clientes oldFkcliOfComprasCollectionNewCompras = comprasCollectionNewCompras.getFkcli();
                    comprasCollectionNewCompras.setFkcli(clientes);
                    comprasCollectionNewCompras = em.merge(comprasCollectionNewCompras);
                    if (oldFkcliOfComprasCollectionNewCompras != null && !oldFkcliOfComprasCollectionNewCompras.equals(clientes)) {
                        oldFkcliOfComprasCollectionNewCompras.getComprasCollection().remove(comprasCollectionNewCompras);
                        oldFkcliOfComprasCollectionNewCompras = em.merge(oldFkcliOfComprasCollectionNewCompras);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clientes.getIdcli();
                if (findClientes(id) == null) {
                    throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clientes clientes;
            try {
                clientes = em.getReference(Clientes.class, id);
                clientes.getIdcli();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.", enfe);
            }
            Collection<Compras> comprasCollection = clientes.getComprasCollection();
            for (Compras comprasCollectionCompras : comprasCollection) {
                comprasCollectionCompras.setFkcli(null);
                comprasCollectionCompras = em.merge(comprasCollectionCompras);
            }
            em.remove(clientes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Clientes> findClientesEntities() {
        return findClientesEntities(true, -1, -1);
    }

    public List<Clientes> findClientesEntities(int maxResults, int firstResult) {
        return findClientesEntities(false, maxResults, firstResult);
    }

    private List<Clientes> findClientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clientes.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Clientes findClientes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clientes> rt = cq.from(Clientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

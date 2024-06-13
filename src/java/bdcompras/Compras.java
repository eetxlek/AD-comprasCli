/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bdcompras;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author eetxa
 */
@Entity
@Table(name = "compras")
@NamedQueries({
    @NamedQuery(name = "Compras.findAll", query = "SELECT c FROM Compras c"),
    @NamedQuery(name = "Compras.findByIdcom", query = "SELECT c FROM Compras c WHERE c.idcom = :idcom"),
    @NamedQuery(name = "Compras.findByFcompra", query = "SELECT c FROM Compras c WHERE c.fcompra = :fcompra"),
    @NamedQuery(name = "Compras.findByImporte", query = "SELECT c FROM Compras c WHERE c.importe = :importe"),
    @NamedQuery(name = "Compras.findByDescripcion", query = "SELECT c FROM Compras c WHERE c.descripcion = :descripcion")})
public class Compras implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idcom")
    private Integer idcom;
    @Column(name = "fcompra")
  
    private LocalDate fcompra;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "importe")
    private BigDecimal importe;
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "fkcli", referencedColumnName = "idcli")
    @ManyToOne
    private Clientes fkcli;

    public Compras() {
    }

    public Compras(Integer idcom) {
        this.idcom = idcom;
    }

    public Integer getIdcom() {
        return idcom;
    }

    public void setIdcom(Integer idcom) {
        this.idcom = idcom;
    }

    public LocalDate getFcompra() {
        return fcompra;
    }

    public void setFcompra(LocalDate fcompra) {
        this.fcompra = fcompra;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Clientes getFkcli() {
        return fkcli;
    }

    public void setFkcli(Clientes fkcli) {
        this.fkcli = fkcli;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcom != null ? idcom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Compras)) {
            return false;
        }
        Compras other = (Compras) object;
        if ((this.idcom == null && other.idcom != null) || (this.idcom != null && !this.idcom.equals(other.idcom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bdcompras.Compras[ idcom=" + idcom + " ]";
    }
    
}

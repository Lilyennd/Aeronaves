package cl.GestionDrones.v1.aeronaves.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "aeronaves")
public class Aeronave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; 

    
    @Column(name = "id_empresa_proveedora", nullable = false)
    private Long idEmpresaProveedora; 

    @Column(name = "patente", nullable = false, unique = true, length = 30)
    private String patente; 

    @Column(name = "numero_serie", nullable = false, unique = true, length = 100)
    private String numeroSerie; 

    @Column(name = "marca", nullable = false, length = 50)
    private String marca; 

    @Column(name = "modelo", nullable = false, length = 100)
    private String modelo; 

    @Column(name = "estado", nullable = false, length = 30)
    private String estado; 

    @Column(name = "fecha_vencimiento_seguro", nullable = false)
    private LocalDate fechaVencimientoSeguro; 

    
    public Aeronave() {
    }

    
    public Aeronave(Long id, Long idEmpresaProveedora, String patente, String numeroSerie, String marca, String modelo, String estado, LocalDate fechaVencimientoSeguro) {
        this.id = id;
        this.idEmpresaProveedora = idEmpresaProveedora;
        this.patente = patente;
        this.numeroSerie = numeroSerie;
        this.marca = marca;
        this.modelo = modelo;
        this.estado = estado;
        this.fechaVencimientoSeguro = fechaVencimientoSeguro;
    }

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdEmpresaProveedora() {
        return idEmpresaProveedora;
    }

    public void setIdEmpresaProveedora(Long idEmpresaProveedora) {
        this.idEmpresaProveedora = idEmpresaProveedora;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaVencimientoSeguro() {
        return fechaVencimientoSeguro;
    }

    public void setFechaVencimientoSeguro(LocalDate fechaVencimientoSeguro) {
        this.fechaVencimientoSeguro = fechaVencimientoSeguro;
    }
}
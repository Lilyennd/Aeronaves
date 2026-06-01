package cl.GestionDrones.v1.aeronaves.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.GestionDrones.v1.aeronaves.model.Aeronave;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AeronaveRepository extends JpaRepository<Aeronave, Long> { 

    
    @Query(value = "SELECT * FROM aeronaves WHERE patente = :patente", nativeQuery = true)
    List<Aeronave> selectPorPatente(@Param("patente") String patente);

    
    @Query(value = "SELECT * FROM aeronaves WHERE numero_serie = :numeroSerie", nativeQuery = true)
    List<Aeronave> selectPorNumeroSerie(@Param("numeroSerie") String numeroSerie);

    
    @Query(value = "SELECT * FROM aeronaves WHERE id_empresa_proveedora = :idEmpresaProveedora", nativeQuery = true)
    List<Aeronave> selectPorEmpresaProveedora(@Param("idEmpresaProveedora") Long idEmpresaProveedora);

    default int totalAeronaves() {
        return (int) this.count(); 
    }

    List<Aeronave> findByFechaVencimientoSeguroLessThanEqual(
            LocalDate fechaLimite
    );
}

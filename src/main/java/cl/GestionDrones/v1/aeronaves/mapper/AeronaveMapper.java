package cl.GestionDrones.v1.aeronaves.mapper;

import cl.GestionDrones.v1.aeronaves.dto.CreateAeronaveRequest;
import cl.GestionDrones.v1.aeronaves.dto.UpdateAeronaveRequest;
import cl.GestionDrones.v1.aeronaves.model.Aeronave;

public class AeronaveMapper {

    
    public static Aeronave toModel(CreateAeronaveRequest request) {
        return new Aeronave(
                null,
                request.idEmpresaProveedora(),
                request.patente(),
                request.numeroSerie(),
                request.marca(),
                request.modelo(),
                request.estado(),
                request.fechaVencimientoSeguro()
        );
    }

    
    public static Aeronave toModel(Long id, UpdateAeronaveRequest request) {
        return new Aeronave(
                id, 
                request.idEmpresaProveedora(), 
                request.patente(),
                request.numeroSerie(),
                request.marca(),
                request.modelo(),
                request.estado(),
                request.fechaVencimientoSeguro()
        );
    }

    
}

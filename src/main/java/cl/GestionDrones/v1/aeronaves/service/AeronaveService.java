package cl.GestionDrones.v1.aeronaves.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.GestionDrones.v1.aeronaves.model.Aeronave;
import cl.GestionDrones.v1.aeronaves.repository.AeronaveRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AeronaveService {

    @Autowired
    private AeronaveRepository aeronaveRepository;

    public List<Aeronave> getAeronaves() {
        return aeronaveRepository.findAll();
    }

    public Aeronave saveAeronave(Aeronave aeronave) {
        return aeronaveRepository.save(aeronave);
    }

    
    public Aeronave getAeronaveId(Long id) {
        return aeronaveRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("La aeronave con ID " + id + " no existe en la DGAC."));
    }

    public Aeronave updateAeronave(Aeronave aeronave) {
        
        return aeronaveRepository.save(aeronave);
    }

    
    public String deleteAeronave(Long id) {
        
        if (!aeronaveRepository.existsById(id)) {
            throw new EntityNotFoundException("No se puede eliminar: La aeronave con ID " + id + " no existe.");
        }
        aeronaveRepository.deleteById(id);
        return "Aeronave eliminada exitosamente del registro DGAC.";
    }

    
    public int totalAeronaves() {
        return (int) aeronaveRepository.count();
    }

    
    public int totalAeronavesV2() {
        return aeronaveRepository.totalAeronaves();
    }

    public List<Aeronave> obtenerPorPatente(String patente) {
        return aeronaveRepository.selectPorPatente(patente);
    }

    public List<Aeronave> obtenerPorNumeroSerie(String numeroSerie) {
        return aeronaveRepository.selectPorNumeroSerie(numeroSerie);
    }

    public List<Aeronave> obtenerPorEmpresaProveedora(Long idEmpresaProveedora) {
        return aeronaveRepository.selectPorEmpresaProveedora(idEmpresaProveedora);
    }

    public List<Aeronave> getAeronavesConSeguroPorVencer() {

    LocalDate hoy = LocalDate.now();
    LocalDate fechaLimite = hoy.plusDays(10);

    return aeronaveRepository
            .findAll()
            .stream()
            .filter(a -> !a.getFechaVencimientoSeguro().isBefore(hoy))
            .filter(a -> !a.getFechaVencimientoSeguro().isAfter(fechaLimite))
            .toList();
    }
}

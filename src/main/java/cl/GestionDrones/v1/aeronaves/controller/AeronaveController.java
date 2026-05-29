package cl.GestionDrones.v1.aeronaves.controller;

import java.util.List;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.GestionDrones.v1.aeronaves.dto.CreateAeronaveRequest;
import cl.GestionDrones.v1.aeronaves.dto.UpdateAeronaveRequest;
import cl.GestionDrones.v1.aeronaves.mapper.AeronaveMapper;
import cl.GestionDrones.v1.aeronaves.model.Aeronave;
import cl.GestionDrones.v1.aeronaves.service.AeronaveService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/aeronaves")
public class AeronaveController {

        private final AeronaveService aeronaveService;

        public AeronaveController(AeronaveService aeronaveService) {
                this.aeronaveService = aeronaveService;
        }

        @GetMapping
        public ResponseEntity<List<Aeronave>> listarAeronaves() {
                List<Aeronave> aeronaves = aeronaveService.getAeronaves();
                
                if (aeronaves.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                
                return ResponseEntity.ok(aeronaves);
        }

        @PostMapping
        public ResponseEntity<Aeronave> agregarAeronave(@Valid @RequestBody CreateAeronaveRequest request) {
                
                if (request.patente() == null || request.patente().trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                
                if (request.fechaVencimientoSeguro() != null && request.fechaVencimientoSeguro().isBefore(LocalDate.now())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                Aeronave nuevaAeronave = aeronaveService.saveAeronave(AeronaveMapper.toModel(request));
                
                if (nuevaAeronave == null) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
                
                return ResponseEntity.status(HttpStatus.CREATED).body(nuevaAeronave);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Aeronave> buscarAeronave(@PathVariable Long id) {
                if (id <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                Aeronave aeronave = aeronaveService.getAeronaveId(id);
                
                if (aeronave == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                
                return ResponseEntity.ok(aeronave);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Aeronave> actualizarAeronave(@PathVariable Long id,
                        @Valid @RequestBody UpdateAeronaveRequest request) {
                
                if (id <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                if (request.patente() == null || request.patente().contains(" ")) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                Aeronave aeronaveActualizada = aeronaveService.updateAeronave(AeronaveMapper.toModel(id, request));
                
                if (aeronaveActualizada == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                
                return ResponseEntity.ok(aeronaveActualizada);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminarAeronave(@PathVariable Long id) {
                if (id <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                aeronaveService.deleteAeronave(id);
                return ResponseEntity.noContent().build(); 
        }

        @GetMapping("/total")
        public ResponseEntity<Integer> totalAeronaves() {
                int total = aeronaveService.totalAeronavesV2();
                
                if (total < 0) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
                }
                
                return ResponseEntity.ok(total);
        }

        @GetMapping("/patente/{patente}")
        public ResponseEntity<List<Aeronave>> selectPorPatente(@PathVariable String patente) {
                if (patente == null || patente.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                List<Aeronave> aeronaves = aeronaveService.obtenerPorPatente(patente);
                
                if (aeronaves.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(aeronaves);
                }
                
                return ResponseEntity.ok(aeronaves);
        }

        @GetMapping("/numeroSerie/{numeroSerie}")
        public ResponseEntity<List<Aeronave>> selectPorNumeroSerie(@PathVariable String numeroSerie) {
                if (numeroSerie == null || numeroSerie.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                List<Aeronave> aeronaves = aeronaveService.obtenerPorNumeroSerie(numeroSerie);
                
                if (aeronaves.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(aeronaves);
                }
                
                return ResponseEntity.ok(aeronaves);
        }

        @GetMapping("/empresa/{idEmpresaProveedora}")
        public ResponseEntity<List<Aeronave>> buscarPorEmpresa(@PathVariable Long idEmpresaProveedora) {
                if (idEmpresaProveedora <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
                
                List<Aeronave> aeronaves = aeronaveService.obtenerPorEmpresaProveedora(idEmpresaProveedora);
                
                if (aeronaves.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                
                return ResponseEntity.ok(aeronaves);
        }
}
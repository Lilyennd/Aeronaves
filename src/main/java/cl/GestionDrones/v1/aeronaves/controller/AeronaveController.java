package cl.GestionDrones.v1.aeronaves.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

// @Tag agrupa y define el nombre principal en Swagger UI
@Tag(name = "Aeronaves", description = "Operaciones relacionadas con las aeronaves")
@RestController
@RequestMapping("/api/v1/aeronaves")
public class AeronaveController {

        private final AeronaveService aeronaveService;

        public AeronaveController(AeronaveService aeronaveService) {
                this.aeronaveService = aeronaveService;
        }

        @Operation(summary = "Obtener todas las aeronaves", description = "Retorna una lista completa de todas las aeronaves registradas en el sistema")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de aeronaves obtenida con éxito", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Aeronave.class))),
            @ApiResponse(responseCode = "204", description = "No hay contenido disponible (Lista vacía)", content = @Content)
        })
        @GetMapping
        public ResponseEntity<List<Aeronave>> listarAeronaves() {
                List<Aeronave> aeronaves = aeronaveService.getAeronaves();
                
                if (aeronaves.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                
                return ResponseEntity.ok(aeronaves);
        }

        @Operation(summary = "Crear una nueva aeronave", description = "Registra una nueva aeronave en el sistema previa validación de sus datos")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Aeronave creada exitosamente", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Aeronave.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o formato incorrecto", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        })
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

        @Operation(summary = "Obtener una aeronave por código", description = "Busca y retorna los detalles de un dron específico utilizando su ID")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aeronave encontrada exitosamente", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Aeronave.class))),
            @ApiResponse(responseCode = "400", description = "ID de aeronave inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Aeronave no encontrada", content = @Content)
        })
        @GetMapping("/{id}")
        public ResponseEntity<Aeronave> buscarAeronave(
            @Parameter(description = "ID de la aeronave a buscar", required = true, example = "1") 
            @PathVariable Long id
        ) {
                if (id <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                Aeronave aeronave = aeronaveService.getAeronaveId(id);
                
                if (aeronave == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                
                return ResponseEntity.ok(aeronave);
        }

        @Operation(summary = "Actualizar una aeronave", description = "Modifica los datos de una aeronave existente a partir de su ID")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aeronave actualizada de manera exitosa", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Aeronave.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido o datos de entrada incorrectos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Aeronave no encontrada", content = @Content)
        })
        @PutMapping("/{id}")
        public ResponseEntity<Aeronave> actualizarAeronave(
            @Parameter(description = "ID de la aeronave que se desea actualizar", required = true, example = "1") 
            @PathVariable Long id,
            @Valid @RequestBody UpdateAeronaveRequest request
        ) {
                
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

        @Operation(summary = "Eliminar una aeronave", description = "Elimina físicamente una aeronave del inventario por su ID")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Aeronave eliminada de forma exitosa"),
            @ApiResponse(responseCode = "400", description = "ID proporcionado es inválido", content = @Content)
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminarAeronave(
            @Parameter(description = "ID de la aeronave que se desea eliminar", required = true, example = "1") 
            @PathVariable Long id
        ) {
                if (id <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                aeronaveService.deleteAeronave(id);
                return ResponseEntity.noContent().build(); 
        }

        @Operation(summary = "Obtener total de aeronaves", description = "Devuelve la cantidad total de drones registrados en el sistema")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo obtenido con éxito", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        })
        @GetMapping("/total")
        public ResponseEntity<Integer> totalAeronaves() {
                int total = aeronaveService.totalAeronavesV2();
                
                if (total < 0) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
                }
                
                return ResponseEntity.ok(total);
        }

        @Operation(summary = "Obtener una aeronave por patente", description = "Busca los detalles de un dron mediante su número único de patente")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aeronave encontrada", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Aeronave.class))),
            @ApiResponse(responseCode = "400", description = "Patente nula o vacía", content = @Content),
            @ApiResponse(responseCode = "404", description = "No existe aeronave con la patente especificada", content = @Content)
        })
        @GetMapping("/patente/{patente}")
        public ResponseEntity<?> selectPorPatente(
            @Parameter(description = "Patente del dron a buscar", required = true, example = "DRON-99X-CL") 
            @PathVariable String patente
        ) {
                if (patente == null || patente.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Patente inválida");
                }

                List<Aeronave> aeronaves = aeronaveService.obtenerPorPatente(patente);
                
                if (aeronaves.isEmpty()) {
                        Map<String, String> error = new HashMap<>();
                        error.put("error", "No encontrado");
                        error.put("mensaje", "No existe aeronave con patente: " + patente);
                        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
                }
                
                return ResponseEntity.ok(aeronaves.get(0));
        }

        @Operation(summary = "Obtener aeronaves por número de serie", description = "Lista aeronaves asociadas a un número de serie específico")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registros encontrados con éxito", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Aeronave.class))),
            @ApiResponse(responseCode = "400", description = "Número de serie nulo o inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontraron registros", content = @Content)
        })
        @GetMapping("/numeroSerie/{numeroSerie}")
        public ResponseEntity<List<Aeronave>> selectPorNumeroSerie(
            @Parameter(description = "Número de serie del fabricante", required = true, example = "SN-554203-A9") 
            @PathVariable String numeroSerie
        ) {
                if (numeroSerie == null || numeroSerie.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                List<Aeronave> aeronaves = aeronaveService.obtenerPorNumeroSerie(numeroSerie);
                
                if (aeronaves.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(aeronaves);
                }
                
                return ResponseEntity.ok(aeronaves);
        }

        @Operation(summary = "Obtener aeronaves por empresa proveedora", description = "Lista todos los drones asignados a un proveedor específico")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registros de la empresa devueltos con éxito", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Aeronave.class))),
            @ApiResponse(responseCode = "400", description = "ID de la empresa proveedora inválido", content = @Content),
            @ApiResponse(responseCode = "204", description = "La empresa no posee drones registrados", content = @Content)
        })
        @GetMapping("/empresa/{idEmpresaProveedora}")
        public ResponseEntity<List<Aeronave>> buscarPorEmpresa(
            @Parameter(description = "ID de la empresa proveedora", required = true, example = "45") 
            @PathVariable Long idEmpresaProveedora
        ) {
                if (idEmpresaProveedora <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
                
                List<Aeronave> aeronaves = aeronaveService.obtenerPorEmpresaProveedora(idEmpresaProveedora);
                
                if (aeronaves.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                
                return ResponseEntity.ok(aeronaves);
        }

        @Operation(summary = "Obtener aeronaves con seguro por vencer", description = "Devuelve un listado de aeronaves cuya póliza de seguro está por caducar")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda finalizada de manera correcta", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Aeronave.class))),
            @ApiResponse(responseCode = "404", description = "No existen drones con seguro próximo a vencer", content = @Content)
        })
        @GetMapping("/seguros/por-vencer")
        public ResponseEntity<?> getAeronavesConSeguroPorVencer() {

                List<Aeronave> aeronaves = aeronaveService.getAeronavesConSeguroPorVencer();

                if (aeronaves.isEmpty()) {
                        Map<String, String> respuesta = new HashMap<>();
                        respuesta.put(
                                "mensaje",
                                "No existen aeronaves con seguro próximo a vencer"
                        );
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
                }

                return ResponseEntity.ok(aeronaves);
        }
}
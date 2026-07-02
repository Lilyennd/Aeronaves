package cl.GestionDrones.v1.aeronaves.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
        System.out.println("✅ GlobalExceptionHandler de Aeronaves SE HA REGISTRADO CORRECTAMENTE");
    }

    
@ExceptionHandler(SeguroInvalidoException.class)
    public ResponseEntity<Map<String, Object>> handleSeguroInvalido(SeguroInvalidoException ex) {
        System.out.println("🔴 GlobalExceptionHandler EJECUTADO - Violación de seguro aeronáutico: " + ex.getMessage());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("title", "Seguro Aeronáutico Inválido o Vencido");
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("detail", ex.getMessage());
        errorResponse.put("timestamp", Instant.now().toString());
        errorResponse.put("codigo_error", "DGAC-ERR-SEGURO");
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(jakarta.persistence.EntityNotFoundException ex) {
        System.out.println("🔴 GlobalExceptionHandler EJECUTADO - Aeronave no encontrada: " + ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, 
                ex.getMessage()
        );

        problem.setTitle("Aeronave No Registrada");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("codigo_error", "DGAC-ERR-AERONAVE-404");
        
        return problem;
    }

    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        System.out.println("🔴 GlobalExceptionHandler EJECUTADO - Errores de validación en Aeronave detectados");

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Error de validación en los datos de la aeronave enviados");

        problem.setTitle("Aeronave Validation Error");
        problem.setProperty("timestamp", Instant.now());

        
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage()
                                : "Valor inválido"));

        problem.setProperty("errors", errors);

        System.out.println("🔴 Errores encontrados en la aeronave: " + errors);
        return problem;
    }

    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleJsonParseError(HttpMessageNotReadableException ex) {
        System.out.println("🟡 Error de parseo JSON capturado en Aeronaves");
        System.out.println("🟡 Mensaje: " + ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Error al procesar el JSON enviado para la aeronave");

        problem.setTitle("JSON Parse Error");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("detalle", ex.getMostSpecificCause().getMessage());
        return problem;
    }

    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {
        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());

        problem.setTitle("Aeronave Not Found");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {
        System.out.println("🔴 EXCEPCIÓN CAPTURADA EN AERONAVES: " + ex.getClass().getName());
        System.out.println("🔴 Mensaje: " + ex.getMessage());
        ex.printStackTrace();

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor en el microservicio de aeronaves");

        problem.setTitle("Internal Server Error");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("detalle", ex.getMessage());
        problem.setProperty("tipoExcepcion", ex.getClass().getSimpleName());
        return problem;
    }
}

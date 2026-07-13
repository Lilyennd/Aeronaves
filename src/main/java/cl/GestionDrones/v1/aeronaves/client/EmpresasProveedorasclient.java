package cl.GestionDrones.v1.aeronaves.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class EmpresasProveedorasclient {

    private final WebClient webClient;

    public EmpresasProveedorasclient(@Qualifier("empresasProveedorasWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public boolean existeEmpresaProveedora(Long idEmpresaProveedora) {
        try {
            webClient.get()
                    .uri("/{id}", idEmpresaProveedora)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            return true;

        } catch (WebClientResponseException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.out.println("🔴 Error al conectar con EmpresasProveedoras: " + e.getMessage());
            throw new RuntimeException(
                "No se puede conectar con el servicio de Empresas Proveedoras.");
        }
    }
}
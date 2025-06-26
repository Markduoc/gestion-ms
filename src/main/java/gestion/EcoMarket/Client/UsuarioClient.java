package gestion.EcoMarket.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import gestion.EcoMarket.DTO.UsuarioDTO;

@FeignClient(name = "usuarios-ms", url = "http://34.196.63.137:8080/api/v2/usuarios")
public interface UsuarioClient {
    @GetMapping("/{id}")
    UsuarioDTO getUsuarioById(@PathVariable Long id);
    
} 

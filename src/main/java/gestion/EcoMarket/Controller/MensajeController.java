package gestion.EcoMarket.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gestion.EcoMarket.Model.Mensaje;
import gestion.EcoMarket.Service.MensajeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


//CONTROLADOR SIN UTILIZAR
@RestController
@RequestMapping("/api/v1/mensajes")
@Tag(name = "Mensaje", description = "Gestion de reclamos")
public class MensajeController {
    @Autowired
    private MensajeService mensajeService;
    
    @GetMapping
    public ResponseEntity<List<Mensaje>> Listar(){
        List<Mensaje> mensajes = mensajeService.findAll();
        if (mensajes.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else{
            return ResponseEntity.ok(mensajes);
        }
    }

    @PostMapping
    public ResponseEntity<Mensaje> crearMensaje(@RequestBody Mensaje mensaje){
        Mensaje nuevoMensaje = mensajeService.save(mensaje);
        return ResponseEntity.ok(nuevoMensaje);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id){
        boolean eliminado = mensajeService.delete(id);
        if (eliminado) {
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } else {
            return ResponseEntity.status(404).body("El usuario no ha sido eliminado de la base de datos");
        }

    }


    @GetMapping("/{id}")
    public ResponseEntity<Mensaje> buscarPorId(@PathVariable Long id){
        Optional<Mensaje> mensaje = mensajeService.findById(id);
        if (mensaje.isPresent()) {
            return ResponseEntity.ok(mensaje.get());
        } else{
            return ResponseEntity.status(404).build();
        }
    }
    

}

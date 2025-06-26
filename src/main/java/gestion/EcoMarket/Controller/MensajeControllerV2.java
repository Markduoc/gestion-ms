package gestion.EcoMarket.Controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gestion.EcoMarket.Assemblers.MensajeModelAssembler;
import gestion.EcoMarket.Client.UsuarioClient;
import gestion.EcoMarket.DTO.MensajeConUsuarioDTO;
import gestion.EcoMarket.DTO.UsuarioDTO;
import gestion.EcoMarket.Model.Mensaje;
import gestion.EcoMarket.Repository.MensajeRepository;
import gestion.EcoMarket.Service.MensajeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/v2/mensajes")
@Tag(name = "Mensaje", description = "Gestion de reclamos")
public class MensajeControllerV2 {
    
    @Autowired
    private MensajeService mensajeService;

    @Autowired
    MensajeModelAssembler assembler;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private MensajeRepository mensajeRepository;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Mensaje>>> Listar(){
        List<Mensaje> mensajes = mensajeService.findAll();
        if (mensajes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<EntityModel<Mensaje>> mensajesModel = mensajes.stream()
        .map(assembler::toModel)
        .collect(Collectors.toList());
        CollectionModel<EntityModel<Mensaje>> collectionModel =
        CollectionModel.of(mensajesModel, 
        linkTo(methodOn(MensajeControllerV2.class).Listar()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }
    
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Mensaje>> crearMensaje(@RequestBody Mensaje mensaje){
        Mensaje nuevoMensaje = mensajeService.save(mensaje);
        return ResponseEntity
            .created(linkTo(methodOn(MensajeControllerV2.class).buscarPorId(nuevoMensaje.getId())).toUri())
            .body(assembler.toModel(nuevoMensaje));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        boolean eliminado = mensajeService.delete(id);
        if (eliminado) {
            return ResponseEntity
            .ok("Usuario eliminado correctamente");
        } else {
            return ResponseEntity
            .status(404).body("El usuario no ha sido eliminado de la base de datos");
        }

    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Mensaje>> actualizar(@PathVariable Long id, @RequestBody Mensaje mensaje){
        Optional<Mensaje> actualizado = mensajeService.update(id, mensaje);
        if (actualizado.isPresent()) {
            Mensaje mensaje2 = actualizado.get();
            EntityModel<Mensaje> resource = EntityModel.of(mensaje2,
            linkTo(methodOn(MensajeControllerV2.class).actualizar(id, mensaje)).withSelfRel(),
                linkTo(methodOn(MensajeControllerV2.class).buscarPorId(id)).withRel("mensaje"),
                linkTo(methodOn(MensajeControllerV2.class).Listar()).withRel("mensajes"),
                linkTo(methodOn(MensajeControllerV2.class).eliminar(id)).withRel("eliminar"));
            return ResponseEntity.ok(resource);
        } else{
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Mensaje> buscarPorId(@PathVariable Long id){
        Optional<Mensaje> mensaje = mensajeService.findById(id);
        if (mensaje.isPresent()) {
            return ResponseEntity.ok(mensaje.get());
        } else{
            return ResponseEntity.status(404).build();
        }
    }
    public void setMensajeService(MensajeService mensajeService) {
        this.mensajeService = mensajeService;
    }

    public void setAssembler(MensajeModelAssembler assembler) {
        this.assembler = assembler;
    }
    @GetMapping("/con-usuario/{id}")
    public ResponseEntity<MensajeConUsuarioDTO> getMensajeConUsuario(@PathVariable Long id) {
        try {
            Optional<Mensaje> mensajeOpt = mensajeRepository.findById(id);
            if (mensajeOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Mensaje mensaje = mensajeOpt.get();
            
            // Validar que el mensaje tenga un idUsuario
            if (mensaje.getIdUsuario() == null) {
                return ResponseEntity.badRequest().build();
            }

            try {
                UsuarioDTO usuario = usuarioClient.getUsuarioById(mensaje.getIdUsuario());
                
                MensajeConUsuarioDTO respuesta = new MensajeConUsuarioDTO();
                respuesta.setMensaje(mensaje);
                respuesta.setUsuario(usuario);

                return ResponseEntity.ok(respuesta);
                
            } catch (Exception e) {
                // Log del error
                System.err.println("Error al obtener usuario: " + e.getMessage());
                
                // Retornar solo el mensaje si no se puede obtener el usuario
                MensajeConUsuarioDTO respuesta = new MensajeConUsuarioDTO();
                respuesta.setMensaje(mensaje);
                respuesta.setUsuario(null); // o un usuario por defecto
                
                return ResponseEntity.ok(respuesta);
            }
            
        } catch (Exception e) {
            System.err.println("Error general: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
        
}

    @GetMapping("/con-usuario")
    public ResponseEntity<List<MensajeConUsuarioDTO>> getAllMensajesConUsuario() {
        try {
            List<Mensaje> mensajes = mensajeRepository.findAll();
            List<MensajeConUsuarioDTO> mensajesConUsuario = new ArrayList<>();
            
            for (Mensaje mensaje : mensajes) {
                MensajeConUsuarioDTO dto = new MensajeConUsuarioDTO();
                dto.setMensaje(mensaje);
                
                // Solo buscar usuario si idUsuario no es null
                if (mensaje.getIdUsuario() != null) {
                    try {
                        UsuarioDTO usuario = usuarioClient.getUsuarioById(mensaje.getIdUsuario());
                        dto.setUsuario(usuario);
                    } catch (Exception e) {
                        System.err.println("Error al obtener usuario " + mensaje.getIdUsuario() + 
                                        " para mensaje " + mensaje.getId() + ": " + e.getMessage());
                        dto.setUsuario(null); // Usuario null si hay error
                    }
                } else {
                    dto.setUsuario(null); // Usuario null si no hay idUsuario
                }
                
                mensajesConUsuario.add(dto);
            }
            
            return ResponseEntity.ok(mensajesConUsuario);
            
        } catch (Exception e) {
            System.err.println("Error general en getAllMensajesConUsuario: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}



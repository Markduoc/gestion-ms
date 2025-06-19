package gestion.EcoMarket.Assemblers;

import gestion.EcoMarket.Controller.MensajeControllerV2;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import gestion.EcoMarket.Model.Mensaje;

@Component
public class MensajeModelAssembler implements RepresentationModelAssembler<Mensaje, EntityModel<Mensaje>>{

    @Override
    public EntityModel<Mensaje> toModel(Mensaje mensaje) {
        return EntityModel.of(mensaje,
        linkTo(methodOn(MensajeControllerV2.class).buscarPorId(mensaje.getId())).withSelfRel(),
        linkTo(methodOn(MensajeControllerV2.class).Listar()).withRel("Mensajes"),
        linkTo(methodOn(MensajeControllerV2.class).actualizar(mensaje.getId(), mensaje)).withRel("actualizar"),
        linkTo(methodOn(MensajeControllerV2.class).eliminar(mensaje.getId())).withRel("eliminar"));
    }

}

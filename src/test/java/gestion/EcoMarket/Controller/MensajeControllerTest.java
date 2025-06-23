package gestion.EcoMarket.Controller;

import gestion.EcoMarket.Model.Mensaje;
import gestion.EcoMarket.Service.MensajeService;
import gestion.EcoMarket.Assemblers.MensajeModelAssembler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MensajeControllerTest {
    @Mock
    private MensajeService mensajeService;
    @Mock
    private MensajeModelAssembler assembler;
    @InjectMocks
    private MensajeControllerV2 controller;



    @Test
    void testListarMensajes_conContenido() {
        Mensaje mensaje1 = new Mensaje();
        Mensaje mensaje2 = new Mensaje();
        when(mensajeService.findAll()).thenReturn(List.of(mensaje1, mensaje2));
        when(assembler.toModel(any())).thenReturn(EntityModel.of(new Mensaje()));
        ResponseEntity<CollectionModel<EntityModel<Mensaje>>> response = controller.Listar();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testListarMensajes_sinContenido() {
        when(mensajeService.findAll()).thenReturn(List.of());

        ResponseEntity<CollectionModel<EntityModel<Mensaje>>> response = controller.Listar();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testCrearMensaje() {
        Mensaje mensaje = new Mensaje();
        mensaje.setId(1L);
        when(mensajeService.save(any(Mensaje.class))).thenReturn(mensaje);
        when(assembler.toModel(any(Mensaje.class))).thenReturn(EntityModel.of(mensaje));

        ResponseEntity<EntityModel<Mensaje>> response = controller.crearMensaje(mensaje);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testEliminar_existente() {
        when(mensajeService.delete(1L)).thenReturn(true);

        ResponseEntity<?> response = controller.eliminar(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario eliminado correctamente", response.getBody());
    }

    @Test
    void testEliminar_noExistente() {
        when(mensajeService.delete(1L)).thenReturn(false);

        ResponseEntity<?> response = controller.eliminar(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testActualizar_existente() {
        Mensaje mensaje = new Mensaje();
        mensaje.setId(1L);
        when(mensajeService.update(eq(1L), any(Mensaje.class))).thenReturn(Optional.of(mensaje));

        ResponseEntity<EntityModel<Mensaje>> response = controller.actualizar(1L, mensaje);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testActualizar_noExistente() {
        Mensaje mensaje = new Mensaje();
        when(mensajeService.update(eq(1L), any(Mensaje.class))).thenReturn(Optional.empty());

        ResponseEntity<EntityModel<Mensaje>> response = controller.actualizar(1L, mensaje);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testBuscarPorId_existente() {
        Mensaje mensaje = new Mensaje();
        mensaje.setId(1L);
        when(mensajeService.findById(1L)).thenReturn(Optional.of(mensaje));

        ResponseEntity<Mensaje> response = controller.buscarPorId(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testBuscarPorId_noExistente() {
        when(mensajeService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Mensaje> response = controller.buscarPorId(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}

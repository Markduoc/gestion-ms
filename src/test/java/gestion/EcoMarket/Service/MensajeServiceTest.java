package gestion.EcoMarket.Service;

import gestion.EcoMarket.Model.Mensaje;
import gestion.EcoMarket.Repository.MensajeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MensajeServiceTest {

    @Mock
    private MensajeRepository mensajeRepository;

    @InjectMocks
    private MensajeService mensajeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Mensaje m1 = new Mensaje();
        Mensaje m2 = new Mensaje();
        when(mensajeRepository.findAll()).thenReturn(List.of(m1, m2));

        List<Mensaje> resultado = mensajeService.findAll();
        assertEquals(2, resultado.size());
    }

    @Test
    void testFindById_existente() {
        Mensaje mensaje = new Mensaje();
        mensaje.setId(1L);
        when(mensajeRepository.findById(1L)).thenReturn(Optional.of(mensaje));

        Optional<Mensaje> resultado = mensajeService.findById(1L);
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void testFindById_noExistente() {
        when(mensajeRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Mensaje> resultado = mensajeService.findById(1L);
        assertFalse(resultado.isPresent());
    }

    @Test
    void testSave() {
        Mensaje mensaje = new Mensaje();
        mensaje.setTitulo("Test");
        when(mensajeRepository.save(mensaje)).thenReturn(mensaje);

        Mensaje resultado = mensajeService.save(mensaje);
        assertEquals("Test", resultado.getTitulo());
    }

    @Test
    void testDelete_existente() {
        when(mensajeRepository.existsById(1L)).thenReturn(true);

        boolean resultado = mensajeService.delete(1L);
        assertTrue(resultado);
        verify(mensajeRepository).deleteById(1L);
    }

    @Test
    void testDelete_noExistente() {
        when(mensajeRepository.existsById(1L)).thenReturn(false);

        boolean resultado = mensajeService.delete(1L);
        assertFalse(resultado);
        verify(mensajeRepository, never()).deleteById(anyLong());
    }

    @Test
    void testUpdate_existente() {
        Mensaje original = new Mensaje();
        original.setId(1L);
        original.setTitulo("Antiguo");

        Mensaje actualizado = new Mensaje();
        actualizado.setTitulo("Nuevo");
        actualizado.setDescripcion("desc");
        actualizado.setCuerpo("cuerpo");
        actualizado.setActivo(true);

        when(mensajeRepository.findById(1L)).thenReturn(Optional.of(original));
        when(mensajeRepository.save(any(Mensaje.class))).thenReturn(original);

        Optional<Mensaje> resultado = mensajeService.update(1L, actualizado);
        assertTrue(resultado.isPresent());
        assertEquals("Nuevo", resultado.get().getTitulo());
        assertEquals("desc", resultado.get().getDescripcion());
        assertEquals("cuerpo", resultado.get().getCuerpo());
        assertTrue(resultado.get().isActivo());
    }

    @Test
    void testUpdate_noExistente() {
        Mensaje mensaje = new Mensaje();
        when(mensajeRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Mensaje> resultado = mensajeService.update(1L, mensaje);
        assertFalse(resultado.isPresent());
    }
}

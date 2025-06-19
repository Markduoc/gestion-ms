package gestion.EcoMarket.Service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gestion.EcoMarket.Model.Mensaje;
import gestion.EcoMarket.Repository.MensajeRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MensajeService {

    @Autowired
    private MensajeRepository mensajeRepository;

    public List<Mensaje> findAll(){
        return mensajeRepository.findAll();
    }

    public Optional<Mensaje> findById(Long id){
        return mensajeRepository.findById(id);
    }

    public Mensaje save(Mensaje mensaje){
        return mensajeRepository.save(mensaje);
    }

    public boolean delete(Long id){
        if (mensajeRepository.existsById(id)) {
            mensajeRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Optional<Mensaje> update(Long id, Mensaje mensaje){
        Optional<Mensaje> mensajeExiste = mensajeRepository.findById(id);
        if (mensajeExiste.isPresent()) {
            Mensaje m = mensajeExiste.get();
            m.setActivo(mensaje.isActivo());
            m.setCuerpo(mensaje.getCuerpo());
            m.setDescripcion(mensaje.getDescripcion());
            m.setTitulo(mensaje.getTitulo());
            mensajeRepository.save(m);
            return Optional.of(m);
        } else{
            return Optional.empty();
        }
    }
}

package gestion.EcoMarket.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gestion.EcoMarket.Model.Mensaje;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long>{

    @Query("SELECT m FROM Mensaje m WHERE m.titulo= :titulo")
    List<Mensaje> buscarPorTitulo(@Param("titulo") String titulo);
}

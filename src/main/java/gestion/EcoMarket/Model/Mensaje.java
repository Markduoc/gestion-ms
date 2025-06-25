package gestion.EcoMarket.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Mensaje")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mensaje {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable= false)
    private String titulo;

    @Column
    private String descripcion;

    @Column(nullable= false)
    private String cuerpo;

    @Column
    private boolean activo;

    @Column(name = "id_usuario") 
    private Long idUsuario;

}

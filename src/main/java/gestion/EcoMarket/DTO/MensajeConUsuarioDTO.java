package gestion.EcoMarket.DTO;

import gestion.EcoMarket.Model.Mensaje;
import lombok.Data;

@Data
public class MensajeConUsuarioDTO {
    private Mensaje mensaje;
    private UsuarioDTO usuario;
}
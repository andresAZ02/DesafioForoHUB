package com.forohub.forohub.domain.topico;


import java.time.LocalDateTime;

public record DtoListarTopicos(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fecha_Creacion,
        Boolean status,
        String autor,
        String curso
) {
    public DtoListarTopicos(Topico topico) {
        this(topico.getId(), topico.getTitulo(), topico.getMensaje(), topico.getFecha_Creacion(), topico.getStatus(), topico.getAutor(), topico.getCurso());
    }
}
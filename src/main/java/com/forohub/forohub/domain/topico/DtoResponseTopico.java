package com.forohub.forohub.domain.topico;



import java.time.LocalDateTime;

public record DtoResponseTopico(
        Long id,
        String titulo,
        String mensaje,

        String autor,
        String curso) {
}
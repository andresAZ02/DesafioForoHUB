package com.forohub.forohub.controller;

import com.forohub.forohub.domain.repository.TopicoRepository;
import com.forohub.forohub.domain.topico.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    // crea un nuevo tema
    @PostMapping
    public ResponseEntity<DtoResponseTopico> registrarTopico(
            @RequestBody @Valid DtoRegistrarTopico dtoRegistrarTopico,
            UriComponentsBuilder uriComponentsBuilder) {
        // Metodo de guardar
        var topico = topicoRepository.save(new Topico(dtoRegistrarTopico));


        var dtoResponseTopico = new DtoResponseTopico(topico.getId(), topico.getTitulo(), topico.getMensaje(),
                topico.getAutor(), topico.getCurso());

        // Resource URI
        URI location = uriComponentsBuilder.path("/topicos/{id}")
                .buildAndExpand(topico.getId())
                .toUri();

        return ResponseEntity.created(location).body(dtoResponseTopico);
    }


    @GetMapping
    public ResponseEntity<List<DtoListarTopicos>> listarTopicos() {

        var topicsList = topicoRepository.findAll()
                .stream()
                .map(DtoListarTopicos::new)
                .toList();
        return ResponseEntity.ok(topicsList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<DtoListarTopicos> muestraTopico(@PathVariable Long id) {

        Optional<Topico> topicoOptional = topicoRepository.findById(id);
        if (topicoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var topicoDto = new DtoListarTopicos(topicoOptional.get());
        return ResponseEntity.ok(topicoDto);
    }


    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DtoListarTopicos> actualizaTopico(
            @RequestBody @Valid DtoActualizarTopico dtoActualizarTopico,
            @PathVariable Long id) {

        Optional<Topico> topicoOptional = topicoRepository.findById(id);
        if (topicoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var topico = topicoOptional.get();
        topico.actualizarDatos(dtoActualizarTopico);

        return ResponseEntity.ok(new DtoListarTopicos(topico));
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarTopico(@PathVariable Long id) {
        System.out.println("DELETE Request received for ID: " + id);

        Optional<Topico> topicoOptional = topicoRepository.findById(id);
        if (topicoOptional.isEmpty()) {
            System.out.println("Topic not found for ID: " + id);
            return ResponseEntity.notFound().build();
        }

        System.out.println("Deleting topic with ID: " + id);
        topicoRepository.delete(topicoOptional.get());
        return ResponseEntity.noContent().build();
    }
}
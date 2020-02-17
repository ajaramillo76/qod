package edu.cnm.deepdive.qod.controller;

import com.sun.org.apache.bcel.internal.generic.ARETURN;
import edu.cnm.deepdive.qod.model.entity.Quote;
import edu.cnm.deepdive.qod.model.entity.Source;
import edu.cnm.deepdive.qod.service.SourceRepository;
import java.net.URI;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/sources")
public class SourceController {

  private final SourceRepository repository;

  @Autowired
  public SourceController(SourceRepository repository) {
    this.repository = repository;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Source> post(@RequestBody Source source) {
    repository.save(source);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .build(source.getId());
    return ResponseEntity.created(location).body(source);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Source> get() {
    return repository.findAllByOrderByName();
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Source get(@PathVariable UUID id) {
    return repository.findById(id).get();
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID id) {
    repository.findById(id).ifPresent((source) -> {
      Set<Quote> quotes = source.getQuotes();
      quotes.forEach((quote) -> quote.setSource(null));
      quotes.clear();
      repository.delete(source);
    });
  }

  @PutMapping(value = "/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Source put(@PathVariable UUID id, @RequestBody Source updated){
    Source source = get(id);
    source.setName(updated.getName());
    return repository.save(source);
  }

}

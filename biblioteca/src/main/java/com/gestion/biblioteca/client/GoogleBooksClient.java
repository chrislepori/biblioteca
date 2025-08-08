package com.gestion.biblioteca.client;

import com.gestion.biblioteca.dto.GoogleBooksApiResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "google-books", url = "https://www.googleapis.com")
public interface GoogleBooksClient {

    @GetMapping("/books/v1/volumes")
    GoogleBooksApiResponseDTO buscarPorIsbn(@RequestParam("q") String query);
}

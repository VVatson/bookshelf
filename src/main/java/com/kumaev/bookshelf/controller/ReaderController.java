package com.kumaev.bookshelf.controller;

import com.kumaev.bookshelf.api.ReaderApi;
import com.kumaev.bookshelf.model.Reader;
import com.kumaev.bookshelf.service.ReaderService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
public class ReaderController implements ReaderApi {

    private final ReaderService readerService;

    @Autowired
    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @Override
    public ResponseEntity<Void> createReader(
            @ApiParam(value = "Created reader object", required = true) @Valid @RequestBody Reader body) {
        readerService.addReader(body);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteReader(
            @ApiParam(value = "The readerId that needs to be deleted", required = true)
            @PathVariable("readerId") Long readerId) {
        readerService.deleteReader(readerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Reader> getReaderById(
            @ApiParam(value = "The id that needs to be fetched.", required = true)
            @PathVariable("readerId") Long readerId) {
        Reader reader = readerService.getReader(readerId);
        return new ResponseEntity<>(reader, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Reader> findReaderByName(
            @NotNull @ApiParam(value = "Name to filter by", required = true)
            @Valid @RequestParam(value = "name") String name) {
        Reader reader = readerService.getReaderByName(name);
        return new ResponseEntity<>(reader, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateReader(
            @ApiParam(value = "name that need to be updated", required = true)
            @PathVariable("readerId") Long readerId,
            @ApiParam(value = "Updated reader object", required = true)
            @Valid @RequestBody Reader body) {
        readerService.updateReader(readerId, body);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}

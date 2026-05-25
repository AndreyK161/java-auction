package ws.academy.auction.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import ws.academy.auction.api.dto.rs.photos.ImageRef;

@Validated
@RequestMapping("/api/v1/file/")
@Tag(name = "FileController", description = "Контроллер для загрузки файлов")
public interface FileController {

    @Operation(summary = "Загрузка файлов")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    ImageRef uploadFile(@RequestParam("file") MultipartFile file);
}

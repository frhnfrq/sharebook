package com.sharebook.backend.controllers

import com.sharebook.backend.dto.ResponseDto
import org.springframework.core.io.FileSystemResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.io.path.exists


@RestController
@RequestMapping("api/image")
class ImageController {
    var UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads"

    @GetMapping("{imageName}")
    fun displayImage(@PathVariable imageName: String): ResponseEntity<FileSystemResource> {
        val imagePath = Paths.get(UPLOAD_DIRECTORY, imageName)

        if (!imagePath.exists()) {
            throw Exception("File not found")
        }

        val imageResource = FileSystemResource(imagePath.toFile())


        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG) // Change the MediaType based on your image type
            .body(imageResource)
    }

    @PostMapping
    fun uploadImage(@RequestParam("image") file: MultipartFile): ResponseDto<String> {

        if (file.contentType?.startsWith("image") == false) {
            return ResponseDto(Result.failure(Exception("Invalid file format.")))
        }

        val fileName =
            LocalDateTime.now().toInstant(ZoneOffset.UTC).epochSecond.toString() + "_" + file.originalFilename
        val fileNameAndPath: Path = Paths.get(
            UPLOAD_DIRECTORY,
            fileName,
        )

        Files.createDirectories(Paths.get(UPLOAD_DIRECTORY))
        Files.write(fileNameAndPath, file.bytes)

        return ResponseDto(success = true, message = null, data = fileName)
    }

}
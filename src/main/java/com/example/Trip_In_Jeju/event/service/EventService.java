package com.example.Trip_In_Jeju.event.service;

import com.example.Trip_In_Jeju.event.entity.Event;
import com.example.Trip_In_Jeju.event.entity.ImageStep;
import com.example.Trip_In_Jeju.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    @Value("${custom.fileDirPath}")
    private String fileDirPath;

    @Transactional
    public void saveEvent(Event event, MultipartFile thumbnailImg, MultipartFile[] steps) {
        // Save the thumbnail image
        String thumbnailImgPath = saveFile(thumbnailImg);
        event.setThumbnailImg(thumbnailImgPath);

        // Save step images
        List<ImageStep> imageSteps = new ArrayList<>();
        for (int i = 0; i < steps.length; i++) {
            String stepImgPath = saveFile(steps[i]);
            ImageStep imageStep = new ImageStep();
            imageStep.setStepNumber(i + 1);
            imageStep.setImageFilename(steps[i].getOriginalFilename());
            imageStep.setImageFilePath(stepImgPath);
            imageStep.setEvent(event);
            imageSteps.add(imageStep);
        }
        event.setSteps(imageSteps);

        eventRepository.save(event);
    }

    private String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        try {
            // 생성할 파일명(UUID 사용)
            String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(fileDirPath, uniqueFilename);
            Files.createDirectories(path.getParent()); // Ensure the directories exist
            Files.write(path, file.getBytes());
            // 반환할 웹 경로
            return "/gen/" + uniqueFilename;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    public Event findEventById(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        return event.orElse(null);
    }
}

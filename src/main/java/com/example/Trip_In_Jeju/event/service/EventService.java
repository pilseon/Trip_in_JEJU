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

    @Transactional
    public void updateEvent(Event event, MultipartFile thumbnailImg, MultipartFile[] steps) {
        if (!thumbnailImg.isEmpty()) {
            String thumbnailImgPath = saveFile(thumbnailImg);
            event.setThumbnailImg(thumbnailImgPath);
        }

        if (steps != null && steps.length > 0) {
            List<ImageStep> imageSteps = event.getSteps();
            for (int i = 0; i < steps.length; i++) {
                if (i < imageSteps.size()) {
                    // 기존 이미지를 업데이트
                    if (!steps[i].isEmpty()) {
                        String stepImgPath = saveFile(steps[i]);
                        ImageStep imageStep = imageSteps.get(i);
                        imageStep.setImageFilename(steps[i].getOriginalFilename());
                        imageStep.setImageFilePath(stepImgPath);
                    }
                } else {
                    // 새로운 이미지를 추가
                    if (!steps[i].isEmpty()) {
                        String stepImgPath = saveFile(steps[i]);
                        ImageStep newImageStep = new ImageStep();
                        newImageStep.setStepNumber(i + 1);
                        newImageStep.setImageFilename(steps[i].getOriginalFilename());
                        newImageStep.setImageFilePath(stepImgPath);
                        newImageStep.setEvent(event);
                        imageSteps.add(newImageStep);
                    }
                }
            }
        }

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

    @Transactional
    public void deleteEventById(Long id) {
        eventRepository.deleteById(id);
    }
}

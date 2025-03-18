package ru.sber.bookingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sber.bookingservice.dto.ResourceDTO;
import ru.sber.bookingservice.exceptions.ResourceNotFoundException;
import ru.sber.bookingservice.mapper.ResourceMapper;
import ru.sber.bookingservice.model.Resource;
import ru.sber.bookingservice.repository.ResourceRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для управления ресурсами.
 */
@RequiredArgsConstructor
@Service
public class ResourceService {
    private final ResourceRepository repository;
    private final ResourceMapper mapper;

    /**
     * Создает новый ресурс на основании переданного DTO и сохраняет его в хранилище.
     * @param resourceDTO DTO нового ресурса
     * @return DTO сохраненного ресурса
     */
    public ResourceDTO createResource(ResourceDTO resourceDTO) {
        Resource resource = mapper.toEntity(resourceDTO);
        return mapper.toDTO(repository.save(resource));
    }

    /**
     * Возвращает ресурс по переданному идентификатору. Бросает {@link ResourceNotFoundException}
     * @param id Идентификатор ресурса
     * @return Ресурс с указанным идентификатором
     * @throws ResourceNotFoundException Если ресурс не найден.
     */
    public Resource getResourceById(Long id) throws ResourceNotFoundException {
        return repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ресурс с id = " + id + " не найден"));
    }

    /**
     * Возвращает список всех ресурсов.
     * @return Список всех ресурсов
     */
    public List<ResourceDTO> getAllResources() {
        List<Resource> resourceList = repository.findAll();
        List<ResourceDTO> resourceDTOS = new ArrayList<>();
        resourceList.forEach(resource -> resourceDTOS.add(mapper.toDTO(resource)));
        return resourceDTOS;
    }

    /**
     * Выполняет поиск ресурсов по частичному совпадению имени.
     * @param name Частичное имя ресурса для поиска
     * @return Список ресурсов, имена которых содержат указанное имя
     */
    public List<ResourceDTO> findAllByName(String name) {
        List<Resource> resourceList = repository.findResourceByNameContainingIgnoreCase(name);
        List<ResourceDTO> resourceDTOS = new ArrayList<>();
        resourceList.forEach(resource -> resourceDTOS.add(mapper.toDTO(resource)));
        return resourceDTOS;
    }

    /**
     * Возвращает список всех ресурсов.
     * @return Список всех ресурсов
     */
    public List<Resource> findAll() {
        return repository.findAll();
    }

    /**
     * Обновляет существующий ресурс на основании переданного DTO.
     * @param resourceDTO DTO ресурса с новыми данными
     * @return DTO обновленного ресурса
     * @throws ResourceNotFoundException Если ресурс с указанным идентификатором не найден
     */
    public ResourceDTO update(ResourceDTO resourceDTO) throws ResourceNotFoundException {
        Resource resource = repository.findById(resourceDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Ресурс с id = " +
                        resourceDTO.getId() + " не найден"));
        return mapper.toDTO(repository.save(resource));
    }

    /**
     * Удаляет ресурс по указанному идентификатору.
     * @param id Идентификатор ресурса для удаления
     * @throws ResourceNotFoundException Если ресурс с указанным идентификатором не найден
     */
    public void deleteResource(Long id) throws ResourceNotFoundException {
        Resource resource = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ресурс с id = " + id + " не найден"));
        repository.delete(resource);
    }
}

package ru.sber.bookingservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import ru.sber.bookingservice.config.ModelMapperConfig;
import ru.sber.bookingservice.dto.ResourceDTO;
import ru.sber.bookingservice.exceptions.ResourceNotFoundException;
import ru.sber.bookingservice.mapper.ResourceMapper;
import ru.sber.bookingservice.model.Resource;
import ru.sber.bookingservice.repository.ResourceRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {ModelMapperConfig.class})
class ResourceServiceTest {
    @Mock
    private ResourceRepository repository;
    @Mock
    private ResourceMapper resourceMapper;

    @InjectMocks
    private ResourceService resourceService;

    private Resource resource;
    private ResourceDTO resourceDTO;

    @BeforeEach
    void setup() {

        resource = new Resource();
        resource.setId(1L);
        resource.setName("resource_name");
        resource.setDescription("resource_desr");

        resourceDTO = new ResourceDTO();
        resourceDTO.setId(resource.getId());
        resourceDTO.setName(resource.getName());
        resourceDTO.setDescription(resource.getDescription());
    }

    @Test
    void givenExistingResourceId_whenGetResourceById_thenReturnResource() throws ResourceNotFoundException {
        Long resourceId = 1L;
        when(repository.findById(resourceId)).thenReturn(Optional.of(resource));

        Resource returnedResource = resourceService.getResourceById(resourceId);

        assertNotNull(returnedResource);
        verify(repository).findById(resourceId);
    }

    @Test
    void givenNonExistingResourceId_whenGetResourceById_thenThrowResourceNotFoundException() {
        Long nonExistingResourceId = 999L;
        when(repository.findById(nonExistingResourceId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> resourceService.getResourceById(nonExistingResourceId));

        assertTrue(exception.getMessage().contains("Ресурс с id = " + nonExistingResourceId + " не найден"));
        verify(repository).findById(nonExistingResourceId);
    }

    @Test
    void givenExistingResources_whenGetAllResources_thenReturnListOfResourceDTOs() {
        List<Resource> resources = Arrays.asList(new Resource(), new Resource());
        when(repository.findAll()).thenReturn(resources);

        List<ResourceDTO> returnedResourceDTOs = resourceService.getAllResources();

        assertFalse(returnedResourceDTOs.isEmpty());
        verify(repository).findAll();
        verify(resourceMapper, times(resources.size())).toDTO(any(Resource.class));
    }

    @Test
    void givenPartialResourceName_whenFindAllByName_thenReturnMatchingResourceDTOs() {
        String partialName = "ресурс";
        List<Resource> matchingResources = Arrays.asList(new Resource(), new Resource());
        when(repository.findResourceByNameContainingIgnoreCase(partialName)).thenReturn(matchingResources);

        List<ResourceDTO> returnedResourceDTOs = resourceService.findAllByName(partialName);

        assertFalse(returnedResourceDTOs.isEmpty());
        verify(repository).findResourceByNameContainingIgnoreCase(partialName);
        verify(resourceMapper, times(matchingResources.size())).toDTO(any(Resource.class));
    }

    @Test
    void givenInvalidResourceId_whenUpdate_thenThrowResourceNotFoundException() {
        ResourceDTO invalidResourceDTO = new ResourceDTO();
        when(repository.findById(invalidResourceDTO.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> resourceService.update(invalidResourceDTO));

        assertTrue(exception.getMessage().contains("Ресурс с id = " + invalidResourceDTO.getId() + " не найден"));
        verify(repository).findById(invalidResourceDTO.getId());
    }

    @Test
    void givenExistingResourceId_whenDeleteResource_thenRemoveFromRepository()
            throws ResourceNotFoundException {
        Long existingResourceId = 1L;
        Resource existingResource = new Resource();
        when(repository.findById(existingResourceId)).thenReturn(Optional.of(existingResource));

        resourceService.deleteResource(existingResourceId);

        verify(repository).findById(existingResourceId);
        verify(repository).delete(existingResource);
    }

    @Test
    void givenNonExistingResourceId_whenDeleteResource_thenThrowResourceNotFoundException() {
        Long nonExistingResourceId = 999L;
        when(repository.findById(nonExistingResourceId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> resourceService.deleteResource(nonExistingResourceId));

        assertTrue(exception.getMessage().contains("Ресурс с id = " + nonExistingResourceId + " не найден"));
        verify(repository).findById(nonExistingResourceId);
    }
}
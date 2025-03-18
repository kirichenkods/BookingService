package ru.sber.bookingservice.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sber.bookingservice.dto.ResourceDTO;
import ru.sber.bookingservice.model.Resource;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class ResourceMapper {
    private final ModelMapper mapper;

    public Resource toEntity(ResourceDTO resourceDTO) {
        return Objects.isNull(resourceDTO) ?
                null :
                mapper.map(resourceDTO, Resource.class);
    }

    public ResourceDTO toDTO(Resource resource) {
        return Objects.isNull(resource) ?
                null :
                mapper.map(resource, ResourceDTO.class);
    }
}

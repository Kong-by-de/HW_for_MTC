package com.mipt.aleksandrivanovich.second_sem.hw_1.config;

import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.TaskCreateDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.TaskResponseDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.TaskUpdateDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Task;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Маппер для преобразования.
 */
@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Task updateEntity(TaskUpdateDto dto, @MappingTarget Task task);

    TaskResponseDto toResponseDto(Task task);
}
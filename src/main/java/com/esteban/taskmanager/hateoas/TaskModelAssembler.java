package com.esteban.taskmanager.hateoas;

import com.esteban.taskmanager.controller.TaskController;
import com.esteban.taskmanager.dto.TaskResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TaskModelAssembler implements RepresentationModelAssembler<TaskResponse, EntityModel<TaskResponse>> {

    private final static String ALL_TASKS_REL = "To all tasks";

    @Override
    public EntityModel<TaskResponse> toModel(TaskResponse entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(TaskController.class).getTaskById(entity.id())).withSelfRel(),
                linkTo(methodOn(TaskController.class).getAllTasks()).withRel(ALL_TASKS_REL));
    }

    @Override
    public CollectionModel<EntityModel<TaskResponse>> toCollectionModel(Iterable<? extends TaskResponse> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}

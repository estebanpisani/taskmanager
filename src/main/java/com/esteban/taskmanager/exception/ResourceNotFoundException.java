package com.esteban.taskmanager.exception;

public final class ResourceNotFoundException extends TaskException{

    public ResourceNotFoundException(String id){
        super("No se encontró la tarea con id: "+id);
    }
}

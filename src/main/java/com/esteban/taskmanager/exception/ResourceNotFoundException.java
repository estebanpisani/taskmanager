package com.esteban.taskmanager.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String id){
        super("No se encontró la tarea con id: "+id);
    }
}

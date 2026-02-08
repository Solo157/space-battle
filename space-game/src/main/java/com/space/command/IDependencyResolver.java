package com.space.command;

public interface IDependencyResolver {

    Object resolve(String dependency, Object[] args);

}

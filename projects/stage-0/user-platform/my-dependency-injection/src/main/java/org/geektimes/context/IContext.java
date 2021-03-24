package org.geektimes.context;

public interface IContext {

    <C> C getComponent(String name);

    void setParentContainer(IContext parentContainer);

    IContext getParentContainer();
}

package org.dreamscale.transaction;

public interface UnitOfWorkWithResponse<T> {

    T execute();

}

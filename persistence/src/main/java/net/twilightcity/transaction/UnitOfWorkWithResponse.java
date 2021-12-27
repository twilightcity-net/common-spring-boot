package net.twilightcity.transaction;

public interface UnitOfWorkWithResponse<T> {

    T execute();

}

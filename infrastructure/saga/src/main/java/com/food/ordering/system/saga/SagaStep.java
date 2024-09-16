package com.food.ordering.system.saga;

import com.food.ordering.system.domain.event.DomainEvent;

/**
 * Interface representing a step in a Saga pattern.
 *
 * @param <T> the type of data being processed
 * @param <S> the type of domain event produced by the process method
 * @param <U> the type of domain event produced by the rollback method
 */
public interface SagaStep<T, S extends DomainEvent, U extends DomainEvent> {

    /**
     * Processes the given data and returns a domain event.
     *
     * @param data the data to be processed
     * @return a domain event of type S
     */
    S process(T data);

    /**
     * Rolls back the given data and returns a domain event.
     *
     * @param data the data to be rolled back
     * @return a domain event of type U
     */
    U rollback(T data);
}
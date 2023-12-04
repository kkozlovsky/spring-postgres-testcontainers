package ru.kerporation.web.mapper;

import java.util.List;

public interface Mappable<E, D> {

    E toEntity(final D dto);

    D toDto(final E entity);

    List<D> toDto(final List<E> entityList);
}

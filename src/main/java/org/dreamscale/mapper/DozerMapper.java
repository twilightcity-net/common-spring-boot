package org.dreamscale.mapper;

import org.dozer.Mapper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DozerMapper {

    private Mapper mapper;

    public DozerMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public <S, D> D mapIfNotNull(S source, Class<D> destType) {
        D dest = null;
        if (source != null) {
            dest = mapper.map(source, destType);
        }
        return dest;
    }

    public <S, D> List<D> mapList(Iterable<S> source, Class<D> destType) {
        return mapList(source, (entity) -> mapIfNotNull(entity, destType));
    }

    public <S, D> List<D> mapList(Iterable<S> source, Function<S, D> function) {
        return toStream(source)
                .map(function)
                .collect(Collectors.toList());
    }

    private <T> Stream<T> toStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

}

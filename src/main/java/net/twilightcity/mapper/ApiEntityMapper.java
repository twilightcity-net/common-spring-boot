package net.twilightcity.mapper;

import java.util.List;

public class ApiEntityMapper<API_TYPE, ENTITY_TYPE> {

    private Class<API_TYPE> apiType;
    private Class<ENTITY_TYPE> entityType;
    private DozerMapper mapper;

    public ApiEntityMapper(Class<API_TYPE> apiType, Class<ENTITY_TYPE> entityType) {
        this(new DozerMapperFactory(), apiType, entityType);
    }

    public ApiEntityMapper(DozerMapperFactory mapperFactory, Class<API_TYPE> apiType, Class<ENTITY_TYPE> entityType) {
        this.apiType = apiType;
        this.entityType = entityType;
        mapper = mapperFactory.createDozerMapper();
    }

    public ENTITY_TYPE toEntity(API_TYPE api) {
        ENTITY_TYPE entity = mapper.mapIfNotNull(api, entityType);
        if (entity != null) {
            onEntityConversion(api, entity);
        }
        return entity;
    }

    protected void onEntityConversion(API_TYPE source, ENTITY_TYPE target) {
        // subclass to override if custom mapping is required
    }

    public List<ENTITY_TYPE> toEntityList(Iterable<API_TYPE> apiList) {
        return mapper.mapList(apiList, this::toEntity);
    }

    public API_TYPE toApi(ENTITY_TYPE entity) {
        API_TYPE api = mapper.mapIfNotNull(entity, apiType);
        if (api != null) {
            onApiConversion(entity, api);
        }
        return api;
    }

    protected void onApiConversion(ENTITY_TYPE source, API_TYPE target) {
        // subclass to override if custom mapping is required
    }

    public List<API_TYPE> toApiList(List<ENTITY_TYPE> entityList) {
        return mapper.mapList(entityList, this::toApi);
    }

}

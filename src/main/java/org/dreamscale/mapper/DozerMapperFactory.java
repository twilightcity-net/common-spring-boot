package org.dreamscale.mapper;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.List;

public class DozerMapperFactory {

    private List<String> mappingConfigFileList = new ArrayList<>();

    public DozerMapperFactory() {
        addMappingConfigFile("org/dreamscale/mapper/defaultDozerConfig.xml");
    }

    public void addMappingConfigFile(String mappingConfigFile) {
        mappingConfigFileList.add(mappingConfigFile);
    }

    public DozerMapper createDozerMapper() {
        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.setMappingFiles(mappingConfigFileList);
        return new DozerMapper(mapper);
    }

}

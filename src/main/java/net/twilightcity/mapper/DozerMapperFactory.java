package net.twilightcity.mapper;

import org.dozer.DozerBeanMapper;

import java.util.ArrayList;
import java.util.List;

public class DozerMapperFactory {

    private List<String> mappingConfigFileList = new ArrayList<>();

    public DozerMapperFactory() {
        addMappingConfigFile("net/twilightcity/mapper/defaultDozerConfig.xml");
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

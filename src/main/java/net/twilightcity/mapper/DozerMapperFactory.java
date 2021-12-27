package net.twilightcity.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

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
        Mapper mapper = DozerBeanMapperBuilder.create()
                .withMappingFiles(mappingConfigFileList)
                .build();
        return new DozerMapper(mapper);
    }

}

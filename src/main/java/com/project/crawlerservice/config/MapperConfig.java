package com.project.crawlerservice.config;

import com.project.crawlerservice.dto.DataDTO;
import com.project.crawlerservice.entity.DataEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MapperConfig {

    @Bean
    @Primary
    public ModelMapper mapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(new PropertyMap<DataDTO, DataEntity>() {
            @Override
            protected void configure() {
                map().getDataEmbeddableId().setCode(source.getCode());
                map().getDataEmbeddableId().setType(source.getType());
            }
        });
        mapper.addMappings(new PropertyMap<DataEntity, DataDTO>() {
            @Override
            protected void configure() {
                map().setCode(source.getDataEmbeddableId().getCode());
                map().setType(source.getDataEmbeddableId().getType());
            }
        });
        return mapper;
    }

}

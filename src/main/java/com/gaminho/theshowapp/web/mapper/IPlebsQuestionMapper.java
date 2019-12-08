package com.gaminho.theshowapp.web.mapper;


import com.gaminho.theshowapp.model.game.plebs.PlebsQuestion;
import com.gaminho.theshowapp.model.game.plebs.PlebsQuestionCategory;
import com.gaminho.theshowapp.web.model.PlebsQuestionAPI;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapper(componentModel="spring")
public abstract class IPlebsQuestionMapper {

    private static final Logger log = LoggerFactory.getLogger(IPlebsQuestionMapper.class);

    @BeforeMapping
    void enrichDTOWithCategory(PlebsQuestionAPI plebsAPI,
                               @MappingTarget PlebsQuestion plebsDTO) {

        if(plebsAPI.getCategory().equalsIgnoreCase("Child")){
            plebsDTO.setCategory(PlebsQuestionCategory.CHILD);
        } else if(plebsAPI.getCategory().equalsIgnoreCase("Crazy Girl")){
            plebsDTO.setCategory(PlebsQuestionCategory.CRAZY_GIRL);
        } else if(plebsAPI.getCategory().equalsIgnoreCase("Angry Hustler")){
            plebsDTO.setCategory(PlebsQuestionCategory.ANGRY_HUSTLER);
        } else if(plebsAPI.getCategory().equalsIgnoreCase("Interesting")){
            plebsDTO.setCategory(PlebsQuestionCategory.INTERESTING);
        } else {
            log.error("Category '{}' could not be parsed, set to {}",
                    plebsAPI.getCategory(), PlebsQuestionCategory.CHILD);
            plebsDTO.setCategory(PlebsQuestionCategory.CHILD);
        }
    }

    @Mappings({
            @Mapping(target="question", source="plebsApi.question"),
            @Mapping(target="creationDate", ignore=true),
            @Mapping(target = "category", ignore = true)
    })
    public abstract PlebsQuestion toPlebsQuestionDTO(PlebsQuestionAPI plebsApi);

    public static IPlebsQuestionMapper INSTANCE = Mappers.getMapper( IPlebsQuestionMapper.class );

//    @Mappings({
//            @Mapping(target="question", source="entity.question"),
//            @Mapping(target="creationDate", source = "entity.creation",
//                    dateFormat = "dd-MM-yyyy HH:mm:ss")
//    })
//    PlebsQuestionAPI plebsToPlebsDTO(PlebsQuestionAPI entity);
//
//    @Mappings({
//            @Mapping(target="id", source="dto.employeeId"),
//            @Mapping(target="name", source="dto.employeeName")
//    })
//    PlebsQuestion plebsDTOToPlebs(PlebsQuestion dto);
}

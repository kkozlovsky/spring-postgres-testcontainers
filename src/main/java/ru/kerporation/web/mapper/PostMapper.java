package ru.kerporation.web.mapper;

import org.mapstruct.Mapper;
import ru.kerporation.model.Post;
import ru.kerporation.web.dto.PostDto;

@Mapper(componentModel = "spring")
public interface PostMapper extends Mappable<Post, PostDto> {
}

package com.hust.webchat.mapper;

import com.hust.webchat.domain.Posting;
import com.hust.webchat.dto.PostingDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostingMapper extends EntityMapper<PostingDTO, Posting> {

    Posting toEntity(PostingDTO postingDTO);

    PostingDTO toDto(Posting posting);
}

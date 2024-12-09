package com.example.library2.mapper;

import com.example.library2.dto.library.LibraryDto;
import com.example.library2.entity.Library;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LibraryMapper {
    LibraryMapper INSTANCE = Mappers.getMapper(LibraryMapper.class);

    Library LibraryDtoToLibrary(LibraryDto libraryDto);
}


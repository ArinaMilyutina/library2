package com.example.library2.mapper;

import com.example.library2.dto.BookDto;
import com.example.library2.entity.book.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    Book bookDtoToBook(BookDto bookDto);
}

package com.example.library2.dto.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryDto {
    @Future(message = "Return date must be in the future")
    private LocalDateTime returnDate;
}

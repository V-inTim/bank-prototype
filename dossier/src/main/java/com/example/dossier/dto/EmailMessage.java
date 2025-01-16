package com.example.dossier.dto;

import com.example.dossier.type.Topic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage {
    private String address;
    private Topic theme;
    private UUID statementId;
    private String text;
}

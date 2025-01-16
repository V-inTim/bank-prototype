package com.example.deal.dto;

import com.example.deal.type.Topic;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class EmailMessage {
     private String address;
     private Topic theme;
     private UUID statementId;
     private String text;
}

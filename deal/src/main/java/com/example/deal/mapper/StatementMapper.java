package com.example.deal.mapper;

import com.example.deal.dto.StatementDto;
import com.example.deal.entity.Statement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {ClientMapper.class, CreditMapper.class})
public interface StatementMapper {
    StatementMapper INSTANCE = Mappers.getMapper(StatementMapper.class);

    @Mapping(target = "client", source = "clientId")
    @Mapping(target = "credit", source = "creditId")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "creationDate", source = "creationDate")
    @Mapping(target = "appliedOffer", source = "appliedOffer")
    @Mapping(target = "signDate", source = "signDate")
    @Mapping(target = "sesCode", source = "sesCode")
    @Mapping(target = "statusHistory",  source = "statusHistory")
    StatementDto statementToDto(Statement dto);

}




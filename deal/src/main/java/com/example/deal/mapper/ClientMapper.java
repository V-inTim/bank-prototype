package com.example.deal.mapper;

import com.example.deal.dto.LoanStatementRequestDto;
import com.example.deal.entity.Client;
import com.example.deal.entity.Employment;
import com.example.deal.entity.Passport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "middleName", source = "middleName")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "birthDate", source = "birthdate")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "clientId", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "maritalStatus", ignore = true)
    @Mapping(target = "dependentAmount", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "passport", expression = "java(createPassport(dto))")
    @Mapping(target = "employment", expression = "java(createEmployment())")
    Client dtoToClient(LoanStatementRequestDto dto);

    default Passport createPassport(LoanStatementRequestDto dto){
        return Passport.builder().series(dto.getPassportSeries())
                .number(dto.getPassportNumber()).build();
    }
    default Employment createEmployment(){
        return Employment.builder().build();
    }
}

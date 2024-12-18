package com.example.deal.mapper;

import com.example.deal.dto.CreditDto;
import com.example.deal.entity.Credit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CreditMapper {
    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "term", source = "term")
    @Mapping(target = "monthlyPayment", source = "monthlyPayment")
    @Mapping(target = "rate", source = "rate")
    @Mapping(target = "psk", source = "psk")
    @Mapping(target = "insuranceEnabled", source = "isInsuranceEnabled")
    @Mapping(target = "salaryClient", source = "isSalaryClient")
    @Mapping(target = "paymentSchedule", source = "paymentSchedule")
    @Mapping(target = "creditStatus", ignore = true)
    @Mapping(target = "creditId", ignore = true)
    Credit dtoToCredit(CreditDto dto);

}
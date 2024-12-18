package com.example.deal.mapper;

import com.example.deal.dto.LoanOfferDto;
import com.example.deal.entity.AppliedOffer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OfferMapper {

    @Mapping(target = "requestedAmount", source = "requestedAmount")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "term", source = "term")
    @Mapping(target = "monthlyPayment", source = "monthlyPayment")
    @Mapping(target = "rate", source = "rate")
    @Mapping(target = "isInsuranceEnabled", source = "isInsuranceEnabled")
    @Mapping(target = "isSalaryClient", source = "isSalaryClient")
    AppliedOffer dtoToAppliedOffer(LoanOfferDto dto);

}

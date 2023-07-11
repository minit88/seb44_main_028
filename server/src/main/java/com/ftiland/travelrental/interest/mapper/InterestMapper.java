package com.ftiland.travelrental.interest.mapper;

import com.ftiland.travelrental.interest.dto.InterestDto;
import com.ftiland.travelrental.interest.entity.Interest;
import org.mapstruct.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.ArrayList;

@Mapper(componentModel = "spring")
public interface InterestMapper {

    default InterestDto.ResponseDto interestToResponseDto(Interest interest){
        InterestDto.ResponseDto response = new InterestDto.ResponseDto();

        response.setInterestId(interest.getInterestId());
        response.setProduct(interest.getProduct());
        response.setMember(interest.getMember());

        return response;
    }

    default InterestDto.ResponsesDto interestsToResponsesDto (ArrayList<Interest> interests,long page,long size){
        InterestDto.ResponsesDto responses = new InterestDto.ResponsesDto();
        for(Interest interest : interests){
            InterestDto.ResponseDto response = interestToResponseDto(interest);
            responses.addResponse(response);
        }
        responses.setPage(page);
        responses.setSize(size);
        return responses;
    }
}

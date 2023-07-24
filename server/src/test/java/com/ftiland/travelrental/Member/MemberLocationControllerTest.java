package com.ftiland.travelrental.Member;

import antlr.preprocessor.Preprocessor;
import com.ftiland.travelrental.member.controller.MemberLocationController;
import com.ftiland.travelrental.member.dto.MemberDto;
import com.ftiland.travelrental.member.entity.Member;
import com.ftiland.travelrental.member.service.MemberLocationService;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

import org.springframework.security.test.context.support.WithMockUser;


import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(MemberLocationController.class)
@AutoConfigureRestDocs
public class MemberLocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberLocationService memberLocationService;

    @Autowired
    private Gson gson;

    @Test
    @WithMockUser(username = "사용자", roles = {"USER"})
    public void updateLocationTest() throws Exception {
        // given
        Member member = Member.builder()
                .memberId(1L)
                .email("test@test.com")
                .displayName("둘리")
                .imageUrl("")
                .address("서울특별시 서대문구 증가로12가길 49-50")
                .latitude(37.5793493362539)
                .longitude(126.91794995956589).build();

        given(memberLocationService.updateLocation(Mockito.any(Long.class),Mockito.any(Double.class),Mockito.any(Double.class))).willReturn(member);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/members/location")
                        .header("Authorization", "Bearer your-access-token")
                        .param("latitude",member.getLatitude().toString())
                        .param("longitude",member.getLongitude().toString())
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("members/updateLocation",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())
                        ,requestParameters(parameterWithName("longitude").description("사용자 좌표 경도"),
                                parameterWithName("latitude").description("사용자 좌표 (위도)"),
                                parameterWithName("_csrf").description("사용자 인증 토큰"))
                        )
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}

package com.ftiland.travelrental.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftiland.travelrental.chat.Controller.ChatController;
import com.ftiland.travelrental.chat.dto.ChatRoomDto;
import com.ftiland.travelrental.chat.dto.RequestDto;
import com.ftiland.travelrental.chat.dto.ResponseDto;
import com.ftiland.travelrental.chat.entity.ChatMessage;
import com.ftiland.travelrental.chat.entity.ChatRoom;
import com.ftiland.travelrental.chat.mapper.ChatMapper;
import com.ftiland.travelrental.chat.repository.ChatRoomMembersRepository;
import com.ftiland.travelrental.chat.service.ChatDtoService;
import com.ftiland.travelrental.chat.service.ChatEntityService;
import com.ftiland.travelrental.common.annotation.CurrentMember;
import com.ftiland.travelrental.common.resolver.CurrentMemberResolver;
import com.ftiland.travelrental.member.entity.Member;
import com.ftiland.travelrental.member.service.MemberService;
import com.ftiland.travelrental.product.service.ProductService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(ChatController.class)
@AutoConfigureRestDocs
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private ChatDtoService chatDtoService;

    @MockBean
    private ChatRoomMembersRepository chatRoomMembersRepository;

    @MockBean
    private MemberService memberService;

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;

    @MockBean
    private ChatMapper chatMapper;

    @MockBean
    private ChatEntityService chatEntityService;


    @Autowired
    private Gson gson;

    @Test
    public void getSellerControllerTest() throws Exception{
        long memberId = 1;
        String productId = "0dcaef9a-d52b-41c9-9ee4-3a3017ccc5d4";
        ResponseDto.SellerInfoForCustomer sellerInfoForCustomer = new ResponseDto.SellerInfoForCustomer();
        sellerInfoForCustomer.setSellerId(memberId);
        sellerInfoForCustomer.setChatRoomExists(false);


        // given
        given(productService.findSellerId(Mockito.anyString())).willReturn(memberId);
        given(chatEntityService.existsChatRooms(Mockito.anyLong(),Mockito.anyLong())).willReturn(false);
        //given(currentMemberResolver.resolveArgument(Mockito.any(MethodParameter.class),Mockito.any(ModelAndViewContainer.class),Mockito.any(NativeWebRequest.class),Mockito.any(WebDataBinderFactory.class))).willReturn(1);
        //given(currentMember.required()).willReturn(true);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/chat/seller")
                .header("Authorization", "Bearer"+"eyJhbGciOiJIUzUxMiJ9.eyJtZW1iZXJJZCI6Miwic3ViIjoiZGFkYSIsImlhdCI6MTY4OTY2MTE3NiwiZXhwIjoxNjkwMjYxMTc2fQ.8dlZMbgWcusz7ykGQ9XeqIQY3Kk2usnPdLHOZFKgk0t72mDC7jRA2TOSgXF8IYtDtnJwFX5aUWjqP5gsW2jxFQ")
                .param("productId",productId.toString())
                //.param("memberId",)
                .with(csrf())
        )
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("chat/getSeller",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("productId").description("상품 아이디"),
                                parameterWithName("_csrf").description("csrf 토큰")
                                )
                ))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "사용자", roles = {"USER"})
    public void createRoomTest() throws Exception{
        long memberId = 1;
        long receiverId =2;String productId = "0dcaef9a-d52b-41c9-9ee4-3a3017ccc5d4";
        ChatRoomDto chatRoomDto =new ChatRoomDto();
        chatRoomDto.setName("dsa");
        chatRoomDto.setRoomId("dasdsa");
        RequestDto.Post post = new RequestDto.Post();
        post.setName("dsa");
        post.setProductId(productId);
        given(productService.findSellerId(Mockito.anyString())).willReturn(receiverId);
        given(chatEntityService.existsChatRooms(Mockito.anyLong(),Mockito.anyLong())).willReturn( false);
        given(chatDtoService.createRoom(Mockito.anyString())).willReturn(chatRoomDto);
        given(chatEntityService.storeChatRoom(Mockito.anyLong(),Mockito.anyLong(),Mockito.anyString(),Mockito.anyString())).willReturn(null);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/chat")
                        .header("Authorization", "Bearer your-access-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(post))
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("chat/createChatroom",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("채팅방 이름"),
                                fieldWithPath("productId").description("상품 아이디")
                        )
                ))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    @WithMockUser(username = "사용자", roles = {"USER"})
    public void findChatroomMessageTest() throws Exception{
        String roomId = "dada";
        ArrayList<ChatMessage> chatMessageArrayList = new ArrayList<>();
        ResponseDto.Messages messages = new ResponseDto.Messages();

        given(chatEntityService.findChatroomMessages(Mockito.anyString())).willReturn(chatMessageArrayList);
        given(chatMapper.ChatMessagesToResponseMessages(Mockito.any())).willReturn(messages);


        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/chat/message1")
                        .header("Authorization", "Bearer your-access-token")
                        .param("roomId",roomId.toString())
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("chat/findChatroomMessage",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("roomId").description("채팅방 아이디"),
                                parameterWithName("_csrf").description("csrf 토큰")
                        )

                ))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    @WithMockUser(username = "사용자", roles = {"USER"})
    public void findChatroomTest() throws Exception{

        long receiverId= 1;
        String productId = "dasd-3213-das-fsfa";
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatroomId("0f3204c0-e52a-4900-800a-70ac3880b97f");
        chatRoom.setName("사용자 1과 사용자 2의 채팅방");
        given(productService.findSellerId(Mockito.anyString())).willReturn(receiverId);
        given(chatEntityService.findChatRoom(Mockito.anyLong(),Mockito.anyLong())).willReturn(chatRoom);


        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/chat/message1")
                        .header("Authorization", "Bearer your-access-token")
                        .param("productId",productId.toString())
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("chat/findChatroom",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("productId").description("상품 아이디"),
                                parameterWithName("_csrf").description("csrf 토큰")
                        )

                ))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}

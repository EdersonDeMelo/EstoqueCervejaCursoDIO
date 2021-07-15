package com.ederson.beerstock.controller;

import com.ederson.beerstock.builder.BeerDTOBuilder;
import com.ederson.beerstock.dto.BeerDTO;
import com.ederson.beerstock.dto.QuantityDTO;
import com.ederson.beerstock.exception.BeerNotFoundException;
import com.ederson.beerstock.service.BeerService;
import com.ederson.beerstock.utils.JsonConvertionUtils;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

@ExtendWith({MockitoExtension.class})
public class BeerControllerTest {
    private static final String BEER_API_URL_PATH = "/api/v1/beers";
    private static final long VALID_BEER_ID = 1L;
    private static final long INVALID_BEER_ID = 2L;
    private static final String BEER_API_SUBPATH_INCREMENT_URL = "/increment";
    private static final String BEER_API_SUBPATH_DECREMENT_URL = "/decrement";
    private MockMvc mockMvc;
    @Mock
    private BeerService beerService;
    @InjectMocks
    private BeerController beerController;

    public BeerControllerTest() {
    }

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new Object[]{this.beerController}).setCustomArgumentResolvers(new HandlerMethodArgumentResolver[]{new PageableHandlerMethodArgumentResolver()}).setViewResolvers(new ViewResolver[]{(s, locale) -> {
            return new MappingJackson2JsonView();
        }}).build();
    }

    @Test
    void whenPOSTIsCalledThenABeerIsCreated() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Mockito.when(this.beerService.createBeer(beerDTO)).thenReturn(beerDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/beers", new Object[0]).contentType(MediaType.APPLICATION_JSON).content(JsonConvertionUtils.asJsonString(beerDTO))).andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is(beerDTO.getName()))).andExpect(MockMvcResultMatchers.jsonPath("$.brand", Is.is(beerDTO.getBrand()))).andExpect(MockMvcResultMatchers.jsonPath("$.type", Is.is(beerDTO.getType().toString())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        beerDTO.setBrand((String)null);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/beers", new Object[0]).contentType(MediaType.APPLICATION_JSON).content(JsonConvertionUtils.asJsonString(beerDTO))).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Mockito.when(this.beerService.findByName(beerDTO.getName())).thenReturn(beerDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beers/" + beerDTO.getName(), new Object[0]).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is(beerDTO.getName()))).andExpect(MockMvcResultMatchers.jsonPath("$.brand", Is.is(beerDTO.getBrand()))).andExpect(MockMvcResultMatchers.jsonPath("$.type", Is.is(beerDTO.getType().toString())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Mockito.when(this.beerService.findByName(beerDTO.getName())).thenThrow(BeerNotFoundException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beers/" + beerDTO.getName(), new Object[0]).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void whenGETListWithBeersIsCalledThenOkStatusIsReturned() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Mockito.when(this.beerService.listAll()).thenReturn(Collections.singletonList(beerDTO));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beers", new Object[0]).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Is.is(beerDTO.getName()))).andExpect(MockMvcResultMatchers.jsonPath("$[0].brand", Is.is(beerDTO.getBrand()))).andExpect(MockMvcResultMatchers.jsonPath("$[0].type", Is.is(beerDTO.getType().toString())));
    }

    @Test
    void whenGETListWithoutBeersIsCalledThenOkStatusIsReturned() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Mockito.when(this.beerService.listAll()).thenReturn(Collections.singletonList(beerDTO));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beers", new Object[0]).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        ((BeerService)Mockito.doNothing().when(this.beerService)).deleteById(beerDTO.getId());
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beers/" + beerDTO.getId(), new Object[0]).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        ((BeerService)Mockito.doThrow(BeerNotFoundException.class).when(this.beerService)).deleteById(2L);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beers/2", new Object[0]).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void whenPATCHIsCalledToIncrementDiscountThenOKstatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder().quantity(10).build();
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        beerDTO.setQuantity(beerDTO.getQuantity() + quantityDTO.getQuantity());
        Mockito.when(this.beerService.increment(1L, quantityDTO.getQuantity())).thenReturn(beerDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/beers/1/increment", new Object[0]).contentType(MediaType.APPLICATION_JSON).content(JsonConvertionUtils.asJsonString(quantityDTO))).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is(beerDTO.getName()))).andExpect(MockMvcResultMatchers.jsonPath("$.brand", Is.is(beerDTO.getBrand()))).andExpect(MockMvcResultMatchers.jsonPath("$.type", Is.is(beerDTO.getType().toString()))).andExpect(MockMvcResultMatchers.jsonPath("$.quantity", Is.is(beerDTO.getQuantity())));
    }
}

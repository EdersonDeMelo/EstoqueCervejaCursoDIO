package com.ederson.beerstock.service;

import com.ederson.beerstock.builder.BeerDTOBuilder;
import com.ederson.beerstock.dto.BeerDTO;
import com.ederson.beerstock.entity.Beer;
import com.ederson.beerstock.exception.BeerAlreadyRegisteredException;
import com.ederson.beerstock.exception.BeerNotFoundException;
import com.ederson.beerstock.exception.BeerStockExceededException;
import com.ederson.beerstock.mapper.BeerMapper;
import com.ederson.beerstock.repository.BeerRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith({MockitoExtension.class})
public class BeerServiceTest {
    private static final long INVALID_BEER_ID = 1L;
    @Mock
    private BeerRepository beerRepository;
    private BeerMapper beerMapper;
    @InjectMocks
    private BeerService beerService;

    public BeerServiceTest() {
        this.beerMapper = BeerMapper.INSTANCE;
    }

    @Test
    void whenBeerInformedThenItShouldBeCreated() throws BeerAlreadyRegisteredException {
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedSavedBeer = this.beerMapper.toModel(expectedBeerDTO);
        Mockito.when(this.beerRepository.findByName(expectedBeerDTO.getName())).thenReturn(Optional.empty());
        Mockito.when((Beer)this.beerRepository.save(expectedSavedBeer)).thenReturn(expectedSavedBeer);
        BeerDTO createdBeerDTO = this.beerService.createBeer(expectedBeerDTO);
        MatcherAssert.assertThat(createdBeerDTO.getId(), Matchers.is(Matchers.equalTo(expectedBeerDTO.getId())));
        MatcherAssert.assertThat(createdBeerDTO.getName(), Matchers.is(Matchers.equalTo(expectedBeerDTO.getName())));
        MatcherAssert.assertThat(createdBeerDTO.getQuantity(), Matchers.is(Matchers.equalTo(expectedBeerDTO.getQuantity())));
    }

    @Test
    void whenAlreadyRegisteredBeerInformedThenAnExceptionShouldBeThrown() {
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer duplicatedBeer = this.beerMapper.toModel(expectedBeerDTO);
        Mockito.when(this.beerRepository.findByName(expectedBeerDTO.getName())).thenReturn(Optional.of(duplicatedBeer));
        Assertions.assertThrows(BeerAlreadyRegisteredException.class, () -> {
            this.beerService.createBeer(expectedBeerDTO);
        });
    }

    @Test
    void whenValidBeerNameIsGivenThenReturnABeer() throws BeerNotFoundException {
        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedFoundBeer = this.beerMapper.toModel(expectedFoundBeerDTO);
        Mockito.when(this.beerRepository.findByName(expectedFoundBeer.getName())).thenReturn(Optional.of(expectedFoundBeer));
        BeerDTO foundBeerDTO = this.beerService.findByName(expectedFoundBeerDTO.getName());
        MatcherAssert.assertThat(foundBeerDTO, Matchers.is(Matchers.equalTo(expectedFoundBeerDTO)));
    }

    @Test
    void whenNotRegisteredBeerNameIsGivenThenThrowAnException() {
        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Mockito.when(this.beerRepository.findByName(expectedFoundBeerDTO.getName())).thenReturn(Optional.empty());
        Assertions.assertThrows(BeerNotFoundException.class, () -> {
            this.beerService.findByName(expectedFoundBeerDTO.getName());
        });
    }

    @Test
    void whenListBeerIsCalledThenReturnAListOfBeers() {
        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedFoundBeer = this.beerMapper.toModel(expectedFoundBeerDTO);
        Mockito.when(this.beerRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundBeer));
        List<BeerDTO> foundListBeersDTO = this.beerService.listAll();
        MatcherAssert.assertThat(foundListBeersDTO, Matchers.is(Matchers.not(Matchers.empty())));
        MatcherAssert.assertThat((BeerDTO)foundListBeersDTO.get(0), Matchers.is(Matchers.equalTo(expectedFoundBeerDTO)));
    }

    @Test
    void whenListBeerIsCalledThenReturnAnEmptyListOfBeers() {
        Mockito.when(this.beerRepository.findAll()).thenReturn(Collections.EMPTY_LIST);
        List<BeerDTO> foundListBeersDTO = this.beerService.listAll();
        MatcherAssert.assertThat(foundListBeersDTO, Matchers.is(Matchers.empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenABeerShouldBeDeleted() throws BeerNotFoundException {
        BeerDTO expectedDeletedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedDeletedBeer = this.beerMapper.toModel(expectedDeletedBeerDTO);
        Mockito.when(this.beerRepository.findById(expectedDeletedBeerDTO.getId())).thenReturn(Optional.of(expectedDeletedBeer));
        ((BeerRepository)Mockito.doNothing().when(this.beerRepository)).deleteById(expectedDeletedBeerDTO.getId());
        this.beerService.deleteById(expectedDeletedBeerDTO.getId());
        ((BeerRepository)Mockito.verify(this.beerRepository, Mockito.times(1))).findById(expectedDeletedBeerDTO.getId());
        ((BeerRepository)Mockito.verify(this.beerRepository, Mockito.times(1))).deleteById(expectedDeletedBeerDTO.getId());
    }

    @Test
    void whenIncrementIsCalledThenIncrementBeerStock() throws BeerNotFoundException, BeerStockExceededException {
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedBeer = this.beerMapper.toModel(expectedBeerDTO);
        Mockito.when(this.beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
        Mockito.when((Beer)this.beerRepository.save(expectedBeer)).thenReturn(expectedBeer);
        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement = expectedBeerDTO.getQuantity() + quantityToIncrement;
        BeerDTO incrementedBeerDTO = this.beerService.increment(expectedBeerDTO.getId(), quantityToIncrement);
        MatcherAssert.assertThat(expectedQuantityAfterIncrement, Matchers.equalTo(incrementedBeerDTO.getQuantity()));
        MatcherAssert.assertThat(expectedQuantityAfterIncrement, Matchers.lessThan(expectedBeerDTO.getMax()));
    }

    @Test
    void whenIncrementIsGreatherThanMaxThenThrowException() {
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedBeer = this.beerMapper.toModel(expectedBeerDTO);
        Mockito.when(this.beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
        int quantityToIncrement = 80;
        Assertions.assertThrows(BeerStockExceededException.class, () -> {
            this.beerService.increment(expectedBeerDTO.getId(), quantityToIncrement);
        });
    }

    @Test
    void whenIncrementAfterSumIsGreatherThanMaxThenThrowException() {
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedBeer = this.beerMapper.toModel(expectedBeerDTO);
        Mockito.when(this.beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
        int quantityToIncrement = 45;
        Assertions.assertThrows(BeerStockExceededException.class, () -> {
            this.beerService.increment(expectedBeerDTO.getId(), quantityToIncrement);
        });
    }

    @Test
    void whenIncrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToIncrement = 10;
        Mockito.when(this.beerRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(BeerNotFoundException.class, () -> {
            this.beerService.increment(1L, quantityToIncrement);
        });
    }
}

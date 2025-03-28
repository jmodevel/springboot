package com.jmo.devel.cards.service;

import com.jmo.devel.cards.constants.CardsConstants;
import com.jmo.devel.cards.dto.CardDto;
import com.jmo.devel.cards.exception.CardAlreadyExistsException;
import com.jmo.devel.cards.exception.ResourceNotFoundException;
import com.jmo.devel.cards.mapper.CardsMapper;
import com.jmo.devel.cards.model.Card;
import com.jmo.devel.cards.repository.CardsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class CardsService implements ICardsService {

    private CardsRepository cardsRepository;

    @Override
    public void createCard( String mobileNumber ) {
        Optional<Card> optionalCard= cardsRepository.findByMobileNumber( mobileNumber );
        if(optionalCard.isPresent()){
            throw new CardAlreadyExistsException( "Card already registered with given mobileNumber " + mobileNumber );
        }
        cardsRepository.save( createNewCard( mobileNumber ) );
    }


    private Card createNewCard(String mobileNumber) {
        Card newCard = new Card();
        long randomCardNumber = 100000000000L + new Random().nextInt( 900000000 );
        newCard.setCardNumber     ( Long.toString( randomCardNumber ) );
        newCard.setMobileNumber   ( mobileNumber                      );
        newCard.setCardType       ( CardsConstants.CREDIT_CARD        );
        newCard.setTotalLimit     ( CardsConstants.NEW_CARD_LIMIT     );
        newCard.setAmountUsed     ( 0                                 );
        newCard.setAvailableAmount( CardsConstants.NEW_CARD_LIMIT     );
        return newCard;
    }

    @Override
    public CardDto fetchCard(String mobileNumber) {
        Card card = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
            () -> new ResourceNotFoundException( "card", "mobileNumber", mobileNumber )
        );
        return CardsMapper.toCardDto( card );
    }

    @Override
    public boolean updateCard(CardDto cardsDto) {
        Card card = cardsRepository.findByCardNumber( cardsDto.getCardNumber() ).orElseThrow(
            () -> new ResourceNotFoundException( "card", "cardNumber", cardsDto.getCardNumber() )
        );
        cardsRepository.save( CardsMapper.updateCard( card, cardsDto ) );
        return true;
    }

    @Override
    public boolean deleteCard(String mobileNumber) {
        Card cards = cardsRepository.findByMobileNumber( mobileNumber ).orElseThrow(
            () -> new ResourceNotFoundException( "card", "mobileNumber", mobileNumber )
        );
        cardsRepository.deleteById( cards.getCardId() );
        return true;
    }

}

package com.jmo.devel.cards.mapper;

import com.jmo.devel.cards.model.Card;
import com.jmo.devel.cards.dto.CardDto;

public class CardsMapper {

    public static CardDto toCardDto( Card card ) {
        return CardDto.builder()
            .cardNumber     ( card.getCardNumber()      )
            .cardType       ( card.getCardType()        )
            .mobileNumber   ( card.getMobileNumber()    )
            .totalLimit     ( card.getTotalLimit()      )
            .availableAmount( card.getAvailableAmount() )
            .amountUsed     ( card.getAmountUsed()      )
            .build();
    }

    public static Card toCard(CardDto cardDto) {
        return Card.builder()
            .cardNumber     ( cardDto.getCardNumber()      )
            .cardType       ( cardDto.getCardType()        )
            .mobileNumber   ( cardDto.getMobileNumber()    )
            .totalLimit     ( cardDto.getTotalLimit()      )
            .availableAmount( cardDto.getAvailableAmount() )
            .amountUsed     ( cardDto.getAmountUsed()      )
            .build();
    }

    public static Card updateCard( Card card, CardDto cardDto ) {
        card.setCardNumber     ( cardDto.getCardNumber()      );
        card.setCardType       ( cardDto.getCardType()        );
        card.setMobileNumber   ( cardDto.getMobileNumber()    );
        card.setTotalLimit     ( cardDto.getTotalLimit()      );
        card.setAvailableAmount( cardDto.getAvailableAmount() );
        card.setAmountUsed     ( cardDto.getAmountUsed()      );
        return card;
    }

}

package com.jmo.devel.cards.service;

import com.jmo.devel.cards.dto.CardDto;

public interface ICardsService {

    void        createCard( String mobileNumber );
    CardDto     fetchCard ( String mobileNumber );
    boolean     updateCard( CardDto cardDto );
    boolean     deleteCard( String mobileNumber );

}

package com.jmo.devel.cards.repository;

import com.jmo.devel.cards.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardsRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByMobileNumber( String mobileNumber );
    Optional<Card> findByCardNumber  ( String cardNumber   );

}

Feature: Es posible definir una fecha de validez para las JobOffers

    Scenario: Publicación de oferta con fecha de validez futura
        Given Estoy logueado como oferente
        When Creo una oferta con fecha de validez futura
        Then La oferta se muestra en mi lista de ofertas
        And La oferta se muestra en la lista pública de ofertas

    Scenario: Publicación de oferta sin fecha de validez
        Given Estoy logueado como oferente
        When Creo una oferta sin fecha de validez
        Then La oferta se muestra en mi lista de ofertas
        And La oferta se muestra en la lista pública de ofertas
        And La oferta se guarda con fecha de validez a un mes en el futuro

    Scenario: Publicación de oferta con fecha de validez pasada
        Given Estoy logueado como oferente
        When Creo una oferta con fecha de validez pasada
        Then La oferta no se crea

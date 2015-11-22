Feature: Es posible ver las fechas de inicio y validez para las JobOffers

    Scenario: Publicación de oferta
        Given Estoy logueado como oferente
        When Creo una oferta
        Then La oferta se ve en mi lista de ofertas mostrando la fecha de inicio y de validez

    Scenario: Detalles de una oferta
        Given Estoy logueado como oferente
        When Accedo a los detalles de una oferta
        Then La oferta se muestra incluyendo la fecha de inicio y de validez

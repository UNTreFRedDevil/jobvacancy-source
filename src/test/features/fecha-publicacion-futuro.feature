Feature: Es posible definir una fecha de publicacion para las JobOffers

Scenario: Publicación de oferta con fecha futura
    Given Estoy logueado como oferente
    When Creo una oferta con fecha de publicación futura
    Then La oferta se muestra en mi lista de ofertas
    And La oferta no se muestra en la lista pública de ofertas

Scenario: Publicación de oferta sin fecha
    Given Estoy logueado como oferente
    When Creo una oferta sin fecha de publicación
    Then La oferta se muestra en mi lista de ofertas
    And La oferta se muestra en la lista pública de ofertas

Scenario: Publicación de oferta con fecha pasada
    Given Estoy logueado como oferente
    When Creo una oferta con fecha de publicación pasada
    Then La oferta no se crea

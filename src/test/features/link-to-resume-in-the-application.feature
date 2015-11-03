Feature: Un usuario debe ingresar un link a su CV cuando aplica a una oferta

  Scenario: El usuario ingresa un CV válido al aplicar a una oferta
    Given un usuario quiere aplicar para una búsqueda
    When ingresa un nombre y una dirección de email válidos
    And carga un link válido a su CV
    Then se muestra una notificación de confirmación al usuario
    And se envia una aplicación al oferente de la oferta

  Scenario: El usuario ingresa un link invalido a un CV
    Given un usuario quiere aplicar para una búsqueda
    When ingresa un nombre y una dirección de email válidos
    And carga un link inválido a su CV
    Then se muestra una notificación que indica que el link es invalido
    And no se le permite al usuario que envíe la aplicación

  Scenario: El usuario no ingresa un CV al aplicar una oferta
    Given un usuario busca aplicar para una búsqueda
    When ingresa un nombre y una dirección de email válidos
    And no carga un CV
    Then no se le permite al usuario que envíe la aplicación
    And se muestra una notificacion que indica que el link es requerido

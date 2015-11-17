Feature: Como oferente quiero poder registar una oferta como "satisfecha" y que la misma ya no se muestre a los candidatos

  Scenario: Un oferente marca una oferta como finalizada
    Given un oferente
    And su lista de ofertas
    When el oferente marca una oferta como finalizada por <motivo>
    Then esa oferta no se muestra más en la lista de ofertas disponibles

    Examples:
      | motivo                                  |
      | candidato del sitio contratado          |
      | candidato de fuera del sitio contratado |
      | no se contrató a nadie                  |

Feature: Como oferente quiero poder registar una oferta como "satisfecha" y que la misma ya no se muestre a los candidatos

  Scenario: Un oferente marca una oferta como finalizada
    Given un oferente
    And su lista de ofertas
    When el oferente marca una oferta como finalizada por <motivo>
    Then esa oferta no se muestra en la lista de ofertas para los postulantes
    And el oferente ya no puede editar la oferta

    Examples:
      | motivo                                      |
      | se contrató un candidato del sitio          |
      | se contrató un candidato de fuera del sitio |
      | no se contrató a nadie                      |

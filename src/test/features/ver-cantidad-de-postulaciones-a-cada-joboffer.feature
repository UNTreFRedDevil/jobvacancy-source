Feature: Como oferente quiero saber la cantidad de postulaciones de cada una de mis ofertas
  Como oferente
  Quiero saber la cantidad de postulaciones de cada una de mis ofertas

Scenario: Listado de ofertas con columna de postulaciones
  Given un usuario que ofrece un trabajo
  When ve su lista de ofertas
  Then se muestra la cantidad de aplicaciones a cada una de las ofertas

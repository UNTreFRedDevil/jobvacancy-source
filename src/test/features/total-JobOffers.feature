Scenario: El administrador ingresa a la aplicación y ve la cantidad de publicaciones histórica
  Given un usuario administrador logueado en la aplicación
  When ingresa en la sección "estadísticas"
  Then la información de cantidad de publicaciones históricas está disponible en la página

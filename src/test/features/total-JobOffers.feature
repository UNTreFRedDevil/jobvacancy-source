Scenario: El administrador ingresa a la aplicación y ve la cantidad de publicaciones histórica
  Given un usuario administrador logueado en la aplicación
  When ingresa en la sección "estadísticas"
  Then la información de cantidad de publicaciones históricas está disponible en la página

Scenario: Al borrar una oferta el contador no disminuye
  Given un usuario administrador logueado en la aplicación
  When ingresa en la sección "estadísticas"
  Then la información de cantidad de publicaciones históricas está disponible en la página
  When un usuario borra una oferta
  And el administrador ingresa en la sección "estadísticas"
  Then la información de cantidad de publicaciones sigue igual

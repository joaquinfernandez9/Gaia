Feature: DaoTreeImpl

  Scenario: Obtener árbol exitosamente
    Given que tengo una conexión a la base de datos
    When obtengo el árbol para el usuario "<username>"
    Then obtengo un árbol válido

  Scenario: Actualizar nivel de árbol exitosamente
    Given que tengo una conexión a la base de datos
    When actualizo el nivel del árbol para el usuario "<username>"
    Then obtengo un árbol actualizado válido

  Scenario: Manejo de errores al obtener el árbol
    Given que tengo una conexión a la base de datos
    When intento obtener el árbol para el usuario "<username>"
    Then obtengo un error de base de datos

  Scenario: Manejo de errores al actualizar el nivel del árbol
    Given que tengo una conexión a la base de datos
    When intento actualizar el nivel del árbol para el usuario "<username>"
    Then obtengo un error de base de datos

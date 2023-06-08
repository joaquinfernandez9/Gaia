Feature: DaoFriendImpl

  Scenario: Obtener árbol de amigos exitosamente
    Given que tengo una conexión a la base de datos
    When obtengo el árbol de amigos para el usuario "<username>"
    Then obtengo una lista de árboles de amigos válida

  Scenario: Enviar solicitud de amistad exitosamente
    Given que tengo una conexión a la base de datos
    When envío una solicitud de amistad entre los usuarios "<username1>" y "<username2>"
    Then obtengo una solicitud de amistad válida entre los usuarios

  Scenario: Aceptar solicitud de amistad exitosamente
    Given que tengo una conexión a la base de datos
    When acepto la solicitud de amistad entre los usuarios "<username1>" y "<username2>"
    Then obtengo una amistad válida entre los usuarios

  Scenario: Rechazar solicitud de amistad exitosamente
    Given que tengo una conexión a la base de datos
    When rechazo la solicitud de amistad entre los usuarios "<username1>" y "<username2>"
    Then obtengo una solicitud de amistad rechazada entre los usuarios

  Scenario: Obtener lista de amigos exitosamente
    Given que tengo una conexión a la base de datos
    When obtengo la lista de amigos para el usuario "<username>"
    Then obtengo una lista de amigos válida

  Scenario: Obtener solicitudes pendientes exitosamente
    Given que tengo una conexión a la base de datos
    When obtengo las solicitudes pendientes para el usuario "<username>"
    Then obtengo una lista de solicitudes pendientes válida

  Scenario: Manejo de errores al obtener el árbol de amigos
    Given que tengo una conexión a la base de datos
    When intento obtener el árbol de amigos para el usuario "<username>"
    Then obtengo un error de base de datos

  Scenario: Manejo de errores al enviar solicitud de amistad
    Given que tengo una conexión a la base de datos
    When intento enviar una solicitud de amistad entre los usuarios "<username1>" y "<username2>"
    Then obtengo un error de base de datos

  Scenario: Manejo de errores al aceptar solicitud de amistad
    Given que tengo una conexión a la base de datos
    When intento aceptar la solicitud de amistad entre los usuarios "<username1>" y "<username2>"
    Then obtengo un error de base de datos

  Scenario: Manejo de errores al rechazar solicitud de amistad
    Given que tengo una conexión a la base de datos
    When intento rechazar la solicitud de amistad entre los usuarios "<username1>" y "<username2>"
    Then obtengo un error de base de datos

  Scenario: Manejo de errores al obtener la lista de amigos
    Given que tengo una conexión a la base de datos
    When intento obtener la lista de amigos para el usuario "<username>"
    Then obtengo un error de base de datos

  Scenario: Manejo de errores al obtener las solicitudes pendientes
    Given que tengo una conexión a la base de datos
    When intento obtener las solicitudes pendientes para el usuario "<username>"
    Then obtengo un error de base de datos

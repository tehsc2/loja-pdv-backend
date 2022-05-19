Feature: the version can be retrieved
  Scenario: client makes call to GET /pedidos
    Given that I want to see my first pedido
    When the client calls /pedidos
    Then the client receives status code of 200
    And the client receives a pedido
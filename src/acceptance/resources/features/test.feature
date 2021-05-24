Feature: test
  test description

  Scenario Outline: valid id
    Given "<state>" id
    When I ask if this id is ok
    Then I should be told "<answer>"

  Examples:
    | state   | answer  |
    | valid   | yes     |
    | invalid | no      |

  Scenario Outline: get car
    Given "<state>" id
    When I ask to get the car
    Then I should be told "<answer>"

    Examples:
      | state   | answer |
      | valid   | 200    |
      | invalid | 400    |

  Scenario Outline: add car
    Given "<state>" car
    When I ask to add the car
    Then I should be told "<answer>"

    Examples:
      | state   | answer |
      | valid   | 200    |
      | invalid | 400    |

  Scenario Outline: delete car
    Given "<state>" id
    When I ask to delete the car
    Then I should be told "<answer>"

    Examples:
      | state   | answer |
      | valid   | 200    |
      | invalid | 400    |

  Scenario Outline: modify car
    Given "<state>" car
    Given "<idState>" id to modify
    When I ask to modify the car
    Then I should be told "<answer>"

    Examples:
      | state   | idState | answer |
      | valid   | valid   | 200    |
      | invalid | valid   | 200    |
      | valid   | invalid | 400    |
      | invalid | invalid | 400    |


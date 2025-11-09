```mermaid
classDiagram
    direction LR
    class Order {
	    Long id
	    User user
	    OrderStatus status
    }
    class OrderItem {
	    Long id
	    Order order
	    Product product
	    int quantity
	    Money totalPrice
    }

    class Point {
	    Long id
	    User user
	    Money balance
    }

    class Like {
	    Long id
	    User user
	    Product product
	    LocalDateTime deletedAt
    }

    class User {
	    Long id
	    String loginId
	    String password
        Gender gender
    }


    class Product {
	    Long id
	    Brand brand
	    String name
	    Money price
	    Stock stock
	    int likeCount
    }

    class Brand {
	    Long id
	    String name
    }


    class Gender {
        <<enumeration>>
        FEMALE,
        MALE,
        UNKNOWN
    }
    class OrderStatus {
        <<enumeration>>
	    CREATED
	    PAID
	    CANCELED
    }




    Point "1" --> "1" User
    Like "N" --> "1" User
    User --> Gender
    Like "N" --> "1" Product
    OrderItem "N" --> "1" Order
    OrderItem "N" --> "1" Product
    Order "N" --> "1" User
    Order --> OrderStatus
    Product "N" --> "1" Brand
```
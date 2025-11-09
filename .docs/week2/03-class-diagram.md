```mermaid
classDiagram
    class Brand {
        Long id
        String name
    }
    class Product {
        Long id
        Brand brand
        String name
        Money price
        Stock stock
        int likeCount
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
    }
    class Point {
        Long id
        User user
        Money balance
    }
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
    
    class OrderStatus {
        <<enumeration>>
        CREATED
        PAID
        CANCELED
    }

    Product --> Brand
    ProductLike --> Product
    Point --> User
    OrderItem --> Order
    OrderItem --> Product
```
# Store API - Rares Bran

A Spring Boot backend application that provides RESTful APIs for managing products and orders.

## Features

- User Authentication and Authorization (JWT-based)
- Product Management (CRUD operations)
- Order Processing
- Role-based Access Control (Admin/User)

## API Endpoints

### Authentication
- `POST /auth/register` - Register a new user
- `POST /auth/authenticate` - Login and receive JWT token
- `GET /auth` - Get current user details

### Products
- `GET /products` - List all products (paginated)
- `POST /products` - Create a new product (Admin only)
- `PATCH /products/{id}` - Update a product (Admin only)
- `DELETE /products/{id}` - Delete a product (Admin only)

### Orders
- `POST /orders` - Place a new order
- `GET /orders` - Get current user's orders

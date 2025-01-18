
# Item Management
This project originally intended for managing storage item and stocks, then i dont know what happen it suddenly become grocery store.

## Starting Project

**JDK:** JDK 17 or higher

**Build Tool:** Maven

**IDE:** Intellij or Eclipse

**Database:** Postgres

**Web Browser:** Any modern browser

**Testing:** Postman

Clone the project

```bash
git clone https://github.com/syiifahusna/itemManagement.git
```

Use master branch

```bash
git checkout master
```

Create database "itemmanagement" in local postgres

```bash
CREATE DATABASE itemmanagement;
```

Set database configuration according to your local machines in application.properties

```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/itemmanagement
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
```

Compile project

```bash
mvn compile package
```

Run Project

```bash
mvn spring-boot:run
```

## User Available

Admin
```bash
username : admin
password : admin123
```

Employee
```bash
username : employee
password : employee123
```

Client
```bash
username : iamclient
password : iamclient123
```

## Functionality

- User registration and login
- Account verification
- Password recovery
- Update user's profile
- Item cataloge
- Add and remove items
- Item search bar by name and price
- Add and remove item from cart
- Update cart item quantity
- Display total cost
- Order
- Order history and status update
- Order search bar by order date and status
- Modify order status
- Update mailing server details

## User Interface

**Home Page (Unfinished)**

- Url : http://localhost:8080/
- Auth : none, Client, Employee, Admin
- Desc : The landing page for the application. It includes an introduction to the platform and navigation links to other sections like About, Register, and Login.

**About Page (Unfinished)**

- Url : http://localhost:8080/about
- Auth : none, Client, Employee, Admin
- Desc :

**Register Page**

- Url : http://localhost:8080/register
- Auth : none
- Desc : Allows new users to sign up by providing details like name, email, password, and other relevant information.

**Login Page**

- Url : http://localhost:8080/login
- Auth : none
- Desc : A secure login page where users enter their credentials to access the platform. Includes support for password recovery.

**Forgot Password Page**

- Url : http://localhost:8080/forgot_password
- Auth : none
- Desc : Enables users to reset their password by providing their registered email address. Sends a password reset link to the email.

**Reset Password Page**

- Url : http://localhost:8080/reset_password?token={token}
- Auth : token
- Desc : Form page that allow user to reset their account password. Page is expired after user reset their password.


**Dashboard Page (Unfinished)**

- Url : http://localhost:8080/user/dashboard
- Auth : Client, Employee, Admin
- Desc :

**Profile Page**

- Url : http://localhost:8080/user/profile
- Auth : Client, Employee, Admin
- Desc : Allows users to view and update their profile information, such as name, email, and password.

**Items Page**

- Url : http://localhost:8080/client/items
- Auth : Client
- Desc : Displays a list of items available for clients to browse and purchase. Includes search and filter options.

**Item Page**

- Url : http://localhost:8080/client/item/{id}
- Auth : Client
- Desc : Shows detailed information about a specific item, including its description and price.

**Orders History Page**

- Url : http://localhost:8080/client/orders
- Auth : Client
- Desc : Lists all past orders placed by the client, including order status, dates, and total amounts.

**Order Information Page**

- Url : http://localhost:8080/client/order/{id}
- Auth : Client
- Desc : Displays detailed information about a specific order, including items purchased and order status.

**Items Management Page**

- Url : http://localhost:8080/employee/management/items
- Auth : Admin, Employee
- Desc : Enables employees and admins to manage items in the inventory, including adding, updating, and deleting items.

**Edit Item Page**

- Url : http://localhost:8080/employee/management/item/edit/{id}
- Auth : Admin, Employee
- Desc : Provides a form for editing details of an existing item, such as its name, price and description.

**Orders Management Page**

- Url : http://localhost:8080/employee/management/orders
- Auth : Admin, Employee
- Desc : Allows employees and admins to view and manage all orders, including updating their statuses.

**Edit Order Page**

- Url : http://localhost:8080/employee/management/order/edit/{id}
- Auth : Admin, Employee
- Desc : Enables authorized users to edit the details of a specific order, such as updating the order status or correcting customer details.

**Employees Management Page**

- Url : http://localhost:8080/admin/management/employees
- Auth : Admin
- Desc : Lists all employees in the system with options to add, edit, or remove employee records.

**New Employee Page**

- Url : http://localhost:8080/admin/management/employee/new
- Auth : Admin
- Desc : Provides a form for adding a new employee to the system with fields like name and email.

**Edit Employee Page**

- Url : http://localhost:8080/admin/management/employee/edit/{id}
- Auth : Admin
- Desc : Allows admins to update details of an existing employee.

**Mailing Settings Page**

- Url : http://localhost:8080/admin/settings/mailing
- Auth : Admin
- Desc : Enables the admin to configure email settings for the platform, such as SMTP server and sender email.

 


## APIs

I did some basic apis this to learn how jwts work. Api can only be use by Client. Use client auth to get jwt token.

**Endpoints**


| Request       | Endpoint                           |Description   |
| ------------- |------------------------------------|--------------|
| POST          | http://localhost:8080/api/auth     |Get jwt token |
| GET           | http://localhost:8080/api/items    |List of items |
| GET           | http://localhost:8080/api/item/{id}|Item info     |
| GET           | http://localhost:8080/test/{state} |Testing api   |

## CUrl

**Auth**

Request
```bash
curl -XPOST -d '{
    "username" : "iamclient",
    "password" : "iamclient123"
}' 'http://localhost:8080/api/auth'
```

Example Response
```bash
{
    "status": "OK",
    "operationStatusEnum": "SUCCESS",
    "message": "Authorized",
    "body": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpYW1jbGllbnQiLCJpYXQiOjE3MzcwOTU5ODIsImV4cCI6MTczNzA5OTU4Mn0.P7l9g94Jehs_7UkCDxwnuZzLC7MGIzlNEAaI2KBrMVSoMsxVDybSw_sVa6h_aUn-YP1Cp1DID1MZ6XBE1irW2w"
}
```

**getItems**

Request
```bash
curl -XGET -H 'Auth: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpYW1jbGllbnQiLCJpYXQiOjE3MzcwOTU5ODIsImV4cCI6MTczNzA5OTU4Mn0.P7l9g94Jehs_7UkCDxwnuZzLC7MGIzlNEAaI2KBrMVSoMsxVDybSw_sVa6h_aUn-YP1Cp1DID1MZ6XBE1irW2w' 'http://localhost:8080/api/items'
```
Example Response
```bash
{
    "status": "OK",
    "operationStatusEnum": "SUCCESS",
    "message": "Item found",
    "body": [
        {
            "createdDate": "2025-01-17T12:40:15.403498",
            "lastModifiedDate": "2025-01-17T12:40:15.403498",
            "createdBy": null,
            "lastModifiedBy": null,
            "id": 1,
            "name": "Flour",
            "description": "This is the description for Flour",
            "price": 5.00,
            "status": true
        },
        {
            "createdDate": "2025-01-17T12:40:15.410496",
            "lastModifiedDate": "2025-01-17T12:40:15.410496",
            "createdBy": null,
            "lastModifiedBy": null,
            "id": 2,
            "name": "Bread",
            "description": "This is the description for Bread",
            "price": 10.00,
            "status": true
        },
        {
            "createdDate": "2025-01-17T12:40:15.413497",
            "lastModifiedDate": "2025-01-17T12:40:15.413497",
            "createdBy": null,
            "lastModifiedBy": null,
            "id": 3,
            "name": "Salmon Fillet",
            "description": "This is the description for Salmon Fillet",
            "price": 15.00,
            "status": true
        },
        {
            "createdDate": "2025-01-17T12:40:15.415498",
            "lastModifiedDate": "2025-01-17T12:40:15.415498",
            "createdBy": null,
            "lastModifiedBy": null,
            "id": 4,
            "name": "Black Pepper",
            "description": "This is the description for Black Pepper",
            "price": 20.00,
            "status": true
        }
	]
}
```

**getItem**

Request
```bash
curl -XGET -H 'Auth: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpYW1jbGllbnQiLCJpYXQiOjE3MzcwOTU5ODIsImV4cCI6MTczNzA5OTU4Mn0.P7l9g94Jehs_7UkCDxwnuZzLC7MGIzlNEAaI2KBrMVSoMsxVDybSw_sVa6h_aUn-YP1Cp1DID1MZ6XBE1irW2w' 'http://localhost:8080/api/item/1'
```

Example Response
```bash
{
    "status": "OK",
    "operationStatusEnum": "SUCCESS",
    "message": "Found item",
    "body": {
        "createdDate": "2025-01-17T12:40:15.403498",
        "lastModifiedDate": "2025-01-17T12:40:15.403498",
        "createdBy": null,
        "lastModifiedBy": null,
        "id": 1,
        "name": "Flour",
        "description": "This is the description for Flour",
        "price": 5.00,
        "status": true
    }
}
```

**test**

Request
```bash
curl -XGET 'http://localhost:8080/test/labuan'
```

Example Response
```bash
{
    "status": "OK",
    "operationStatusEnum": "SUCCESS",
    "message": "Postcodes found",
    "body": {
        "name": "Wp Labuan",
        "city": [
            {
                "name": "Labuan",
                "postcode": [
                    "87000",
                    "87007",
                    "87008",
                    "87010",
                    "87011",
                    "87012",
                    "87013",
                    "87014",
                    "87015",
                    "87016",
                    "87017",
                    "87018",
                    "87019",
                    "87020"
                ]
            }
        ]
    }
}   
```

State available
- http://localhost:8080/test/johor
- http://localhost:8080/test/kedah
- http://localhost:8080/test/kelantan
- http://localhost:8080/test/kuala_lumpur
- http://localhost:8080/test/labuan
- http://localhost:8080/test/melaka
- http://localhost:8080/test/negeri_sembilan
- http://localhost:8080/test/pahang
- http://localhost:8080/test/penang
- http://localhost:8080/test/perak
- http://localhost:8080/test/perlis
- http://localhost:8080/test/putrajaya
- http://localhost:8080/test/sabah
- http://localhost:8080/test/sarawak
- http://localhost:8080/test/selangor
- http://localhost:8080/test/terengganu

**error**

Example Common Error Response
```bash
{
    "status": "NOT_FOUND",
    "operationStatusEnum": "FAILED",
    "message": "No token found",
    "body": null
}
```

Example Internal Server Error Response
```bash
{
    "status": "INTERNAL_SERVER_ERROR",
    "operationStatusEnum": "FAILED",
    "message": "Failed to fetch postcodes",
    "body": "[404 Not Found] during [GET] to [https://client/url/here] [PostcodeClient#getPostcodesByState(String)]: [404: Not Found]"
}
```
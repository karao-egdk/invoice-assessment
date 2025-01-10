# Invoice Assessment

## How to start the invoice application using docker

1. Run `mvn clean install` to build your application
1. Make necessary changes in `docker-compose.yml` to connect to your DB
1. Run `docker-compose up --build` to run your java application with Postgresql

## How to start the invoice application without docker

1. Run `mvn clean install` to build your application
1. Change `jdbc:postgresql://db:5432/invoice` to `jdbc:postgresql://localhost:5432/invoice`
1. Start application with `java -jar target/invoice-0.0.1-SNAPSHOT.jar server config.yml`

## Endpoints

-   `/invoices` - **POST**
    -   Insert new invoice into Invoice table
-   `/invoices` - **GET**
    -   Retrieves all the data from invoice table
-   `/invoices/{id}/payments` - **POST**
    -   Paid amount is updated based on the amount that needs to be paid
-   `/invoices/process-overdue` - **POST**
    -   Processes all the overdue based on today's date and inserts new data for each overdue invoice
-   `/invoices/{id}` - **GET**
    -   Retrieves invoices based on the id or the parent id of invoice

## Extra Functionality

-   Added new field for invoice to have reference to which invoice has been processed as overdue
-   Added an endpoint to display the same

## Assumptions

-   Overdue days are added from the current day

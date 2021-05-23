# cashRegister
Cash register web application. Servlets and JSP are used.

The program implements 3 user roles: goods expert, cashier and senior cashier.


- Goods expert can get a list of goods, add new one or edit existing.

- Cashier can create new order(check), add positions to it and close the check.

- Senior cashier can remove some position from the check or cancel it completely. Also user with this role can get a report about today orders.

The data is stored in the MySQL database. You could see the scheme of the database in the following picture:

<img src="https://github.com/tHolubets/cashRegister/blob/master/DB_EER_Diagram.png" width="800" height="400">

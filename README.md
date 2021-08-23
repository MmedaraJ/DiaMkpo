# DiaMkpo
Created a restaurant app for android. 

This app is made for a sample restaurant called "Dia Mkpo".
Database - Google firestore
Architecture - MVVM
Language - Java
Navigation - Bottom nav

FEATURES FOR USERS
1. Search for meals. 
2. Like a meal by double clicking a meal
3. Access restaurant menu
4. Access past orders
5. Like a past order by double clicking a past orderp
6. Use bottom navigation to navigate the app
7. Sign in using google sign in
8. Sign out
9. Access list of liked meals.
10. Change delivery address
11. Reorder previous orders
12. Buy a meal
13. Rate a meal

FEATURES FOR STAFF
1. Search for meals
2. Access restaurant menu
3. Edit details of a meal
4. Add a new meal
5. Delete a meal
6. Email and password sign in
7. Sign out

SCREENS
1. Spalsh screen - Displays the restaurant logo
2. Sign in - Users can sign in using Google sign in. Staff can sign in using emial and password

--CUSTOMER SCREENS--
3. Customer home - First section shows four random meals depending on the time of the day
                 - Second section shows ten most rated meals 
                 - Third section shows four of the customer's most ordered meals
                 - Clickin a meal opens the product page. 
                 - Double clicking a meal adds or removes it from the list of liked meals. Then a red heart appears/dissapears on the meal 
                 - Lastly, the customer's most recent order is shown.
                 
4. Search - There is a search bar which when clicked, opens a search fragment.
          - Meal categories are shown. Clicking a category opens the menu

5. Past Orders - All past orders are displayed witha reorder button. Clicking this button adds all meals in the order to the cart
               - Clicking the past order itself opens a page that shows details of the order

6. Accounts - Customer name, list of liked meals, delivery address, and a sign-out button.

7. Menu - Meals are displayed based on Category. Clicking a meal opens a product page. 
        - Double clicking a meal adds or removes it from the list of liked meals. Then a red heart appears/dissapears on the meal
        - Clicking the +/- button affects the numbers of orders of the meal and the displayed price.
        - Users can share a meal using the share button. 
        - Clicking the add to cart button adds the meal to the cart

8. Search Clicked - User can search for meals. Clicking a meal opens the product page.

9. Past Order Details: The following details are displayed:
        - Order number
        - Completion status
        - Order date
        - List of meals in the order
        - Total price
        - A reorder button
        Clicking a meal opens the product page. Clicking the reorder button adds all the listed meals to the cart.

10. Liked meals - A list of liked meals are displayed. Clicking a meal Opens the product page

11. Delivery Address - User can search for a delivery address. Clicking the update delivery location button changes the stored delivery address

12. Sign out Dialog - Clicking yes allows a user to sign out.

13. Rate Meal Dialog - User can submit a star and text rating for each meal in a past order.

14. Cart - Meals can be added to or removed from the cart. Clicking the go to checkout button opens the checkout screen.

15. Checkout - Users can Change delivery address
             - All meals to be checked out are listed with their prices
             - Subtotal, delivery fee, axes and total prices are displayed
             - There is a pay now button. When clicked, the meals are added to the list of past orders.

--STAFF SCREENS--
3. Search - Staff can search for a meal or click a category to access the menu
          - Staff can sign out by clicking the sign out button
          - Clicking the +Add new meal button opens an empty product page.

4. Menu - Same as for user
        - Clicking a meal opens an editable product page

5. Add New Meal Product Page - This product page is blank. Staff can add details of a new meal. Clicking the done button adds the meal to the database. Clicking the delete button closes the product page and discards the process.

6. Edit Meal Product Page - The details of a meal can be editted. Clicking the done button saves the meal with it's new details. Clicking the delete button deletes 
the meal and discards the process.

ISSUES
1. Payment not allowed
2. Text colour on tab layout
             

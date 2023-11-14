# Pet Shop Swagger API Test Suite

This test suite verifies the functionality of the Pet Shop Swagger API, covering different endpoints related to pets, the store, and users. The tests are implemented using TestNG and RestAssured.

## Prerequisites

Ensure that you have the following tools installed on your machine:

- Java Development Kit (JDK)
- Maven
- Your preferred Integrated Development Environment (IDE)

## Getting Started

1. **Clone the Repository:**

    ```bash
    git clone https://github.com/erdi-ozturk/SwaggerApi-Automation-Testing-Tasks.git
    ```

2. **Open the Project:**

    Open the project in your preferred IDE.

3. **Download Dependencies:**

    Make sure to download the project dependencies using Maven:

    ```bash
    mvn clean install
    ```

## Running the Tests

1. **Navigate to Test Classes:**

    Open the `PetShopPetsTest`, `PetShopStoreTest`, and `PetShopUsersTest` classes located in the `src/test/java` directory.

2. **Run Test Classes:**

    Right-click on each class and run it as a TestNG test.

3. **View Results:**

    View the test results and logs in your IDE console.

## Test Classes

### 1. PetShopPetsTest

This class contains tests related to pet operations, including adding a new pet, updating, uploading an image, getting a pet by ID, and more.

### 2. PetShopStoreTest

This class covers tests related to the store, such as getting inventory, placing an order, getting an order, and deleting orders.

### 3. PetShopUsersTest

This class focuses on user-related tests, including creating a user, logging in, updating user details, getting user information, and deleting a user.

## Notes

- Ensure a stable internet connection to access the Pet Shop Swagger API.

- Some tests may have dependencies on the execution order, as specified by `dependsOnMethods` annotations.

Feel free to customize and extend these tests based on your specific requirements.

Happy testing!

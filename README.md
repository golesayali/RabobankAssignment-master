## Account Authorization Application

A Power of Attorney is used when someone (grantor) wants to give access to his/her account to someone else (grantee).
This could be read access or write access. In this way the grantee can read/write in the grantors account. This
application will enable users to create authorization for different types of accounts and retrieve authorized accounts
for grantees.

### About the application

* Users can create write or read access for payments and savings accounts
* Users can retrieve a list of accounts they have read or write access for

### Prerequisites

* [Java 11](http://www.oracle.com/technetwork/java/javase/downloads/index.html)  - Programming language Please install
  JDK 9+ and set JAVA_HOME to this jdk.
* [MongoDB Community Server](https://www.mongodb.com/try/download/community) - MongoDB Community Server
* [MongoDB Database Tools](https://www.mongodb.com/try/download/database-tools) - MongoDB database tools
* [Maven](https://maven.apache.org/) - Build tool
* [Junit](https://junit.org/junit5/) - Junit
* [Postman](https://www.postman.com/) - Postman

## Setup Local MongoDB Server

Setup MongoDB server on local using the steps
mentioned [here](https://docs.mongodb.com/manual/administration/install-community/)

## Install & Run Application

##### 1) Open Git Bash. Go to the folder where you want to checkout the code.

cd AccountAuthorizationApplication/

##### 2) Clone the code from git into the folder

git clone _https://github.com/golesayali/RabobankAssignment-master.git_

##### 3) Go into the folder RabobankAssignment-master

cd RabobankAssignment-master

##### 4) Start the application on local using maven

mvn spring-boot:run

Please ensure that the MongoDB local server is running at localhost:27017. The application is now started on local.
There is Troubleshooting Guide for commonly faced issues when doing local setup. Please refer to [TroubleshootingGuide](TroubleshootingGuide.md)

### API documentation

After running the project on dev/local environment and browse **http://localhost:8080/swagger-ui.html**


### Javadoc
command mvn javadoc:javadoc
For javadocs, please refer to links below for each module:
- api     : **http://localhost:63342/rabobank-assignment/rabobank-assignment-api/target/site/apidocs/index.html**
- data    : **http://localhost:63342/rabobank-assignment/rabobank-assignment-data/target/site/apidocs/index.html**
- domain  : **http://localhost:63342/rabobank-assignment/rabobank-assignment-domain/target/site/apidocs/index.html**


## Test & Run Application

### Using Unit Test Cases and Integration Test Cases

You can create code coverage reports by running the following commands on command line:

| Profile                          | Command                                                 | Description                                                                                                                |
| :----------------------------    | :------------------------------------------------------ | :------------------------------------------------------------------------------------------------------------------------- |
| dev                              | mvn clean test                                          | runs unit tests and creates the code coverage report for unit tests to the directory target/site/jacoco-ut                 |
| integration-test                 | mvn clean verify -P integration-test -Dserver.port=8081 | runs integration tests and creates the code coverage report for integration tests to the directory target/site/jacoco-it   |
| all-tests                        | mvn clean verify -P all-tests -Dserver.port=8081        | runs unit and integration tests and creates code coverage reports for unit and integration tests                           |

Profiles are defined for application to enable user to run different set of test cases according to need.

### Using Postman API Application (on Local machine)

For testing the application using postman tool, we will need to insert some test account records in local test database. 
This can be done by using steps mentioned below:

Step 1 and 2 are describes way of importing data using mongoimport tool. 
(Optional step for 1 & 2)
If the mongo import tool does not work on your local the please use the test method defined in \RabobankAssignment-master\api\src\test\java\nl\rabobank\TestDataImportUtility.java.
There is a test method loadTestDataForPostmanTesting() defined in this class which is disabled by default. You can uncomment the @Test annotation and run this test to load test data in your local DB.
Please ensure that the port and ip are correctly pointing to your local DB instance. Also do not commit this class to main. After doing this you can proceed to step 3 mentioned below

1. Setup mongo import tool on local
    - Step 1: To use mongoimport tool we have to first download the MongoDB database tools .zip file
      from _https://www.mongodb.com/try/download/database-tools_.
    - Step 2: After downloading the zip file and unzip the downloaded folder.
    - Step 3: Goto MongoDB bin folder and copy-paste the bin folders all .exe files into the MongoDB bin
      folder[for Windows : C:\Program Files\MongoDB\Server\4.4\bin].

2. Import the test data for bank accounts : This will import the bank account info which are used for testing in postman
   collection
    - Step 1: Open a command prompt and give command mongod to connect with MongoDB server and don’t close this cmd to
      stay connected to the server.
    - Step 2: Open another command prompt and run the mongo shell. Using the mongo command.
    - Step 3: Open one more command prompt window and direct it to bin folder[C:\Program Files\MongoDB\Server\4.4\bin]
      and now you are ready to import files in mongoDB database.
    - Step 4: Run this command
      : mongoimport –-jsonArray –-db test –-collection bankaccount –-file <path_to_test_data_file>
      Note: test data file is present at  [~\RabobankAssignment-master\api\src\test\resources\bankaccount.json]
      
3. Import the collection in Postman application and run the collection Import the collection present
   at [~\RabobankAssignment-master\api\src\test\resources\AccountAuthorizationApplication.postman_collection.json] into
   your Postman application.

4. Run the collection.

### Future improvements

1. Some more constraints on account numbers and account holder name as per business needs.
2. Implement security in the requests (using SSL/TLS).
3. Containerize the application(eg. using docker).
4. Deploy applications in Kubernetes for high scalability and high availability.
5. When the application is up, running integration tests results in error. This needs to be fixed by doing proper profiling for application.

## Author

Sayali Gole



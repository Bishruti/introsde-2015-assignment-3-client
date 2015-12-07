# Introduction to Service Design And Engineering Assignment 3 Client
Submitted By: Bishruti Siku

This assignment is mainly focused on implementing services that follows the SOAP protocol. In this module, we will get acquainted with implementing SOAP web services using JAX-WS, adding JPA to access databases. In this project I have implemented the client side of the SOAP application. This application stores data in database using SQLite and performs various operations like `read`, `write`, `update` and `delete`. The server application of this project is deployed in Heroku and can be accessed from [Heroku Link](https://introsde-assignment3-ehealth.herokuapp.com/ws/people?wsdl). Furthermore, the server application for this client application is implemented in [this project](https://github.com/Bishruti/introsde-2015-assignment-3-server).

#### Structure

In the root file I have the following files.

**Source Folder (src)**

Possess source code files utilized in this application.

*WEB SERVICES (WS)*

This folder consists of the java classes which are responsible to implement the body of the SOAP message. The class in this folder are responsible to perform various operations like `read`, `write`, `update` and `delete` as well as pass the parameters for querying and give the output in appropriate parameters. `PeopleService.java` is responsible to make communication with the server. Similarly, `package-info.java` gives the information on `namespace` and `packages`.

*Client.java*
Implements various methods that send requests to the server.

*build.xml*

It is a low-level mechanism to package. It compiles and archives source code into a single `jar` file.

*ivy.xml*

Contains description of the dependencies of a module, its published artifacts and its configurations.

*client.log*
Logs the output acquired from the server by the client.

#### Supported Database Queries.

`Method #1: readPersonList() => List` 

Obtains all the people and their details in the list.

`Method #2: readPerson(Long id) => Person`

Obtains a person and the details associated to that person from the list.

`Method #3: updatePerson(Person p) => Person`

Edits a person in the list.

`Method #4: createPerson(Person p) => Person`

Adds a new person in the list.

`Method #5: deletePerson(Long id) => Deleted Person Id`

Deletes a person from the list.

`Method #6: readPersonHistory(Long id, String measureType) => List`

Obtains all measure details about a measure of a person in the list.

`Method #7: readMeasureTypes() => List`

Obtains all measures in the list.

`Method #8: readPersonMeasure(Long id, String measureType, Long mid) => Measure`

Obtains measure details about a particular measure of a person in the list.

`Method #9: savePersonMeasure(Long id, Measure m) => Person`

Creates measure details about a measure of a person in the list.

`Method #10: updatePersonMeasure(Long id, Measure m) => Measure`

Updates measure details about a measure of a person in the list.

#### Program Execution

To Execute Client:

1. Open the terminal.

2. Go to the root directory of the program.

3. Run `ant execute.client`
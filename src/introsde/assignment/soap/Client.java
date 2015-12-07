package introsde.assignment.soap;

import introsde.assignment.soap.ws.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.Holder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by bishruti on 12/2/15.
 */

public class Client {

    public static List<Person> personList;
    public static Person firstPerson;
    public static Person newPerson;
    public static BufferedWriter bufferedWriter;

    public static void main(String args[]) throws Exception {

        PeopleService service = new PeopleService();
        People people = service.getPeopleImplPort();

        File log = new File("././client.log");
        FileWriter fileWriter = new FileWriter(log.getAbsoluteFile());
        bufferedWriter = new BufferedWriter(fileWriter);

        personList = getListOfPerson(people);
        getPersonDetail(people,personList);
        updateExistingPerson(people);
        newPerson = createPersonWithDetails(people);
        deleteExistingPerson(people);
        getPersonMeasure(people);
        getMeasureTypes(people);
        getPersonMeasureById(people);
        createNewMeasure(people);
        updateExistingMeasure(people);

        bufferedWriter.close();
    }

    //************************************************************************************************************************

    /* Request 1
        Request to obtain all the people and their details in the list.
        Expected Input: -
        Expected Output: List of people (String) */

    public static List<Person> getListOfPerson(People people) throws Exception {
        printAndSaveInLog(bufferedWriter,"\n");
        printAndSaveInLog(bufferedWriter, "Method #1: readPersonList() => List \n Lists all the people in the database and their current measures");
        List<Person> person = people.readPersonList();
        if (person == null) {
            printAndSaveInLog(bufferedWriter, "Database is empty");
            return null;
        }
        else {
            printAndSaveInLog(bufferedWriter, "List of people in this database: ");
            printPersonList(person);
            return person;
        }
    }

    /* Request 2
        Request to obtain a person and the details associated to that person from the list.
        Expected Input: PersonId (Integer)
        Expected Output: Person and the details associated to that person. (String) */

    public static Person getPersonDetail(People people, List<Person> personList) throws IOException {
        int pId = personList.get(0).getIdPerson();
        printAndSaveInLog(bufferedWriter, "\n");
        printAndSaveInLog(bufferedWriter, "Method #2: readPerson(Long id) => Person \n Gives all the personal information plus current measures of person with id: " + pId);
        firstPerson = people.readPerson(pId);
        if (firstPerson == null) {
            printAndSaveInLog(bufferedWriter, "Database is empty");
            return null;
        }
        else {
            printPerson(firstPerson);
            return firstPerson;
        }
    }

     /* Request 3
        Request to edit a person in the list.
        Expected Input: PersonId (Integer) and Person (Object)
        Expected Output: Edited Person with the details associated to that person. (String) */

    public static void updateExistingPerson(People people) throws IOException {
        printAndSaveInLog(bufferedWriter,"\n");
        int firstPersonId = firstPerson.getIdPerson();
        printAndSaveInLog(bufferedWriter, "Method #3: updatePerson(Person p) => Person \n Updates the personal information of the person with id: " + firstPersonId);
        if (firstPerson == null) {
            printAndSaveInLog(bufferedWriter,"Unable to find the person.");
        }
        else {
            people.updatePerson(updatePersonInfo(firstPersonId, "Kit", "Harrington", new GregorianCalendar(1981, 11, 21)));
            printAndSaveInLog(bufferedWriter, "Successfully Updated Person with id " + firstPersonId);
            Person updatedPersonDetails = people.readPerson(firstPersonId);
            printPerson(updatedPersonDetails);
        }
    }

    /* Request 4
        Request to add a new person in the list.
        Expected Input: Person (Object)
        Expected Output: Newly created Person with the details associated to that person. (String) */

    public static Person createPersonWithDetails(People people) throws IOException {
        printAndSaveInLog(bufferedWriter, "\n");
        printAndSaveInLog(bufferedWriter, "Method #4: createPerson(Person p) => Person \n Creates a new person and returns the newly created person with its assigned id");
        newPerson = people.createPerson(createNewPerson("Cersi", "Lannister", new GregorianCalendar(1988,07,11),
                createNewHealthProfile("weight", "57", "Integer"),
                createNewHealthProfile("height", "169", "Integer"),
                createNewHealthProfile("bmi", "2.1", "Integer"),
                createNewHealthProfile("bloodpressure", "118", "Integer"),
                createNewHealthProfile("steps", "91", "Integer") ));
        if (newPerson != null) {
            printAndSaveInLog(bufferedWriter,"Successfully created person with the following details.");
            printPerson(newPerson);
            return newPerson;
        }
        else {
            printAndSaveInLog(bufferedWriter, "Unable to create the person.");
            return null;
        }
    }

    /* Request 5
        Request to delete a person from the list.
        Expected Input: personId (Integer)
        Expected Output: Response Message. */

    public static void deleteExistingPerson (People people) throws IOException {
        printAndSaveInLog(bufferedWriter, "\n");
        int newPersonId = newPerson.getIdPerson();
        printAndSaveInLog(bufferedWriter,"Method #5: deletePerson(Int id) \n Deletes the Person identified by " + newPersonId +" from the system");
        people.deletePerson(newPersonId);
        if (people.readPerson(newPersonId) == null) {
            printAndSaveInLog(bufferedWriter, "Successfully Deleted Person with id: " + newPersonId);
        }
        else {
            printAndSaveInLog(bufferedWriter, "Unable to delete the person.");
        }
    }

    /* Request 6
       Request to obtain all measure details about a measure of a person in the list.
       Expected Input: personId (Integer)
                       measureType (String)
       Expected Output: List of measure types. (String) */

    public static void getPersonMeasure(People people) throws IOException {
        printAndSaveInLog(bufferedWriter, "\n");
        int firstPersonId = firstPerson.getIdPerson();
        String measure = firstPerson.getCurrentHealth().getMeasureType().get(0).getMeasureType();
        printAndSaveInLog(bufferedWriter, "Method #6: readPersonHistory(Int id, String measureType) \n Returns the list of values (the history) of " + measure + " of person with id: " +firstPersonId);
        List<HealthMeasureHistory> personHealthHistory = people.readPersonHistory(firstPersonId, measure);
        printAndSaveInLog(bufferedWriter, "Measure History of " + measure + " of person with id: " + firstPersonId);
        printPersonMeasureHistoryList(personHealthHistory);
    }

    /* Request 7
      Request to obtain all measures in the list.
      Expected Input: -
      Expected Output: List of measures.  (String) */

    public static void getMeasureTypes(People people) throws IOException {
        printAndSaveInLog(bufferedWriter, "\n");
        printAndSaveInLog(bufferedWriter, "Method #7: readMeasureTypes() => List \n Returns the list of measures");
        MeasureTypes measureTypes = people.readMeasureTypes();
        printMeasureType(measureTypes);
    }

    /* Request 8
        Request to obtain measure details about a particular measure of a person in the list.
        Expected Input: personId (Integer)
                        measureType (String)
                        measureId (Integer)
        Expected Output: Details of a particular measure. (String) */


    public static void getPersonMeasureById(People people) throws IOException {
        printAndSaveInLog(bufferedWriter, "\n");
        int firstPersonId = people.readPersonList().get(0).getIdPerson();
        String measure = people.readPersonList().get(0).getCurrentHealth().getMeasureType().get(0).getMeasureType();
        int mid = people.readPersonHistory(firstPersonId, measure).get(0).getMid();
        printAndSaveInLog(bufferedWriter, "Method #8: readPersonMeasure(Int id, String measureType, Int mid) \n Returns the value of " + measure + " with Measure History id: " + mid + " of person with id: " + firstPersonId);
        List<HealthMeasureHistory> PersonHealthHistoryByMeasure = people.readPersonMeasure(firstPersonId, measure, mid);
        printPersonMeasureHistoryList(PersonHealthHistoryByMeasure);
    }

    /* Request 9
        Request to create measure details about a measure of a person in the list.
        Expected Input: personId (Integer)
        measureType (String)
        MeasureDetails (Object)
        Expected Output:
        List of newly created measure. (String) */

    public static void createNewMeasure(People people) throws IOException {
        printAndSaveInLog(bufferedWriter,"\n");
        int firstPersonId = people.readPersonList().get(0).getIdPerson();
        String measure = people.readPersonList().get(0).getCurrentHealth().getMeasureType().get(0).getMeasureType();
        if (people.readPersonList().get(0) == null) {
            printAndSaveInLog(bufferedWriter, "Unable to find the person.");
        }
        else {
            printAndSaveInLog(bufferedWriter, "Method #9: savePersonMeasure(Long id, Measure m) \n Saves a new measure " + measure + " of Person with id :" + firstPersonId + " and archives the old value in the history");
            int measureCount = people.readPersonHistory(firstPersonId, measure).size();
            printAndSaveInLog(bufferedWriter, "The count of " + measure + " history for person with id " + firstPersonId + " is " + measureCount);
            people.savePersonMeasure(firstPersonId, createNewHealthMeasureHistory(measure, "83", "Integer"));
            printAndSaveInLog(bufferedWriter, "Successfully Created Measure Details for person with id " + firstPersonId);
            printPerson(people.readPerson(firstPersonId));
            int updatedMeasureCount = people.readPersonHistory(firstPersonId, measure).size();
            printAndSaveInLog(bufferedWriter, "The count of " + measure + " history for person with id " + firstPersonId + " is " + updatedMeasureCount);
        }
    }

    /* Request 10
        Request to update measure details about a measure of a person in the list.
        Expected Input: personId (Integer)
        measureType (String)
        mId (Integer)
        MeasureDetails (Object)
        Expected Output:
        List of updated measure. (String) */

    public static void updateExistingMeasure(People people) throws IOException {
        printAndSaveInLog(bufferedWriter,"\n");
        int firstPersonId = people.readPersonList().get(0).getIdPerson();
        String measure = people.readPersonList().get(0).getCurrentHealth().getMeasureType().get(0).getMeasureType();
        List<HealthMeasureHistory> personHealthHistory = people.readPersonHistory(firstPersonId, measure);
        if (firstPerson == null) {
            printAndSaveInLog(bufferedWriter, "Unable to find the person.");
        }
        else {
            printAndSaveInLog(bufferedWriter, "Method #10: updatePersonMeasure(Long id, Measure m) => Measure \n Updates the " + measure + " of the person with id: " + firstPersonId);
            personHealthHistory.get(0).setMeasureValue("100");
            personHealthHistory.get(0).setMeasureValue("100");
            Holder<HealthMeasureHistory> holderForHealthHistory = new Holder<HealthMeasureHistory>(personHealthHistory.get(0));
            people.updatePersonMeasure(firstPersonId, holderForHealthHistory);
            printAndSaveInLog(bufferedWriter, "Successfully Updated Measure Details for person with id: " + firstPersonId);
            printPerson(people.readPerson(firstPersonId));
        }
    }

    //************************************************************************************************************************
    //Helper Methods
    public static Holder<Person> updatePersonInfo(int id, String firstname, String lastname, GregorianCalendar birthdate) {
        Person p = new Person();
        p.setIdPerson(id);
        p.setFirstname(firstname);
        p.setLastname(lastname);
        try {
            p.setBirthdate(DatatypeFactory.newInstance().newXMLGregorianCalendar(birthdate));
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        Holder<Person> person = new Holder<Person>(p);
        return person;
    }

    public static Person createNewPerson(String firstname, String lastname, GregorianCalendar birthdate, HealthProfile...healthProfiles) {
        Person p = new Person();
        p.setFirstname(firstname);
        p.setLastname(lastname);
        try {
            p.setBirthdate(DatatypeFactory.newInstance().newXMLGregorianCalendar(birthdate));
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        Person.CurrentHealth currentHealth = new Person.CurrentHealth();
        for (HealthProfile healthProfile: healthProfiles) {
            currentHealth.getMeasureType().add(healthProfile);
        }
        p.setCurrentHealth(currentHealth);
        return p;
    }

    public static HealthProfile createNewHealthProfile(String measureType, String measureValue, String measureValueType) {
        HealthProfile hp = new HealthProfile();
        hp.setMeasureType(measureType);
        hp.setMeasureValue(measureValue);
        hp.setMeasureValueType(measureValueType);
        return hp;
    }

    public static Holder<HealthMeasureHistory> createNewHealthMeasureHistory(String measureType, String measureValue, String measureValueType) {
        HealthMeasureHistory healthMeasureHistory = new HealthMeasureHistory();
        healthMeasureHistory.setMeasureType(measureType);
        healthMeasureHistory.setMeasureValue(measureValue);
        healthMeasureHistory.setMeasureValueType(measureValueType);
        Holder<HealthMeasureHistory> healthMeasureHistoryHolder = new Holder<HealthMeasureHistory>(healthMeasureHistory);
        return healthMeasureHistoryHolder;
    }

    //************************************************************************************************************************
    //Methods to print outputs.
    public static void printPersonList(List<Person> person) throws IOException {
        for (Person p : person) {
            printPerson(p);
        }
    }

    public static void printPerson(Person person) throws IOException {
        printAndSaveInLog(bufferedWriter, "personId: " + person.getIdPerson());
        printAndSaveInLog(bufferedWriter, "FirstName: " + person.getFirstname());
        printAndSaveInLog(bufferedWriter, "LastName: " + person.getLastname());
        printAndSaveInLog(bufferedWriter, "Birthdate: " + person.getBirthdate());
        ArrayList<HealthProfile> hp = (ArrayList<HealthProfile>) person.getCurrentHealth().getMeasureType();
        printPersonHealthProfile(hp);
        printAndSaveInLog(bufferedWriter, "*******************************************************");
    }

    public static void printMeasureType(MeasureTypes measureType) throws IOException {
        List<String> measures = measureType.getMeasureType();
        for (String m : measures) {
            printAndSaveInLog(bufferedWriter, m);
        }
    }

    public static void printPersonMeasureHistoryList(List<HealthMeasureHistory> healthMeasureHistories) throws IOException {
        for (HealthMeasureHistory healthMeasureHistory : healthMeasureHistories) {
            printPersonMeasureHistory(healthMeasureHistory);
            printAndSaveInLog(bufferedWriter, "-------------------------------------------------------");
        }
    }

    public static void printPersonMeasureHistory(HealthMeasureHistory healthMeasureHistory) throws IOException {
        printAndSaveInLog(bufferedWriter, "Mid: " + healthMeasureHistory.getMid());
        printAndSaveInLog(bufferedWriter, "Date Registered: " + healthMeasureHistory.getDateRegistered());
        printAndSaveInLog(bufferedWriter, "Measure Type: " + healthMeasureHistory.getMeasureType());
        printAndSaveInLog(bufferedWriter, "Measure Value: " + healthMeasureHistory.getMeasureValue());
        printAndSaveInLog(bufferedWriter, "Measure Value Type: " + healthMeasureHistory.getMeasureValueType());
    }

    public static void printPersonHealthProfile(ArrayList<HealthProfile> healthProfiles) throws IOException {
        for (HealthProfile healthProfile : healthProfiles) {
            printAndSaveInLog(bufferedWriter, "-------------------------------------------------------");
            printAndSaveInLog(bufferedWriter, "IdHealthProfile: " + healthProfile.getIdHealthProfile());
            printAndSaveInLog(bufferedWriter, "Date Registered: " + healthProfile.getDateRegistered());
            printAndSaveInLog(bufferedWriter, "Measure Type: " + healthProfile.getMeasureType());
            printAndSaveInLog(bufferedWriter, "Measure Value: " + healthProfile.getMeasureValue());
            printAndSaveInLog(bufferedWriter, "Measure Value Type: " + healthProfile.getMeasureValueType());
        }
    }

    //************************************************************************************************************************
    //Methods to print and save in log file.
    private static void printAndSaveInLog(BufferedWriter bufferwriter, String outputResponse) throws IOException {
        System.out.println(outputResponse);
        bufferwriter.write(outputResponse + "\n");
    }
}
import java.io.*;
import java.util.*;


public class PatientSystem {

    public static String isBirthValid(int month, int day, int year) {
        //check if the given birth is valid.
        String stringMonth = "";
        String stringDay = "";
        String stringYear = "";
        String stringBirthdate = "";

        // checking base range of month
        if (month >= 1 && month <= 12) {
            // check is month is one/two digit number
            if (month <= 9) {
                stringMonth += "0" + month + "/";
            }
            else {
                stringMonth += month + "/";
            }
        }
        else {
            stringMonth += "Invalid";
        }

        // checking base range of days in month
        if (day >= 1 && day <= 31) {
            // checking if day is one/two digit number
            if (day <= 9) {
                stringDay += "0" + day + "/";
            }
            else {
                stringDay += day + "/";
            }
        }
        else {
            stringDay += "Invalid";
        }

        // checking base range of year
        if (year >= 1922 && year <= 2022) {
            stringYear += year;
        }
        else {
            stringYear += "Invalid";
        }

        // combining month/day/year string
        stringBirthdate += stringMonth + stringDay + stringYear;

        // accurate examples of invalid birthdates
        if ((stringBirthdate.startsWith("02/29/")) && (Integer.parseInt(stringYear) % 4 != 0)) {
            stringBirthdate = "Invalid";
        }

        if (stringBirthdate.startsWith("04/31") || stringBirthdate.startsWith("06/31") || stringBirthdate.startsWith("09/31") || stringBirthdate.startsWith("11/31")) {
            stringBirthdate = "Invalid";
        }

        if (stringBirthdate.contains("Invalid")) {
            stringBirthdate = "Invalid";
        }
        // return statement for birthdate, either returns birthdate in MM/DD/YYYY or "Invalid"
        return stringBirthdate;
    }


    public static String addPatient(String name, String birth, String fileName) throws IOException {
        // Add a new patient record to the file.
        // if given birth is not valid, then patient will not be added into the file.
        // Birth must save in a format of Month/Day/Year, in total length of 10, such
        // as “02/03/2022”, “11/12/2001”, “01/24/1998”, “12/01/1980” and so on.
        String patientAddStatus;
        // check if first digit of 'month' in string is zero or non-zero
        int month;
        int firstDigitOfMonth = Integer.parseInt(birth.substring(0, 1));
        if (firstDigitOfMonth == 0) {
            month = Integer.parseInt(birth.substring(1, 2));
        }
        else {
            month = Integer.parseInt(birth.substring(0, 2));
        }
        // check if first digit of 'day' in string is zero or non-zero
        int day;
        int firstDigitOfDay = Integer.parseInt(birth.substring(3, 4));
        if (firstDigitOfDay == 0) {
            day = Integer.parseInt(birth.substring(4, 5));
        }
        else {
            day = Integer.parseInt(birth.substring(3, 5));
        }
        int year = Integer.parseInt(birth.substring(6, 10));
        ArrayList<String> patientList = new ArrayList<>();
        File file = new File(fileName);
        Scanner scan = new Scanner(file);
        while (scan.hasNextLine()) {
            String patient = scan.nextLine();
            patientList.add(patient);
        }
        if (isBirthValid(month, day, year).equals("Invalid")) {
            patientAddStatus = "Patient not added";
        }
        else {
            patientList.add(name + " " + isBirthValid(month, day, year) + " sick");
        }
        PrintWriter out = new PrintWriter(file);

        for (String s : patientList) {
            out.println(s);
        }


        patientAddStatus = "Patient added";
        out.close();
        scan.close();

        return patientAddStatus;
    }


    public static void deletePatient(String name, String fileName) throws IOException {
        // Delete an existing patient record from the file
        // can’t delete if the patient still sick.
        // if there are two patientList occur with the same name,
        // and they are both not sick, only delete the first one.
        // Otherwise, delete the first patient who is not sick.
        File file = new File(fileName);
        Scanner scan = new Scanner(file);
        ArrayList<String> patientList = new ArrayList<>();

        while (scan.hasNextLine()) {
            String patient = scan.nextLine();
            patientList.add(patient);
        }

        for (int i = 0; i < patientList.size(); i++) {
            if ((patientList.get(i).contains("recover")) && (patientList.get(i).contains(name))) {
                patientList.remove(i);
                break;
            }
        }
        PrintWriter out = new PrintWriter(fileName);
        for (String p : patientList) {
            out.println(p);
        }
        out.close();
        scan.close();
    }

    public static void sickToRecovered(String name, String fileName) throws IOException {
        File file = new File(fileName);
        Scanner scan = new Scanner(file);
        ArrayList<String> patientList = new ArrayList<>();

        while (scan.hasNextLine()) {
            String patient = scan.nextLine();
            patientList.add(patient);

        }

        for (int i = 0; i < patientList.size(); i++) {
            if (patientList.get(i).contains("sick") && patientList.get(i).contains(name)) {
                String patientLine = patientList.get(i);
                String[] patientLineArray = patientLine.split("\\s+");
                patientLineArray[3] = "recover";
                String recoveredPatientLine = patientLineArray[0] + " " + patientLineArray[1] + " " + patientLineArray[2] + " " + patientLineArray[3];
                patientList.set(i, recoveredPatientLine);
                break;
            }
        }
        PrintWriter out = new PrintWriter(fileName);
        for (String p : patientList) {
            out.println(p);
        }
        out.close();
        scan.close();
    }

    public static int countPatients(String status, String fileName) throws FileNotFoundException {
        // return numbers of sick patients or recovery patients in the file.
        // if client given “” (empty string), then return the total number of patients.
        File file = new File(fileName);
        Scanner scan = new Scanner(file);
        ArrayList<String> patientList = new ArrayList<>();
        int sickCount = 0;
        int recoverCount = 0;
        int totalCount = 0;

        while (scan.hasNextLine()) {
            String patient = scan.nextLine();
            patientList.add(patient);
            totalCount++;
        }

        for (String s : patientList) {
            if (s.contains("sick")) {
                sickCount++;
            }
            if (s.contains("recover")) {
                recoverCount++;
            }
        }

        if (status.equals("sick")) {
            return sickCount;
        }
        else if (status.equals("recover")) {
            return recoverCount;
        }
        else if (status.equals("total")) {
            return totalCount;
        }
        else {
            return 0;
        }
    }

    public static void averageAge(String fileName) throws FileNotFoundException {
        // find the average age for all patients in the file
        File file = new File(fileName);
        Scanner scan = new Scanner(file);
        ArrayList<String> patientList = new ArrayList<>();
        ArrayList<String> birthDateList = new ArrayList<>();
        ArrayList<Integer> ageList = new ArrayList<>();

        //PrintWriter out = new PrintWriter(fileName);
        int currentMonth = 4;
        int currentDay = 12;
        int currentYear = 2022;

        while (scan.hasNextLine()) {
            String patient = scan.nextLine();
            patientList.add(patient);
        }

        for (String s : patientList) {
            if (s.contains("sick")) {
                birthDateList.add(s.substring(s.length() - 15, s.length() - 5 /*, patientList.get(i).length()*/));
            } else {
                birthDateList.add(s.substring(s.length() - 18, s.length() - 8));
            }
        }

        int age;
        int parsedYear;
        int parsedMonth;
        int parsedDay;

        for (String s : birthDateList) {
            parsedYear = Integer.parseInt(s.substring(6, 10));
            parsedMonth = Integer.parseInt(s.substring(0, 2));
            parsedDay = Integer.parseInt(s.substring(3, 5));
            if (parsedMonth > currentMonth) {
                age = (currentYear - parsedYear) - 1;
            }
            else if (parsedMonth == currentMonth) {
                if (parsedDay > currentDay) {
                    age = (currentYear - parsedYear) - 1;
                }
                else if (parsedDay == currentDay) {
                    age = currentYear - parsedYear;
                }
                else {
                    age = currentYear - parsedYear;
                }
            }
            else {
                age = currentYear - parsedYear;
            }
            ageList.add(age);
        }
        scan.close();
        int sumOfPatientAges = 0;

        for (Integer integer : ageList) {
            sumOfPatientAges += integer;
        }
        double averagePatientAge = (double) sumOfPatientAges / ageList.size();
        System.out.print("Average patient age: ");
        System.out.printf("%.2f", averagePatientAge);
    }

    public static void sortPatientsByAge(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scan = new Scanner(file);
        //PrintWriter out = new PrintWriter(file);
        ArrayList<String> patientList = new ArrayList<>();
        ArrayList<String> birthDateList = new ArrayList<>();
        ArrayList<Integer> ageList = new ArrayList<>();
        ArrayList<String> ageListString = new ArrayList<>();
        ArrayList<String> newPatientList = new ArrayList<>();

        while (scan.hasNextLine()) {
            String patient = scan.nextLine();
            patientList.add(patient);
        }

        for (String s : patientList) {
            if (s.contains("sick")) {
                birthDateList.add(s.substring(s.length() - 15, s.length() - 5));
            }
            else {
                birthDateList.add(s.substring(s.length() - 18, s.length() - 8));
            }
        }

        int currentMonth = 4;
        int currentDay = 12;
        int currentYear = 2022;

        int age;
        int parsedYear;
        int parsedMonth;
        int parsedDay;
        for (String s : birthDateList) {
            parsedYear = Integer.parseInt(s.substring(6, 10));
            parsedMonth = Integer.parseInt(s.substring(0, 2));
            parsedDay = Integer.parseInt(s.substring(3, 5));
            if (parsedMonth > currentMonth) {
                age = (currentYear - parsedYear) - 1;
            }
            else if (parsedMonth == currentMonth) {
                if (parsedDay > currentDay) {
                    age = (currentYear - parsedYear) - 1;
                }
                else if (parsedDay == currentDay) {
                    age = currentYear - parsedYear;
                }
                else {
                    age = currentYear - parsedYear;
                }
            }
            else {
                age = currentYear - parsedYear;
            }
            ageList.add(age);
        }


        for (Integer integer : ageList) {
            ageListString.add(integer.toString());
        }

        for (int i = 0; i < ageList.size(); i++) {
            for (int k = 0; k < ageList.size(); k++) {
                int tempOne = ageList.get(i);
                int tempTwo = ageList.get(k);
                if (tempOne < tempTwo) {
                    ageList.set(i, tempTwo);
                    ageList.set(k, tempOne);
                }
            }
        }

        for (int i = 0; i < ageListString.size(); i++) {
            patientList.set(i, patientList.get(i) + " " + ageListString.get(i));
        }

        for (int i = 0; i < patientList.size(); i++) {
            patientList.set(i, patientList.get(i).replace(' ', '-'));
        }

        for (Integer integer : ageList) {
            for (String s : patientList) {
                if (integer == (Integer.parseInt(s.substring(s.length() - 2, s.length())))) {
                    newPatientList.add(s);
                }
            }
        }

        for (int i = 0; i < newPatientList.size(); i++) {
            newPatientList.set(i, newPatientList.get(i).replace('-', ' '));
        }

        for (int i = 0; i < newPatientList.size(); i++) {
            newPatientList.set(i, newPatientList.get(i).substring(0, newPatientList.get(i).length()-3));
        }

        PrintWriter out = new PrintWriter(fileName);
        for (String s : newPatientList) {
            out.println(s);
        }
        out.close();
        scan.close();
    }

    public static void sortPatientsByName(String firstOrLast, String fileName) throws FileNotFoundException {
        // modify file by sorting patients by first name or last name for all patients from a-z
        File file = new File(fileName);
        Scanner scan = new Scanner(file);
        ArrayList<String> patientList = new ArrayList<>(); // initial patient arraylist

        ArrayList<String> patientFirstLetterFirstNameList = new ArrayList<>();
        ArrayList<Character> patientFirstNameFirstCharList = new ArrayList<Character>();
        ArrayList<Integer> patientFirstNameFirstCharDecList = new ArrayList<>();

        ArrayList<String> patientFirstLetterLastNameList = new ArrayList<>();
        ArrayList<Character> patientLastNameFirstCharList = new ArrayList<Character>();
        ArrayList<Integer> patientLastNameFirstCharDecList = new ArrayList<>();

        ArrayList<String> newPatientList = new ArrayList<>();

        if (firstOrLast.equals("first")) {

            while (scan.hasNextLine()) {
                String patient = scan.nextLine();
                patientList.add(patient);
            }

            for (String s : patientList) { // taking the first letter of patients first name and adding to arraylist (String)
                patientFirstLetterFirstNameList.add(s.substring(0,1));
            }

            for (String s : patientFirstLetterFirstNameList) { // taking the first letter of patients first name and adding to arraylist (char)
                patientFirstNameFirstCharList.add(s.charAt(0));
            }

            for (int i = 0; i < patientList.size(); i++) { // adding first letter of f.n. (String) to patientList
                patientList.set(i, patientList.get(i) + " " + patientFirstLetterFirstNameList.get(i));
            }

            for (Character character : patientFirstNameFirstCharList) {
                patientFirstNameFirstCharDecList.add((int) character);
            }

            for (int i = 0; i < patientFirstNameFirstCharDecList.size(); i++) { // sorting values in ascending order
                for (int k = 0; k < patientFirstNameFirstCharDecList.size(); k++) {
                    int tempOne = patientFirstNameFirstCharDecList.get(i);
                    int tempTwo = patientFirstNameFirstCharDecList.get(k);
                    if (tempOne < tempTwo) {
                        patientFirstNameFirstCharDecList.set(i, tempTwo);
                        patientFirstNameFirstCharDecList.set(k, tempOne);
                    }
                }
            }

            for (Integer integer : patientFirstNameFirstCharDecList) {
                for (String s : patientList) {
                    //char c = patientList.get(i).charAt(0);
                    if (integer == ((int) (s.substring(s.length() - 1)).charAt(0))) {
                        newPatientList.add(s);
                    }
                }
            }

            for (int i = 0; i < newPatientList.size(); i++) {
                for (int k = i + 1; k < newPatientList.size(); k++) {
                    if (newPatientList.get(i).equals(newPatientList.get(k))) {
                        newPatientList.remove(k);
                        k--;
                    }
                }
            }

            for (int i = 0; i < newPatientList.size(); i++) {
                newPatientList.set(i, newPatientList.get(i).substring(0, newPatientList.get(i).length()-2));
            }

            PrintWriter out = new PrintWriter(fileName);
            for (String s : newPatientList) {
                out.println(s);
            }
            out.close();
            scan.close();
        }

        else if (firstOrLast.equals("last")) {
            ArrayList<String> lastNamesOfPatients = new ArrayList<>();
            while (scan.hasNextLine()) {
                String patient = scan.nextLine();
                patientList.add(patient);
                String[] info = patient.split(" ");
                lastNamesOfPatients.add(info[1]);
            }

            for (String s : lastNamesOfPatients) { // taking the first letter of patients last name and adding to arraylist (String)
                patientFirstLetterLastNameList.add(s.substring(0,1));
            }

            for (String s : patientFirstLetterLastNameList) { // taking the first letter of patients last name and adding to arraylist (char)
                patientLastNameFirstCharList.add(s.charAt(0));
            }

            for (int i = 0; i < patientList.size(); i++) { // adding first letter of f.n. (String) to patientList
                patientList.set(i, patientList.get(i) + " " + patientFirstLetterLastNameList.get(i));
            }

            for (Character character : patientLastNameFirstCharList) {
                patientLastNameFirstCharDecList.add((int) character);
            }

            for (int i = 0; i < patientLastNameFirstCharDecList.size(); i++) { // sorting values in ascending order
                for (int k = 0; k < patientLastNameFirstCharDecList.size(); k++) {
                    int tempOne = patientLastNameFirstCharDecList.get(i);
                    int tempTwo = patientLastNameFirstCharDecList.get(k);
                    if (tempOne < tempTwo) {
                        patientLastNameFirstCharDecList.set(i, tempTwo);
                        patientLastNameFirstCharDecList.set(k, tempOne);
                    }
                }
            }

            for (Integer integer : patientLastNameFirstCharDecList) {
                for (String s : patientList) {
                    //char c = patientList.get(i).charAt(0);
                    if (integer == ((int) (s.substring(s.length() - 1)).charAt(0))) {
                        newPatientList.add(s);
                    }
                }
            }

            for (int i = 0; i < newPatientList.size(); i++) {
                for (int k = i + 1; k < newPatientList.size(); k++) {
                    if (newPatientList.get(i).equals(newPatientList.get(k))) {
                        newPatientList.remove(k);
                        k--;
                    }
                }
            }

            for (int i = 0; i < newPatientList.size(); i++) {
                newPatientList.set(i, newPatientList.get(i).substring(0, newPatientList.get(i).length()-2));
            }

            PrintWriter out = new PrintWriter(fileName);
            for (String s : newPatientList) {
                out.println(s);
            }
            out.close();
            scan.close();
        }
    }

    public static void shufflePatients(String fileName) throws FileNotFoundException {
        // modify file by shuffle all patients, so they are not in any order
        // Using random in this method is required.
        File file = new File(fileName);
        Scanner scan = new Scanner(file);
        ArrayList<String> patientList = new ArrayList<>();
        Random r = new Random();


        while (scan.hasNextLine()) {
            String patient = scan.nextLine();
            patientList.add(patient);
        }

        final int PATIENT_LENGTH = patientList.size();
        for (int k = PATIENT_LENGTH - 1; k >= 1; k--) {
            Collections.swap(patientList, k, r.nextInt(k + 1));
        }

        PrintWriter out = new PrintWriter(file);

        for (String s : patientList) {
            out.println(s);
        }

        out.close();
        scan.close();
    }


    public static void main(String[] args) throws IOException {
        String fileName = "patients.txt";
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("Patient System Menu Version 1.0");
            System.out.println("Choose a menu option between A and I: ");
            System.out.println("\tA. Add patient to record");
            System.out.println("\tB. Delete a patient record");
            System.out.println("\tC. Transfer patient from sick to recovered");
            System.out.println("\tD. Count the total number of sick and/or recovered patients");
            System.out.println("\tE. Find average age for all patients");
            System.out.println("\tF. Sort patients by age");
            System.out.println("\tG. Sort patients by first name or last name");
            System.out.println("\tH. Shuffle patients");
            System.out.println("\tI. Exit");
            System.out.print("Operation: ");
            String operation = scan.nextLine();

            if (operation.equals("A")) {
                System.out.println("Enter first and last name of patient (First Last): ");
                String patientName = scan.nextLine();
                System.out.println("Enter birthdate of patient (MM/DD/YYYY): ");
                String patientBirthdate = scan.nextLine();
                System.out.println(addPatient(patientName, patientBirthdate, fileName));
                System.out.println("Operation completed\n");
            }
            else if (operation.equals("B")) {
                System.out.println("Enter first and last name of patient (First Last): ");
                String patientName = scan.nextLine();
                deletePatient(patientName, fileName);
                System.out.println("Operation completed\n");
            }
            else if (operation.equals("C")) {
                System.out.println("Enter first and last name of patient for recover status (First Last): ");
                String patientName = scan.nextLine();
                sickToRecovered(patientName, fileName);
                System.out.println("Operation completed\n");
            }
            else if (operation.equals("D")) {
                System.out.println("Enter status (sick, recover, total): ");
                String status = scan.nextLine();
                System.out.println(countPatients(status, fileName));
                System.out.println("Operation completed\n");
            }
            else if (operation.equals("E")) {
                averageAge(fileName);
                System.out.println("Operation completed\n");
            }
            else if (operation.equals("F")) {
                sortPatientsByAge(fileName);
                System.out.println("Operation completed\n");
            }
            else if (operation.equals("G")) {
                System.out.println("Enter filter to sort patients (first or last): ");
                String firstOrLast = scan.nextLine();
                sortPatientsByName(firstOrLast, fileName);
                System.out.println("Operation completed\n");
            }
            else if (operation.equals("H")) {
                shufflePatients(fileName);
                System.out.println("Operation completed\n");
            }
            else if (operation.equals("I")) {
                System.out.println("Exiting system...\n");
                System.exit(0);
            }
            else {
                System.out.println("Invalid operation\n");
                System.out.println("");
            }
        }
    }
}
package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;


public class FinancialTracker {
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATETIME_PATTERN = DATE_PATTERN + "|" + TIME_PATTERN + "|";

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern(DATETIME_PATTERN);


    public static void main(String[] args) {
        loadTransactions(FILE_NAME, transactions);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to JABOB'S BANKS");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D" -> addDeposit(scanner);
                case "P" -> addPayment(scanner);
                case "L" -> ledgerMenu(scanner);
                case "X" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
        scanner.close();
    }



    public static void loadTransactions(String fileName, ArrayList<Transaction> transactions) {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;

            while((line = reader.readLine()) != null){
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0], DATE_FMT);
                LocalTime time = LocalTime.parse(parts[1], TIME_FMT);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);
                transactions.add(new Transaction(date, time, description, vendor, amount));
                transactions.sort(Comparator.comparing(Transaction::getDateTime));
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Something went wrong " + e.getMessage());;
        }
    }



    private static void addDeposit(Scanner scanner) {
        System.out.println("DEPOSIT");
        System.out.println("How much money would you like to deposit? ");
        double amount = scanner.nextDouble();
        String formatdub = String.format("%.2f", amount);
        scanner.nextLine();
        System.out.print("Who is paying you back? ");
        String vendor = scanner.nextLine();

        if (amount > 0){
            LocalDate today = LocalDate.now();
            LocalTime time = LocalTime.now();

            Transaction deposit = new Transaction(today, time, "deposit", vendor, amount);
            transactions.add(deposit);

            System.out.println("Thank you for the deposit of " + amount);
            try{
                BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
                writer.write(LocalDateTime.now().format(DATETIME_FMT) + "deposit|" + vendor + "|" + formatdub);
                writer.newLine();
                writer.close();

            } catch (IOException e){
                System.err.println("Sorry something went wrong. " + e.getMessage());
            }
        }
        else {
            System.out.println("Sorry that amount is not able to be accepted");
        }

    }

    
    private static void addPayment(Scanner scanner) {
        System.out.println("PAYMENT");
        System.out.println("What amount do you have to pay back? (Enter as a positive number) ");
        double posToNeg = scanner.nextDouble();
        double amount = posToNeg*(-1);
        String formatdub = String.format("%.2f", amount);
        scanner.nextLine();

        System.out.print("Who or what is receiving the money? ");
        String vendor = scanner.nextLine();

        if (amount < 0){
            System.out.println("The debt owed is " + amount);

            LocalDate today = LocalDate.now();
            LocalTime time = LocalTime.now();

            Transaction payment = new Transaction(today, time, "Payment", vendor, amount);
            transactions.add(payment);

            try{
                BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
                writer.write(LocalDateTime.now().format(DATETIME_FMT) + "Payment|" + vendor + "|" + formatdub);
                writer.newLine();
                writer.close();

            } catch (IOException e){
                System.err.println("Sorry something went wrong. " + e.getMessage());
            }
        }
        else {
            System.out.println("Sorry that amount can't be owed.");
        }
    }


    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A" -> displayLedger();
                case "D" -> displayDeposits();
                case "P" -> displayPayments();
                case "R" -> reportsMenu(scanner);
                case "H" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }


    private static void displayLedger() {
        for (int i = transactions.size()-1; i >= 0; i--) {
            System.out.println(transactions.get(i));
        }// for i
    }

    private static void displayDeposits() {

            for (Transaction transaction : transactions) {
                if (transaction.amount() > 0) {
                    System.out.println(transaction);
                }
        }//iter aka for each
    }

    private static void displayPayments() {

        for (int i = transactions.size()-1; i >= 0; i--) {
            if(transactions.get(i).amount() < 0){
                System.out.println(transactions.get(i));
            }
        }
    }


    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> {monthTilNow();}
                case "2" -> {prevMonth();}
                case "3" -> {yearTilNow();}
                case "4" -> {prevYear();}
                case "5" -> {filterTransactionsByVendor(scanner); }
                case "6" -> customSearch(scanner);
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void filterTransactionsByDate(LocalDate start, LocalDate end, Scanner scanner) {
        System.out.print("What date would you like to start from(\"yyyy-MM-dd\")? ");
        String begin = scanner.nextLine();
        start = LocalDate.parse(begin, DATE_FMT);
        System.out.println("What date would you like to end from\"yyyy-MM-dd\")");
        String finish = scanner.nextLine();
        end = LocalDate.parse(finish, DATE_FMT);

    }

    private static void filterTransactionsByVendor(Scanner scanner) {
        System.out.print("Who would you like to search for? ");
        String vendor = scanner.nextLine();
        for (int i = transactions.size()-1; i >= 0; i--) {
            if (vendor.equalsIgnoreCase(transactions.get(i).vendor())){
                System.out.println(transactions.get(i));
            }
        }
    }

    private static void customSearch(Scanner scanner) {
        System.out.println("Enter a date (MM-dd-yyyy) or press Enter to skip:");
        String dateInput = scanner.nextLine();
        LocalDate userDate = null;

            if (!dateInput.isEmpty()) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                userDate = LocalDate.parse(dateInput, format);
            }

            System.out.println("Enter a description or press Enter to skip:");
            String description = scanner.nextLine();

            System.out.println("Enter a vendor or press Enter to skip:");
            String vendor = scanner.nextLine();

            System.out.println("Enter an exact amount or press Enter to skip:");
            String amountInput = scanner.nextLine();
            Double amount = null;

            if (!amountInput.isEmpty()) {
                amount = Double.parseDouble(amountInput);
            }

            boolean found = false;
            for (int i = transactions.size() - 1; i >= 0; i--) {
                Transaction transaction = transactions.get(i);

                if ((userDate == null || userDate.equals(transaction.date())) &&
                        (description.isEmpty() || description.equalsIgnoreCase(transaction.description())) &&
                        (vendor.isEmpty() || vendor.equalsIgnoreCase(transaction.vendor())) &&
                        (amount == null || amount == transaction.amount())) {

                    System.out.println(transaction);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No transactions found.");
            }
    }


    private static void monthTilNow(){
        LocalDate today = LocalDate.now();
        LocalDate oneMonthAgo = today.minusMonths(1);

        for (int i = transactions.size()-1; i >= 0; i--){//Make it be able to organize any order correctly
            LocalDate date = transactions.get(i).date();

            if(!date.isBefore(oneMonthAgo) && !date.isAfter(today)){
                System.out.println(transactions.get(i));
            }
        }
    }

    private static void prevMonth(){
        LocalDate today = LocalDate.now();
        LocalDate firstDay = today.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDay = today.withDayOfMonth(1).minusDays(1);

        for (int i=transactions.size()-1; i >= 0; i--){
            LocalDate date = transactions.get(i).date();

            if(!date.isBefore(firstDay) && !date.isAfter(lastDay)){
                System.out.println(transactions.get(i));
            }
        }
    }

    private static void yearTilNow(){
        LocalDate today = LocalDate.now();
        LocalDate oneYearAgo = today.minusYears(1);

        for (int i = transactions.size()-1; i >= 0; i--){
            LocalDate date = transactions.get(i).date();

            if(!date.isBefore(oneYearAgo) && !date.isAfter(today)){
                System.out.println(transactions.get(i));
            }
        }
    }

    private static void prevYear(){
        LocalDate today = LocalDate.now();
        LocalDate firstDay = today.minusYears(1).withDayOfYear(1);
        LocalDate lastDay = today.withDayOfYear(1).minusDays(1);

        for (int i=transactions.size()-1; i >= 0; i--){
            LocalDate date = transactions.get(i).date();

            if(!date.isBefore(firstDay) && !date.isAfter(lastDay)){//Clears all before the year and all after the previous year
                System.out.println(transactions.get(i));
            }
        }

    }

}

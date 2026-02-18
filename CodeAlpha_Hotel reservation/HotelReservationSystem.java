import java.io.*;
import java.util.*;

class Room {
    int roomNumber;
    String category;
    double price;
    boolean isAvailable;

    Room(int roomNumber, String category, double price) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
        this.isAvailable = true;
    }
}

class Booking {
    String customerName;
    int roomNumber;
    String category;
    double amount;

    Booking(String customerName, int roomNumber, String category, double amount) {
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return customerName + "," + roomNumber + "," + category + "," + amount;
    }
}

public class HotelReservationSystem {

    static List<Room> rooms = new ArrayList<>();
    static List<Booking> bookings = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        initializeRooms();
        loadBookings();

        while (true) {
            System.out.println("\n==== HOTEL RESERVATION SYSTEM ====");
            System.out.println("1. Search Available Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View Booking Details");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    searchRooms();
                    break;
                case 2:
                    bookRoom();
                    break;
                case 3:
                    cancelBooking();
                    break;
                case 4:
                    viewBookings();
                    break;
                case 5:
                    saveBookings();
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    static void initializeRooms() {
        rooms.add(new Room(101, "Standard", 2000));
        rooms.add(new Room(102, "Standard", 2000));
        rooms.add(new Room(201, "Deluxe", 4000));
        rooms.add(new Room(202, "Deluxe", 4000));
        rooms.add(new Room(301, "Suite", 7000));
    }

    static void searchRooms() {
        System.out.println("\nAvailable Rooms:");
        for (Room r : rooms) {
            if (r.isAvailable) {
                System.out.println("Room " + r.roomNumber + " | " + r.category + " | ₹" + r.price);
            }
        }
    }

    static void bookRoom() {
        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter room number to book: ");
        int roomNo = sc.nextInt();
        sc.nextLine();

        for (Room r : rooms) {
            if (r.roomNumber == roomNo && r.isAvailable) {

                System.out.println("Room Price: ₹" + r.price);
                System.out.println("Processing Payment...");
                System.out.println("Payment Successful!");

                r.isAvailable = false;

                Booking booking = new Booking(name, r.roomNumber, r.category, r.price);
                bookings.add(booking);

                System.out.println("Booking Confirmed!");
                return;
            }
        }

        System.out.println("Room not available!");
    }

    static void cancelBooking() {
        System.out.print("Enter room number to cancel: ");
        int roomNo = sc.nextInt();
        sc.nextLine();

        Iterator<Booking> iterator = bookings.iterator();

        while (iterator.hasNext()) {
            Booking b = iterator.next();
            if (b.roomNumber == roomNo) {

                iterator.remove();

                for (Room r : rooms) {
                    if (r.roomNumber == roomNo) {
                        r.isAvailable = true;
                        break;
                    }
                }

                System.out.println("Booking Cancelled Successfully!");
                return;
            }
        }

        System.out.println("Booking not found!");
    }

    static void viewBookings() {
        System.out.println("\n===== BOOKING DETAILS =====");
        for (Booking b : bookings) {
            System.out.println("Name: " + b.customerName +
                    " | Room: " + b.roomNumber +
                    " | Category: " + b.category +
                    " | Paid: ₹" + b.amount);
        }
    }

    static void saveBookings() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("bookings.txt"))) {

            for (Booking b : bookings) {
                writer.println(b.toString());
            }

            System.out.println("Bookings saved successfully!");

        } catch (IOException e) {
            System.out.println("Error saving bookings.");
        }
    }

    static void loadBookings() {
        try (BufferedReader reader = new BufferedReader(new FileReader("bookings.txt"))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Booking booking = new Booking(
                        parts[0],
                        Integer.parseInt(parts[1]),
                        parts[2],
                        Double.parseDouble(parts[3])
                );
                bookings.add(booking);

                for (Room r : rooms) {
                    if (r.roomNumber == booking.roomNumber) {
                        r.isAvailable = false;
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("No previous bookings found.");
        }
    }
}

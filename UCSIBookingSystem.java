import java.util.*;

class User {
    String id;
    String password;

    public User(String id, String password) {
        this.id = id;
        this.password = password;
    }
}

class Admin extends User {
    public Admin(String id, String password) {
        super(id, password);
    }
}

class Member extends User {
    String name;
    String membershipType;
    String phoneNumber;
    String email;

    public Member(String id, String password, String name, String membershipType, String phoneNumber, String email) {
        super(id, password);
        this.name = name;
        this.membershipType = membershipType;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}

class Room {
    String roomType;
    String roomId;
    int capacity;

    public Room(String roomType, String roomId, int capacity) {
        this.roomType = roomType;
        this.roomId = roomId;
        this.capacity = capacity;
    }

    // Override toString() to return a string representation of the room
    @Override
    public String toString() {
        return "Room type: " + roomType + ", ID: " + roomId + ", capacity: " + capacity;
    }
}
class Booking {
    String memberId;
    String date;
    String timeStart;
    String timeEnd;
    String roomId;
    String organizer;
    int participants;

    public Booking(String memberId, String date, String timeStart, String timeEnd, String roomId, String organizer, int participants) {
        this.memberId = memberId;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.roomId = roomId;
        this.organizer = organizer;
        this.participants = participants;
    }
}

public class UCSIBookingSystem {
    static Admin admin = new Admin("admin", "admin");
    static LinkedList<Member> members = new LinkedList<>();
    static LinkedList<Room> rooms = new LinkedList<>();
    static LinkedList<Booking> bookings = new LinkedList<>();

    public static void addMember(Member member) {
        members.add(member);
    }

    public static void editMember(String memberId, Member member) {
        ListIterator<Member> iterator = members.listIterator();
        while (iterator.hasNext()) {
            Member m = iterator.next();
            if (m.id.equals(memberId)) {
                iterator.set(member);
            }
        }
    }

    public static void deleteMember(String memberId) {
        ListIterator<Member> iterator = members.listIterator();
        while (iterator.hasNext()) {
            Member m = iterator.next();
            if (m.id.equals(memberId)) {
                iterator.remove();
            }
        }
    }

    public static void addRoom(Room room) {
        rooms.add(room);
    }

    public static void editRoom(String roomId, Room room) {
        ListIterator<Room> iterator = rooms.listIterator();
        while (iterator.hasNext()) {
            Room r = iterator.next();
            if (r.roomId.equals(roomId)) {
                iterator.set(room);
            }
        }
    }

    public static void deleteRoom(String roomId) {
        ListIterator<Room> iterator = rooms.listIterator();
        while (iterator.hasNext()) {
            Room r = iterator.next();
            if (r.roomId.equals(roomId)) {
                iterator.remove();
            }
        }
    }

    public static void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public static void editBooking(int bookingIndex, Booking booking) {
        bookings.set(bookingIndex, booking);
    }

    public static void deleteBooking(int bookingIndex) {
        bookings.remove(bookingIndex);
    }

    public static User login(String id, String password) {
        if (id.equals(admin.id) && password.equals(admin.password)) {
            return admin;
        }
        for (Member member : members) {
            if (member.id.equals(id) && member.password.equals(password)) {
                return member;
            }
        }
        return null;
    }

    public static LinkedList<Booking> searchBookingsByDate(String date) {
        LinkedList<Booking> results = new LinkedList<>();
        for (Booking booking : bookings) {
            if (booking.date.equals(date)) {
                results.add(booking);
            }
        }
        return results;
    }

    public static LinkedList<Booking> searchBookingsByRoom(String roomId) {
        LinkedList<Booking> results = new LinkedList<>();
        for (Booking booking : bookings) {
            if (booking.roomId.equals(roomId)) {
                results.add(booking);
            }
        }
        return results;
    }

    public static LinkedList<Booking> searchBookingsByMember(String memberId) {
        LinkedList<Booking> results = new LinkedList<>();
        for (Booking booking : bookings) {
            if (booking.memberId.equals(memberId)) {
                results.add(booking);
            }
        }
        return results;
    }


    private static void adminFunctionalities(Scanner scanner) {
        // Admin functionalities
        while (true) {
            System.out.println("\nAdmin menu:");
            System.out.println("1. Manage members");
            System.out.println("2. Manage rooms");
            System.out.println("3. Manage bookings");
            System.out.println("4. Search bookings");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            int adminChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            if (adminChoice == 1) {
                // Manage members
                while (true) {
                    System.out.println("\nManage members:");
                    System.out.println("1. Add member");
                    System.out.println("2. Edit member");
                    System.out.println("3. Delete member");
                    System.out.println("4. View member");
                    System.out.println("5. Back to admin menu");
                    System.out.print("Enter your choice: ");
                    int memberChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline

                    if (memberChoice == 1) {
                        // Add member
                        System.out.print("Enter member ID: ");
                        String id = scanner.nextLine();
                        System.out.print("Enter member password: ");
                        String password = scanner.nextLine();
                        System.out.print("Enter member name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter member membership type: ");
                        String membershipType = scanner.nextLine();
                        System.out.print("Enter member phone number: ");
                        String phoneNumber = scanner.nextLine();
                        System.out.print("Enter member email: ");
                        String email = scanner.nextLine();

                        Member member = new Member(id, password, name, membershipType, phoneNumber, email);
                        addMember(member);
                        System.out.println("Member added.");
                    } else if (memberChoice == 2) {
                        // Edit member
                        System.out.print("Enter member ID to edit: ");
                        String id = scanner.nextLine();

                        boolean foundMember = false;
                        for (Member member : members) {
                            if (member.id.equals(id)) {
                                foundMember = true;
                                System.out.print("Enter member password: ");
                                String password = scanner.nextLine();
                                System.out.print("Enter member name: ");
                                String name = scanner.nextLine();
                                System.out.print("Enter member membership type: ");
                                String membershipType = scanner.nextLine();
                                System.out.print("Enter member phone number: ");
                                String phoneNumber = scanner.nextLine();
                                System.out.print("Enter member email: ");
                                String email = scanner.nextLine();

                                Member editedMember = new Member(id, password, name, membershipType, phoneNumber, email);
                                editMember(id, editedMember);
                                System.out.println("Member edited.");
                                break;
                            }
                        }
                        if (!foundMember) {
                            System.out.println("Member not found.");
                        }
                    } else if (memberChoice == 3) {
                        // Delete member
                        System.out.print("Enter member ID to delete: ");
                        String id = scanner.nextLine();

                        boolean foundMember = false;
                        for (Member member : members) {
                            if (member.id.equals(id)) {
                                foundMember = true;
                                deleteMember(id);
                                System.out.println("Member deleted.");
                                break;
                            }
                        }
                        if (!foundMember) {
                            System.out.println("Member not found.");
                        }
                    } else if (memberChoice == 4) {
                        // View members
                        System.out.println("All Members:");
                        if (members.size() == 0) {
                            System.out.println("No members found.");
                        } else {
                            for (Member member : members) {
                                System.out.println("ID: " + member.id);
                                System.out.println("Name: " + member.name);
                                System.out.println("Membership type: " + member.membershipType);
                                System.out.println("Phone number: " + member.phoneNumber);
                                System.out.println("Email: " + member.email);
                                System.out.println("--------------");
                            }
                        }
                    } else if (memberChoice == 5) {
                        break;
                    } else {
                        System.out.println("Invalid choice.");
                    }
                }
            } else if (adminChoice == 2) {
                // Manage rooms
                while (true) {
                    System.out.println("\nManage rooms:");
                    System.out.println("1. Add room");
                    System.out.println("2. Edit room");
                    System.out.println("3. Delete room");
                    System.out.println("4. View all rooms"); // added option
                    System.out.println("5. Back to admin menu");
                    System.out.print("Enter your choice: ");
                    int roomChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline

                    if (roomChoice == 1) {
                        // Add room
                        System.out.print("Enter room type: ");
                        String roomType = scanner.nextLine();
                        System.out.print("Enter room ID: ");
                        String roomId = scanner.nextLine();
                        System.out.print("Enter room capacity: ");
                        int capacity = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline

                        Room room = new Room(roomType, roomId, capacity);
                        addRoom(room);
                        System.out.println("Room added.");
                    } else if (roomChoice == 2) {
                        // Edit room
                        System.out.print("Enter room ID to edit: ");
                        String roomId = scanner.nextLine();

                        boolean foundRoom = false;
                        for (Room room : rooms) {
                            if (room.roomId.equals(roomId)) {
                                foundRoom = true;
                                System.out.print("Enter room type: ");
                                String roomType = scanner.nextLine();
                                System.out.print("Enter room capacity: ");
                                int capacity = scanner.nextInt();
                                scanner.nextLine(); // Consume the newline

                                Room editedRoom = new Room(roomType, roomId, capacity);
                                editRoom(roomId, editedRoom);
                                System.out.println("Room edited.");
                                break;
                            }
                        }
                        if (!foundRoom) {
                            System.out.println("Room not found.");
                        }
                    } else if (roomChoice == 3) {
                        // Delete room
                        System.out.print("Enter room ID to delete: ");
                        String roomId = scanner.nextLine();

                        boolean foundRoom = false;
                        for (Room room : rooms) {
                            if (room.roomId.equals(roomId)) {
                                foundRoom = true;
                                deleteRoom(roomId);
                                System.out.println("Room deleted.");
                                break;
                            }
                        }
                        if (!foundRoom) {
                            System.out.println("Room not found.");
                        }
                    } else if (roomChoice == 4) {
                        // View all rooms
                        System.out.println("\nAll rooms:");
                        for (Room room : rooms) {
                            System.out.println(room.toString());
                        }
                    } else if (roomChoice == 5) {
                        break;
                    } else {
                        System.out.println("Invalid choice.");
                    }
                }
            } else if (adminChoice == 3) {
                // Manage bookings
                while (true) {
                    System.out.println("\nManage bookings:");
                    System.out.println("1. Add booking");
                    System.out.println("2. Edit booking");
                    System.out.println("3. Delete booking");
                    System.out.println("4. Back to admin menu");
                    System.out.print("Enter your choice: ");
                    int bookingChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline

                    if (bookingChoice == 1) {
                        // Add booking
                        System.out.print("Enter member ID: ");
                        String memberId = scanner.nextLine();
                        System.out.print("Enter date (yyyy-mm-dd): ");
                        String date = scanner.nextLine();
                        System.out.print("Enter start time (hh:mm): ");
                        String timeStart = scanner.nextLine();
                        System.out.print("Enter end time (hh:mm): ");
                        String timeEnd = scanner.nextLine();
                        System.out.print("Enter room ID: ");
                        String roomId = scanner.nextLine();
                        System.out.print("Enter organizer name: ");
                        String organizer = scanner.nextLine();
                        System.out.print("Enter number of participants: ");
                        int participants = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline

                        Booking booking = new Booking(memberId, date, timeStart, timeEnd, roomId, organizer, participants);
                        addBooking(booking);
                        System.out.println("Booking added.");
                    } else if (bookingChoice == 2) {
                        // Edit booking
                        System.out.print("Enter booking index to edit: ");
                        int bookingIndex = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline

                        if (bookingIndex >= 0 && bookingIndex < bookings.size()) {
                            Booking oldBooking = bookings.get(bookingIndex);
                            System.out.print("Enter member ID: ");
                            String memberId = scanner.nextLine();
                            System.out.print("Enter date (yyyy-mm-dd): ");
                            String date = scanner.nextLine();
                            System.out.print("Enter start time (hh:mm): ");
                            String timeStart = scanner.nextLine();
                            System.out.print("Enter end time (hh:mm): ");
                            String timeEnd = scanner.nextLine();
                            System.out.print("Enter room ID: ");
                            String roomId = scanner.nextLine();
                            System.out.print("Enter organizer name: ");
                            String organizer = scanner.nextLine();
                            System.out.print("Enter number of participants: ");
                            int participants = scanner.nextInt();
                            scanner.nextLine(); // Consume the newline

                            Booking editedBooking = new Booking(memberId, date, timeStart, timeEnd, roomId, organizer, participants);
                            editBooking(bookingIndex, editedBooking);
                            System.out.println("Booking edited.");
                        } else {
                            System.out.println("Invalid booking index.");
                        }
                    } else if (bookingChoice == 3) {
                        // Delete booking
                        System.out.print("Enter booking index to delete: ");
                        int bookingIndex = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline

                        if (bookingIndex >= 0 && bookingIndex < bookings.size()) {
                            deleteBooking(bookingIndex);
                            System.out.println("Booking deleted.");
                        } else {
                            System.out.println("Invalid booking index.");
                        }
                    } else if (bookingChoice == 4) {
                        break;
                    } else {
                        System.out.println("Invalid choice.");
                    }
                }
            } else if (adminChoice == 4) {
                // Search bookings
                while (true) {
                    System.out.println("\nSearch bookings:");
                    System.out.println("1. By date");
                    System.out.println("2. By room");
                    System.out.println("3. By member");
                    System.out.println("4. Back to admin menu");
                    System.out.print("Enter your choice: ");
                    int searchChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline

                    if (searchChoice == 1) {
                        // Search bookings by date
                        System.out.print("Enter date to search (yyyy-mm-dd): ");
                        String date = scanner.nextLine();
                        LinkedList<Booking> results = searchBookingsByDate(date);
                        if (results.size() > 0) {
                            System.out.println("Results:");
                            for (Booking booking : results) {
                                System.out.println(booking.memberId + " booked room " + booking.roomId + " from " +
                                        booking.timeStart + " to " + booking.timeEnd + " with " + booking.participants + " participants.");
                            }
                        } else {
                            System.out.println("No bookings found for the specified date.");
                        }
                    } else if (searchChoice == 2) {
                        // Search bookings by room
                        System.out.print("Enter room ID to search: ");
                        String roomId = scanner.nextLine();
                        LinkedList<Booking> results = searchBookingsByRoom(roomId);
                        if (results.size() > 0) {
                            System.out.println("Results:");
                            for (Booking booking : results) {
                                System.out.println(booking.memberId + " booked room " + booking.roomId + " from " +
                                        booking.timeStart + " to " + booking.timeEnd                             + " with " + booking.participants + " participants.");
                            }
                        } else {
                            System.out.println("No bookings found for the specified room.");
                        }
                    } else if (searchChoice == 3) {
                        // Search bookings by member
                        System.out.print("Enter member ID to search: ");
                        String memberId = scanner.nextLine();
                        LinkedList<Booking> results = searchBookingsByMember(memberId);
                        if (results.size() > 0) {
                            System.out.println("Results:");
                            for (Booking booking : results) {
                                System.out.println(booking.memberId + " booked room " + booking.roomId + " from " +
                                        booking.timeStart + " to " + booking.timeEnd + " with " + booking.participants + " participants.");
                            }
                        } else {
                            System.out.println("No bookings found for the specified member.");
                        }
                    } else if (searchChoice == 4) {
                        break;
                    } else {
                        System.out.println("Invalid choice.");
                    }
                }
            } else if (adminChoice == 5) {
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private static void memberFunctionalities(Scanner scanner, Member member) {
        // Member functionalities
        while (true) {
            System.out.println("\nMember menu:");
            System.out.println("1. Book a room");
            System.out.println("2. View my bookings");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");
            int memberChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            if (memberChoice == 1) {
                // Book a room
                System.out.print("Enter the date (YYYY-MM-DD): ");
                String date = scanner.nextLine();
                System.out.print("Enter the start time (HH:MM): ");
                String timeStart = scanner.nextLine();
                System.out.print("Enter the end time (HH:MM): ");
                String timeEnd = scanner.nextLine();

                System.out.println("Available rooms:");
                for (Room room : rooms) {
                    System.out.printf("%s - Capacity: %d\n", room.roomId, room.capacity);
                }

                System.out.print("Enter the room ID: ");
                String roomId = scanner.nextLine();

                Room selectedRoom = null;
                for (Room room : rooms) {
                    if (room.roomId.equals(roomId)) {
                        selectedRoom = room;
                        break;
                    }
                }

                if (selectedRoom == null) {
                    System.out.println("Invalid room ID.");
                    continue;
                }

                System.out.print("Enter the number of participants: ");
                int participants = scanner.nextInt();
                scanner.nextLine(); // Consume the newline

                if (participants > selectedRoom.capacity) {
                    System.out.println("The room capacity is not sufficient for the number of participants.");
                    continue;
                }

                Booking booking = new Booking(member.id, date, timeStart, timeEnd, roomId, member.name, participants);
                addBooking(booking);

                System.out.println("Booking successful.");
            } else if (memberChoice == 2) {
                // View my bookings
                LinkedList<Booking> memberBookings = searchBookingsByMember(member.id);
                System.out.printf("\n%-15s%-15s%-15s%-15s%-15s%-15s\n", "Date", "Start Time", "End Time", "Room ID", "Organizer", "Participants");
                for (Booking booking : memberBookings) {
                    System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s\n", booking.date, booking.timeStart, booking.timeEnd, booking.roomId, booking.organizer, booking.participants);
                }
            } else if (memberChoice == 3) {
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        UCSIBookingSystem system = new UCSIBookingSystem();

        // Add rooms and members
        system.addRoom(new Room("Lecture", "C401", 100));
        system.addRoom(new Room("Meeting", "G101", 20));
        system.addRoom(new Room("ExamHall", "DestarC501",200));
        system.addMember(new Member("m1", "pass", "Abdirisaq Adan", "Staff", "011232-84450", "abdirisaq@example.com"));
        system.addMember(new Member("m2", "pass", "Abdallah Abushammala", "Student", "011555-5678", "Abdallah@example.com"));
        system.addMember(new Member("m3", "pass", "Kolomnove surdor", "Lecturer", "012123944","Kolomnove@example.com"));

        // Add sample bookings for demonstration purposes
        User sampleUser = system.login("m1", "pass");
        if (sampleUser != null && sampleUser instanceof Member) {
            Member member = (Member) sampleUser;
            Room room = new Room("Lecture", "C401", 100);
            Booking booking = new Booking(member.id, "2023-04-01", "09:00", "11:00", room.roomId, member.name, 30);
            system.addBooking(booking);
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to UCSI Booking System!");
            System.out.println("1. Admin login");
            System.out.println("2. Member login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            if (choice == 1) {
                System.out.print("Enter admin ID: ");
                String id = scanner.nextLine();
                System.out.print("Enter admin password: ");
                String password = scanner.nextLine();

                User user = login(id, password);

                if (user != null && user instanceof Admin) {
                    adminFunctionalities(scanner);
                } else {
                    System.out.println("Invalid admin ID or password.");
                }
            } else if (choice == 2) {
                System.out.print("Enter member ID: ");
                String id = scanner.nextLine();
                System.out.print("Enter member password: ");
                String password = scanner.nextLine();

                User user = login(id, password);

                if (user != null && user instanceof Member) {
                    memberFunctionalities(scanner, (Member) user);
                } else {
                    System.out.println("Invalid member ID or password.");
                }
            } else if (choice == 3) {
                System.out.println("Thank you for using UCSI Booking System!");
                break;
            } else {
                System.out.println("Invalid choice.");

            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Running time: " + (endTime - startTime) + " ms");
    }
}
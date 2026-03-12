package hotel_management_system;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class HotelRoomManagementSystem {

    // ---------------- ADMIN ----------------
    public static void admin(Scanner sc, Connection con) throws SQLException {

        System.out.print("Enter Admin Username: ");
        String name = sc.next();

        System.out.print("Enter Admin Password: ");
        String pwd = sc.next();

        if (name.equals("Admin") && pwd.equals("Admin@235")) {

            System.out.println("Login Successful");

            boolean run = true;
            while (run) {
                System.out.println("""
                        \nEnter Your Choice:
                        1.Create Staff Account
                        2.Update Staff Account
                        3.Delete Staff Account
                        4.Find Staff
                        5.Find All Staff
                        6.Logout""");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1 : createStaffAccount(sc, con);break;
                    case 2 : updateStaffAccount(sc, con);break;
                    case 3 : deleteStaffAccount(sc, con);break;
                    case 4 : findStaff(sc, con);break;
                    case 5 : findAllStaff(con);break;
                    case 6 : 
                        run = false;
                        System.out.println("Admin Logged Out");break;
                    default : System.out.println("Invalid Choice");
                }
            }
        } else {
            System.out.println("Invalid Admin Credentials");
        }
    }

    // ---------------- STAFF ----------------
    public static void staff(Scanner sc, Connection con) throws SQLException {

        boolean run = true;
        while (run) {
            System.out.println("\nEnter Your Choice:\n1.Login\n2.Exit");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> {
                    if (loginAccount(sc, con)) {
                        staffMenu(sc, con);
                    }
                }
                case 2 -> run = false;
                default -> System.out.println("Invalid Choice");
            }
        }
    }

    private static void staffMenu(Scanner sc, Connection con) throws SQLException {

        boolean start = true;
        while (start) {
            System.out.println("""
                    \nEnter Your Choice:
                    1.Allocate Room
                    2.Update Rent
                    3.Find Room
                    4.Deallocate Room
                    5.View All Rooms
                    6.Exit""");

            int ch = sc.nextInt();

            switch (ch) {
                case 1 : allocateRoom(sc, con);break;
                case 2 : updateRent(sc, con);break;
                case 3 : findRoom(sc, con);break;
                case 4 : deallocateRoom(sc, con);break;
                case 5 : findAllRooms(con);break;
                case 6 : start = false;break;
                default : System.out.println("Invalid Choice");
            }
        }
    }

    // ---------------- CREATE STAFF ACCOUNT ----------------
    public static void createStaffAccount(Scanner sc, Connection con) throws SQLException {

        String sql = "INSERT INTO users(username,password,ph_no) VALUES(?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            System.out.print("Staff Username: ");
            ps.setString(1, sc.next());

            System.out.print("Staff Password: ");
            ps.setString(2, sc.next());

            System.out.print("Phone No: ");
            ps.setLong(3, sc.nextLong());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("Staff Account Created Successfully. Staff ID: " + rs.getInt(1));
            }
        }
    }

    // ---------------- UPDATE STAFF ----------------
    public static void updateStaffAccount(Scanner sc, Connection con) throws SQLException {
        System.out.print("Enter Staff ID to Update: ");
        int id = sc.nextInt();

        String sql = "UPDATE users SET username=?, password=?, ph_no=? WHERE staff_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("New Username: ");
            ps.setString(1, sc.next());

            System.out.print("New Password: ");
            ps.setString(2, sc.next());

            System.out.print("New Phone No: ");
            ps.setLong(3, sc.nextLong());

            ps.setInt(4, id);

            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Staff Updated" : "Staff not found");
        }
    }

    // ---------------- DELETE STAFF ----------------
    public static void deleteStaffAccount(Scanner sc, Connection con) throws SQLException {
        System.out.print("Enter Staff ID to Delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM users WHERE staff_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Staff Deleted" : "Staff not found");
        }
    }

    // ---------------- FIND STAFF ----------------
    public static void findStaff(Scanner sc, Connection con) throws SQLException {
        System.out.print("Enter Staff ID: ");
        int id = sc.nextInt();

        String sql = "SELECT * FROM users WHERE staff_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println(
                        "Staff ID: " + rs.getInt(1) +
                                "\nUsername: " + rs.getString(2) +
                                "\nPhone No: " + rs.getLong(4)
                );
            } else {
                System.out.println("Staff not found");
            }
        }
    }

    // ---------------- FIND ALL STAFF ----------------
    public static void findAllStaff(Connection con) throws SQLException {
        String sql = "SELECT * FROM users";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                System.out.println(
                        "Staff ID: " + rs.getInt(1) +
                                " | Username: " + rs.getString(2) +
                                " | Phone: " + rs.getLong(4)
                );
            }
        }
    }

    // ---------------- LOGIN STAFF ----------------
    public static boolean loginAccount(Scanner sc, Connection con) throws SQLException {

        System.out.println("\n1.Username & Password\n2.Phone & OTP");
        System.out.print("Enter choice: ");

        int choice = sc.nextInt();
        Random r = new Random();

        if (choice == 1) {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {

                System.out.print("Username: ");
                ps.setString(1, sc.next());

                System.out.print("Password: ");
                ps.setString(2, sc.next());

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.println("Login Successful");
                    return true;
                }
            }
        } else if (choice == 2) {
            String sql = "SELECT * FROM users WHERE ph_no=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {

                System.out.print("Phone No: ");
                ps.setLong(1, sc.nextLong());

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int otp = 100000 + r.nextInt(900000);
                    System.out.println("Generated OTP: " + otp);

                    System.out.print("Enter OTP: ");
                    if (otp == sc.nextInt()) {
                        System.out.println("Login Successful");
                        return true;
                    }
                } else {
                    System.out.println("Phone number not registered");
                }
            }
        }

        System.out.println("Login Failed");
        return false;
    }

    // ---------------- ALLOCATE ROOM ----------------
    public static void allocateRoom(Scanner sc, Connection con) {

        String sql = "INSERT INTO hotel_room VALUES(?,?,?,?,?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Room ID: ");
            ps.setInt(1, sc.nextInt());

            System.out.print("Guest Name: ");
            ps.setString(2, sc.next());

            System.out.print("Block: ");
            ps.setString(3, sc.next());

            System.out.print("Rent: ");
            ps.setDouble(4, sc.nextDouble());

            System.out.print("Contact: ");
            ps.setLong(5, sc.nextLong());

            System.out.print("Email: ");
            ps.setString(6, sc.next());

            System.out.print("Check-in Date (yyyy-mm-dd): ");
            ps.setDate(7, Date.valueOf(sc.next()));

            ps.executeUpdate();
            System.out.println("Room Allocated Successfully");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Room already allocated or guest already has a room");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- UPDATE RENT ----------------
    public static void updateRent(Scanner sc, Connection con) throws SQLException {

        String sql = "UPDATE hotel_room SET room_rent=? WHERE room_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Room ID: ");
            ps.setInt(2, sc.nextInt());

            System.out.print("New Rent: ");
            ps.setDouble(1, sc.nextDouble());

            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Rent Updated" : "Room not found");
        }
    }

    // ---------------- FIND ROOM ----------------
    public static void findRoom(Scanner sc, Connection con) throws SQLException {

        System.out.print("Enter Room ID: ");
        int id = sc.nextInt();

        String sql = "SELECT * FROM hotel_room WHERE room_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\nRoom ID : "+rs.getInt(1)+"\nGuest : "+ rs.getString(2)+"\nBlock :"+ rs.getString(3)
                        +"\nRent : "+rs.getDouble(4)+"\nContact : "+ rs.getLong(5)+"\nEmail : "+
                        rs.getString(6)+"\nCheck-in Date : "+ rs.getDate(7));
            } else {
                System.out.println("Room not found");
            }
        }
    }

    // ---------------- DEALLOCATE ROOM ----------------
    public static void deallocateRoom(Scanner sc, Connection con) throws SQLException {

        System.out.print("Enter Room ID: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM hotel_room WHERE room_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Room Deallocated" : "Room not found");
        }
    }

    // ---------------- FIND ALL ROOMS ----------------
    public static void findAllRooms(Connection con) throws SQLException {

        String sql = "SELECT * FROM hotel_room";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                System.out.println(
                        rs.getInt(1) + " \n " +
                                rs.getString(2) + " \n " +
                                rs.getString(3) + "  " +
                                rs.getDouble(4));
            }
        }
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/hotel_management";
        String user = "root";
        String password = "root";

        try (Scanner sc = new Scanner(System.in);
             Connection con = DriverManager.getConnection(url, user, password)) {

            Class.forName("com.mysql.cj.jdbc.Driver");

            boolean run = true;
            while (run) {
                System.out.println("\nEnter Your Choice : \n1.Admin\n2.Staff\n3.Exit");
                int ch = sc.nextInt();
                switch (ch) {
                    case 1 : admin(sc, con);break;
                    case 2 : staff(sc, con);break;
                    case 3 : run = false;
                    System.out.println("Thank You...");break;
                    default : System.out.println("Invalid Choice");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

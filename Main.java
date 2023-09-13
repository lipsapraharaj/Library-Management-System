// Library Management System

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static class Title {
        private String title;

        public Title(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    public static class LendAudit {
        private String checkout;
        private String checkin;

        public LendAudit(String checkout, String checkin) {
            this.checkout = checkout;
            this.checkin = checkin;
        }

        public String getCheckout() {
            return checkout;
        }

        public String getCheckin() {
            return checkin;
        }

        public void setCheckin(String checkin) {
            this.checkin = checkin;
        }
    }

    public static class Member {
        private String name;
        private Map<Title, LendAudit> books;

        public Member(String name) {
            this.name = name;
            this.books = new HashMap<>();
        }

        public String getName() {
            return name;
        }

        public Map<Title, LendAudit> getBooks() {
            return books;
        }
    }

    public static class Book {
        private String title;
        private int totalCopies;
        private int availableCopies;
        private int lendedCopies;

        public Book(String title, int totalCopies) {
            this.title = title;
            this.totalCopies = totalCopies;
            this.availableCopies = totalCopies;
            this.lendedCopies = 0;
        }

        public String getTitle() {
            return title;
        }

        public int getTotalCopies() {
            return totalCopies;
        }

        public int getAvailableCopies() {
            return availableCopies;
        }

        public int getLendedCopies() {
            return lendedCopies;
        }

        public void setAvailableCopies(int availableCopies) {
            this.availableCopies = availableCopies;
        }

        public void setLendedCopies(int lendedCopies) {
            this.lendedCopies = lendedCopies;
        }
    }

    private Map<String, Member> members;
    private Book[] books;

    public Main() {
        this.members = new HashMap<>();
        this.books = new Book[100]; // Assuming a maximum of 100 books for now
    }

    public void addMember(String memberId, String memberName) {
        if (!members.containsKey(memberId)) {
            members.put(memberId, new Member(memberName));
            System.out.println("Member added successfully.");
        } else {
            System.out.println("Member already exists.");
        }
    }

    public void removeMember(String memberId) {
        if (members.containsKey(memberId)) {
            members.remove(memberId);
            System.out.println("Member removed successfully.");
        } else {
            System.out.println("Member not found.");
        }
    }

    public void addBook(String bookTitle, int totalCopies) {
        for (int i = 0; i < books.length; i++) {
            if (books[i] == null) {
                books[i] = new Book(bookTitle, totalCopies);
                System.out.println("Book added successfully.");
                return;
            }
        }
        System.out.println("Library is full. Cannot add more books.");
    }

    public void removeBook(String bookTitle) {
        for (int i = 0; i < books.length; i++) {
            if (books[i] != null && books[i].getTitle().equals(bookTitle)) {
                books[i] = null;
                System.out.println("Book removed successfully.");
                return;
            }
        }
        System.out.println("Book not found.");
    }

    public Book searchBook(String bookTitle) {
        for (int i = 0; i < books.length; i++) {
            if (books[i] != null && books[i].getTitle().equals(bookTitle)) {
                return books[i];
            }
        }
        return null;
    }

    public void printMemberAudit(Member member) {
        System.out.println("Member: " + member.getName());
        for (Map.Entry<Title, LendAudit> entry : member.getBooks().entrySet()) {
            Title title = entry.getKey();
            LendAudit audit = entry.getValue();

            String returnTime = audit.getCheckin() == null ? "[not returned yet]" : audit.getCheckin();

            System.out.println("- Book: " + title.getTitle() + " | Checkout: " +
                    audit.getCheckout() + " | Return: " + returnTime);
        }
    }

    public void printLibraryBooks() {
        System.out.println("Library Books:");
        for (Book book : books) {
            if (book != null) {
                System.out.println("- Title: " + book.getTitle() + " | Total: " + book.getTotalCopies() +
                        " | Available: " + book.getAvailableCopies() + " | Lended: " + book.getLendedCopies());
            }
        }
    }

    public boolean checkoutBook(String bookTitle, Member member) {
        if (member == null) {
            System.out.println("Member not found");
            return false;
        }

        Book book = searchBook(bookTitle);

        if (book == null) {
            System.out.println("Book not found");
            return false;
        }

        if (book.getAvailableCopies() == 0) {
            System.out.println("No more copies available for this book");
            return false;
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        book.setLendedCopies(book.getLendedCopies() + 1);

        member.getBooks().put(new Title(book.getTitle()), new LendAudit("checkout", null));

        System.out.println("Book checked out successfully.");
        return true;
    }

    public boolean returnBook(String bookTitle, Member member) {
        if (member == null) {
            System.out.println("Member not found");
            return false;
        }

        Book book = searchBook(bookTitle);

        if (book == null) {
            System.out.println("Book not found");
            return false;
        }

        LendAudit audit = member.getBooks().get(new Title(book.getTitle()));

        if (audit == null) {
            System.out.println("Member did not check out this book");
            return false;
        }

        book.setAvailableCopies(book.getAvailableCopies() + 1);
        book.setLendedCopies(book.getLendedCopies() - 1);

        audit.setCheckin("checkin");
        member.getBooks().put(new Title(book.getTitle()), audit);

        System.out.println("Book returned successfully.");
        return true;
    }

    public static void main(String[] args) {
        Main library = new Main();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add Member");
            System.out.println("2. Remove Member");
            System.out.println("3. Add Book");
            System.out.println("4. Remove Book");
            System.out.println("5. Checkout Book");
            System.out.println("6. Return Book");
            System.out.println("7. Print Member Audit");
            System.out.println("8. Print Library Books");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter member ID: ");
                    String memberId = scanner.nextLine();
                    System.out.print("Enter member name: ");
                    String memberName = scanner.nextLine();
                    library.addMember(memberId, memberName);
                    break;
                case 2:
                    System.out.print("Enter member ID: ");
                    memberId = scanner.nextLine();
                    library.removeMember(memberId);
                    break;
                case 3:
                    System.out.print("Enter book title: ");
                    String bookTitle = scanner.nextLine();
                    System.out.print("Enter total copies: ");
                    int totalCopies = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character
                    library.addBook(bookTitle, totalCopies);
                    break;
                case 4:
                    System.out.print("Enter book title: ");
                    bookTitle = scanner.nextLine();
                    library.removeBook(bookTitle);
                    break;
                case 5:
                    System.out.print("Enter member ID: ");
                    memberId = scanner.nextLine();
                    System.out.print("Enter book title: ");
                    bookTitle = scanner.nextLine();
                    Member member = library.members.get(memberId);
                    library.checkoutBook(bookTitle, member);
                    break;
                case 6:
                    System.out.print("Enter member ID: ");
                    memberId = scanner.nextLine();
                    System.out.print("Enter book title: ");
                    bookTitle = scanner.nextLine();
                    member = library.members.get(memberId);
                    library.returnBook(bookTitle, member);
                    break;
                case 7:
                    System.out.print("Enter member ID: ");
                    memberId = scanner.nextLine();
                    member = library.members.get(memberId);
                    library.printMemberAudit(member);
                    break;
                case 8:
                    library.printLibraryBooks();
                    break;
                case 9:
                    System.out.println("Exiting the program");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
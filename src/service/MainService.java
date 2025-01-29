package service;

import model.User;
import model.Book;
import model.Role;
import utils.MyList;

import java.time.LocalDate;

public interface MainService {

    public User getActivUser();
    boolean registerUser(String email, String password);

    boolean loginUser(String email, String password);

    public boolean isUserBLOCKED(String email);

    void logout();

    //Удаление Пользователя
    public boolean delUser(String email);

    //Список всех пользователей
    MyList<User> userList();


    //Взять книгу
    Book takeBook(int bookId);

    //Добавить книгу
    void addBook(String name, String author);

    //Список всех книг
    MyList<Book> getAllBooks();


    //Получить список книг по автору
    MyList<Book> getBooksByAuthor(String author);

    //Получить список книг по названию
    public MyList<Book> getBooksByName(String name);


    //Получить список свободных книг
    MyList<Book> getFreeBooks();


    //Удаление книги
    boolean delBookById(int id);


    //У кого из пользователей книга
    String findUserByBookId(int id);


    //Редактировать книгу
    boolean bookUpdateById(int id,String name,String author);

     boolean UserUpdatePassword(String email,String password);


    //Список книг у пользователя
    MyList<Book> getBooksByUser(String email);

    //Возврат книги
    public Book returnBook(int bookId);

    public boolean UserStatusUpdate(String email,Role role);


    public MyList<Book> getBooksSortByName();

    public MyList<Book> getBooksSortByAuthor();

    public LocalDate getTakeBookDate(int idBook);

    public boolean updateTakeBookDate(int idBook, LocalDate newDate);

    public boolean isEmailExist(String email);

}

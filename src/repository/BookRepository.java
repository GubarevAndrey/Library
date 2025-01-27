package repository;

import model.User;
import utils.MyList;
import model.Book;

public interface BookRepository {
    //1.Create
    public void  addBook(String name, String author);

    //2. Read
    // получить список книг
    MyList<Book> getAllBooks();

    // получение книги по id
    public  Book getById(int id);


    // список книг по автору
    MyList<Book> getBookByAuthor(String author);

    // список книг по названию
    public MyList<Book> getBooksByName(String name);

    // список свободных книг
    public MyList<Book> getFreeBooks();


    // поменять статус книги
    void updateBookStatus(int id, boolean newStatus);


    //Delete
    void deleteById(int id);

    public void bookUpdateById(int id,String name, String author);

    public MyList<Book> getBooksByUser(String email);

    public MyList<Book> getBooksSortByName();

    public MyList<Book> getBooksSortByAuthor();
}

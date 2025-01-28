package repository;

import model.Book;
import model.User;
import utils.MyArrayList;
import utils.MyList;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class BookRepositoryImpl implements BookRepository{
    MyList<Book> books;

    public BookRepositoryImpl() {

        this.books =new MyArrayList<>();
        addStartBooks();
    }
    private final AtomicInteger currentId=new AtomicInteger(1);// спец обьект

    private void addStartBooks() {
        books.addAll( new Book("Курс Java","Иванов",currentId.getAndIncrement()),
                      new Book("Курс Python","Иванов",currentId.getAndIncrement()),
                      new Book("Война и мир","Толстой",currentId.getAndIncrement()),
                      new Book("Анна Каренина","Толстой",currentId.getAndIncrement()),
                      new Book("А жизнь так коротка","Чейз",currentId.getAndIncrement()),
                      new Book("Чародейка","Чейз",currentId.getAndIncrement()),
                      new Book("Изготовление мебели","Толокнеев",currentId.getAndIncrement())
                );

    }

    @Override
    public void  addBook(String name, String author) {
        books.add(new Book(name,author,currentId.getAndIncrement()));

    }

    @Override
    public MyList<Book> getAllBooks() {
        return books;
    }

    @Override
    public Book getById(int id) {
        for (Book book:books) {
            if(book.getId()==id) return book;
        }
        return null;
    }

    @Override
    public MyList<Book> getBookByAuthor(String author) {
        MyList<Book> list = new MyArrayList<>();
        for (Book book:books) {
            if(book.getAuthor().toUpperCase().indexOf(author.toUpperCase())!=-1) {
                list.add(book);
            }
        }
        return list;
    }

    @Override
    public MyList<Book> getBooksByName(String name) {
        MyList<Book> list = new MyArrayList<>();
        for (Book book:books) {
            if(book.getName().toUpperCase().indexOf(name.toUpperCase())!=-1) {
                list.add(book);
            }
        }
        return list;
    }




    public MyList<Book> getFreeBooks(){
        MyList<Book> list = new MyArrayList<>();
        for (Book book:books) {
            if(book.isBusy()==false) {
                list.add(book);
            }
        }
        return list;
    }


    @Override
    public void updateBookStatus(int id, boolean newStatus) {
        Book book = getById(id);
        book.setBusy(newStatus);
    }



    @Override
    public void deleteBook(Book book) {
        books.remove(book);
    }

    @Override
    public void bookUpdateById(int id,String name, String author) {
        Book book= getById(id);
        book.setName(name);
        book.setAuthor(author);
    }

    @Override
    public MyList<Book> getBooksByUser(String email) {
        MyList<Book> list = new MyArrayList<>();
        for (Book book : books) {
            if (email.equals(book.getUserUse())) {
                list.add(book);
            }
        }
        return list;
    }

    @Override
    public MyList<Book> getBooksSortByName() {
        int compare=0;
        Book temp;
        MyList<Book> list = new MyArrayList<>();
        for(Book book: books) {
            list.add(book);
        }
        for (int i = 0; i < books.size(); i++) {
            for (int j = 0; j < books.size() - 1 - i; j++) {
                compare=books.get(j).getName().compareTo(books.get(j+1).getName());
                if (compare>0) {
                    temp = books.get(j);
                    books.set(j,books.get(j+1));
                    books.set(j+1,temp);
                }
            }
        }
        return list;
    }


    @Override
    public MyList<Book> getBooksSortByAuthor() {
        int compare=0;
        Book temp;
        MyList<Book> list = new MyArrayList<>();
        for(Book book: books) {
            list.add(book);
        }
        for (int i = 0; i < books.size(); i++) {
            for (int j = 0; j < books.size() - 1 - i; j++) {
                compare=books.get(j).getAuthor().compareTo(books.get(j+1).getAuthor());
                if (compare>0) {
                    temp = books.get(j);
                    books.set(j,books.get(j+1));
                    books.set(j+1,temp);
                }
            }
        }
        return list;
    }
    @Override
    public LocalDate getTakeBookDate (int idBook){
        Book book= getById(idBook);
        return book.getTakeDate();
    }


    @Override
    public void updateTakeBookDate (int idBook, LocalDate newDate){
        Book book= getById(idBook);
        book.setTakeDate(newDate);
    }



}

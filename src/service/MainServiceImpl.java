package service;

import model.Book;
import model.Role;
import model.User;
import repository.BookRepository;
import repository.UserRepository;
import utils.MyList;
import utils.PersonValidation;

public class MainServiceImpl implements MainService{
    private final BookRepository bookRepository;
    private final UserRepository userRepository;



    private User activUser;

    public MainServiceImpl(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public User getActivUser() {
        return activUser;
    }

    @Override
    public boolean registerUser(String email, String password) {
        if (activUser.getRole()==Role.ADMIN) {
            if (!PersonValidation.isEmailValid(email)) {
                System.out.println("Email - "+email+" не корректный !");
                return false;
            }
            if (!PersonValidation.isPasswordValid(password)) {
                System.out.println("Пароль - "+password+" не корректный.");
                return false;
            }

            // Проверим есть ли такой пользователь уже
            if (userRepository.isEmailExist(email)) {
                System.out.println("Пользователь с Email- "+email+" уже существует !");
                return false;
            }
            userRepository.addUser(email, password);
            return true;
        }
        return false;
    }

    @Override
    public boolean loginUser(String email, String password) {
           if (activUser!=null) {
              logout();
            }
            // Проверим есть ли такой пользователь
            if (!userRepository.isEmailExist(email)) {
                System.out.println("Пользователя с Email - " + email + " нет ! Сначала зарегистрируйтесь");
                return false;
            }
            for (User user : userRepository.getUsers()) {
                if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                    if (user.getRole() == Role.ADMIN) {
                        activUser = user;
                        activUser.setRole(Role.ADMIN);
                        return true;
                    }
                    if (user.getRole() == Role.USER) {
                        activUser = user;
                        activUser.setRole(Role.USER);
                        return true;
                    }
                    if (user.getRole() == Role.BLOCKED) {
                        System.out.println("Вы ЗАБЛОКИРОВАНЫ ! Обратитесь к администратору");
                        return false;
                    }
                }
            }
            System.out.println("Пароль не верный");
            return false;
    }

    @Override
    public void logout() {
        activUser=null;
    }

    @Override
    public boolean delUser(String email){
        User isDelUser;
        if (activUser.getRole()==Role.ADMIN) {
            isDelUser=userRepository.delUser(email);
            if (isDelUser!=null) {
                return true;
            }
        }
        return false;
    }


    @Override
    public MyList<User> userList() {
        if (activUser.getRole()!=Role.BLOCKED) {
            return userRepository.getUsers();
        }else {
            System.out.println("Вы ЗАБЛОКИРОВАНЫ ! Обратитесь к администратору");
        }
        return null;
    }

    @Override
    public Book takeBook(int bookId) {
        boolean find=false;
        if (activUser.getRole()!=Role.BLOCKED) {
            for (Book book : bookRepository.getAllBooks()) {
                if (book.getId() == bookId) {
                    find=true;
                    if(book.isBusy()==false) {
                        book.setBusy(true);
                        book.setUserUse(activUser.getEmail());
                        return book;
                    }else {
                        System.out.println("Книга с id:"+bookId+" занята");
                    }
                }
            }
            if (find==false) {
                System.out.println("Книга с id:"+bookId+" не существует");
            }
        }else {
            System.out.println("Вы ЗАБЛОКИРОВАНЫ ! Обратитесь к администратору");
        }
        return null;
    }


    @Override
    public Book returnBook(int bookId) {
        boolean find=false;
        if (activUser.getRole()!=Role.BLOCKED) {
            for (Book book : bookRepository.getAllBooks()) {
                if (book.getId() == bookId) {
                    find=true;
                    if(book.isBusy()==true) {
                        book.setBusy(false);
                        book.setUserUse(null);
                        return book;
                    }else {
                        System.out.println("Книга с id:"+bookId+" не была занята");
                    }
                }
            }
            if (find==false) {
                System.out.println("Книга с id:"+bookId+" не существует");
            }
        }else {
            System.out.println("Вы ЗАБЛОКИРОВАНЫ ! Обратитесь к администратору");
        }
        return null;

    }

    @Override
    public boolean addBook(String name, String author) {
        if (activUser.getRole()==Role.ADMIN) {
            bookRepository.addBook(name,author);
            return true;
        } else {
            System.out.println("Добавлять книги может только администратор");
        }
        return false;
    }

    @Override
    public MyList<Book> getAllBooks() {
        if (activUser.getRole()!=Role.BLOCKED) {
            return bookRepository.getAllBooks();
        }else {
            System.out.println("Вы ЗАБЛОКИРОВАНЫ ! Обратитесь к администратору");
        }
        return null;
    }

    @Override
    public MyList<Book> getBooksByAuthor(String author) {
        if (activUser.getRole()!=Role.BLOCKED) {
            return bookRepository.getBookByAuthor(author);
        }else {
            System.out.println("Вы ЗАБЛОКИРОВАНЫ ! Обратитесь к администратору");
        }
        return null;
    }


    public MyList<Book> getBooksByName(String name) {
        if (activUser.getRole()!=Role.BLOCKED) {
            return bookRepository.getBooksByName(name);
        }else {
            System.out.println("Вы ЗАБЛОКИРОВАНЫ ! Обратитесь к администратору");
        }
        return null;
    }


    @Override
    public MyList<Book> getFreeBooks() {
        if (activUser.getRole()!=Role.BLOCKED) {
            return bookRepository.getFreeBooks();
        } else {
            System.out.println("Вы ЗАБЛОКИРОВАНЫ ! Обратитесь к администратору");
        }
        return null;
    }

    @Override
    public boolean delBookById(int id) {
        int idInMyList=-1;
        if (activUser.getRole()==Role.ADMIN) {
            for(Book book:bookRepository.getAllBooks()) {
                idInMyList++;
                if(book.getId()==id) {
                    bookRepository.deleteById(idInMyList);
                    return true;
                }
            }
            System.out.println("Вы ввели не правильный id книги");
        } else {
            System.out.println("Удалять книги может только администратор");
        }
        return false;
    }

    @Override
    public String findUserByBookId(int id) {
        if (activUser.getRole()!=Role.BLOCKED) {
            for (Book book : bookRepository.getAllBooks()) {
                if (book.getId() == id) {
                    return book.getUserUse();
                }
            }
            System.out.println("Вы ввели не правильный id книги");
        }else {
            System.out.println("Вы ЗАБЛОКИРОВАНЫ ! Обратитесь к администратору");
        }
        return null;
    }

    @Override
    public boolean bookUpdateById(int id,String name, String author) {
        if (activUser.getRole()==Role.ADMIN) {
            for (Book book : bookRepository.getAllBooks()) {
                if (book.getId() == id) {
                    bookRepository.bookUpdateById(id,name,author);
                    return true;
                }
            }
            System.out.println("Вы ввели не правильный id книги");
        }else {
            System.out.println("Редактировать книги может только администратор");
        }
        return false;
    }


    @Override
    public boolean UserUpdatePassword(String email,String password) {
        if (activUser.getRole()==Role.ADMIN ) {
            if(PersonValidation.isPasswordValid(password)) {
                for (User user : userRepository.getUsers()) {
                    if (user.getEmail().equals(email)) {
                        if (!PersonValidation.isPasswordValid(password))
                            userRepository.UserUpdatePassword(email, password);
                            return true;
                    }
                }
                System.out.println("Пользователя с Email- "+email+" не существует!");
            } else {
                System.out.println("Пароль - "+password+" не корректный.");
            }
        }
        return false;
    }


    @Override
    public MyList<Book> getBooksByUser(String email) {
        if (activUser.getRole()!=Role.BLOCKED) {
           return bookRepository.getBooksByUser(email);
        }else {
            System.out.println("Вы ЗАБЛОКИРОВАНЫ ! Обратитесь к администратору");
        }
        return null;


    }

    @Override
    public boolean UserStatusUpdate(String email,Role role){
        boolean isUpdate;
        if (activUser.getRole()==Role.ADMIN ) {
            isUpdate=userRepository.UserStatusUpdate(email,role);
            if (isUpdate == true) {
                return true;
            }
        }
        return false;
    }

    @Override
    public MyList<Book> getBooksSortByName(){
        if (activUser.getRole()!=Role.BLOCKED) {
            return bookRepository.getBooksSortByName();
        }else {
            System.out.println("Вы ЗАБЛОКИРОВАНЫ ! Обратитесь к администратору");
        }
        return null;
    }

    @Override
    public MyList<Book> getBooksSortByAuthor(){
        if (activUser.getRole()!=Role.BLOCKED) {
            return bookRepository.getBooksSortByAuthor();
        }else {
            System.out.println("Вы ЗАБЛОКИРОВАНЫ ! Обратитесь к администратору");
        }
        return null;
    }

}

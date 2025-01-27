import repository.BookRepository;
import repository.BookRepositoryImpl;
import repository.UserRepository;
import repository.UserRepositoryImpl;
import service.MainService;
import service.MainServiceImpl;
import view.Menu;

import java.util.Arrays;
import java.util.Date;

public class BookApp {
    public static void main(String[] args) {

        Date date = new Date();
        String s=date.toString();
        System.out.println(s);


        UserRepository userRepository = new UserRepositoryImpl();
        BookRepository bookRepository = new BookRepositoryImpl();

        MainService service = new MainServiceImpl(bookRepository, userRepository);
        Menu menu = new Menu(service);
        menu.start();

    }



}

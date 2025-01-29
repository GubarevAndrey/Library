package view;

import model.Role;
import model.User;
import model.Book;
import service.MainService;
import utils.MyList;
import utils.PersonValidation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Menu {
    private final MainService service;
    private final Scanner scanner= new Scanner(System.in);
    private boolean exitAdminMenu;
    private boolean exitUserMenu;
    private boolean exitBookMenu;

    public Menu(MainService service) {

        this.service = service;
    }

    public void start(){
        showMenu();
    }


    private void showMenu() {
        boolean isAutoriz=false;
        boolean isRegistration=false;
        System.out.println();
        System.out.println();
        int input=InputUser();
        if (input==1) {
            isAutoriz = authorizationUser();
        }
        if (input==2) {
            isRegistration = registrationUser();
        }
        if (isAutoriz == true || isRegistration==true) {
            while (true) {
                System.out.println();
                System.out.println();
                System.out.println("╔════════════════════════════════════════╗");
                System.out.println("║   ДОБРО ПОЖАЛОВАТЬ В МЕНЮ БИБЛИОТЕКИ:  ║");
                System.out.println("║         1. Меню книг                   ║");
                System.out.println("║         2. Меню пользователя           ║");
                System.out.println("║         3. Меню администратора         ║");
                System.out.println("║         0- Выход из системы            ║");
                System.out.println("╚════════════════════════════════════════╝");
                System.out.print("Сделайте выбор:");

                String choice = scanner.nextLine();

                if (choice.equals("0")) {
                    System.out.println("Давай До свидания...");
                    // Завершить работу приложения
                    System.exit(0);
                }
                showMenuCase(choice);

            }
        }else {
            System.out.println("ОШИБКА авторизации !");
        }
    }

    private void showMenuCase(String choice) {
       // boolean isAutoriz;
        switch (choice) {
            case "1":
                    showBookMenu();
                break;
            case "2":
                    showUserMenu();
                break;
            case "3":
                    if(service.getActivUser().getRole()== Role.ADMIN) {
                        showAdminMenu();
                    }else {
                        System.out.println("Вы не являетесь Администратором !");
                    }
                break;
            default:
                System.out.println("Сделайте корректный выбор...");
                waitRead();
        }

    }
    private void waitRead() {
        System.out.println("\n Для продолжения нажмите Enter...");
        scanner.nextLine();
    }


    private int InputUser() {
        while (true) {
            System.out.println();
            System.out.println("┌──────────────────────────────────────┐");
            System.out.println("│    ДОБРО ПОЖАЛОВАТЬ В БИБЛИОТЕКУ     │");
            System.out.println("│   1. Авторизация                     │");
            System.out.println("│   2. Регистрация нового пользователя │");
            System.out.println("│   0. Выход                           │");
            System.out.println("└──────────────────────────────────────┘");
            System.out.print("Сделайте выбор:");
            String input = scanner.nextLine();
            if (input.equals("0")) {
                System.out.println("Давай До свидания...");
                // Завершить работу приложения
                System.exit(0);
            }
            if (input.equals("1")==true) return 1;
            if (input.equals("2")==true) return 2;
            System.out.print("Сделайте корректный выбор....");
            waitRead();
        }
    }




    private  boolean   authorizationUser() {
        String email;
        String password;
        boolean register;
        System.out.println();
        System.out.println();
        System.out.print("Введите Email:");
        email = scanner.nextLine();
        if (email.length() != 0 && email != null && service.isEmailExist(email) == true) {
            if (service.isUserBLOCKED(email) == true) {
                System.out.println("Вы ЗАБАНЕНЫ! Обратитесь к Администратору");
                return false;
            }
            System.out.print("Введите пароль:");
            password = scanner.nextLine();
            boolean log = service.loginUser(email, password);
            if (log == true) {
                return true;
            }
        } else {
            System.out.println("Авторизация ПРОВАЛЕНА !");
        }
        return false;
    }

    private  boolean   registrationUser() {
        String email;
        String password;
        boolean register;

        System.out.println();
        System.out.println();
        System.out.println("Регистрация нового Пользователя.");
        System.out.print("Введите Email пользователя:");
        email= scanner.nextLine();
        if(PersonValidation.isEmailValid(email)==false) {
            System.out.println("НЕ КОРЕКТНЫЙ EMAIL !");
            return false;
        }
        System.out.print("Введите Пароль пользователя:");
        password= scanner.nextLine();
        register=service.registerUser(email,password);
        if (register==true){
            System.out.println("Пользователь- "+email+" успешно зарегистрирован");
            return true;
        } else {
            System.out.println("Регистрация провалена !");
        }
        return false;
    }


    private void showUserMenu() {
          exitUserMenu=false;
        while (true) {
            System.out.println();
            System.out.println("┌──────────────────────────────────────┐");
            System.out.println("│           МЕНЮ ПОЛЬЗОВАТЕЛЯ:         │");
            System.out.println("│   1. Список книг у пользователя      │");
            System.out.println("│   2. Выдача книги                    │");
            System.out.println("│   3. Возврат книги                   │");
            System.out.println("│   0. Возврат в предыдущее меню       │");
            System.out.println("└──────────────────────────────────────┘");
            System.out.printf(service.getActivUser().getRole()==Role.ADMIN ? "(ВЫ АДМИНИСТРАТОР -'":"(ВЫ ПОЛЬЗОВАТЕЛЬ -'");
            System.out.print(service.getActivUser().getEmail()+"') Сделайте выбор:") ;
            String input= scanner.nextLine();
            showUserMenuCase(input);
            if (exitUserMenu==true) break;
            waitRead();

        }
    }


    private void showUserMenuCase(String input){
        exitUserMenu=false;
        switch (input){
            case "1"://Список книг у пользователя
                String activeUserEmail=service.getActivUser().getEmail();
                MyList<Book> isActiveUserBooksList=service.getBooksByUser(activeUserEmail);
                if(isActiveUserBooksList.size()>0) {
                    System.out.println("Список книг у пользователя -"+service.getActivUser().getEmail()+" :");
                    bookPrintToConsole(isActiveUserBooksList);
                } else {
                    System.out.println();
                    System.out.println("У Вас нет используемых Книг");
                }
                break;

            case "2":
                // Выдача книги
                System.out.println();
                System.out.println();
                System.out.println("Выдача книги:");
                System.out.print("Введите id книги:");
                String inputStr=scanner.nextLine();
                Integer idBookInt= checkInput(inputStr);
                int idBook=0;
                if (idBookInt==null) {
                    System.out.println("Сделайте корректный выбор...");
                    break;
                } else {
                    idBook=idBookInt;
                }
                Book take =service.takeBook(idBook);
                if(take!=null) {
                    System.out.println("Вы взяли книгу c названием- '"+take.getName()+"'   Автор-"+take.getAuthor()+"'  ID:"+idBook);
                }
                break;

            case "3":
                //Возврат книги
                System.out.println();
                System.out.println();
                System.out.println("Возврат книги:");
                System.out.print("Введите id книги:");
                String inputStr1=scanner.nextLine();
                Integer idBookInt1= checkInput(inputStr1);
                int idBook1=0;
                if (idBookInt1==null) {
                    System.out.println("Сделайте корректный выбор...");
                    break;
                } else {
                    idBook1=idBookInt1;
                }
                Book retBook =service.returnBook(idBook1);
                if(retBook!=null) {
                    System.out.println("Вы Вернули книгу c названием- '"+retBook.getName()+
                            "'   Автор-"+retBook.getAuthor()+"'  ID:"+idBook1);
                }
                break;

            case "0":
                //logout:
                exitUserMenu=true;
                System.out.println("Вы вышли из Меню пользователя");
                break;

            default:
                System.out.println("Сделайте корректный выбор...");
        }
    }

    private Integer checkInput(String input) {
        try {
            return Integer.valueOf(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    private  void  showAdminMenu(){
        exitAdminMenu=false;
        while (true) {
            System.out.println();
            System.out.println();
            System.out.println("┌──────────────────────────────────────┐");
            System.out.println("│       ВЫ В МЕНЮ АДМИНИСТРАТОРА:      │");
            System.out.println("│  1. Регистрация нового пользователя  │");
            System.out.println("│  2. Изменение пароля пользователя    │");
            System.out.println("│  3. Изменение Статуса пользователя   │");
            System.out.println("│  4. Список всех пользователей        │");
            System.out.println("│  5. Удаление пользователя            │");
            System.out.println("│  6. Добавление книги                 │");
            System.out.println("│  7. Удаление книги                   │");
            System.out.println("│  8. Редактирование книги             │");
            System.out.println("│  9. Список всех книг                 │");
            System.out.println("│  0. Возврат в предыдущ меню          │");
            System.out.println("└──────────────────────────────────────┘");
            System.out.printf(service.getActivUser().getRole()==Role.ADMIN ? "(ВЫ АДМИНИСТРАТОР -'":"(ВЫ ПОЛЬЗОВАТЕЛЬ -'");
            System.out.print(service.getActivUser().getEmail()+"') Сделайте выбор:") ;

            String input = scanner.nextLine();
            if (input.equals("0")) {
                break;
            }

            showAdminMenuCase(input);
            if (exitAdminMenu==true) break;
            waitRead();
        }

    }

    private void showAdminMenuCase(String input) {
        int i=0;
        String s1="";
        int lenEmail=0;
        String email;
        String password;
        String name;
        String author;
        boolean register;
        boolean updatePassword;
        boolean isDeleteBook;
        boolean isUpdateBook;
        int idBook;

        switch (input) {
            case "1":
                //Регистрация нового пользователя
                registrationUser();
                break;

            case "2":
                //Изменение пароля пользователя
                System.out.println();
                System.out.println();
                System.out.println("Изменение пароля Пользователя:");
                System.out.print("Введите email пользователя:");
                email= scanner.nextLine();
                System.out.print("Введите Новый пароль пользователя:");
                password= scanner.nextLine();
                updatePassword=service.UserUpdatePassword(email,password);
                if (updatePassword==true){
                    System.out.println("У пользователя- "+email+" пароль успешно изменен");
                } else {
                    System.out.println("Изменение пароля провалено !");
                }
                break;

            case "3":
                //Изменение Статуса пользователя
                System.out.println();
                System.out.println();
                System.out.println("Изменение Статуса пользователя:");
                System.out.print("Введите email пользователя:");
                email= scanner.nextLine();
                System.out.print("Введите статус пользователя (1-User, 2- BLOCKED, 3-ADMIN)");
                String inputStatusStr= scanner.nextLine();
                Integer status= checkInput(inputStatusStr);
                int inputStatus=0;
                if (status==null) {
                    System.out.println("Сделайте корректный выбор...");
                    break;
                } else {
                    inputStatus=status;
                }
                Role role = null;
                boolean isUpdate=false;
                if (inputStatus == 1) role = Role.USER;
                if (inputStatus == 2) role = Role.BLOCKED;
                if (inputStatus == 3) role = Role.ADMIN;
                if (role!=null) {
                    isUpdate = service.UserStatusUpdate(email, role);
                }
                if(isUpdate==true) {
                    System.out.println("Статус пользователя -"+email+" изменен на -"+role);
                } else {
                    System.out.println("Статус НЕ ИЗМЕНЕН!");
                }
                break;

            case "4":
                //Список всех пользователей
                if(service.userList() !=null) {
                    i=0;
                    System.out.println();
                    System.out.println();
                    lenEmail=0;
                    System.out.println("Список пользователей:");
                    for (User user : service.userList()) {
                        if(user.getEmail().length()>lenEmail) lenEmail=user.getEmail().length();
                    }
                    for (User user : service.userList()) {
                        i++;
                        for(int j=0; j<lenEmail+2-user.getEmail().length();j++){
                            s1=s1+" ";
                        }
                        System.out.println(i+") Пользователь- " + user.getEmail()+s1 + "  Права- " + user.getRole());
                        s1="";
                    }
                } else {
                    System.out.println("Пользователей нет");
                }
                break;

            case "5":
                //Удаление пользователя
                System.out.println();
                System.out.println();
                System.out.println("УДАЛЕНИЕ пользователя.");
                System.out.print("Введите email пользователя для УДАЛЕНИЯ:");
                email= scanner.nextLine();
                boolean isDel= service.delUser(email);
                if (isDel==true) {
                    System.out.println("Пользователь- "+email+" успешно удален");
                } else {
                    System.out.println("Пользователь- "+email+" НЕ БЫЛ удален");
                }
                break;

            case "6":
                //Добавление книги
                System.out.println();
                System.out.println();
                System.out.println("Добавление новой книги.");
                System.out.print("Введите название книги:");
                name= scanner.nextLine();
                System.out.print("Введите автора книги:");
                author= scanner.nextLine();
                service.addBook(name,author);
                System.out.println("Книга успешно добавлена");
                break;

            case "7":
                //Удаление книги
                System.out.println();
                System.out.println();
                System.out.println("УДАЛЕНИЕ книги.");
                System.out.print("Введите ID книги:");
                String inputStr=scanner.nextLine();
                Integer idBookInt= checkInput(inputStr);
                if (idBookInt==null) {
                    System.out.println("Сделайте корректный выбор...");
                    break;
                } else {
                    idBook=idBookInt;
                }
                isDeleteBook=service.delBookById(idBook);
                if (isDeleteBook==true){
                    System.out.println("Книга успешно УДАЛЕНА");
                } else {
                    System.out.println("Книга НЕ УДАЛЕНА !");
                }
                break;

            case "8":
                //Редактирование книги
                System.out.println();
                System.out.println();
                System.out.println("Редактирование книги.");
                System.out.print("Введите ID книги:");
                String inputStr1=scanner.nextLine();
                Integer idBookInt1= checkInput(inputStr1);
                if (idBookInt1==null) {
                    System.out.println("Сделайте корректный выбор...");
                    break;
                } else {
                    idBook=idBookInt1;
                }
                System.out.print("Введите Новое название книги:");
                name= scanner.nextLine();
                System.out.print("Введите Нового автора книги:");
                author= scanner.nextLine();
                isUpdateBook=service.bookUpdateById(idBook,name,author);
                if (isUpdateBook==true){
                    System.out.println("Книга успешно Изменена");
                } else {
                    System.out.println("Изменения НЕ выполнены !");
                }
                break;

            case "9":
                //Список всех книг
                if(service.getAllBooks()!=null) {
                    System.out.println();
                    System.out.println("Список всех книг:");
                    bookPrintToConsole(service.getAllBooks());
                } else {
                    System.out.println("В библиотеке нет книг");
                }
                break;

            case "0":
                exitAdminMenu=true;
                System.out.println("Вы вышли из Меню Администратора");
                waitRead();
                break;
            default:
                System.out.println("Сделайте корректный выбор...");

        }
    }



    private  void  showBookMenu(){
        while (true) {
            System.out.println();
            System.out.println();
            System.out.println("┌───────────────────────────────────────────────────────────┐");
            System.out.println("│                 ВЫ В МЕНЮ КНИГ:                           │");
            System.out.println("│    1. Список всех книг                                    │");
            System.out.println("│    2. Список свободных                                    │");
            System.out.println("│    3. Список всех книг,отсортированный по автору          │");
            System.out.println("│    4. Список всех книг, отсортированный по названию книги │");
            System.out.println("│    5. Поиск книг по Автору                                │");
            System.out.println("│    6. Поиск книг по Названию                              │");
            System.out.println("│    7. За каким Пользователем книга                        │");
            System.out.println("│    0. Вернуться в предыдущее меню                         │");
            System.out.println("└───────────────────────────────────────────────────────────┘");
            System.out.printf(service.getActivUser().getRole()==Role.ADMIN ? "(ВЫ АДМИНИСТРАТОР -'":"(ВЫ ПОЛЬЗОВАТЕЛЬ -'");
            System.out.print(service.getActivUser().getEmail()+"') Сделайте выбор:") ;

            String input = scanner.nextLine();

            if (input.equals("0")) {
                break;
            }
            showBookMenuCase(input);
            if (exitBookMenu==true) break;
            waitRead();
        }
    }



    private void showBookMenuCase(String input) {
        switch (input) {
            case "1":
                //Список всех книг
                if(service.getAllBooks()!=null) {
                    System.out.println();
                    System.out.println("Список всех книг:");
                    bookPrintToConsole(service.getAllBooks());
                } else {
                    System.out.println("В библиотеке нет книг");
                }
                break;

            case "2":
                //Список свободных
                if(service.getFreeBooks()!=null) {
                    System.out.println();
                    System.out.println("Список свободных книг:");
                    bookPrintToConsole(service.getFreeBooks());
                } else {
                    System.out.println("В библиотеке нет свободных книг");
                }
                break;

            case "3":
                //Список всех книг,отсортированный по автору
                if(service.getBooksSortByAuthor()!=null) {
                    System.out.println();
                    System.out.println("Список всех книг, отсортированный по автору:");
                    bookPrintToConsole(service.getBooksSortByAuthor());
                } else {
                    System.out.println("В библиотеке нет книг");
                }
                break;

            case "4":
                //Список всех книг, отсортированный по названию книги
                if(service.getBooksSortByName()!=null) {
                    System.out.println();
                    System.out.println("Список всех книг,отсортированный по названию книги:");
                    bookPrintToConsole(service.getBooksSortByName());
                } else {
                    System.out.println("В библиотеке нет книг");
                }
                break;

            case "5":
                //Поиск книг по Автору
                System.out.println();
                System.out.println();
                System.out.println("Список книг по Автору:");
                System.out.print("Введите Автора книги:");
                String inputAuthor= scanner.nextLine();
                // scanner.nextLine();
                if(service.getBooksByAuthor(inputAuthor)!=null) {
                    System.out.println("Список книг по автору - "+inputAuthor+" :");
                    bookPrintToConsole(service.getBooksByAuthor(inputAuthor));
                } else {
                    System.out.println("В библиотеке нет книг с Автором-"+inputAuthor);
                }
                break;

            case "6":
                //Поиск книг по Названию
                System.out.println();
                System.out.println();
                System.out.println("Список книг по Названию:");
                System.out.print("Введите Название книги:");
                String inputName= scanner.nextLine();
                if(service.getBooksByAuthor(inputName)!=null) {
                    System.out.println("Список книг по автору - "+inputName+" :");
                    bookPrintToConsole(service.getBooksByName(inputName));

                } else {
                    System.out.println("В библиотеке нет книг с Автором-"+inputName);
                }
                break;

            case "7":
                //За каким Пользователем книга
                System.out.println();
                System.out.println();
                System.out.println("За каким Пользователем книга:");
                System.out.print("Введите id книги:");
                int idBook= scanner.nextInt();
                scanner.nextLine();
                if(service.findUserByBookId(idBook)!=null) {
                    System.out.print("Книга с id- "+idBook+" за Пользователем : "+service.findUserByBookId(idBook));
                } else {
                    System.out.print("Книга с id- "+idBook+" -свободна");
                }
                break;
            default:
                System.out.println("Сделайте корректный выбор...");

        }
    }


    private void bookPrintToConsole(MyList<Book> list){
        int i=0;
        int lenName=0;
        int lenAuthor=0;
        String s1="";
        String s2="";
        LocalDate date=LocalDate.now();
        for(Book book: list) {
            if (book.getName().length()>lenName) lenName=book.getName().length();
            if (book.getAuthor().length()>lenAuthor) lenAuthor=book.getAuthor().length();
        }
        for(Book book: list) {
            i++;
            for(int j=0; j<lenName+2-book.getName().length();j++){
                s1=s1+" ";
            }
            for(int j=0; j<lenAuthor+2-book.getAuthor().length();j++){
                s2=s2+" ";
            }
            System.out.print(i+") Название: '" + book.getName()+"'"+s1 + "  Автор:'" + book.getAuthor()+"'"+s2+
                    "  ID книги:"+book.getId()+"  Статус:");
            System.out.printf(book.getTakeDate()==null ? " Свободна":" За пользователем-'"+
                    book.getUserUse()+"'  Взята-"+book.getTakeDate()+" ("+
                    (date.until(book.getTakeDate(),ChronoUnit.DAYS))+" дней назад)");
            System.out.println();
            s1="";
            s2="";
        }
    }


}

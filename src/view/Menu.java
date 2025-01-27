package view;

import model.Role;
import model.User;
import model.Book;
import service.MainService;
import utils.MyList;

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

    private void showMenu(){
        while (true) {
            System.out.println();
            System.out.println();
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║   ДОБРО ПОЖАЛОВАТЬ В МЕНЮ БИБЛИОТЕКИ:  ║");
            System.out.println("║         1. Меню книг                   ║");
            System.out.println("║         2. Меню пользователя           ║");
            System.out.println("║         3. Меню администратора         ║");
            System.out.println("║         0- выход из системы            ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.print("Сделайте выбор:");

            int choice= scanner.nextInt();
            scanner.nextLine();

            if (choice==0) {
                System.out.println("Давай До свидания...");
                // Завершить работу приложения
                System.exit(0);
            }
            showMenuCase(choice);

        }
    }

    private void showMenuCase(int choice) {
        boolean isAutoriz;
        switch (choice) {
            case 1:

                isAutoriz=authorization();
                if (isAutoriz==true) {
                    showBookMenu();
                }
                break;
            case 2:

                isAutoriz=authorization();
                if (isAutoriz==true) {
                    showUserMenu();
                }
                break;
            case 3:
                isAutoriz=authorization();
                if (isAutoriz==true ){
                    if(service.getActivUser().getRole()== Role.ADMIN) {
                        showAdminMenu();
                    }else {
                        System.out.println("Вы не являетесь Администратором !");
                    }
                } else {
                    System.out.println("Авторизация провалена !");
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


    private  boolean   authorization(){
        System.out.println();
        System.out.println("Авторизация");
        System.out.print("Введите email:");
        String email= scanner.nextLine();
        System.out.print("Введите пароль:");
        String password= scanner.nextLine();

        boolean log=service.loginUser(email,password);
        if (log==true){
            return true;
        } else {

            return false;
        }
    }


    private void showUserMenu() {
        exitUserMenu=false;
        while (true) {
            System.out.println();
            System.out.println();
            System.out.println("┌──────────────────────────────────────┐");
            System.out.println("│      ВЫ В МЕНЮ ПОЛЬЗОВАТЕЛЯ:         │");
            System.out.println("│   1. Список книг у пользователя      │");
            System.out.println("│   2. Выдача книги                    │");
            System.out.println("│   3. Возврат книги                   │");
            System.out.println("│   0. logout пользователя и           │");
            System.out.println("│      возврат в предыдущее меню       │");
            System.out.println("└──────────────────────────────────────┘");
            System.out.print("Сделайте выбор:");

           int input = scanner.nextInt();
            scanner.nextLine();

            if (input == 0) {
                break;
            }

            if (exitUserMenu==true) break;
            showUserMenuCase(input);
            waitRead();

        }
    }


    private void showUserMenuCase(int input){
        int i=0;
        String s1="";
        String s2="";
        int lenName=0;
        int lenAuthor=0;
        MyList<Book> isActiveUserBooksList;

        switch (input){
            case 1://Список книг у пользователя
                String activeUserEmail=service.getActivUser().getEmail();
                isActiveUserBooksList=service.getBooksByUser(activeUserEmail);
                if(isActiveUserBooksList.size()>0) {
                    i=0;
                    lenName=0;
                    lenAuthor=0;
                    System.out.println();
                    for (Book book : service.getBooksByUser(service.getActivUser().getEmail())) {
                        if (book.getName().length()>lenName) lenName=book.getName().length();
                        if (book.getAuthor().length()>lenAuthor) lenAuthor=book.getAuthor().length();
                    }
                    System.out.println("Список книг у пользователя -"+service.getActivUser().getEmail()+" :");
                    for (Book book : service.getBooksByUser(service.getActivUser().getEmail())) {
                        i++;
                        for(int j=0; j<lenName+2-book.getName().length();j++){
                            s1=s1+" ";
                        }
                        for(int j=0; j<lenAuthor+2-book.getAuthor().length();j++){
                            s2=s2+" ";
                        }
                        System.out.println(i+") Название: '" + book.getName()+"'"+s1 + "  Автор:" + book.getAuthor()+s2+
                                "  ID книги:"+book.getId());
                        s1="";
                        s2="";
                    }
                } else {
                    System.out.println();
                    System.out.println("У Вас нет используемых Книг");
                }
                break;

            case 2:
               // Выдача книги
                System.out.println();
                System.out.println();
                System.out.println("Выдача книги:");
                System.out.print("Введите id книги:");
                int idBook= scanner.nextInt();
                scanner.nextLine();
                Book take =service.takeBook(idBook);
                if(take!=null) {
                    System.out.println("Вы взяли книгу c названием- '"+take.getName()+"'   Автор-"+take.getAuthor()+"'  ID:"+idBook);
                }
                break;

            case 3:
                //Возврат книги
                System.out.println();
                System.out.println();
                System.out.println("Возврат книги:");
                System.out.print("Введите id книги:");
                int idBook1= scanner.nextInt();
                scanner.nextLine();
                Book retBook =service.returnBook(idBook1);
                if(retBook!=null) {
                    System.out.println("Вы Вернули книгу c названием- '"+retBook.getName()+
                            "'   Автор-"+retBook.getAuthor()+"'  ID:"+idBook1);
                }
                break;

            case 0:
                //logout:
                service.logout();
                exitUserMenu=true;
                System.out.println("Вы вышли из Меню пользователя");
                break;

            default:
                System.out.println("Сделайте корректный выбор...");
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
            System.out.println("│  0. logout администратора и          │");
            System.out.println("│      возврат в предыдущ меню         │");
            System.out.println("└──────────────────────────────────────┘");
            System.out.print("Сделайте выбор:");

            int input = scanner.nextInt();
            scanner.nextLine();

            if (input == 0) {
                break;
            }

            showAdminMenuCase(input);
            if (exitAdminMenu==true) break;
            waitRead();
        }

    }

    private void showAdminMenuCase(int input) {
        int i=0;
        String s1="";
        String s2="";
        int lenName=0;
        int lenAuthor=0;
        int lenEmail=0;
        String email;
        String password;
        String name;
        String author;
        boolean reg;
        boolean updatePassword;
        boolean isAddBook;
        boolean isDelBook;
        boolean isUpdateBook;
        boolean isUserStatusUpdate;
        int idBook;

        switch (input) {
            case 1:
            //Регистрация нового пользователя
                System.out.println();
                System.out.println();
                System.out.println("Регистрация нового Пользователя:");
                System.out.print("Введите email пользователя:");
                email= scanner.nextLine();
                System.out.print("Введите пароль пользователя:");
                password= scanner.nextLine();

                reg=service.registerUser(email,password);
                if (reg==true){
                    System.out.println("Пользователь- "+email+" успешно зарегистрирован");
                } else {
                    System.out.println("Регистрация провалена !");
                }
                break;

            case 2:
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

            case 3:
                //Изменение Статуса пользователя
                System.out.println();
                System.out.println();
                System.out.println("Изменение Статуса пользователя:");
                System.out.print("Введите email пользователя:");
                email= scanner.nextLine();
                System.out.print("Введите статус пользователя (1-User, 2- BLOCKED, 3-ADMIN)");
                int inputStatus= scanner.nextInt();
                scanner.nextLine();
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

            case 4:
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

            case 5:
                //Удаление пользователя
                System.out.println();
                System.out.println();
                System.out.println("УДАЛЕНИЕ пользователя.");
                System.out.print("Введите email пользователя:");
                email= scanner.nextLine();
                boolean isDel= service.delUser(email);
                if (isDel==true) {
                    System.out.println("Пользователь- "+email+" успешно удален");
                } else {
                    System.out.println("Пользователь- "+email+" НЕ БЫЛ удален");
                }
                break;

            case 6:
                //Добавление книги
                System.out.println();
                System.out.println();
                System.out.println("Добавление новой книги.");
                System.out.print("Введите название книги:");
                name= scanner.nextLine();
                System.out.print("Введите автора книги:");
                author= scanner.nextLine();
                isAddBook=service.addBook(name,author);
                if (isAddBook==true){
                    System.out.println("Книга успешно добавлена");
                } else {
                    System.out.println("Книга не добавлена !");
                }
                break;

            case 7:
                //Удаление книги
                System.out.println();
                System.out.println();
                System.out.println("УДАЛЕНИЕ книги.");
                System.out.print("Введите ID книги:");
                idBook= scanner.nextInt();
                scanner.nextLine();
                isDelBook=service.delBookById(idBook);
                if (isDelBook==true){
                    System.out.println("Книга успешно УДАЛЕНА");
                } else {
                    System.out.println("Книга НЕ УДАЛЕНА !");
                }
                break;

            case 8:
                //Редактирование книги
                System.out.println();
                System.out.println();
                System.out.println("Редактирование книги.");
                System.out.print("Введите ID книги:");
                idBook= scanner.nextInt();
                scanner.nextLine();
                System.out.print("Введите Новое название книги:");
                name= scanner.nextLine();
                System.out.print("Введите Нового автора книги:");
                author= scanner.nextLine();
                isUpdateBook=service.delBookById(idBook);
                if (isUpdateBook==true){
                    System.out.println("Книга успешно Изменена");
                } else {
                    System.out.println("Изменения НЕ выполнены !");
                }
                break;

            case 9:
                //Список всех книг
                if(service.getAllBooks()!=null) {
                    i=0;
                    lenName=0;
                    lenAuthor=0;
                    System.out.println();
                    System.out.println("Список всех книг:");
                    for(Book book: service.getAllBooks()) {
                        if (book.getName().length()>lenName) lenName=book.getName().length();
                        if (book.getAuthor().length()>lenAuthor) lenAuthor=book.getAuthor().length();
                    }
                    for(Book book: service.getAllBooks()) {
                        i++;
                        for(int j=0; j<lenName+2-book.getName().length();j++){
                            s1=s1+" ";
                        }
                        for(int j=0; j<lenAuthor+2-book.getAuthor().length();j++){
                            s2=s2+" ";
                        }
                        System.out.println(i+") Название: '" + book.getName()+"'"+s1 + "  Автор:" + book.getAuthor()+s2+
                                "  ID книги:"+book.getId());
                        s1="";
                        s2="";
                    }
                } else {
                    System.out.println("В библиотеке нет книг");
                }
                break;

            case 0:
                //logout администратора и возврат в предыдущ меню
                service.logout();
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
            System.out.print("Сделайте выбор:");

            int input = scanner.nextInt();
            scanner.nextLine();

            if (input == 0) {
                break;
            }
            showBookMenuCase(input);
            waitRead();
        }
    }



    private void showBookMenuCase(int input) {
        int i=0;
        String s1="";
        String s2="";
        int lenName=0;
        int lenAuthor=0;
        switch (input) {
            case 1:
                //Список всех книг
                if(service.getAllBooks()!=null) {
                    i=0;
                    lenName=0;
                    lenAuthor=0;
                    System.out.println();
                    System.out.println("Список всех книг:");
                    for(Book book: service.getAllBooks()) {
                        if (book.getName().length()>lenName) lenName=book.getName().length();
                        if (book.getAuthor().length()>lenAuthor) lenAuthor=book.getAuthor().length();
                    }
                    for(Book book: service.getAllBooks()) {
                        i++;
                        for(int j=0; j<lenName+2-book.getName().length();j++){
                            s1=s1+" ";
                        }
                        for(int j=0; j<lenAuthor+2-book.getAuthor().length();j++){
                            s2=s2+" ";
                        }
                        System.out.println(i+") Название: '" + book.getName()+"'"+s1 + "  Автор:" + book.getAuthor()+s2+
                                "  ID книги:"+book.getId());
                        s1="";
                        s2="";
                    }
                } else {
                    System.out.println("В библиотеке нет книг");
                }
                break;

            case 2:
                //Список свободных
                if(service.getFreeBooks()!=null) {
                    i=0;
                    lenName=0;
                    lenAuthor=0;
                    System.out.println();
                    System.out.println("Список свободных книг:");
                    for(Book book: service.getFreeBooks()) {
                        if (book.getName().length()>lenName) lenName=book.getName().length();
                        if (book.getAuthor().length()>lenAuthor) lenAuthor=book.getAuthor().length();
                    }
                    for(Book book: service.getFreeBooks()) {
                        i++;
                        for(int j=0; j<lenName+2-book.getName().length();j++){
                            s1=s1+" ";
                        }
                        for(int j=0; j<lenAuthor+2-book.getAuthor().length();j++){
                            s2=s2+" ";
                        }
                        System.out.println(i+") Название: '" + book.getName()+"'" +s1+ "  Автор:" + book.getAuthor()+s2+
                               "  ID книги:"+book.getId() );
                        s1="";
                        s2="";
                    }
                } else {
                      System.out.println("В библиотеке нет свободных книг");
                }
                break;

            case 3:
                //Список всех книг,отсортированный по автору
                if(service.getBooksSortByAuthor()!=null) {
                    i=0;
                    lenName=0;
                    lenAuthor=0;
                    System.out.println();
                    System.out.println("Список всех книг:");
                    for(Book book: service.getBooksSortByAuthor()) {
                        if (book.getName().length()>lenName) lenName=book.getName().length();
                        if (book.getAuthor().length()>lenAuthor) lenAuthor=book.getAuthor().length();
                    }
                    for(Book book: service.getBooksSortByAuthor()) {
                        i++;
                        for(int j=0; j<lenName+2-book.getName().length();j++){
                            s1=s1+" ";
                        }
                        for(int j=0; j<lenAuthor+2-book.getAuthor().length();j++){
                            s2=s2+" ";
                        }
                        System.out.println(i+") Название: '" + book.getName()+"'"+s1 + "  Автор:" + book.getAuthor()+s2+
                                "  ID книги:"+book.getId());
                        s1="";
                        s2="";
                    }
                } else {
                    System.out.println("В библиотеке нет книг");
                }
                break;

            case 4:
                //Список всех книг, отсортированный по названию книги
                if(service.getBooksSortByName()!=null) {
                    i=0;
                    lenName=0;
                    lenAuthor=0;
                    System.out.println();
                    System.out.println("Список всех книг:");
                    for(Book book: service.getBooksSortByName()) {
                        if (book.getName().length()>lenName) lenName=book.getName().length();
                        if (book.getAuthor().length()>lenAuthor) lenAuthor=book.getAuthor().length();
                    }
                    for(Book book: service.getBooksSortByName()) {
                        i++;
                        for(int j=0; j<lenName+2-book.getName().length();j++){
                            s1=s1+" ";
                        }
                        for(int j=0; j<lenAuthor+2-book.getAuthor().length();j++){
                            s2=s2+" ";
                        }
                        System.out.println(i+") Название: '" + book.getName()+"'"+s1 + "  Автор:" + book.getAuthor()+s2+
                                "  ID книги:"+book.getId());
                        s1="";
                        s2="";
                    }
                } else {
                    System.out.println("В библиотеке нет книг");
                }
                break;

            case 5:
                //Поиск книг по Автору
                System.out.println();
                System.out.println();
                System.out.println("Список книг по Автору:");
                System.out.print("Введите Автора книги:");
                String inputAuthor= scanner.nextLine();
               // scanner.nextLine();
                if(service.getBooksByAuthor(inputAuthor)!=null) {
                    i=0;
                    lenName=0;
                    lenAuthor=0;
                    System.out.println("Список книг по автору - "+inputAuthor+" :");
                    for(Book book: service.getBooksByAuthor(inputAuthor)) {
                        if (book.getName().length()>lenName) lenName=book.getName().length();
                        if (book.getAuthor().length()>lenAuthor) lenAuthor=book.getAuthor().length();
                    }
                    for(Book book: service.getBooksByAuthor(inputAuthor)) {
                        i++;
                        for(int j=0; j<lenName+2-book.getName().length();j++){
                            s1=s1+" ";
                        }
                        for(int j=0; j<lenAuthor+2-book.getAuthor().length();j++){
                            s2=s2+" ";
                        }
                        System.out.println(i+") Название: '" + book.getName() +"'"+s1+ "  Автор:" + book.getAuthor()+s2+
                                "  ID книги:"+book.getId());
                        s1="";
                        s2="";
                    }
                } else {
                    System.out.println("В библиотеке нет книг с Автором-"+inputAuthor);
                }
                break;

            case 6:
                //Поиск книг по Названию
                System.out.println();
                System.out.println();
                System.out.println("Список книг по Названию:");
                System.out.print("Введите Название книги:");
                String inputName= scanner.nextLine();
                if(service.getBooksByAuthor(inputName)!=null) {
                    i=0;
                    lenName=0;
                    lenAuthor=0;
                    System.out.println("Список книг по автору - "+inputName+" :");
                    for(Book book: service.getBooksByName(inputName)) {
                        if (book.getName().length()>lenName) lenName=book.getName().length();
                        if (book.getAuthor().length()>lenAuthor) lenAuthor=book.getAuthor().length();
                    }
                    for(Book book: service.getBooksByName(inputName)) {
                        i++;
                        for(int j=0; j<lenName+2-book.getName().length();j++){
                            s1=s1+" ";
                        }
                        for(int j=0; j<lenAuthor+2-book.getAuthor().length();j++){
                            s2=s2+" ";
                        }
                        System.out.println(i+") Название: '" + book.getName() +"'"+s1+ "  Автор:" + book.getAuthor()+s2+
                                "  ID книги:"+book.getId());
                        s1="";
                        s2="";
                    }
                } else {
                    System.out.println("В библиотеке нет книг с Автором-"+inputName);
                }
                break;

            case 7:
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

}

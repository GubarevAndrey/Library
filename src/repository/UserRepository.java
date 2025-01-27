package repository;

import model.Role;
import model.User;
import utils.MyList;

public interface UserRepository {
    //Создание нового пользователя
    User addUser(String email, String password);

    boolean isEmailExist(String email);

    //получить пользователя по email
    User getUserByEmail(String email);

    // изменение password
    boolean UserUpdatePassword (String email, String newPassword);

    User delUser(String email);

    public MyList<User> getUsers();

    public boolean UserStatusUpdate(String email, Role role);


}

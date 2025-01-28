package repository;

import model.Book;
import model.Role;
import model.User;
import utils.MyArrayList;
import utils.MyList;

public class UserRepositoryImpl implements UserRepository {
    private final MyList<User> users;
    public UserRepositoryImpl() {
        users=new MyArrayList<>();
        addUsers();
    }

    private void addUsers(){
        User admin = new User("1","1");
        admin.setRole(Role.ADMIN);
        User blockUser = new User("2","2");
        blockUser.setRole(Role.BLOCKED);
        users.addAll(
                admin,
                blockUser,
                new User("3","3"),
                new User("4","4"),
                new User("5@mail.com","5")
        );
    }
    @Override
    public User addUser(String email, String password) {
        User user =new User(email,password);
        users.add(user);
        return user;
    }

    @Override
    public boolean isEmailExist(String email) {
        for (User user: users) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public User getUserByEmail(String email) {
        for (User user: users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void UserUpdatePassword(String email, String newPassword) {
        User user=getUserByEmail(email);
        user.setPassword(newPassword);
    }



    @Override
    public User delUser(String email) {
        User user=getUserByEmail(email);
        users.remove(user);
        return user;
    }

    @Override
    public MyList<User> getUsers() {
        return users;
    }

    @Override
    public void UserStatusUpdate(String email,Role role){
        User user=getUserByEmail(email);
        user.setRole(role);
    }

}

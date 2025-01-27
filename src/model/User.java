package model;

import model.Role;
import java.util.Objects;


public class User {
    private final String email;
    private  String password;
    private  Role role;
    private Book bookUse;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.role= Role.USER;
        this.bookUse = null;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Book getBookUse() {
        return bookUse;
    }

    public void setBookUse(Book bookUse) {
        this.bookUse = bookUse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!Objects.equals(email, user.email)) return false;
        if (!Objects.equals(password, user.password)) return false;
        if (!Objects.equals(role, user.role)) return false;
        return Objects.equals(bookUse, user.bookUse);
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (bookUse != null ? bookUse.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", bookUse=" + bookUse +
                '}';
    }

}

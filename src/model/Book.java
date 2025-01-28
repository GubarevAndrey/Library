package model;

import utils.MyArrayList;
import utils.MyList;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class Book {
   private  String name;
   private  String author;
   private boolean isBusy;
   private String userEmail;
   private final int id;
   private LocalDate takeDate;



   public Book(String name, String author, int id) {
      this.name = name;
      this.author = author;
      this.isBusy = false;
      this.userEmail = null;
      this.id = id;
      this.takeDate=null;
   }

   public LocalDate getTakeDate() {
      return takeDate;
   }

   public void setTakeDate(LocalDate takeDate) {
      this.takeDate = takeDate;
   }

   public String getName() {
      return name;
   }

   public String getAuthor() {
      return author;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setAuthor(String author) {
      this.author = author;
   }

   public boolean isBusy() {
      return isBusy;
   }

   public void setBusy(boolean busy) {
      isBusy = busy;
   }

   public String getUserUse() {

      return userEmail;
   }



   public int getId() {

      return id;
   }

   public void setUserUse(String email) {

      this.userEmail = email;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Book book = (Book) o;

      if (isBusy != book.isBusy) return false;
      if (id != book.id) return false;
      if (!Objects.equals(name, book.name)) return false;
      if (!Objects.equals(author, book.author)) return false;
      return Objects.equals(userEmail, book.userEmail);
   }

   @Override
   public int hashCode() {
      int result = name != null ? name.hashCode() : 0;
      result = 31 * result + (author != null ? author.hashCode() : 0);
      result = 31 * result + (isBusy ? 1 : 0);
      result = 31 * result + (userEmail != null ? userEmail.hashCode() : 0);
      result = 31 * result + id;
      return result;
   }
}

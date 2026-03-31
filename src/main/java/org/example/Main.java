package org.example;

import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.example.model.Autor;
import org.example.model.Book;
import org.example.model.Category;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Main {
  private static SessionFactory sessionFactory;
  private static final Faker FAKER = new Faker();

  public static void main(String[] args) {
    sessionFactory =
        new Configuration()
            .addAnnotatedClass(Autor.class)
            .addAnnotatedClass(Book.class)
            .addAnnotatedClass(Category.class)
            .buildSessionFactory();

    generateFakeData();
    sessionFactory.close();
  }

  private static void generateFakeData() {
    try (Session session = sessionFactory.openSession()) {
      Transaction tx = session.beginTransaction();

      for (int i = 1; i <= 100; i++) {
        Autor autor =
            new Autor(
                (long) i,
                FAKER.book().author(),
                java.util.Date.from(
                    LocalDate.of(1980, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                FAKER.internet().emailAddress(),
                null);

        Category category =
            new Category((long) i, FAKER.book().genre(), "Опис категорії " + i, new HashSet<>());

        Book book =
            new Book(
                (long) i,
                FAKER.book().title(),
                autor,
                "ISBN" + i,
                "Видавництво " + i,
                LocalDate.of(2020, 1, 1),
                100 + i,
                9.99 + i,
                new HashSet<>());

        List<Book> authorBooks = new ArrayList<>();
        authorBooks.add(book);
        autor.setBooks(authorBooks);

        Set<Book> categoryBooks = new HashSet<>();
        categoryBooks.add(book);
        category.setBooks(categoryBooks);

        Set<Category> bookCategories = new HashSet<>();
        bookCategories.add(category);
        book.setCategories(bookCategories);

        // Persist author first (Book has ManyToOne to Autor) and category for ManyToMany join row
        session.persist(autor);
        session.persist(category);
        session.persist(book);
      }

      tx.commit();
    }
  }
}

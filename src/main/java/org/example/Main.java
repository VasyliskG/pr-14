package org.example;

import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.example.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class Main {
  private static SessionFactory sessionFactory;
  private static final Faker FAKER = new Faker();

  public static void main(String[] args) {
    sessionFactory = new Configuration()
        .addAnnotatedClass(Autor.class)
        .addAnnotatedClass(Book.class)
        .addAnnotatedClass(Category.class)
        .buildSessionFactory();
     generertefakedata();
  }

  private static void generertefakedata() {
    try (Session session = sessionFactory.openSession()) {
      Transaction tx = session.beginTransaction();
      for (int i = 0; i < 100; i++) {
        Autor autor = new Autor(
            (long) i,
            FAKER.book().author(),
            java.util.Date.from(java.time.LocalDate.of(1980, 1, 1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()),
            FAKER.internet().emailAddress(),
            null
        );

        Book book = new Book(
            (long) i,
            FAKER.book().title(),
            autor,
            "ISBN" + i,
            "Видавництво " + i,
            java.time.LocalDate.of(2020, 1, 1),
            100 + i,
            9.99 + i,
            null
        );

        Category category = new Category(
            (long) i,
            FAKER.book().genre(),
            "Опис категорії " + i,
            null
        );

        List<Book> listbook = new ArrayList<>();
        listbook.add(book);

        autor.setBooks(listbook);
        category.setBooks(new HashSet<>(listbook));

        session.persist(category);
        session.persist(book);
        session.persist(autor);
      }
      tx.commit();
    }
  }
}
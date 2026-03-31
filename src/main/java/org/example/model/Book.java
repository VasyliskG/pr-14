package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book {
  @Id private Long id;

  @NotBlank(message = "Назва книги не може бути порожньою")
  @Size(min = 2, max = 255, message = "Назва книги повинна бути від 2 до 255 символів")
  private String title;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "autor_id")
  private Autor autor;

  @NotBlank(message = "ISBN не може бути порожнім")
  private String isbn;

  @NotBlank(message = "Видавництво не може бути порожнім")
  @Size(min = 2, max = 255, message = "Назва видавництва повинна быть от 2 до 255 символов")
  private String publisher;

  @PastOrPresent(message = "Дата публікації не може бути з майбутнього")
  private LocalDate publicationDate;

  @Min(value = 1, message = "Кількість сторінок повинна бути більше 0")
  private Integer pages;

  @Positive(message = "Ціна повинна бути додатньою")
  private Double price;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "book_category",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id"))
  private Set<Category> categories = new HashSet<>();
}

package org.example.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Autor {
  @Id
  private Long id;
  private String name;
  private Date birthDate;
  private  String email;
  @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
  private List<Book> books = new java.util.ArrayList<>();
}

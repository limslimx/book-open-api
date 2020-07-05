package com.lim.bookopenapi.domain;

import lombok.*;

import javax.persistence.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book {

    @Id @GeneratedValue
    @Column(name = "book_id")
    private Long id;

    private String title;

    private String authors;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String contents;

    private String datetime;

    @Column(unique = true)
    private String isbn;

    private String status;

    private String thumbnail;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String bookUrl;
}

package com.lim.bookopenapi.web;

import com.lim.bookopenapi.dto.BookSearchForm;
import com.lim.bookopenapi.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class BookController {

    private final BookService bookService;

    @GetMapping("/book/search")
    public String bookSearchView(Model model) {
        model.addAttribute("bookSearchForm", new BookSearchForm());
        return "book/search";
    }

    @PostMapping("/book/search/save")
    public String bookSearchFunc(@Valid BookSearchForm bookSearchForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("bookSearchForm", bookSearchForm);
            return "/book/search";
        }
        bookService.getBookByQuery(bookSearchForm.getSearchBy());
        return "redirect:/book/search";
    }
}

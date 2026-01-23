package com.coder.service.impl;

import com.coder.dao.BookDao;
import com.coder.service.BookSave;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BookSaveImpl implements BookSave {
    private BookDao bookDao1;
    @Override
    public void saveBook() {
        System.out.println("service save begin");
        bookDao1.save();
    }
}

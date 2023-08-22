package zz.note.list;

import lombok.Data;

@Data
public class Book {
    private final String name;
    private final String author;
    private final double price;

    public Book(String name, String author, double price) {
        this.name = name;
        this.author = author;
        this.price = price;
    }

    @Override
    public String toString() {
        return
                "名称='" + name + '\'' +
                        ", 作者='" + author + '\'' +
                        ", 价格=" + price;
    }
}

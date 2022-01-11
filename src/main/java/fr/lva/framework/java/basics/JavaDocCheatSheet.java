package fr.lva.framework.java.basics;

import fr.lva.framework.pojo.Author;
import fr.lva.framework.pojo.Book;
import fr.lva.framework.utils.Args;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Class used to describe Javadoc
 *
 * <p>You cas use HTML in javadoc</p>
 * @see <a href="https://github.com/LoVas2/cheat-sheet">Cheat Sheet link</a>
 *
 * @author Loic
 */
public class JavaDocCheatSheet {

    /**
     * Get books by author
     * @param author book's author
     * @return list of books
     *
     * @deprecated use getBooks by author's ID {@link #getBook(Long)}
     */
    public List<Book> getBook(Author author) {
        return Collections.emptyList();
    }

    /**
     * Get books by author's ID
     * @param authorId author's id. Cannot be <code>null</code>
     * @return set of books
     */
    public Set<Book> getBook(Long authorId) {
        Args.notNull(authorId, "authorId cannot be null");
        return Collections.emptySet();
    }
}

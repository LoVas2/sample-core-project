package fr.lva.framework.db;

import fr.lva.framework.pojo.Author;
import fr.lva.framework.pojo.Book;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommonErrors {

    private static final Logger log = LogManager.getLogger();

    private final EntityManager em;

    public CommonErrors(EntityManager em) {
        this.em = em;
    }

    /**
     * The initialization of lazily fetched associations
     * It’s often called the n+1 select issue.
     * Whenever you access an uninitialized, lazily fetched association, Hibernate executes a query that loads all elements of this association from the database.
     */
    public void lazilyFetchedAssociations() {
        List<Author> authors = em.createQuery("SELECT a FROM Author a", Author.class).getResultList();
        for (Author a : authors) {
            log.info(a.getFirstName() + " " + a.getLastName() + " wrote " + a.getBooks().size() + " books.");
        }
        // As result = select authors then select books for each author => WRONG. If n authors => n+1 requests
        List<Author> authorsOK = em.createQuery("SELECT a FROM Author a LEFT JOIN FETCH a.books", Author.class).getResultList();
        for (Author a : authorsOK) {
            log.info(a.getFirstName() + " " + a.getLastName() + " wrote " + a.getBooks().size() + " books.");
        }
        // Only 1 query => OK
    }

    /**
     * Not only the initialization of lazily fetched associations can cause lots of unexpected queries. That’s also the case if you use FetchType.EAGER.
     *
     * This is the <b>DEFAULT</b> behavior for the to-one associations ! (@OneToOne or @ManyToOne associations)
     */
    public void fetchTypeEager() {
        // Specify the FetchType.LAZY to the to-one associations
    }

    /**
     * Removing child entities with CascadeType.Remove
     * OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
     * private List<Review> reviews = new ArrayList<Review>();
     *
     * When we use this, Hibernate loads all the reviews (select * from review where bookId=...) then deletes 1 by 1 the reviews.
     * This is done because it transitions the lifecycle state of each entity from managed to removed.
     * That provides the advantage that all lifecycle events get triggered and caches are updated.
     */
    public void removeByCascade() {
        em.flush();
        em.clear();
        // Remove Child Entities With a Bulk Operation if we don't use any frameworks that use lifecycle callbacks or EntityListeners
        Book b = em.find(Book.class, 2L);
        Query q = em.createQuery("DELETE FROM Review r WHERE r.book.id = :bid");
        q.setParameter("bid", 2L);
        q.executeUpdate();
        em.remove(b);
        // Be sure that we didn’t fetch any of the removed entities before
        // or call the flush() and clear() methods on the EntityManager before.
    }

    /**
     * Modeling Many-to-Many Associations as a List
     * Hibernate deletes all existing association records and then inserts a new one for each managed association whenever we add an element to or remove one from the association.
     * @ManyToMany
     * @JoinTable(name = "book_author",
     *            joinColumns = { @JoinColumn(name = "fk_book") },
     *             inverseJoinColumns = { @JoinColumn(name = "fk_author") })
     * private List<Author> authors = new ArrayList<Author>();
     * If we add an author to the book, Hibernate deletes all the recors in 'book_author' and recreate each one
     */
    public void manyToManyAsList() {
        // Use a Set instead of a List
        Set<Author> authors = new HashSet<>();
    }

}

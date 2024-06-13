package com.jmo.devel.bookstore.api.configuration;

import com.jmo.devel.bookstore.api.model.Author;
import com.jmo.devel.bookstore.api.model.Book;
import com.jmo.devel.bookstore.api.model.Publisher;
import com.jmo.devel.bookstore.api.repository.AuthorsRepository;
import com.jmo.devel.bookstore.api.repository.BooksRepository;
import com.jmo.devel.bookstore.api.repository.PublishersRepository;
import com.jmo.devel.bookstore.api.service.AuthorsService;
import com.jmo.devel.bookstore.api.service.BooksService;
import com.jmo.devel.bookstore.api.service.PublishersService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.embedded.RedisServer;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@Import({ CacheConfiguration.class, AuthorsService.class, BooksService.class, PublishersService.class })
@EnableCaching
@ImportAutoConfiguration( classes = {
    CacheAutoConfiguration.class,
    RedisAutoConfiguration.class
})
class CacheConfigurationTest {

    private static final long AUTHOR_ID    = 1L;
    private static final long BOOK_ID      = 1L;
    private static final long PUBLISHER_ID = 1L;

    @MockBean
    private AuthorsRepository    authorsRepository;
    @MockBean
    private BooksRepository      booksRepository;
    @MockBean
    private PublishersRepository publishersRepository;

    @Autowired
    private AuthorsService    authorsService;
    @Autowired
    private BooksService      booksService;
    @Autowired
    private PublishersService publishersService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private static RedisServer server;

    @BeforeAll
    public static void setUp() {
        server = new RedisServer(6379);
        server.start();
    }

    @AfterAll
    public static void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    void givenRedisCaching_whenFindAuthorById_thenAuthorReturnedFromCache() {

        Author author = Author.builder()
            .id( AUTHOR_ID )
            .name( "Patricia" )
            .surnames( "Highsmith" )
            .build();

        BDDMockito.given( authorsRepository.findById( AUTHOR_ID ) )
            .willReturn( Optional.of( author ) );

        Author authorCacheMiss = authorsService.getById( AUTHOR_ID );
        Author authorCacheHit  = authorsService.getById( AUTHOR_ID );

        Assertions.assertThat( authorCacheMiss.getId() ).isEqualTo( author.getId() );
        Assertions.assertThat( authorCacheHit.getId()  ).isEqualTo( author.getId() );

        Mockito.verify( authorsRepository, Mockito.times(1) )
            .findById( AUTHOR_ID );
        Cache authorsCache = cacheManager.getCache( CacheConfiguration.AUTHORS_CACHE );
        Assertions.assertThat( authorsCache ).isNotNull();
        Assertions.assertThat( authorsCache.get( 1L ) ).isNotNull();
        Author cached = (Author) authorsCache.get(1L).get();
        Assertions.assertThat( cached.getSurnames() ).isEqualTo( "Highsmith" );

    }

    @Test
    void givenRedisCaching_whenFindBookById_thenBookReturnedFromCache() {

        Book book = Book.builder().id( BOOK_ID ).isbn( "9788432221255" ).build();

        BDDMockito.given( booksRepository.findById( BOOK_ID ) )
            .willReturn( Optional.of( book ) );

        Book bookCacheMiss = booksService.getById( BOOK_ID );
        Book bookCacheHit  = booksService.getById( BOOK_ID );

        Assertions.assertThat( bookCacheMiss.getId() ).isEqualTo( book.getId() );
        Assertions.assertThat( bookCacheHit.getId()  ).isEqualTo( book.getId() );

        Mockito.verify( booksRepository, Mockito.times(1) )
            .findById( BOOK_ID );
        Cache booksCache = cacheManager.getCache( CacheConfiguration.BOOKS_CACHE );
        Assertions.assertThat( booksCache ).isNotNull();
        Assertions.assertThat( booksCache.get( 1L ) ).isNotNull();
        Book cached = (Book) booksCache.get(1L).get();
        Assertions.assertThat( cached.getIsbn() ).isEqualTo( "9788432221255" );

    }

    @Test
    void givenRedisCaching_whenFindPublisherById_thenPublisherReturnedFromCache() {

        Publisher publisher = Publisher.builder()
            .id( PUBLISHER_ID )
            .name( "Blackie Books" )
            .build();

        BDDMockito.given( publishersRepository.findById( PUBLISHER_ID ) )
            .willReturn( Optional.of( publisher ) );

        Publisher publisherCacheMiss = publishersService.getById( PUBLISHER_ID );
        Publisher publisherCacheHit  = publishersService.getById( PUBLISHER_ID );

        Assertions.assertThat( publisherCacheMiss.getId() ).isEqualTo( publisher.getId() );
        Assertions.assertThat( publisherCacheHit.getId()  ).isEqualTo( publisher.getId() );

        Mockito.verify( publishersRepository, Mockito.times(1) )
            .findById( PUBLISHER_ID );
        Cache publishersCache = cacheManager.getCache( CacheConfiguration.PUBLISHERS_CACHE );
        Assertions.assertThat( publishersCache ).isNotNull();
        Assertions.assertThat( publishersCache.get( 1L ) ).isNotNull();
        Publisher cached = (Publisher) publishersCache.get(1L).get();
        Assertions.assertThat( cached.getName() ).isEqualTo( "Blackie Books" );

    }

}
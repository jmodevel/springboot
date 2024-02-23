package com.jmo.devel.tv.shows.repository;

import com.jmo.devel.tv.shows.model.Show;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShowsRepository extends MongoRepository<Show, String> {

    @Query("{name:'?0'}")
    Optional<Show> findItemByName(String name);

    @Query(
            value  = "{genres:{$in: ['?0']}}",
            sort   = "{'premiered' : 1}",
            fields = "{'name' : 1, 'runtime' : 1, 'genres' : 1}"
    )
    List<Show> findByGenre(String genre);

    public long count();

    List<Show> findByTypeOrderByNameAsc(String type);
    List<Show> findByStatusOrderByNameAsc(String status);
    List<Show> findByRuntimeOrderByNameAsc(int runtime);


}

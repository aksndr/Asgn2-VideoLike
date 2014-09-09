package org.magnum.mobilecloud.video.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Set;

/**
 * User: a.arzamastsev Date: 09.09.2014 Time: 11:23
 */
@RepositoryRestResource
public interface UnlikeRepository extends CrudRepository<Unlike, Long> {

    Set<Unlike> findByVideo_id(@Param("id") long id);

    Unlike findByVideo_idAndUser(@Param("id") long id, @Param("user") String user);
}

package org.magnum.mobilecloud.video.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * User: a.arzamastsev Date: 08.09.2014 Time: 16:40
 */
@RepositoryRestResource
public interface LikeRepository extends CrudRepository<Like, Long> {


}

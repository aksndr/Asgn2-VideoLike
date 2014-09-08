package org.magnum.mobilecloud.video.repository;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

/**
 * User: a.arzamastsev Date: 08.09.2014 Time: 11:27
 */
//@Component
@RepositoryRestResource(path = VideoSvcApi.VIDEO_SVC_PATH)
public interface VideoRepository extends CrudRepository<Video, Long> {


    @Query("SELECT DISTINCT v FROM Video v " +
            "left join fetch v.like " +
            "WHERE v.id = :id")
    public Video findById(@Param("id") Long id);

    public Collection<Video> findByName(
            @Param(VideoSvcApi.TITLE_PARAMETER) String title);

    public Collection<Video> findByDurationLessThan(
            @Param(VideoSvcApi.DURATION_PARAMETER) long maxduration);
}

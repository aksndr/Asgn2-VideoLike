package org.magnum.mobilecloud.video.repository;

import javax.persistence.*;

/**
 * User: a.arzamastsev Date: 08.09.2014 Time: 15:52
 */
@Embeddable
@Entity
@Table(name = "likes")
public class Like {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "video_id")
    private Long video_id;
    @Column(name = "user")
    private String user;

    public Long getVideo_id() {
        return video_id;
    }

    public void setVideo_id(Long video_id) {
        this.video_id = video_id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

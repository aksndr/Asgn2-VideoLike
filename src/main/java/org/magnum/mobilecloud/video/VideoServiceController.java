/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.magnum.mobilecloud.video;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Like;
import org.magnum.mobilecloud.video.repository.LikeRepository;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Set;

@Controller
public class VideoServiceController {

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    LikeRepository likeRepository;


    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.GET)
    @ResponseBody
    Iterable<Video> getVideoList() {
        return videoRepository.findAll();
    }

    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}", method = RequestMethod.GET)
    @ResponseBody
    Video getVideoById(@PathVariable("id") long id) {
        return videoRepository.findById(id);
    }

    @PreAuthorize("hasRole(user)")
    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.POST)
    @ResponseBody
    public Video addVideo(@RequestBody Video v) {
        return videoRepository.save(v);
    }

    @PreAuthorize("hasRole(user)")
    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/like", method = RequestMethod.POST)
    public void likeVideo(@PathVariable("id") long id, HttpServletResponse response, Principal user) {
        String userName = user.getName();
        Video v = videoRepository.findById(id);
        Set<Like> likes = v.getLikeSet();
        if (likes.size() == 0) {
            Like l = new Like();
            l.setVideo_id(v.getId());
            l.setUser(userName);
            v.getLikeSet().add(l);
            long likeCount = v.getLikes();
            v.setLikes(++likeCount);
//            likeRepository.save(l);
            videoRepository.save(v);
            response.setStatus(200);
        } else {
            for (Like like : likes) {
                if (like.getUser().equals(userName)) {
                    response.setStatus(400);
                } else {
                    Like l = new Like();
                    l.setVideo_id(v.getId());
                    l.setUser(userName);
                    likeRepository.save(l);
                    response.setStatus(200);
                }
            }
        }


    }

}

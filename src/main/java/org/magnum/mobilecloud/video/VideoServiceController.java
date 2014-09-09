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
import org.magnum.mobilecloud.video.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Collection;
import java.util.Set;

@Controller
public class VideoServiceController {

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    UnlikeRepository unlikeRepository;


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
        if (v == null) {
            response.setStatus(404);
            return;
        }
        Set<Like> likes = likeRepository.findByVideo_id(id);
        Unlike unlike = unlikeRepository.findByVideo_idAndUser(id, userName);
        if (unlike != null) unlikeRepository.delete(unlike);
        if (likes.size() == 0) {
            addNewLike(v, userName);
            response.setStatus(200);
        } else {
            for (Like like : likes) {
                if (like.getUser().equals(userName)) {
                    response.setStatus(400);
                }
            }
            if (response.getStatus() != 400) {
                addNewLike(v, userName);
                response.setStatus(200);
            }
        }
    }

    @PreAuthorize("hasRole(user)")
    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/unlike", method = RequestMethod.POST)
    public void unlikeVideo(@PathVariable("id") long id, HttpServletResponse response, Principal user) {
        String userName = user.getName();
        Video v = videoRepository.findById(id);
        if (v == null) {
            response.setStatus(404);
            return;
        }
        Set<Unlike> unlikeSet = unlikeRepository.findByVideo_id(id);
        Like like = likeRepository.findByVideo_idAndUser(id, userName);
        if (like != null) likeRepository.delete(like);
        if (unlikeSet.size() == 0) {
            addNewUnLike(v, userName);
            response.setStatus(200);
        } else {
            for (Unlike unlike : unlikeSet) {
                if (unlike.getUser().equals(userName)) {
                    response.setStatus(404);
                }
            }
            if (response.getStatus() != 404) {
                addNewUnLike(v, userName);
                response.setStatus(200);
            }
        }
    }

    @PreAuthorize("hasRole(user)")
    @RequestMapping(value = VideoSvcApi.VIDEO_TITLE_SEARCH_PATH, method = RequestMethod.GET)
    @ResponseBody
    Collection<Video> findByTitle(@RequestParam(VideoSvcApi.TITLE_PARAMETER) String title) {
        return videoRepository.findByName(title);
    }

    @PreAuthorize("hasRole(user)")
    @RequestMapping(value = VideoSvcApi.VIDEO_DURATION_SEARCH_PATH, method = RequestMethod.GET)
    @ResponseBody
    Collection<Video> findByDurationLessThan(@RequestParam(VideoSvcApi.DURATION_PARAMETER) long duration) {
        return videoRepository.findByDurationLessThan(duration);
    }

    @PreAuthorize("hasRole(user)")
    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/likedby", method = RequestMethod.GET)
    @ResponseBody
    Collection<String> getUsersWhoLikedVideo(@PathVariable("id") long id) {
        return likeRepository.findUsersByVideoId(id);
    }


    private void addNewUnLike(Video v, String userName) {
        Unlike u = new Unlike(userName, v);
        unlikeRepository.save(u);
        long likeCount = v.getLikes();
        v.setLikes(--likeCount);

        videoRepository.save(v);
    }

    private void addNewLike(Video v, String userName) {
        Like l = new Like(userName, v);
        likeRepository.save(l);
        long likeCount = v.getLikes();
        v.setLikes(++likeCount);

        videoRepository.save(v);
    }

}

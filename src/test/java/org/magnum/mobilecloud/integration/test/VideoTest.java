package org.magnum.mobilecloud.integration.test;

import org.junit.Test;
import org.magnum.mobilecloud.video.TestData;
import org.magnum.mobilecloud.video.client.SecuredRestBuilder;
import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import retrofit.client.ApacheClient;

import static org.junit.Assert.assertTrue;

/**
 * User: a.arzamastsev Date: 09.09.2014 Time: 9:35
 */
public class VideoTest {
    private final String TEST_URL = "https://localhost:8443";
    private final String _URL = "http://localhost:8080";
    private final String USERNAME1 = "admin";
    private final String USERNAME2 = "user0";
    private final String PASSWORD = "pass";
    private final String CLIENT_ID = "mobile";
    private VideoSvcApi readWriteVideoSvcUser1 = new SecuredRestBuilder()
            .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
            .setEndpoint(TEST_URL)
            .setLoginEndpoint(TEST_URL + VideoSvcApi.TOKEN_PATH)
                    // .setLogLevel(LogLevel.FULL)
            .setUsername(USERNAME1).setPassword(PASSWORD).setClientId(CLIENT_ID)
            .build().create(VideoSvcApi.class);
    private VideoSvcApi readWriteVideoSvcUser2 = new SecuredRestBuilder()
            .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
            .setEndpoint(TEST_URL)
            .setLoginEndpoint(TEST_URL + VideoSvcApi.TOKEN_PATH)
                    // .setLogLevel(LogLevel.FULL)
            .setUsername(USERNAME2).setPassword(PASSWORD).setClientId(CLIENT_ID)
            .build().create(VideoSvcApi.class);
    private Video video = TestData.randomVideo();

    @Test
    public void testLikeCount() throws Exception {
        Video v;
        // Add the video
        v = readWriteVideoSvcUser1.addVideo(video);
        long id = v.getId();
        // Like the video
        readWriteVideoSvcUser1.likeVideo(id);

        // Get the video again
        v = readWriteVideoSvcUser1.getVideoById(id);
        // Make sure the like count is 1
        assertTrue(v.getLikes() == 1);

        // Unlike the video
        readWriteVideoSvcUser1.unlikeVideo(v.getId());

        // Get the video again
        v = readWriteVideoSvcUser1.getVideoById(v.getId());

        // Make sure the like count is 0
        assertTrue(v.getLikes() == 0);
    }


}

package com.github.bugscatcher;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.responses.PostResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

public class TestBase {
    protected static final int USER_ID_1 = 1;
    protected static final int USER_ID_2 = 2;
    protected static final String ACCESS_TOKEN_1 = "1";
    protected static final String ACCESS_TOKEN_2 = "2";
    protected static UserActor actor1;
    protected static UserActor actor2;
    protected static VkApiClient vk;
    protected PostResponse postResponse;

    @BeforeClass
    public static void beforeTests() {
        TransportClient transportClient = new HttpTransportClient();
        vk = new VkApiClient(transportClient);
        actor1 = new UserActor(USER_ID_1, ACCESS_TOKEN_1);
        actor2 = new UserActor(USER_ID_2, ACCESS_TOKEN_2);
    }

    @Before
    public void setUp() throws Exception {
        postResponse = vk.wall().post(actor1)
                .ownerId(USER_ID_1)
                .friendsOnly(false)
                .message(String.valueOf(System.currentTimeMillis()))
                .execute();
    }

    @After
    public void tearDown() throws Exception {
        vk.wall().delete(actor1)
                .ownerId(USER_ID_1)
                .postId(postResponse.getPostId())
                .execute();
//        Too many requests per second
        Thread.sleep(1000);
    }
}

package com.github.bugscatcher;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.likes.responses.AddResponse;
import com.vk.api.sdk.objects.likes.responses.DeleteResponse;
import com.vk.api.sdk.objects.likes.responses.GetListResponse;
import com.vk.api.sdk.objects.likes.responses.IsLikedResponse;
import com.vk.api.sdk.objects.wall.responses.PostResponse;
import com.vk.api.sdk.queries.likes.LikesType;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;


public class LikeTests {
    public static final int USER_ID_1 = 1;
    public static final int USER_ID_2 = 2;
    public static final String ACCESS_TOKEN_1 = "1";
    public static final String ACCESS_TOKEN_2 = "2";
    private static UserActor actor1;
    private static UserActor actor2;
    private static VkApiClient vk;
    private PostResponse postResponse;

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

    @Test
    public void add() throws ClientException, ApiException {
        AddResponse addResponse = vk.likes().add(actor2, LikesType.POST, postResponse.getPostId())
                .ownerId(USER_ID_1)
                .execute();

        Integer expectedLikes = 1;
        assertEquals("incorrect likes count", addResponse.getLikes(), expectedLikes);
    }

    @Test
    public void delete() throws ClientException, ApiException {
        vk.likes().add(actor2, LikesType.POST, postResponse.getPostId())
                .ownerId(USER_ID_1)
                .execute();


        DeleteResponse deleteResponse = vk.likes().delete(actor2, LikesType.POST, postResponse.getPostId())
                .ownerId(USER_ID_1)
                .execute();

        Integer expectedLikes = 0;
        assertEquals("incorrect likes count", deleteResponse.getLikes(), expectedLikes);
    }

    @Test
    public void getList() throws ClientException, ApiException {
        vk.likes().add(actor1, LikesType.POST, postResponse.getPostId())
                .ownerId(USER_ID_1)
                .execute();

        vk.likes().add(actor2, LikesType.POST, postResponse.getPostId())
                .ownerId(USER_ID_1)
                .execute();

        GetListResponse getListResponse = vk.likes().getList(actor1, LikesType.POST)
                .ownerId(USER_ID_1)
                .itemId(postResponse.getPostId())
                .execute();

        Integer expectedCount = 2;
        assertEquals("incorrect count", getListResponse.getCount(), expectedCount);
        assertEquals("incorrect likes count", getListResponse.getItems(), List.of(USER_ID_2, USER_ID_1));
    }

    @Test
    public void isLiked() throws ClientException, ApiException {
        vk.likes().add(actor2, LikesType.POST, postResponse.getPostId())
                .ownerId(USER_ID_1)
                .execute();

        IsLikedResponse isLikedResponse = vk.likes().isLiked(actor1, LikesType.POST, postResponse.getPostId())
                .userId(USER_ID_2)
                .ownerId(USER_ID_1)
                .execute();

        assertTrue("incorrect isLiked", isLikedResponse.isLiked());
        assertFalse("incorrect isCopied", isLikedResponse.isCopied());
    }

    @Test
    public void add_negative() {
        AddResponse addResponse = null;
        try {
            addResponse = vk.likes().add(actor2, LikesType.AUDIO, postResponse.getPostId())
                    .ownerId(USER_ID_1)
                    .execute();
        } catch (ApiException e) {
            assertEquals(Integer.valueOf(100), e.getCode());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        assertNull(addResponse);
    }

    @Test
    public void delete_negative() {
        DeleteResponse deleteResponse = null;
        try {
            deleteResponse = vk.likes().delete(actor2, LikesType.POST, postResponse.getPostId())
                    .ownerId(USER_ID_1)
                    .execute();
        } catch (ApiException e) {
            assertEquals(Integer.valueOf(15), e.getCode());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        assertNull(deleteResponse);
    }

    @Test
    public void getList_negative() {
        GetListResponse getListResponse = null;
        try {
            getListResponse = vk.likes().getList(actor1, LikesType.SITEPAGE)
                    .itemId(postResponse.getPostId())
                    .execute();
        } catch (ApiException e) {
            assertEquals(Integer.valueOf(100), e.getCode());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        assertNull(getListResponse);
    }


    @Test
    public void isLiked_negative() {
        int fakeID = -1;
        IsLikedResponse isLikedResponse = null;
        try {
            isLikedResponse = vk.likes().isLiked(actor1, LikesType.POST, fakeID)
                    .userId(USER_ID_2)
                    .ownerId(USER_ID_1)
                    .execute();
        } catch (ApiException e) {
            assertEquals(Integer.valueOf(100), e.getCode());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        assertNull("incorrect response", isLikedResponse);
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

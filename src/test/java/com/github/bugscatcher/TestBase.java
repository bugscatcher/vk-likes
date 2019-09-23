package com.github.bugscatcher;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ApiTooManyException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.responses.PostResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.fail;

public class TestBase {
    protected static final Logger LOG = LoggerFactory.getLogger(TestBase.class);
    protected static int user1ID;
    protected static int user2ID;
    protected static UserActor actor1;
    protected static UserActor actor2;
    protected static VkApiClient vk;
    protected PostResponse postResponse;

    @BeforeClass
    public static void beforeTests() {
        Properties properties = loadProperties();
        vk = new VkApiClient(new HttpTransportClient());
        user1ID = Integer.parseInt(properties.getProperty("user1.id"));
        user2ID = Integer.parseInt(properties.getProperty("user2.id"));
        actor1 = new UserActor(user1ID, properties.getProperty("user1.accessToken"));
        actor2 = new UserActor(user2ID, properties.getProperty("user2.accessToken"));
    }

    @Before
    public void setUp() {
        try {
            postResponse = vk.wall().post(actor1)
                    .ownerId(user1ID)
                    .friendsOnly(false)
                    .message(String.valueOf(System.currentTimeMillis()))
                    .execute();
        } catch (ApiTooManyException e) {
            LOG.error("Too many requests", e);
            fail();
        } catch (ApiException e) {
            LOG.error("Business logic error", e);
            fail();
        } catch (ClientException e) {
            LOG.error("Transport layer error", e);
            fail();
        }
    }

    @After
    public void tearDown() throws InterruptedException {
        try {
            vk.wall().delete(actor1)
                    .ownerId(user1ID)
                    .postId(postResponse.getPostId())
                    .execute();
        } catch (ApiTooManyException e) {
            LOG.error("Too many requests", e);
            fail();
        } catch (ApiException e) {
            LOG.error("Business logic error", e);
            fail();
        } catch (ClientException e) {
            LOG.error("Transport layer error", e);
            fail();
        }
//        Too many requests per second
        Thread.sleep(1000);
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = TestBase.class.getResourceAsStream("/credentials.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            LOG.error("Can't load properties file", e);
            throw new IllegalStateException(e);
        }

        return properties;
    }
}

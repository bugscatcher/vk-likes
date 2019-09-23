package com.github.bugscatcher.likes;

import com.github.bugscatcher.SmokeTests;
import com.github.bugscatcher.TestBase;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.likes.responses.AddResponse;
import com.vk.api.sdk.queries.likes.LikesType;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
@Category(SmokeTests.class)
public class Add extends TestBase {
    @Parameterized.Parameters(name = "test with actor={0}, likes type = {1}: expected likes count={2}, expected err code={3}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"actor2", LikesType.POST, 1, null},
                {"actor1", LikesType.POST, 1, null},
                {"actor2", LikesType.AUDIO, null, 100},
        });
    }

    private UserActor actor;
    private LikesType likesType;
    private Integer expectedLikes;
    private Integer expectedErrorCode;

    public Add(String actor, LikesType likesType, Integer expectedLikes, Integer errorCode) {
        switch (actor) {
            case "actor1":
                this.actor = actor1;
                break;
            case "actor2":
                this.actor = actor2;
                break;
            default:
                LOG.error("incorrect actor as test data");
                fail();
        }
        this.likesType = likesType;
        this.expectedLikes = expectedLikes;
        this.expectedErrorCode = errorCode;
    }


    @Test
    public void add() {
        try {
            AddResponse addResponse = vk.likes().add(actor, likesType, postResponse.getPostId())
                    .ownerId(user1ID)
                    .execute();

            assertEquals("incorrect likes count", expectedLikes, addResponse.getLikes());
        } catch (NullPointerException e) {
            LOG.warn("Something null", e);
            fail();
        } catch (ApiException e) {
            if (expectedErrorCode != null) {
                assertEquals("incorrect code", expectedErrorCode, e.getCode());
            } else {
                LOG.error("Business logic error", e);
                fail();
            }

        } catch (ClientException e) {
            LOG.error("Transport layer error", e);
            fail();
        }
    }
}

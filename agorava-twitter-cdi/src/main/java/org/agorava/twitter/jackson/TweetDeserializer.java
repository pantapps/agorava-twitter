/*
 * Copyright 2013 Agorava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.agorava.twitter.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.agorava.twitter.model.Tweet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Custom Jackson deserializer for tweets. Tweets can't be simply mapped like other Twitter model objects because the JSON
 * structure varies between the search API and the timeline API. This deserializer determine which structure is in play and
 * creates a tweet from it.
 *
 * @author Craig Walls
 * @author Antoine Sabot-Durand
 */
class TweetDeserializer extends JsonDeserializer<Tweet> {

    @Override
    public Tweet deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode tree = jp.readValueAsTree();
        long id = tree.get("id").asLong();
        String text = tree.get("text").asText();
        JsonNode fromUserNode = tree.get("user");
        String fromScreenName = null;
        long fromId = 0;
        String fromImageUrl = null;
        String dateFormat = TIMELINE_DATE_FORMAT;
        if (fromUserNode != null) {
            fromScreenName = fromUserNode.get("screen_name").asText();
            fromId = fromUserNode.get("id").asLong();
            fromImageUrl = fromUserNode.get("profile_image_url").asText();
        } else {
            fromScreenName = tree.get("from_user").asText();
            fromId = tree.get("from_user_id").asLong();
            fromImageUrl = tree.get("profile_image_url").asText();
            dateFormat = SEARCH_DATE_FORMAT;
        }
        Date createdAt = toDate(tree.get("created_at").asText(), new SimpleDateFormat(dateFormat, Locale.ENGLISH));
        String source = tree.get("source").asText();
        JsonNode toUserIdNode = tree.get("in_reply_to_user_id");
        Long toUserId = toUserIdNode != null ? toUserIdNode.longValue() : null;
        JsonNode languageCodeNode = tree.get("iso_language_code");
        String languageCode = languageCodeNode != null ? languageCodeNode.asText() : null;
        Tweet tweet = new Tweet(id, text, createdAt, fromScreenName, fromImageUrl, toUserId, fromId, languageCode, source);
        JsonNode inReplyToStatusIdNode = tree.get("in_reply_to_status_id");
        Long inReplyToStatusId = inReplyToStatusIdNode != null && !inReplyToStatusIdNode.isNull() ? inReplyToStatusIdNode
                .longValue() : null;
        tweet.setInReplyToStatusId(inReplyToStatusId);
        JsonNode retweetCountNode = tree.get("retweet_count");
        Integer retweetCount = retweetCountNode != null && !retweetCountNode.isNull() ? retweetCountNode.intValue() : null;
        tweet.setRetweetCount(retweetCount);
        JsonNode favoritedNode = tree.get("favorited");
        boolean favorited = favoritedNode != null && !favoritedNode.isNull() ? favoritedNode.booleanValue() : false;
        tweet.setFavorited(favorited);
        jp.skipChildren();
        return tweet;
    }

    private Date toDate(String dateString, DateFormat dateFormat) {
        if (dateString == null) {
            return null;
        }

        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    private static final String TIMELINE_DATE_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

    private static final String SEARCH_DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z";

}
